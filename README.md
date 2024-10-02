# CARD COST API
Developed by Sebastián Rodriguez

![Language Java](https://img.shields.io/badge/Language-Java-blue.svg)
![Technology Maven](https://img.shields.io/badge/Project-Maven-blue.svg)
![Framework](https://img.shields.io/badge/Framework-SpringBoot:3.3.3-green)
![IDE](https://img.shields.io/badge/IDE-IntelliJIDEA:2024.2.1-green)

This is a Spring Boot project that provides REST API endpoints for managing card costs and user data.
It demonstrates the use of security, caching, JPA, and a layered architecture.

---

- [1. Requirements](#1-Requirements)
- [2. Running the application locally](#2-Running-the-application-locally)
- [3. Database](#3-Database)
    - [3.1. MySQL](#31-MySQL)
    - [3.2. H2](#32-H2)
- [4. Using the application](#4-Using-the-application)
    - [4.1. Using Maven or IntelliJ IDEA](#41-Using-Maven-or-IntelliJ-IDEA)
    - [4.2. Using Docker](#42-Using-Docker)
    - [4.3. Authentication](#43-Authentication)
      - [4.3.1. Log in](#431-Log-in)
      - [4.3.2. Sign up](#432-Sign-up)
    - [4.4. Users](#44-Users)
      - [4.4.1. Create administrator](#441-create-administrator)
      - [4.4.2. Get all users](#442-Get-all-users)
    - [4.5. BIN info](#45-BIN-info)
      - [4.5.1. Create BIN info](#451-Create-BIN-info)
      - [4.5.2. Get all BIN info](#452-Get-all-BIN-info)
      - [4.5.3. Get BIN info by BIN](#453-Get-BIN-info-by-BIN)
      - [4.5.4. Update BIN info](#454-Update-BIN-info)
      - [4.5.5. Delete BIN info](#455-Delete-BIN-info)
    - [4.6. Card Cost](#46-Card-Cost)
      - [4.6.1. Create card cost](#461-Create-card-cost)
      - [4.6.2. Get all card cost](#462-Get-all-card-cost)
      - [4.6.3. Get card cost by identifier](#463-Get-card-cost-by-identifier)
      - [4.6.4. Get card cost by country](#464-Get-card-cost-by-country)
      - [4.6.5. Get card cost by cost](#465-Get-card-cost-by-cost)
      - [4.6.6. Update card cost](#466-Update-card-cost)
      - [4.6.7. Delete card cost](#467-Delete-card-cost)
- [5. Testing](#5-Testing)
---

## 1 Requirements
For building and running the application you need the System Requirements describe in [Spring Boot website](https://docs.spring.io/spring-boot/system-requirements.html).

Additionally, you need to install the following software:
* [MySQL](https://dev.mysql.com/downloads/mysql/)
* [Postman](https://www.postman.com/downloads/)
* [Docker](https://docs.docker.com/get-started/get-docker/)
* [Docker Compose](https://docs.docker.com/compose/install/standalone/)

---

## 2 Running the application locally
For running the application locally, you must set the following environment variables:
* ***SCOPE*** to specify the execution environment.
* ***DB_HOST*** to specify the host where MySQL will be running.
* ***DB_PORT*** to specify the port where MySQL will be running.
* ***DB_NAME*** to specify the name of MySQL database.
* ***DB_URL*** to specify the URL of MySQL database.
* ***DB_USER*** to specify MySQL user.
* ***DB_PASS*** to specify MySQL password.

To set the variables, they must be added to the `~/.bashrc` file, for example using `nano`:
```shell
$ nano ~/.bashrc
```
and then adding the following lines:
```shell
$ export SCOPE="prod"
$ export DB_HOST="localhost"
$ export DB_PORT="3306"
$ export DB_NAME="card_cost"
$ export DB_URL="jdbc:mysql://${DB_HOST}:${DB_PORT}/${DB_NAME}?createDatabaseIfNotExist=true"
$ export DB_USER="USER_FOR_MY_SQL"
$ export DB_PASSWORD="PASSWORD_FOR_MY_SQL"
```

Then run this command to apply changes:
```shell
$ source ~/.bashrc
```

Finally, you can verify variables were configured by executing:
```shell
$ echo $DB_HOST
```

If they have been set but MySQL still doesn't raise, restart your PC and run the application again.

---

## 3 Database
In this project we use MySQL database for production (local environment) and H2 database for integration tests.

### 3.1 MySQL
For production (local environment), we use MySQL database.

The configuration already create the database and tables, so you don't need to create them manually:
```yml
spring:
  datasource:
    url: ${DB_URL}
    username: ${DB_USER}
    password: ${DB_PASSWORD}
    driver-class-name: com.mysql.cj.jdbc.Driver
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
```

The above values are set in:

`src/main/resources/application-prod.yml`

### 3.2 H2
For execute integration tests, we use H2 database.

You can access to H2 following these steps:
1. Execute the tests in Run or Debug mode.
2. When the tests are running, you can see the port.
3. Open a browser and go to `http://localhost:{port}/h2-console`.
4. In the login page, set the following values:
    - ***Driver Class***: `org.h2.Driver`
    - ***JDBC URL***: `jdbc:h2:mem:card_cost_integration_test`
    - ***User Name***: `admin`
    - ***Password***: `admin`

The above values are set in:

`src/main/resources/application-integration_test.yml`

If everything went well, it means that the tables have been generated in the local database with preloaded data.
This data is located in:

`src/main/resources/data-integration_test.sql`

---

## 4 Using the application
The application was developed using Role-Based Authorization (RBA) and Json Web Token (JWT) for authentication and authorization,
so you must log in to interact with it. This approach was based on the interaction with front-end applications.

Also, the application has an API Key-Based Authorization. This approach was based on the interaction with internal applications.
Users who can interact with this kind of authorization have the minimum privileges to follow the best practices.

There are three types of users, from highest to lowest privilege:
- ***SUPER ADMINISTRATOR***
- ***ADMINISTRATOR***
- ***USER***

When the application starts to run, the above roles are created by default. Also, the user ***superadmin***
with password ***123456*** and role ***SUPER_ADMIN*** are created.

The application has the feature to add new users with ***ADMIN*** or ***USER*** roles.

In addition, the different endpoints are protected to receive requests from users with certain roles.
Below is a list of the different endpoints and the roles required to access them.

**NOTES**:
* All passwords are stored encrypted using BCryptPasswordEncoder.
* Independent of the way to use the application, if everything goes well, the application will start running
on http://localhost:8080.

### 4.1. Using Maven or IntelliJ IDEA
To run the Spring Boot application, you must execute the following command in the root directory of the project:
```shell
$ mvn spring-boot:run
```

Alternatively, you can run the application using IntelliJ IDEA. To do this, you must open the project and run
the `CardCostApplication` class.

### 4.2. Using Docker
To run the Spring Boot application using Docker, you must execute the following command in the root directory of
the project:
```shell
$ mvn clean package
```
This command generates the .jar file which will be used to create the server container.

Then, in the same path, you must execute the following command:
```shell
$ sudo docker-compose build
```
This command creates images for use later by server and MySQL containers.

Finally, you must execute the following command:
```shell
$ sudo docker-compose up
```
This command creates server and MySQL containers.

### 4.3. Authentication
This functionality handles user authentication logic.
The application allows to you to registration of new users and authentication of existing users.

The Postman collection included in `docs/postman/Etraveli-Group.postman_collection.json` has a folder called
Authentication, in which there is a call to user log in request, and through a script in the scripts tab, set a
collection variable in `{{bearer_token}}`, and then in each call to the application is used as a value of
authorization header:

![PostLogin.png](docs/images/PostLogin.png)

![ScriptPostLogin.png](docs/images/ScriptPostLogin.png)

![SetBearerToken.png](docs/images/SetBearerToken.png)

Finally, we have folders with each kind of authorization (e.g. JWT and API Key). The collection
variable called `{{api_key}}` is used as a value of `x-api-key` header:

![SetApiKey.png](docs/images/SetApiKey.png)

#### 4.3.1. Log in

* **Verb:** `POST`

* **Url:** `http://localhost:8080/payment-cards-cost/auth/login`

* **Function:** Allow a user to log in to the application

* **Required role user:** `N/A`

* **Example Body:**
```json
{
  "username": "user_1",
  "password": "123456"
}
```

* **Example Response:**
```json
{
  "id": 3,
  "username": "user_1",
  "token": "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyXzEiLCJhdXRob3JpdGllcyI6WyJST0xFX1VTRVIiXSwiaWF0IjoxNzI2NTk2OTQwLCJleHAiOjE3MjY1OTc1NDB9.Qwb2nEg0qluhqpqs4J8fAtun5pfTPFZlLWND5-Jx8TpQdiRqaqHYUJZz7nCxpQGnDk1qHUh_RNk6Lo-UsxBQxA",
  "roles": [
    {
      "name": "USER",
      "description": "Default user role"
    }
  ]
}
```

#### 4.3.2. Sign up

* **Verb:** `POST`

* **Url:** `http://localhost:8080/payment-cards-cost/auth/login`

* **Function:** Create a new user with default ***USER*** role

* **Required role user:** `N/A`

* **Example Body:**
```json
{
  "username": "user_2",
  "password": "123456"
}
```

* **Example Response:**
```json
{
  "username": "user_2",
  "password": "$2a$10$gZxDRxWhGyLvA2MJkenOCOjRFelSONTqCxeGGvVZeVxXnq1bSrPXK",
  "user_roles": [
    {
      "name": "USER",
      "description": "Default user role"
    }
  ]
}
```

### 4.4. Users
This functionality handles user logic. The application allows to you to create
a new administrator and list all existing users.

#### 4.4.1. Create administrator

* **Verb:** `POST`

* **Url:** `http://localhost:8080/payment-cards-cost/users/admin`

* **Function:** Create a new administrator user with ***ADMIN*** role

* **Required role user:** `SUPER_ADMIN`

* **Example Body:**
```json
{
  "username": "admin_1",
  "password": "123456"
}
```

* **Example Response:**
```json
{
  "username": "admin_1",
  "password": "$2a$10$InPauTbYZwOzVNA.fWWHc.hBocj9kRdWusSdwK/yrJ7lVnT5qXZ86",
  "roles": [
    {
      "name": "ADMIN",
      "description": "Administrator role"
    }
  ]
}
```

#### 4.4.2. Get all users

* **Verb:** `GET`

* **Url:** `http://localhost:8080/payment-cards-cost/users/all`

* **Function:** Return all users in the application

* **Required role user:** `SUPER_ADMIN` - `ADMIN`

### 4.5. BIN info
This functionality handles BIN info logic.
The application allows to you to perform CRUD operations on BIN info and this information is using to avoid overload
the external API call to https://lookup.binlist.net.

#### 4.5.1. Create BIN info

* **Verb:** `POST`

* **Url:** `http://localhost:8080/payment-cards-cost/bin-info`

* **Function:** Create new BIN info

* **Required role user:** `ADMIN`

* **Example Body:**
```json
{
  "bin": 421821,
  "country": "UY",
  "cost": 10.0
}
```

* **Example Response:**
```json
{
  "bin": 421821,
  "country": "UY",
  "cost": 10.0
}
```

#### 4.5.2. Get all BIN info

* **Verb:** `GET`

* **Url:** `http://localhost:8080/payment-cards-cost/bin-info/all`

* **Function:** Return all the BINs info

* **Required role user:** `SUPER_ADMIN` - `ADMIN` - `USER`

#### 4.5.3. Get BIN info by BIN

* **Verb:** `GET`

* **Url:** `http://localhost:8080/payment-cards-cost/bin-info/{bin}`

* **Function:** Return the BIN info by `bin` BIN

* **Required role user:** `SUPER_ADMIN` - `ADMIN` - `USER`

#### 4.5.4. Update BIN info

* **Verb:** `PUT`

* **Url:** `http://localhost:8080/payment-cards-cost/bin-info/{idBinInfo}`

* **Function:** Update the BIN info by `idBinInfo` identifier

* **Required role user:** `ADMIN`

* **Example Body:**
```json
{
  "bin": 421821,
  "country": "UY",
  "cost": 5.0
}
```

* **Example Response:**
```json
{
  "id": 1,
  "bin": 421821,
  "country": "UY",
  "cost": 5.0
}
```

#### 4.5.5. Delete BIN info

* **Verb:** `DELETE`

* **Url:** `http://localhost:8080/payment-cards-cost/bin-info{idBinInfo}`

* **Function:** Delete the BIN info by `idBinInfo` identifier

* **Required role user:** `ADMIN`

* **Example Response:**
```json
{
  "deleted": true
}
```

### 4.6. Card Cost
This functionality handles card cost logic.
The application allows to you to perform CRUD operations on card costs.

#### 4.6.1. Create card cost

* **Verb:** `POST`

* **Url:** `http://localhost:8080/payment-cards-cost`

* **Function:** Create new card cost

* **Required role user:** `ADMIN`

* **Example Body:**
```json
{
  "card_number": 4218210123456789
}
```

* **Example Response:**
```json
{
  "country": "UY",
  "cost": 10.0
}
```

#### 4.6.2. Get all card cost

* **Verb:** `GET`

* **Url:** `http://localhost:8080/payment-cards-cost/all`

* **Function:** Return all card costs

* **Required role user:** `SUPER_ADMIN` - `ADMIN` - `USER`

#### 4.6.3. Get card cost by identifier

* **Verb:** `GET`

* **Url:** `http://localhost:8080/payment-cards-cost/{idCardCost}`

* **Function:** Return the card cost by `idCardCost` identifier

* **Required role user:** `SUPER_ADMIN` - `ADMIN` - `USER`

#### 4.6.4. Get card cost by country

* **Verb:** `GET`

* **Url:** `http://localhost:8080/payment-cards-cost/country?country=XX`

* **Function:** Return the card cost by `XX` country

* **Required role user:** `SUPER_ADMIN` - `ADMIN` - `USER`

#### 4.6.5. Get card cost by cost

* **Verb:** `GET`

* **Url:** `http://localhost:8080/payment-cards-cost/cost?cost=YY.ZZ`

* **Function:** Return the list of countries that have `YY.ZZ` as cost

* **Required role user:** `SUPER_ADMIN` - `ADMIN` - `USER`

#### 4.6.6. Update card cost

* **Verb:** `PUT`

* **Url:** `http://localhost:8080/payment-cards-cost/{idCardCost}`

* **Function:** Update the card cost by `idCardCost` identifier

* **Required role user:** `ADMIN`

* **Example Body:**
```json
{
  "country": "UY",
  "cost": 5.0
}
```

* **Example Response:**
```json
{
  "country": "UY",
  "cost": 5.0
}
```

#### 4.6.7. Delete card cost

* **Verb:** `DELETE`

* **Url:** `http://localhost:8080/payment-cards-cost/{idCardCost}`

* **Function:** Delete the card cost by `idCardCost` identifier

* **Required role user:** `ADMIN`

* **Example Response:**
```json
{
  "deleted": true
}
```

---

## 5. Testing

The application has unit, integration, and performance tests.

To run the tests, you must execute the following command in the root directory of the project:
```shell
$ mvn test
```

Alternatively, you can run the application using IntelliJ IDEA. To do this, you must open the project and run
manually tests classes.

As said previously, the integration tests use the H2 database. On the other hand, the unit tests use Mockito for mocking
the dependencies.

Also, was developed a performance test to check a GET method application performance,
for concurrent 7000 Request Per Minute (RPM).

About the above, for the goal of having a better performance and high availability, the application
was developed using an In-Memory Caching Caffeine Cache provider.

For the reality of the application, this is a good approach because it offers better performance, memory management,
and flexibility even for small, static datasets. Also, if the application will start to grow, it's more scalable
than default cache (ConcurrentMapCacheManager) and is more efficient than other providers like Redis.

To justify the use of cache, performance tests also were performed using JMeter, for `listCardCost` (GET) and
`updateCardCost` (PUT) methods.
Below is the result of each method without using cache:

![listCardCostWithoutCache.png](docs/images/listCardCostWithoutCache.png)

![updateCardCostWithoutCache.png](docs/images/updateCardCostWithoutCache.png)

Here are the results of each method using cache:

![listCardCostWithCache.png](docs/images/listCardCostWithCache.png)

![updateCardCostWithCache.png](docs/images/updateCardCostWithCache.png)

As can be seen, using cache decreases the average response time and increases the throughput, indicating that the
cached solution improves efficiency.