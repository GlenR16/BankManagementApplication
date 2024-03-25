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
- Transaction service is to be completed.
- User service is to fine tuned for better code practices.
- Account service is to fine tuned for better code practices.
- Card service is to fine tuned for better code practices.
- API Gateway is to be fine tuned for better code practices.
- All configuration must be added to Config server instead of the service itself.
- Feign client should be used instead of RestTemplate.
- Error responses & Custom Responses are to be written. 
- All exceptions are to be handled properly.
- All services must be dockerized.
- Unit Tests are to be written.
- Add role authorization to each endpoint.
- Initialize frontend.