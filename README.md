# Introduction

This project is designed to read data from the `Sample_File.xls` Excel file and store it in a database. The primary functionalities include checking if the `KNIT_CARD_NUMBER` is present in the database, updating relevant columns if it exists, or inserting a new row if it does not. The application provides an insert/update summary report to the UI, indicating the number of rows inserted, updated, and any errors encountered during the process. Additionally, a Jasper Report is generated in PDF format.

## Prerequisites

- Java Development Kit (JDK) 17 or later
- MySQL database server
- [Optional] MySQL client (e.g., MySQL Workbench)
- [Optional] Git for cloning the repository

## Installation

1. Clone the repository.
2. Create a database named "customer_db".
3. Update the `spring.datasource.username` and `spring.datasource.password` in `application.properties` with your MySQL credentials.
4. Build and run the project.

## Third-Party Libraries

- Springdoc (Swagger) Dependency: `org.springframework.boot:spring-boot-starter-data-jpa`
- JasperReports Dependency: `org.apache.poi:poi-ooxml:4.1.2`
- Apache POI Dependency: `org.apache.poi:poi-ooxml:4.1.2`
- Lombok Dependency: `org.projectlombok:lombok`
- MySQL Connector/J Dependency: `com.mysql:mysql-connector-j`

## Testing

- Ensure the project is running.
- Access the Swagger UI on: http://localhost:8024/swagger-ui/index.html.
- Test it with the given Sample_File.xls and with the sheet named "Sheet1" from the workbook
- Test the provided API endpoints for data processing.
