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
2. Run "docker-compose up --build"
If Build issue then do "mvn wrapper:wrapper" in each service folder
- Logs will appear in the same terminal.
- Uses large amount of RAM and CPU resources.

## Tasks remaining
- ALL DONE

## Tasks Done
- ~~Complete frontend.~~
- ~~Webclient should be used instead of RestTemplate.~~
- ~~Role authentication must be added.~~
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
- ~~Add Services Layer.~~
- ~~Check for edge cases before saving data.~~
- ~~All exceptions are to be handled properly.~~
- ~~Transaction management within service~~
- ~~Transaction management across services~~

## Ports
- frontend:80
- api-gateway:8080
- user-service:8081
- account-service:8082
- card-service:8083
- transaction-service:8084
- config-server:8088
- service-registry:8761
- postgres:5432
- zipkin:9411