package com.healthbridge;

import com.healthbridge.patient.Patient;
import com.healthbridge.patient.PatientManager;
import com.healthbridge.medical.MedicalRecord;
import com.healthbridge.appointment.Appointment;
import com.healthbridge.appointment.AppointmentScheduler;
import com.healthbridge.billing.BillingService;
import com.healthbridge.billing.Invoice;
import com.healthbridge.notification.NotificationService;
import com.healthbridge.database.DatabaseConnection;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Main Application class for HealthBridge Hospital Patient Management System
 * This demonstrates the integration of all components
 */
public class HealthBridgeApplication {
    
    public static void main(String[] args) {
        System.out.println("============================================");
        System.out.println("HealthBridge Hospital Patient Management System");
        System.out.println("============================================\n");

        // Initialize database connection
        DatabaseConnection dbConnection = DatabaseConnection.getInstance();
        dbConnection.initialize("jdbc:mysql://localhost:3306/healthbridge", "healthbridge_operator", "secure_password");
        dbConnection.connect();

        // Initialize core services
        PatientManager patientManager = new PatientManager();
        AppointmentScheduler appointmentScheduler = new AppointmentScheduler(patientManager);
        BillingService billingService = new BillingService(patientManager);
        NotificationService notificationService = new NotificationService(patientManager);

        // Register some test patients
        System.out.println("\n--- Registering Patients ---");
        Patient patient1 = patientManager.registerNewPatient(
                "Fatima", "Khan", "1985-03-15", "F", "5551234567", "fatima.khan@email.com",
                "123 Main St", "Karachi", "Sindh", "75500",
                "HBL Insurance", "HBL123456", "Zainab Khan", "5559876543", "O+", 65.0, 165.0,
                "Dr. Ahmed Khan"
        );

        Patient patient2 = patientManager.registerNewPatient(
                "Hassan", "Ali", "1992-07-22", "M", "5552345678", "hassan.ali@email.com",
                "456 Oak Ave", "Lahore", "Punjab", "54000",
                "UBL Insurance", "UBL789012", "Muhammad Ali", "5558765432", "A+", 80.0, 178.0,
                "Dr. Ahmed Khan"
        );

        // Schedule appointments
        System.out.println("\n--- Scheduling Appointments ---");
        Appointment apt1 = appointmentScheduler.scheduleAppointment(
                patient1.getPatientId(), "DOC001", "Dr. Ahmed Khan", "Cardiology",
                LocalDateTime.now().plusDays(3), "CONSULTATION", "Routine checkup"
        );

        Appointment apt2 = appointmentScheduler.scheduleAppointment(
                patient2.getPatientId(), "DOC002", "Dr. Sana Khan", "Neurology",
                LocalDateTime.now().plusDays(5), "FOLLOW_UP", "Follow up visit"
        );

        // Create medical records
        System.out.println("\n--- Creating Medical Records ---");
        MedicalRecord record1 = new MedicalRecord("MR001", patient1.getPatientId(), "DIAGNOSIS");
        record1.setDoctorName("Dr. Ahmed Khan");
        record1.setDepartment("Cardiology");
        record1.setDiagnosis("Hypertension");
        record1.setTreatment("Medication and lifestyle changes");
        record1.setFollowUpInstructions("Follow up in 4 weeks");
        record1.markAsCompleted();
        System.out.println("Medical record created: " + record1.getRecordId());

        // Create invoices
        System.out.println("\n--- Creating Invoices ---");
        Invoice invoice1 = billingService.createInvoice(
                patient1.getPatientId(), "CONSULTATION", "Cardiology",
                "Cardiology Consultation", 150.0
        );

        Invoice invoice2 = billingService.createInvoice(
                patient2.getPatientId(), "PROCEDURE", "Neurology",
                "Neurological Assessment", 500.0
        );

        // Record payments
        System.out.println("\n--- Recording Payments ---");
        billingService.recordPayment(invoice1.getInvoiceId(), 75.0, "CARD");
        billingService.recordPayment(invoice1.getInvoiceId(), 75.0, "INSURANCE");
        billingService.recordPayment(invoice2.getInvoiceId(), 500.0, "TRANSFER");

        // Send notifications
        System.out.println("\n--- Sending Notifications ---");
        notificationService.sendAppointmentReminder(
                patient1.getPatientId(), apt1.getAppointmentId(),
                apt1.getAppointmentDateTime().toString(), "Dr. Ahmed Khan"
        );

        notificationService.sendBillingNotification(
                patient2.getPatientId(), invoice2.getInvoiceId(), invoice2.getAmount()
        );

        // Display system statistics
        System.out.println("\n--- System Statistics ---");
        Map<String, Integer> patientStats = patientManager.getPatientStatistics();
        System.out.println("Patient Statistics: " + patientStats);

        Map<String, Integer> appointmentStats = appointmentScheduler.getAppointmentStatistics();
        System.out.println("Appointment Statistics: " + appointmentStats);

        Map<String, Object> billingStats = billingService.getBillingStatistics();
        System.out.println("Billing Statistics: " + billingStats);

        Map<String, Object> notificationStats = notificationService.getNotificationStatistics();
        System.out.println("Notification Statistics: " + notificationStats);

        Map<String, Object> dbStats = dbConnection.getDatabaseStatistics();
        System.out.println("Database Statistics: " + dbStats);

        // Display patient information
        System.out.println("\n--- Patient Information ---");
        List<Patient> allPatients = patientManager.getAllPatients();
        for (Patient patient : allPatients) {
            System.out.println("Patient: " + patient.getFullName() + 
                             " (ID: " + patient.getPatientId() + 
                             ") Status: " + patient.getStatus());
        }

        // Display appointment information
        System.out.println("\n--- Appointment Information ---");
        List<Appointment> allAppointments = appointmentScheduler.getAllAppointments();
        for (Appointment apt : allAppointments) {
            System.out.println("Appointment: " + apt.getAppointmentId() + 
                             " with " + apt.getDoctorName() + 
                             " Status: " + apt.getStatus());
        }

        // Display billing information
        System.out.println("\n--- Billing Information ---");
        System.out.println("Invoice 1: " + invoice1);
        System.out.println("Invoice 2: " + invoice2);

        // Cleanup
        System.out.println("\n--- Cleanup ---");
        dbConnection.disconnect();

        System.out.println("\n============================================");
        System.out.println("Application terminated successfully");
        System.out.println("============================================");
    }
}
