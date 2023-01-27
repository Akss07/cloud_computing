# Cloud Native Web Application

### This project contains a Web Application which hosts REST API to do operations on user data.

## Technologies Used for this Project
- Spring Boot, Spring MVC Framework
- JPA, Junit
- Java 11

## Prerequisites 
1. Install IDE - Intellij
2. Download the folder and open project in IDE
3. Install MySQL Database. Follow this [link](https://dev.mysql.com/doc/mysql-osx-excerpt/5.7/en/osx-installation-pkg.html) to install
4. Install Postman to test Api

## Steps to Build , Install and Deploy the Application Locally
1. Clone the application - `git clone git@github.com:Akss07/webapp.git`
2. Once the project get imported into IDE
3. Create the database - `create database userDB`
4. Update mysql username and password as per your installation
    open `src/main/resources/application.properties`
    change `spring.datasource.username` and `spring.datasource.password` as per your mysql installation
5. Build the project - by clicking the **`Build`** button on top nav bar and then selecting "**build project**"
6. If build is successful run the project, if it is unsuccessful "**invalidate caches**" from the file tab and restart the IDE 
7. To run the application, click on **`Run`** then select `Run CloudApplication`

## The app defines following REST APIs.
`GET /healthz`

`POST /v1/account/`

`GET /v1/account/{account_id}`

`PUT /v1/account/{account_id}`

## Test the REST API
- You can test them using POSTMAN or through CURL

- To test `GET /healthz` - `http://localhost:8080/healthz`
- To test `POST /v1/account/` - `http://localhost:8080/v1/account/`

  `{
  "first_name": "Akanksha",
  "last_name": "Gupta",
  "user_name": "akanksha07@gmail.com",
  "password": "hello1234!!"
  }`

- To Test `GET /v1/account/{account_id}` - `http://localhost:8080/v1/account/cdde0959-1d02-4b13-9b3e-0b93142a547f`

- To test `PUT /v1/account/{account_id}` - `http://localhost:8080/v1/account/cdde0959-1d02-4b13-9b3e-0b93142a547f`

  `{
  "first_name": "Akanksha",
  "last_name": "Gupta",
  "user_name": "akanksha07@gmail.com",
  "password": "hello1234!!"
  }`

 # Test Document Upload/Get/Delete Service

- To Test `POST /v1/documents/` - `http://44.200.19.219:8080/v1/documents/`
    Upload any file/image from the postman
- To Test `Get /v1/documents/{doc_id}` - `http://44.200.19.219:8080/v1/documents/2b17d24d-58b5-4c7d-b090-2be3a578b085`
- To Test `Delete /v1/documents/{doc_id}` - `http://44.200.19.219:8080/v1/documents/2b17d24d-58b5-4c7d-b090-2be3a578b085`

  # To Import Certificate
 $ aws acm import-certificate --certificate fileb://Certificate.pem \
   --private-key fileb://PrivateKey.pem  