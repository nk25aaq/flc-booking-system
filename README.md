# Furzefield Leisure Centre Booking System

A command-line Java application for managing group exercise lesson bookings.

## Requirements

- Java 17 or higher
- Maven 3.6 or higher (only needed to build from source)

## Running the program

A pre-built executable JAR is included. Run it with:

```
java -jar target/flc-booking-system-1.0-jar-with-dependencies.jar
```

## Building from source

```
mvn clean package
```

This compiles the code, runs all tests, and produces the JAR in `target/`.

## Running tests only

```
mvn test
```

## What the program does

The system manages group exercise lesson bookings for a leisure centre. On startup it loads 10 pre-registered members and a timetable of 48 lessons across 8 weekends (May and June).

Menu options:

1. Book a group exercise lesson
2. Change a booking
3. Cancel a booking
4. Attend a lesson (includes writing a review and rating)
5. Monthly lesson report
6. Monthly champion lesson type report
0. Exit

## Project structure

```
src/main/java/flc/
    model/        - domain classes (Member, Lesson, Booking, Review, enums)
    manager/      - business logic (BookingManager, Timetable)
    report/       - report generation (ReportGenerator)
    data/         - sample data loader (DataSeeder)
    ui/           - command-line interface (CLI)
    Main.java     - entry point
src/test/java/flc/
    - JUnit 5 test classes
```
