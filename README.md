# TrekShield : sos-alert-system

## Overview

This project is a Spring Boot backend service.

The service simulates SOS alerts sent from GPS/satellite tracking devices used during trekking expeditions. It supports device assignment management, alert intake, duplicate detection, concurrency-safe alert claiming, and automatic escalation of unacknowledged alerts.

---

## Features

- Device Management
- Trek Group Management
- Trekker Management
- Order Management
- Device Assignment Management
- SOS Alert Intake
- Device-to-Order Resolution
- Duplicate Alert Detection
- Concurrency-safe Alert Claiming
- Automatic Alert Escalation
- Alert Resolution
- Swagger/OpenAPI Documentation
- Dockerized Deployment with PostgreSQL

---

## Technology Stack

- Java 21
- Spring Boot
- Spring Data JPA
- PostgreSQL
- Maven
- Docker & Docker Compose
- Springdoc OpenAPI (Swagger)
- Lombok

---

## Project Structure

```
src
├── common
├── config
├── feature
│   ├── alert
│   ├── device
│   ├── deviceAssignment
│   ├── order
│   ├── trekGroup
│   └── trekker
└── shared
```

---

## Running the Application

### 1. Clone the repository

```bash
git clone https://github.com/Sampannasapkota/sos-alert-system.git

cd sos-alert-system
```

### 2. Create environment file

Copy

```
.env.example
```

to

```
.env
```

Update values if necessary.

### 3. Start the application

```bash
docker compose up --build
```

The application and PostgreSQL database will start automatically.

---

## Swagger UI

After the application starts:

```
http://localhost:8080/swagger-ui.html
```

OpenAPI JSON:

```
http://localhost:8080/v3/api-docs
```

---

## Seed Data

If database seeding is enabled, the application automatically creates:

- Trek Groups
- Trekkers
- Devices
- Orders
- Device Assignments

No SOS alerts are seeded so they can be tested through the API.

---

## Main API Endpoints

### Device

```
POST   /api/v1/devices
GET    /api/v1/devices
GET    /api/v1/devices/{id}
PUT    /api/v1/devices/{id}
DELETE /api/v1/devices/{id}
```

### Trek Groups

```
POST   /api/v1/trek-groups
GET    /api/v1/trek-groups
PUT    /api/v1/trek-groups/{id}
DELETE /api/v1/trek-groups/{id}
```

### Orders

```
POST   /api/v1/orders
GET    /api/v1/orders
PUT    /api/v1/orders/{id}
DELETE /api/v1/orders/{id}
```

### Device Assignments

```
POST   /api/v1/device-assignments
GET    /api/v1/device-assignments
PUT    /api/v1/device-assignments/{id}
DELETE /api/v1/device-assignments/{id}
```

### SOS Alerts

```
POST   /api/v1/alerts
GET    /api/v1/alerts
GET    /api/v1/alerts/{id}
POST   /api/v1/alerts/{id}/claim
POST   /api/v1/alerts/{id}/resolve
```

---

## Alert Lifecycle

```
RECEIVED
   │
   ├───────────────┐
   │               │
CLAIMED      ESCALATED
   │               │
   └───────┬───────┘
           │
       RESOLVED
```

---

## Assumptions

- A device may be assigned to multiple orders over time.
- A device is assigned to only one order during a specific time period.
- Multiple trekkers may share a single device through their trek group.
- Duplicate SOS alerts from the same device within a two-minute window are treated as retransmissions.
- Alerts not claimed within the configured escalation window are automatically escalated.

---

## Future Improvements

Given additional time, the following improvements could be made:

- Authentication & Authorization
- SMS / Email notifications
- WebSocket live alert updates
- Redis caching
- RabbitMQ/Kafka event processing
- Comprehensive integration and concurrency testing
- Metrics & monitoring

---

## Author

Sampanna Sapkota
