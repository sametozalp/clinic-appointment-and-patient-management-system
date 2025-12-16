# Clinic Appointment & Patient Management System

Event-driven, Spring Boot microservices for clinic appointments, doctors, identity, notifications, reporting, and auditing. Uses RabbitMQ for reliable event distribution and Redis (master + 3 slaves + 3 Sentinels on Docker) for caching.

## Architecture at a Glance

- Services (ports): `identity-service (8082)`, `doctor-service (8083)`, `audit-service (8084)`, `report-service (8085)`, `appointment-service (8086)`, `notification-service (8081)`.
- Datastores: Each business service uses its own PostgreSQL DB (see `application.properties`).
- Messaging: RabbitMQ (`appointment.exchange` topic) fans out `appointment.*` events to consumers.
- Caching: Redis master + 3 slaves with 3 Sentinels (Docker). `doctor-service` caches doctor lists with 10-minute TTL using JSON serialization.

```
               +--------------------+
               |  identity-service  |
               +--------------------+
                         |
                         v
 +------------------+   REST   +-------------------+
 | doctor-service   |<-------->| appointment-service|-- publishes --> RabbitMQ
 +------------------+          +-------------------+
        |   ^                               |
        |   |                               | appointment.exchange (topic)
        |   |                               v
        |  Redis (cache)       +-------------------+   +------------------+
        |  Master + 3 Slaves   | notification-svc  |   |   audit-service  |
        |  3x Sentinels        +-------------------+   +------------------+
        |                                   \            manual ACK + DLQ
        |                                    \------------------------------+
        |                                                               |   |
        |                                                     +-------------------+
        |                                                     |  report-service   |
        |                                                     +-------------------+
        |                                                        manual ACK + DLQ
        |
        v
   PostgreSQL
```

## Services

### identity-service
- Registers/logs in users; default role `PATIENT`, `registerAdmin` sets `ADMIN`.
- Security is open (all requests permitted) for demo (`SecurityConfiguration`).
- Passwords hashed with `PasswordEncoder`; data stored in `clinical_identity_service` DB.

### doctor-service
- CRUD-lite for doctors (`/doctors/create`, `/doctors/getAll`, `/doctors/{id}/deactivate`).
- Caching: `RedisCacheConfig` enables Spring Cache with 10-minute TTL, JSON serialization (Java time supported). Cache key `doctors::'all'`. Writes evict cache, reads cache results.
- Backed by `clinical_doctor_service` DB.

### appointment-service
- Creates appointments (`/api/appointments/create`) with overlap check.
- Persists to `clinical_appointment_service` DB and publishes `AppointmentCreatedEvent` to RabbitMQ topic exchange:
  - Exchange: `appointment.exchange`
  - Routing key: `appointment.created`
  - Messages are marked `PERSISTENT` so they survive broker restarts.

### notification-service
- Subscribes to `appointment.exchange` via queue `appointment.notification.queue` bound with `appointment.*`.
- Logs notification intent for created appointments. Uses JSON message conversion.

### audit-service
- Queue: `audit.queue`, bound to `appointment.*`.
- Dead-letter setup: DLX `audit.dlx`, DLQ `audit.dlq`.
- Manual acknowledgements:
  - On success: `basicAck` confirms delivery.
  - On failure: `basicNack` with `requeue=false` routes to DLQ (no redelivery loop).
- Persists audit logs (event type + payload JSON) to `clinical_audit_service` DB.

### report-service
- Queue: `reporting.queue`, bound to `appointment.created`.
- Dead-letter setup: DLX `report.dlx`, DLQ `report.dlq`.
- Manual ACK like audit-service; on failure NACK to DLQ.
- Builds daily appointment reports per doctor in `clinical_report_service` DB; REST endpoint `/reports/doctor/{id}`.

## RabbitMQ Details
- Broker runs in Docker (user note). Topic exchange `appointment.exchange`.
- Producer: `appointment-service` uses `RabbitTemplate` with `MessageDeliveryMode.PERSISTENT`.
- Consumers:
  - `notification-service`: auto-ack (default) sufficient for at-least-once logging.
  - `audit-service` & `report-service`: use `SimpleRabbitListenerContainerFactory` with `AcknowledgeMode.MANUAL` and `defaultRequeueRejected=false` to avoid infinite redelivery. DLXs catch failed messages.
- Routing:
  - `appointment.*` → `audit.queue` (durable, DLX)
  - `appointment.*` → `appointment.notification.queue` (durable)
  - `appointment.created` → `reporting.queue` (durable, DLX)

Ack Flow (audit/report):
```
consume message
  ├─ process ok → basicAck(deliveryTag, multiple=false)
  └─ process fails → basicNack(deliveryTag, multiple=false, requeue=false)
                      ↳ message routed to DLX → DLQ
```

## Redis, Sentinel, and Cache Topology
- Redis master + 3 slaves; 3 Sentinels supervise the master.
- Sentinels (Docker): sample run command from `create-sentinel-command.txt`:
  - `docker run -d --name sentinel1 -p 6391:26379 --network redis-network -v C:/Users/Samet/Desktop/sentinel.conf:/usr/local/etc/redis/sentinel.conf redis redis-sentinel /usr/local/etc/redis/sentinel.conf`
- Sentinel config (`sentinel.conf`):
  - `sentinel monitor mymaster 172.18.0.2 6379 3`
  - `sentinel down-after-milliseconds mymaster 5000`
  - `sentinel failover-timeout mymaster 1000`
  - `sentinel parallel-syncs mymaster 3`
- Expected behavior:
  - Writes target master; reads served by slaves.
  - If master is down for 5s, Sentinels elect a new master; slaves resync in parallel (up to 3).
  - `doctor-service` connects via Sentinel-managed endpoints so cache survives failover.

Cache Flow (doctor-service):
```
GET /doctors/getAll
  └─ @Cacheable(doctors::'all') → hit Redis (10m TTL) else DB → cache

POST /doctors/create
  └─ @CacheEvict(doctors::'all') → write DB → invalidate cache so next read refreshes
```

## Local Run (dev)

1) Start infrastructure (examples)
- PostgreSQL instances (one per service) – adjust URLs in each `application.properties`.
- RabbitMQ in Docker: `docker run -d --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3-management`
- Redis stack: master + 3 slaves on `redis-network`, plus 3 Sentinels using `sentinel.conf` (see command above; repeat for sentinel2/3 with different ports/names).

2) Build & run services
- Java 17, Maven wrappers available.
- From each service folder:
  - `./mvnw clean package`
  - `./mvnw spring-boot:run`

## Event Lifecyle Example

```
Client → appointment-service (/api/appointments/create)
   ↳ PostgreSQL persist
   ↳ publish AppointmentCreatedEvent (routing key: appointment.created)
       ↳ notification-service logs intent
       ↳ audit-service writes audit log (ACK/NACK to DLX)
       ↳ report-service writes daily report row (ACK/NACK to DLX)
```
