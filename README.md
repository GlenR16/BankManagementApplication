# Bank Management System
This is a simple bank management system made as part of our training at Wissen Technology. It follows standard coding practices and uses a microservices architecture. 
- Status: In progress

## Instructions to run
1. Run docker containers PostgresDB and OpenZipkin.
2. Right click in java projects and run in fixed order:
 - config-server
 - service-registry
 - api-gateway
 - user-service
 - other services according to requirements.  
------------------------------------------------
Using Docker: 
1. Goto root folder with docker-compose file.
2. Run docker-compose up
- Logs will appear in the same terminal.
- Uses large amount of RAM and CPU resources, almost as if a game is running.
- Authentication is not working as webclient is not implemented in api-gateway.
- Better to run individual services as needed.

## Tasks remaining
- Add Services Layer.
- User service is to fine tuned for better code practices.
- Account service is to fine tuned for better code practices.
- Card service is to fine tuned for better code practices.
- Transaction service is to fine tuned for better code practices.
- Webclient should be used instead of RestTemplate.
- Check for edge cases before saving data.
- All exceptions are to be handled properly.
- Add remaining pages to frontend.
------------------------------------------------
- Enabling SSL
- Asynchronous communication
- Real time updates to UI in case of multi user environment
- Role authentication must be added.
- Use at least 3 design patterns while implementing solution
- Transaction management within service
- Transaction management across services

## Tasks Done
- ~~Define and document clear db design~~
- ~~Standard coding practice should be followed~~
- ~~Use SOLID principles while designing and implementing solutions~~
- ~~Implement using microservices architecture~~
- ~~Solution should have at least 2 microservices.~~
- ~~API gateway should be used to connect to these microservices from UI~~
- ~~Standard logging should be used~~
- ~~Authentication and Authorization should be implemented~~
- ~~Service registration and discovery~~
- ~~Load balancer setup~~
- ~~API Gateway is to be fine tuned for better code practices.~~
- ~~All configuration must be added to Config server instead of the service itself.~~
- ~~All services must be dockerized.~~
- ~~Health check and metric collection and monitoring~~


## Ports
- api-gateway:8080
- user-service:8081
- account-service:8082
- card-service:8083
- transaction-service:8084
- config-server:8088
- service-registry:8761
- postgres:5432
- zipkin:9411