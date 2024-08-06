# EduManage - Institute Management System
EduManage is an Institute Management System built with Spring Boot (v3.3.2), MySQL for data storage, designed to manage institutes, including registration, modification, and retrieval of institute information. This application is secured using Spring Security and integrates with Docker for easy deployment.
## Table of Contents

- [Introduction](#introduction)
- [Features](#features)
- [Technologies Used](#TechnologiesUsed)
- [Getting Started](#getting-started)
- [Configuration](#Installation)
- [Database Schema](#Installation)
- [API Endpoints](#APIEndpoints)
- [Postman Collection](#PostmanCollection)
- [Security](#security)
- [Contributing](#contributing)
- [License](#license)

## Introduction

EduManage is designed to streamline the management of institutes, allowing for the registration, modification, and retrieval of institute details. This application ensures data security and provides easy deployment with Docker.

## Features

- **Admin**:
  - Add new Institutes
  - Modify existing institute information
  - Delete an Institute
  - Login

- **User**:
  - List all Institute
  - Retrieve institute details by ID
  - Login

## Technologies Used

- **Spring Boot 3.3.2**: Framework for building Java applications.
- **Spring Security**: Provides authentication and authorization.
- **JWT Authentication**: Secure token-based authentication mechanism.
- **MySQL**: Relational database management system.
- **JPA/Hibernate**: Java Persistence API for ORM (Object-Relational Mapping).
- **Docker**:  Containerization platform.
- **Maven**: Build and dependency management tool.

## Getting Started

### Prerequisites

- **Java 17 or higher**: Install Java Development Kit (JDK) from [Oracle's official website](https://www.oracle.com/java/technologies/javase-jdk17-downloads.html).
- **Maven**: Download from [official Maven website](https://maven.apache.org/download.cgi) or install via your package manager.
- **MySQL**: Download and install from [official MySQL website](https://dev.mysql.com/downloads/).
- **Docker**: Containerization platform.

### Installation

1. **Clone the repository**

   ```
   git clone https://github.com/suhanmhd/Edu-Manage.git
   
  ``

2. **Configure the database**
    Edit src/main/resources/application.properties to configure your MySQL database.
   
3. **Build the project**
   ```
   mvn clean install
 ``
4. **Run the application**
```
     mvn spring-boot:run
```
5. **Docker Configuration**
6. ```
    docker build -t edu-manage .
    docker run -p 9090:9090 edu-manage
   ```  

## API Endpoints
For detailed API documentation, please refer to the Swagger documentation linked below.

## Swagger Collection
The API endpoints are documented using Swagger. You can access the Swagger UI at:

  http://localhost:9090/swagger-ui.html
  




    
