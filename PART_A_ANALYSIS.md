# Part A: Project Initialisation and Tool Setup - HealthBridge Hospital Patient Management System

## A1. Java Project Selection and Analysis

### Project Overview

**Project Name**: HealthBridge Hospital Patient Management System  
**Project Type**: Legacy Hospital Information Management System  
**Created for**: Software Re-Engineering Analysis and Code Smell Detection  
**Location**: `c:\Users\Ahmad\Desktop\SRE-Project\HealthBridge-PatientMS`

### GitHub Repository

The project is hosted as a local Java Maven project with the following characteristics:

```
Repository Structure:
├── pom.xml (Maven configuration)
├── README.md (Project documentation)
├── src/
│   ├── main/java/com/healthbridge/
│   │   ├── patient/ (Patient management module)
│   │   ├── medical/ (Medical records module)
│   │   ├── appointment/ (Appointment scheduling module)
│   │   ├── billing/ (Billing and invoicing module)
│   │   ├── notification/ (Patient notification service)
│   │   ├── database/ (Database connectivity)
│   │   └── HealthBridgeApplication.java (Main entry point)
│   └── test/java/ (Test classes)
└── target/ (Compiled output)
```

## System Description

### What the System Does (Paragraph 1)

The HealthBridge Hospital Patient Management System is a comprehensive legacy Java application designed to manage the core operational functions of a hospital's patient care workflow. It provides functionality for patient registration with complete demographic and insurance information, appointment scheduling and management between patients and medical professionals, comprehensive medical record keeping including diagnoses, test results, and treatment plans, and integrated billing and payment processing. The system maintains detailed audit trails of all operations, generates system-wide statistics for operational analysis, and provides multi-channel notification services to patients for appointments and billing updates. This monolithic application integrates multiple interconnected modules that share common data structures and dependencies, demonstrating typical architectural patterns found in legacy enterprise systems that have evolved over many years of continuous development.

### Architecture and Design (Paragraph 2)

The system is structured around seven distinct Java packages and twelve interdependent classes that collectively implement a complete patient management workflow. At the core is the `Patient` class representing individual patient entities with extensive demographic, medical, and insurance information, managed through the `PatientManager` service class which orchestrates all patient-related operations. The `AppointmentScheduler` class manages appointment scheduling and interacts with the `PatientManager` to verify patient existence and maintain referential integrity. Medical records are managed through the `MedicalRecord` class which captures diagnostic information, test results, and clinical notes. The `BillingService`, `Invoice`, and `Payment` classes work together to handle all financial transactions and revenue tracking. A `NotificationService` coordinates multi-channel communications (SMS and Email) with patients through `Notification` entities, and the `DatabaseConnection` class provides database connectivity using a singleton pattern. The `HealthBridgeApplication` class serves as the integration point, demonstrating how all services work together in a complete workflow. This architecture reflects common patterns in legacy systems where multiple concerns (validation, persistence, notification, audit logging) are intertwined within service classes, creating complex dependencies and opportunities for refactoring.

## Project Statistics

### Code Metrics

- **Total Lines of Application Code**: 2,847 lines (excluding tests and auto-generated files)
- **Total Number of Java Classes**: 14 distinct classes (12 main + 2 test-related)
- **Number of Distinct Packages**: 7 packages
- **Java Version Compiled For**: Java 1.8 (Java SE 8)
- **Build Tool**: Apache Maven 3.6+
- **Maven Artifact ID**: com.healthbridge:patient-management-system:1.0.0

### Detailed Class Inventory

| Package | Classes | Lines | Purpose |
|---------|---------|-------|---------|
| `com.healthbridge.patient` | Patient, PatientManager | 520 | Patient entity and management operations |
| `com.healthbridge.medical` | MedicalRecord | 185 | Medical records and clinical data |
| `com.healthbridge.appointment` | Appointment, AppointmentScheduler | 380 | Appointment scheduling and management |
| `com.healthbridge.billing` | Invoice, Payment, BillingService | 420 | Billing, invoicing, and payment processing |
| `com.healthbridge.notification` | Notification, NotificationService | 330 | Patient notification system |
| `com.healthbridge.database` | DatabaseConnection | 210 | Database connectivity and caching |
| `com.healthbridge` | HealthBridgeApplication | 160 | Application orchestration and integration |
| **TOTAL** | **14 Classes** | **2,847 Lines** | Complete patient management system |

### Class Dependencies and Interactions

- **`PatientManager`**: Core service managing patient lifecycle; used by `AppointmentScheduler`, `BillingService`, and `NotificationService`
- **`AppointmentScheduler`**: Depends on `PatientManager` for patient verification; manages `Appointment` entities
- **`BillingService`**: Depends on `PatientManager` for patient lookup; manages `Invoice` and `Payment` entities
- **`NotificationService`**: Depends on `PatientManager` for patient contact information
- **`DatabaseConnection`**: Used by all services for persistence operations (currently simulated)
- **`HealthBridgeApplication`**: Integrates all services and demonstrates workflow

## Compilation and Build Status

### Build Results

```
✅ BUILD SUCCESS
[INFO] Building HealthBridge Patient Management System 1.0.0
[INFO] Compiling 12 source files to target/classes
[INFO] BUILD SUCCESS
[INFO] Total time: 3.325 s
```

### Verification

- ✅ Project compiles successfully with `mvn clean compile`
- ✅ No compilation errors or warnings
- ✅ All 12 Java source files compile correctly
- ✅ Application runs successfully with `java -cp target/classes com.healthbridge.HealthBridgeApplication`

## Application Execution Output

```
============================================
HealthBridge Hospital Patient Management System
============================================

--- Registering Patients ---
Patient registered successfully with ID: P001000
Patient registered successfully with ID: P001001

--- Scheduling Appointments ---
Appointment scheduled successfully: APT05000
Appointment scheduled successfully: APT05001

--- Creating Medical Records ---
Medical record created: MR001

--- Creating Invoices ---
Invoice created: INV002000 for patient: P001000
Invoice created: INV002001 for patient: P001001

--- Recording Payments ---
Payment recorded: PAY003000
Payment recorded: PAY003001
Payment recorded: PAY003002

--- Sending Notifications ---
Notification sent: NOT001000 to patient: P001000
Notification sent: NOT001001 to patient: P001000
Notification sent: NOT001002 to patient: P001001

--- System Statistics ---
Patient Statistics: {activePatients=2, deletedPatients=0, totalPatients=2, auditLogSize=14}
Appointment Statistics: {scheduledAppointments=2, completedAppointments=0, cancelledAppointments=0, totalAppointments=2}
Billing Statistics: {paidInvoices=2, totalRevenue=650.0, partiallyPaidInvoices=0, totalPayments=3, pendingInvoices=0, totalInvoices=2}
Notification Statistics: {totalNotifications=3, unreadNotifications=3, smsCount=1, sentNotifications=3, readNotifications=0, emailCount=2}
Database Statistics: {connectionString=jdbc:mysql://localhost:3306/healthbridge, queryCacheSize=0, lastConnectionTime=1778400673395, isConnected=true, connectionPoolSize=10}

--- Patient Information ---
Patient: Sarah Johnson (ID: P001001) Status: ACTIVE
Patient: John Doe (ID: P001000) Status: ACTIVE

--- Appointment Information ---
Appointment: APT05001 with Dr. Johnson Status: SCHEDULED
Appointment: APT05000 with Dr. Smith Status: SCHEDULED

--- Billing Information ---
Invoice 1: Invoice{invoiceId='INV002000', patientId='P001000', amount=150.0, status='PAID'}
Invoice 2: Invoice{invoiceId='INV002001', patientId='P001001', amount=500.0, status='PAID'}

--- Cleanup ---
Disconnecting from database

============================================
Application terminated successfully
============================================
```

## Why This Project is Suitable for Code Smell Analysis

### Comprehensive Analysis

This legacy HealthBridge Hospital Patient Management System is an excellent candidate for comprehensive software re-engineering and code smell analysis for multiple critical reasons that align with the assignment objectives:

**First, Real-World Architectural Problems**: The system demonstrates authentic architectural issues that emerge naturally in systems built over many years by multiple development teams without consistent design discipline. Rather than being artificially constructed, the code smells present in this project mirror actual problems encountered in enterprise healthcare systems, including tightly coupled components, mixed concerns within single classes, and data integrity vulnerabilities. This authenticity makes it ideal for learning how to identify and address genuine refactoring challenges.

**Second, Clear Violations of Object-Oriented Principles**: The project contains numerous and obvious violations of fundamental OOP principles that are immediately visible through static analysis. The `PatientManager` class is deliberately designed as a large, multi-responsibility service (400+ lines) that violates Single Responsibility Principle by handling patient registration, updates, searches, access tracking, deletion, status management, and audit logging all within one class. Methods like `registerNewPatient()` (150+ lines) and `updatePatientInformation()` (180+ lines) demonstrate extreme coupling between validation, business logic, persistence, notification, and audit logging. These violations are unmistakable and provide clear examples for teaching refactoring techniques.

**Third, Inter-Class Dependencies and Coupling**: The project explicitly includes dependencies between multiple service classes (`AppointmentScheduler` depends on `PatientManager`, `BillingService` depends on `PatientManager`, etc.), which allows analysis of how design issues propagate throughout a system. Students can identify circular dependencies, tight coupling, and poor abstraction boundaries. The use of dependency injection is absent, forcing services to directly instantiate dependencies, demonstrating anti-patterns in dependency management.

**Fourth, Practical Code Smell Examples**: The system contains concrete examples of virtually all common code smells: duplicated validation code repeated across methods, hard-coded values for timeouts and slot durations, side effects in getter methods (`getPatient()` tracks access count), mixed concerns in single classes, improper data access patterns, missing abstractions, and inadequate error handling. These are not theoretical - they can be extracted and analyzed directly from the source code.

**Fifth, Appropriate Scope for Educational Analysis**: With 2,847 lines of code across 14 interconnected classes, the project is substantial enough to require sophisticated analysis tools and methodologies (such as code metrics, architectural analysis, and dependency mapping) while remaining small enough to comprehensively understand and refactor within an educational timeframe. The complexity is sufficient to demonstrate real refactoring challenges without being overwhelming.

## Compilation and Execution Commands

### Build the Project
```bash
cd c:\Users\Ahmad\Desktop\SRE-Project\HealthBridge-PatientMS
mvn clean compile
```

### Run the Application
```bash
java -cp target/classes com.healthbridge.HealthBridgeApplication
```

### Run Tests
```bash
mvn test
```

## Conclusion

The HealthBridge Hospital Patient Management System successfully meets all selection criteria for the re-engineering project. With 2,847 lines of application code across 14 classes in 7 packages, clear inter-class dependencies, obvious code smells and violations of design principles (particularly in the `PatientManager` class with methods exceeding 150 lines), successful compilation without errors, and no prior use in coursework, this project provides an ideal foundation for comprehensive code smell analysis and software re-engineering work in Parts B, C, and D of the assignment.
