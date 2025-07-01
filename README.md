# Employee Management Web Application

A Web application for managing employees

---

## Features

- Create, Read, Update, Delete employees
- Employee fields:
    - Employee ID (auto-generated)
    - Name
    - Email (should be unique)
    - Phone Number
    - Date of Joining

---

## Technology Stack

- Java 17, Servlets, JSP
- Oracle Database with stored procedures
- Frontend: JSP + jQuery/AJAX
- Maven for build and dependency management
- Docker for local development environment

---

## Prerequisites

- Docker & Docker Compose installed
- Java 17 JDK installed
- Maven installed

---

## How to Run

1. Build and start containers:

```bash
cd docker
./launch.sh
```

2. Access the app in your browser at:

      http://localhost:8080

     Switch between implementations with the links at the top-right (**Servlet/jQuery** and **DWR**)
