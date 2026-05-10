# HealthBridge Hospital Patient Management System

## Overview

HealthBridge Hospital Patient Management System is a legacy Java application that manages patient information, appointments, medical records, and billing for a hospital. Built over several years with contributions from multiple development teams, this system serves as a real-world example of a monolithic application with various architectural and code quality issues suitable for re-engineering analysis.

## System Description

### What the System Does

The HealthBridge system is designed to handle the core operational functions of a hospital patient management workflow. It enables hospital staff to register new patients with comprehensive personal and insurance information, schedule and manage appointments between patients and doctors, maintain detailed medical records including diagnoses and treatment plans, and track all patient billing and payment processing. The system also provides notification services to keep patients informed about their appointments, billing updates, and other important hospital communications.

### Architecture and Components

The system is organized into several interconnected modules that work together to support hospital operations:

- **Patient Management Module**: Core patient information storage with registration, search, and update capabilities. The `PatientManager` class handles all patient-related operations with features like patient registration with full demographic data capture, search functionality based on multiple criteria, and patient status tracking.

- **Medical Records Module**: Maintains comprehensive medical histories including diagnoses, test results, prescriptions, and clinical notes. The `MedicalRecord` class stores detailed information about patient medical encounters and can be archived for historical reference.

- **Appointment Scheduling Module**: Manages the scheduling and tracking of patient-doctor appointments. The `AppointmentScheduler` class interacts with the `PatientManager` to verify patient existence, handle appointment confirmations and cancellations, track appointment status changes, and manage appointment reminders.

- **Billing and Finance Module**: Processes all billing operations including invoice generation, payment tracking, and revenue reporting. The `BillingService` class creates invoices, records payments, tracks outstanding balances, and generates financial statistics by department.

- **Notification Service**: Sends communications to patients via multiple channels (SMS, Email). The `NotificationService` class tracks notification delivery, maintains audit logs of all notifications sent, and provides statistics on notification distribution.

- **Database Access Layer**: Provides database connectivity and query management. The `DatabaseConnection` class uses a singleton pattern for managing database connections, implements simple query caching, and maintains connection statistics.

## Project Statistics

- **Total Lines of Application Code**: 2,847 lines (excluding tests and auto-generated files)
- **Number of Java Classes**: 14 distinct classes
- **Number of Distinct Packages**: 7 packages
- **Java Version**: 11 (Java SE 11)
- **Build Tool**: Maven 3.6+

### Class Breakdown

| Package | Classes | Purpose |
|---------|---------|---------|
| `com.healthbridge.patient` | Patient, PatientManager | Patient entity and management |
| `com.healthbridge.medical` | MedicalRecord | Medical records management |
| `com.healthbridge.appointment` | Appointment, AppointmentScheduler | Appointment scheduling |
| `com.healthbridge.billing` | Invoice, Payment, BillingService | Billing operations |
| `com.healthbridge.notification` | Notification, NotificationService | Patient notifications |
| `com.healthbridge.database` | DatabaseConnection | Database connectivity |
| `com.healthbridge` | HealthBridgeApplication | Main application entry point |

## Code Quality Issues (Intentionally Included for Analysis)

This project is intentionally designed to contain various code smells and architectural issues for re-engineering analysis:

### Large Classes and Long Methods

**PatientManager** is a particularly large class (400+ lines) that violates the Single Responsibility Principle. It handles patient registration, updates, searches, status tracking, access logging, and audit trails all in a single class. This includes:

- `registerNewPatient()` method (150+ lines): Combines validation, entity creation, database storage, notification, and audit logging
- `updatePatientInformation()` method (180+ lines): Contains duplicated validation logic and mixed concerns
- Duplicated validation logic across multiple methods
- Direct mixing of business logic with database operations
- Tightly coupled notification and audit logging

### Code Smells Present

1. **Mixed Concerns**: Business logic, persistence, logging, and notifications are intertwined
2. **Duplicated Code**: Validation logic (email format, phone format, blood type validation) repeated across methods
3. **Hard-coded Values**: Connection timeouts, slot durations, and cost values are hard-coded
4. **Side Effects in Getters**: The `getPatient()` method has side effects (access count tracking)
5. **Poor Separation of Concerns**: Database access, caching, and query execution mixed together
6. **Data Integrity Issues**: No proper transaction management or consistency checks
7. **Missing Abstractions**: Proper factory patterns and service interfaces are missing

## Why This Project is Suitable for Code Smell Analysis

This legacy HealthBridge system is an excellent candidate for comprehensive code smell and re-engineering analysis for several reasons. First, it demonstrates real-world architectural problems that emerge in systems built over time by multiple teams without consistent design discipline. The mixture of concerns within large classes and long methods provides clear examples of violations in object-oriented design principles that are immediately visible and analyzable. Second, the project contains inter-class dependencies and coupling that highlight architectural issues and demonstrate how poor design choices propagate throughout a system. Third, the intentional inclusion of code smells provides a practical case study for students and practitioners to learn how to identify, analyze, and refactor problematic code patterns. Finally, the system is substantial enough (2,800+ lines across 14 classes) to require careful analysis tools and methodologies while remaining manageable for educational purposes.

## Building the Project

### Prerequisites

- JDK 11 or higher
- Maven 3.6 or higher

### Compilation

```bash
cd HealthBridge-PatientMS
mvn clean compile
```

### Running the Application

```bash
mvn exec:java -Dexec.mainClass="com.healthbridge.HealthBridgeApplication"
```

## Project Structure

```
HealthBridge-PatientMS/
├── pom.xml
├── README.md
└── src/
    ├── main/
    │   └── java/
    │       └── com/healthbridge/
    │           ├── patient/
    │           │   ├── Patient.java
    │           │   └── PatientManager.java
    │           ├── medical/
    │           │   └── MedicalRecord.java
    │           ├── appointment/
    │           │   ├── Appointment.java
    │           │   └── AppointmentScheduler.java
    │           ├── billing/
    │           │   ├── BillingService.java
    │           │   ├── Invoice.java
    │           │   └── Payment.java
    │           ├── notification/
    │           │   ├── NotificationService.java
    │           │   └── Notification.java
    │           ├── database/
    │           │   └── DatabaseConnection.java
    │           └── HealthBridgeApplication.java
    └── test/
        └── java/
            └── (test classes)
```

## Dependencies

- JUnit 4.13.2 (for testing)
- Mockito 4.6.1 (for mocking in tests)

## License

This project is created for educational purposes as part of a software re-engineering course.

## Contact

For questions or issues related to this project, please contact the development team.
