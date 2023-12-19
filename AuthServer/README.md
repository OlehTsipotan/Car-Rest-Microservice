# AuthServer

Service which is taking responsibility for authentification of users into the system. Working as a getaway between client and **Auth0**. 
Provides client/user with Bearer JWT token for [Resource Server](https://github.com/OlehTsipotan/Car-Rest-Microservice/tree/main/ResourceServer).

## How to use
- Service accessible by:
```
http://localhost:8081/token
```
- The **ONLY** working user credentials are:
  - username: admin
  - password: MyPassword!

## Swagger
- Can be reached by default: 
```
http://localhost:8081/swagger-ui/index.html
``` 
