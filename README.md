## System Overview
This architecture implements a modern microservices-based e-commerce system with the following components:

* API Gateway: Centralized entry point for all client requests
* OAuth2/KeyCloak: Handles authentication and authorization
* Product Service: Manages product catalog and information
* Inventory Service: Tracks product stock and availability
* Order Service: Processes customer orders
* Notification Service: Handles customer notifications
* Kafka: Event streaming platform for asynchronous communication

## Technology Stack

* Spring Boot: Framework for building microservices
* Spring Cloud: Provides tools for building distributed systems
* Keycloak: Open source identity and access management
* Kafka: Event streaming platform for asynchronous communication
* Docker & Docker Compose: Containerization and orchestration
* Databases: Each service has its own database (SQL/NoSQL)

Setup and Configuration

* Clone the repository
```bash
git clone https://github.com/LeShaurya/Product_Store_Application.git
cd Product_Store_Application
```
* Set-up environment variables in docker-compose.yaml

### Running with Keycloak
* Ensure creation of `myrealm` realm.
* creation of client (client-credential grant type)
* using the client-id and client-secret generate a token to be authorized to use the resource-server.

## Microservice Architecture
![Basic Architecture](images/basic_arch.jpg)

## Running Docker Compose
```bash
# Pull the latest versions of the images and start the services
docker-compose up --build

# To start the services in detached mode
docker-compose up -d

# To stop the running services
docker-compose down
```
