# User Management service
Application created with Java, Spring (Web, JPA). You can build image via Google Jib.

The application user is able to manage users and their roles.

## How to run? 
Please follow these instructions. All commends should be provided in command prompt
### Build UserManagement service 
gradle build jibDockerBuild
### Run database and UserManagement (backend application)
docker-compose up


## Example endpoints
**Authentication is header based**   
Header

```
user-name : Admin
password : password
```
* Try to login: GET; http://localhost:8080/api/auth   
Receive auth token:
```
{
    "message": "e3afed0047b08059d0fada10f400c1e5"
}
```

* Create role: POST; http://localhost:8080/api/roles

  Header:

  ```
  user-name : Admin
  token : e3afed0047b08059d0fada10f400c1e5
  ```

  Request Body:
```
{
    "roleName": "TECHNICAL_USER",
    "permissionTypes": [
        "CREATE_USER",
        "UPDATE_USER",
        "LIST_USERS"
    ]
}
```
Response:
```
{
    "id": 2,
    "roleName": "TECHNICAL_USER",
    "permissionTypes": [
        "CREATE_USER",
        "UPDATE_USER",
        "LIST_USERS"
    ]
}
```
* Create user: POST; http://localhost:8080/api/usersHeader:

  Header:

  ```
  user-name : Admin
  token : e3afed0047b08059d0fada10f400c1e5
  ```

  Request Body:
```
{
    "userName": "Adrian",
    "password": "password1",
    "roleName": "TECHNICAL_USER"
}
```
Response
```
{
    "id": 2,
    "userName": "Adrian",
    "roleName": "TECHNICAL_USER"
}
```
* Try to delete as without permission granted: DELETE; http://localhost:8080/api/users/1
Response:
```
{
    "errorMessage": "Access denied for user Adrian"
}
```

**Application will be available under** http://localhost:8080/
Example endpoint:
* **Method**: GET; **Url**: http://localhost:8080/api/users

## Additional info
* In this project, the Lombok library was added. 
* Models are immutable objects, thanks to using **@Value** annotation. Because of that fields are private and final. 
* ***Sorry for missing tests, I was out of time.***# user-service
