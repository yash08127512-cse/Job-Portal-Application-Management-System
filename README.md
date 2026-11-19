# Job Portal & Application Management System

A Java full-stack portfolio project for browsing job listings, submitting applications, and tracking application progress. The application is structured around Spring Boot REST APIs, MySQL persistence, and a responsive HTML/CSS/JavaScript interface.

> **Project status:** Source code prepared for portfolio use. Local execution and end-to-end testing have not yet been verified. Please follow the setup steps and test the application before describing it as fully working.

## Key Features

- Browse available job listings
- Search jobs by keyword
- Filter listings by location and job type
- View job details and submit an application
- Validate candidate application fields
- Store job listings and applications in MySQL
- Track submitted applications using the candidate email address
- Demonstration employer interface to create listings and update application statuses
- Responsive frontend for desktop and mobile layouts

## Technology Stack

| Layer | Technologies |
|---|---|
| Backend | Java 17, Spring Boot 3, Spring Web |
| Database access | Spring JDBC |
| Database | MySQL |
| Frontend | HTML5, CSS3, JavaScript |
| Build tool | Maven |

**Note:** This project uses Spring JDBC; it does not use Hibernate or Spring Data JPA.

## Application Architecture

1. The browser loads the frontend from Spring Boot's `static` resources.
2. JavaScript sends HTTP requests to the REST API controllers.
3. Controllers validate incoming request data and call repository classes.
4. Repository classes use Spring JDBC to read and write MySQL records.
5. API responses are returned to the frontend as JSON.

## Prerequisites

- JDK 17 or later
- MySQL Server
- Maven 3.6+ (or an IDE with Maven support)
- IntelliJ IDEA, Eclipse, or VS Code with Java support

## Run Locally

### 1. Clone the repository

```bash
git clone https://github.com/YOUR-USERNAME/YOUR-REPOSITORY.git
cd JobPortalApplicationManagementSystem
```

Replace the URL and folder name with the repository details you choose on GitHub.

### 2. Create the database

Run this command in MySQL Workbench or the MySQL command line:

```sql
CREATE DATABASE job_portal_db;
```

### 3. Configure the database password

The application reads the database password from the `DB_PASSWORD` environment variable. Set it before starting the app.

**Windows PowerShell:**

```powershell
$env:DB_PASSWORD="your_mysql_password"
```

**macOS/Linux:**

```bash
export DB_PASSWORD="your_mysql_password"
```

The default database username in `application.properties` is `root`. If your MySQL username is different, update `spring.datasource.username` in that file. Never commit real credentials to GitHub.

### 4. Start the application

From the folder containing `pom.xml`, run:

```bash
mvn spring-boot:run
```

Or import the folder as a Maven project in your IDE and run `JobPortalApplication.java`.

When startup succeeds, open:

```text
http://localhost:8080
```

The application is configured to initialize its database schema and sample data at startup. Verify the console output and test the UI/API locally before treating the setup as complete.

## REST API Reference

| Method | Endpoint | Purpose |
|---|---|---|
| GET | `/api/jobs` | Retrieve job listings |
| GET | `/api/jobs?keyword=java` | Search job listings |
| GET | `/api/jobs?location=noida&jobType=INTERNSHIP` | Filter job listings |
| GET | `/api/jobs/{id}` | Retrieve one job |
| POST | `/api/jobs` | Create a job listing |
| PUT | `/api/jobs/{id}` | Update a job listing |
| DELETE | `/api/jobs/{id}` | Delete a job listing |
| POST | `/api/applications` | Submit a job application |
| GET | `/api/applications` | Retrieve applications (demo employer view) |
| GET | `/api/applications?email=name@example.com` | Find applications by email |
| PATCH | `/api/applications/{id}/status?status=SHORTLISTED` | Update application status |
| DELETE | `/api/applications/{id}` | Delete an application |

### Example: create a job listing

```json
{
  "title": "Java Developer Intern",
  "company": "Example Technologies",
  "location": "Noida",
  "jobType": "INTERNSHIP",
  "salary": 12000,
  "description": "Assist with Java and REST API development."
}
```

### Example: submit an application

```json
{
  "jobId": 1,
  "candidateName": "Aarav Sharma",
  "email": "aarav@example.com",
  "phone": "9876543210",
  "resumeLink": "https://example.com/resume",
  "coverLetter": "I am interested in this opportunity."
}
```

## Project Structure

```text
JobPortalApplicationManagementSystem/
├── pom.xml
├── README.md
├── .gitignore
└── src/
    └── main/
        ├── java/com/yash/jobportal/
        │   ├── JobPortalApplication.java
        │   ├── controller/
        │   ├── dto/
        │   ├── model/
        │   └── repository/
        └── resources/
            ├── application.properties
            ├── schema.sql
            ├── data.sql
            └── static/
                ├── index.html
                ├── styles.css
                └── app.js
```

## Current Limitations & Next Improvements

- The employer panel is a demonstration interface and is not protected by authentication or role-based authorization.
- Add secure login, password hashing, and employer/candidate roles before any real-world deployment.
- Add automated integration tests for database operations and REST endpoints.
- Test validation, error handling, and responsive layouts in a local environment.
- Add screenshots after successfully running the application.

## Portfolio Summary

The Job Portal & Application Management System is a Java full-stack web application concept that brings job discovery and application tracking into one interface. It demonstrates REST API design with Spring Boot, relational database operations using Spring JDBC and MySQL, and frontend integration using HTML, CSS, and JavaScript.

## GitHub Repository Description

Java full-stack job portal using Spring Boot REST APIs, Spring JDBC, MySQL, HTML, CSS and JavaScript. Includes job search, application submission and application status tracking.

## Suggested GitHub Topics

`java` `spring-boot` `mysql` `spring-jdbc` `rest-api` `html` `css` `javascript` `full-stack` `job-portal`
