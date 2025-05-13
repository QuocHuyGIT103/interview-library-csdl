Library Database Demo (JDBC, JPA, and ACID)
This project demonstrates a Library Management System using MySQL, JDBC, and JPA (Hibernate), with a focus on ACID transaction properties. It showcases database concepts, Object-Relational Mapping (ORM), and Maven dependency management.
Features

Manage books, members, and borrow records in a MySQL database.
Implement CRUD operations using JDBC and JPA.
Demonstrate ACID properties (Atomicity, Consistency, Isolation, Durability) through transaction management.
Use Maven for dependency management.

ACID Properties Demonstrated

Atomicity: Ensures borrow transactions (updating book status and adding borrow record) are fully completed or rolled back.
Consistency: Enforces constraints like foreign keys and unique ISBNs.
Isolation: Uses MySQL's REPEATABLE READ isolation level to handle concurrent transactions.
Durability: Relies on InnoDB to persist data after commit.

Prerequisites

MySQL Server and MySQL Workbench.
Java JDK 11 or higher.
Maven 3.6.0 or higher.

Setup

Install MySQL and create a database:CREATE DATABASE LibraryDB;

Run SQL scripts in sql/ to create tables and insert data:mysql -u root -p LibraryDB < sql/create_tables.sql
mysql -u root -p LibraryDB < sql/insert_data.sql

Update database credentials:
For JDBC: Update src/main/java/library/LibraryJDBCDemo.java.
For JPA: Update src/main/resources/META-INF/persistence.xml.

Build and run with Maven:mvn clean install
mvn exec:java -Dexec.mainClass="library.LibraryJDBCDemo"
mvn exec:java -Dexec.mainClass="library.LibraryJPADemo"

Project Structure
LibraryDBJDBCJPADemo/
├── src/
│ ├── main/
│ │ ├── java/
│ │ │ ├── library/
│ │ │ │ ├── LibraryJDBCDemo.java
│ │ │ │ ├── Book.java
│ │ │ │ ├── LibraryJPADemo.java
│ │ ├── resources/
│ │ │ ├── META-INF/
│ │ │ │ ├── persistence.xml
├── sql/
│ ├── create_tables.sql
│ ├── insert_data.sql
├── pom.xml
├── README.md
├── .gitignore

Author

[Your Name] (https://github.com/your-username)

This project was created to demonstrate JDBC, JPA, and ACID properties for a Java internship interview.
