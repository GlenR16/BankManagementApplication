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

## Tasks remaining
- User service is to fine tuned for better code practices.
- Account service is to fine tuned for better code practices.
- Card service is to fine tuned for better code practices.
- Transaction service is to fine tuned for better code practices.
- API Gateway is to be fine tuned for better code practices.
- All configuration must be added to Config server instead of the service itself.
- Webclient should be used instead of RestTemplate.
- Check for edge cases before saving data.
- All exceptions are to be handled properly.
- All services must be dockerized.
- Add remaining pages to frontend.
- Add Services Layer.
- Use at least 3 design patterns while implementing solution
- Transaction management within service
- Transaction management across services
- Health check and metric collection and monitoring
- Enabling SSL
- Asynchronous communication
- Real time updates to UI in case of multi user environment
- Role authentication must be added.

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