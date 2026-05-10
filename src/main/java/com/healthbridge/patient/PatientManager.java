package com.healthbridge.patient;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * LARGE CLASS WITH LONG METHODS - Code Smell Example
 * This class violates Single Responsibility Principle and contains very long methods
 * that handle multiple concerns: patient management, validation, database operations, notifications
 */
public class PatientManager {
    private Map<String, Patient> patientDatabase = new HashMap<>();
    private int patientCounter = 1000;
    private List<String> auditLog = new ArrayList<>();
    private Map<String, Integer> patientAccessCount = new HashMap<>();
    private List<String> notificationQueue = new ArrayList<>();
    private Map<String, Long> lastModifiedTime = new HashMap<>();
    private List<String> deletedPatients = new ArrayList<>();
    private Map<String, String> patientStatusHistory = new HashMap<>();

    /**
     * LONG METHOD #1 - This method has too many responsibilities and is over 100 lines
     * It handles: validation, creation, database storage, notification, audit logging, status tracking
     * This is a classic code smell that should be refactored
     */
    public Patient registerNewPatient(String firstName, String lastName, String dateOfBirth, 
                                      String gender, String contactNumber, String emailAddress,
                                      String address, String city, String state, String zipCode,
                                      String insuranceProvider, String insuranceNumber,
                                      String emergencyContactName, String emergencyContactPhone,
                                      String bloodType, double weight, double height,
                                      String registeredBy) {
        
        // SMELL: Input validation mixed with business logic
        if (firstName == null || firstName.trim().isEmpty()) {
            System.out.println("ERROR: First name cannot be empty");
            return null;
        }
        if (lastName == null || lastName.trim().isEmpty()) {
            System.out.println("ERROR: Last name cannot be empty");
            return null;
        }
        if (dateOfBirth == null || dateOfBirth.trim().isEmpty()) {
            System.out.println("ERROR: Date of birth cannot be empty");
            return null;
        }
        if (contactNumber == null || !contactNumber.matches("\\d{10}")) {
            System.out.println("ERROR: Invalid contact number format. Must be 10 digits.");
            return null;
        }
        if (emailAddress == null || !emailAddress.contains("@")) {
            System.out.println("ERROR: Invalid email address format");
            return null;
        }
        if (insuranceNumber == null || insuranceNumber.trim().isEmpty()) {
            System.out.println("ERROR: Insurance number cannot be empty");
            return null;
        }
        if (weight <= 0 || height <= 0) {
            System.out.println("ERROR: Invalid weight or height values");
            return null;
        }
        if (bloodType == null || !isValidBloodType(bloodType)) {
            System.out.println("ERROR: Invalid blood type");
            return null;
        }

        // SMELL: Direct timestamp checking without proper time management abstraction
        long currentTime = System.currentTimeMillis();
        
        // SMELL: Hardcoded patient ID generation
        String patientId = "P" + String.format("%06d", patientCounter++);
        
        // SMELL: Creating entity object mixed with business logic
        Patient patient = new Patient(patientId, firstName, lastName);
        patient.setDateOfBirth(dateOfBirth);
        patient.setGender(gender);
        patient.setContactNumber(contactNumber);
        patient.setEmailAddress(emailAddress);
        patient.setAddress(address);
        patient.setCity(city);
        patient.setState(state);
        patient.setZipCode(zipCode);
        patient.setInsuranceProvider(insuranceProvider);
        patient.setInsuranceNumber(insuranceNumber);
        patient.setEmergencyContactName(emergencyContactName);
        patient.setEmergencyContactPhone(emergencyContactPhone);
        patient.setBloodType(bloodType);
        patient.setWeight(weight);
        patient.setHeight(height);
        patient.setRegisteredBy(registeredBy);
        patient.setRegistrationDate(LocalDateTime.now());
        patient.setStatus("ACTIVE");
        patient.setActive(true);

        // SMELL: Direct database access in business method
        patientDatabase.put(patientId, patient);
        lastModifiedTime.put(patientId, currentTime);
        patientStatusHistory.put(patientId, "REGISTERED");

        // SMELL: Notification logic mixed in
        notificationQueue.add("New patient registered: " + patientId + " - " + firstName + " " + lastName);
        notificationQueue.add("Insurance verified for: " + insuranceNumber);

        // SMELL: Audit logging with string concatenation
        auditLog.add("[" + System.currentTimeMillis() + "] PATIENT_REGISTERED " + patientId + 
                     " by " + registeredBy + " with email " + emailAddress);
        auditLog.add("[" + System.currentTimeMillis() + "] INSURANCE_SET " + patientId + 
                     " provider=" + insuranceProvider + " number=" + insuranceNumber);

        System.out.println("Patient registered successfully with ID: " + patientId);
        return patient;
    }

    /**
     * LONG METHOD #2 - Another long method handling multiple concerns
     * Updates patient information, validates data, logs changes, sends notifications
     * Over 150 lines of mixed concerns
     */
    public boolean updatePatientInformation(String patientId, String firstName, String lastName,
                                           String contactNumber, String emailAddress, String address,
                                           String city, String state, String zipCode, String phone,
                                           String insuranceProvider, String insuranceNumber,
                                           String emergencyContactName, String emergencyContactPhone,
                                           String medicalHistory, String allergies, String medications,
                                           String bloodType, double weight, double height) {
        
        // SMELL: No null safety check on patientId
        if (!patientDatabase.containsKey(patientId)) {
            System.out.println("ERROR: Patient not found with ID: " + patientId);
            auditLog.add("[" + System.currentTimeMillis() + "] UPDATE_FAILED Patient not found: " + patientId);
            return false;
        }

        Patient patient = patientDatabase.get(patientId);
        String changeLog = "[" + System.currentTimeMillis() + "] PATIENT_UPDATED " + patientId;

        // SMELL: Duplicated validation logic
        if (firstName != null && !firstName.trim().isEmpty()) {
            patient.setFirstName(firstName);
            changeLog += " firstName=" + firstName;
        }
        if (lastName != null && !lastName.trim().isEmpty()) {
            patient.setLastName(lastName);
            changeLog += " lastName=" + lastName;
        }
        if (contactNumber != null && !contactNumber.isEmpty()) {
            if (!contactNumber.matches("\\d{10}")) {
                System.out.println("ERROR: Invalid contact number format");
                return false;
            }
            patient.setContactNumber(contactNumber);
            changeLog += " contactNumber=" + contactNumber;
        }
        if (emailAddress != null && !emailAddress.isEmpty()) {
            if (!emailAddress.contains("@")) {
                System.out.println("ERROR: Invalid email address");
                return false;
            }
            patient.setEmailAddress(emailAddress);
            changeLog += " email=" + emailAddress;
        }
        if (address != null && !address.trim().isEmpty()) {
            patient.setAddress(address);
            changeLog += " address=" + address;
        }
        if (city != null && !city.trim().isEmpty()) {
            patient.setCity(city);
            changeLog += " city=" + city;
        }
        if (state != null && !state.trim().isEmpty()) {
            patient.setState(state);
            changeLog += " state=" + state;
        }
        if (zipCode != null && !zipCode.trim().isEmpty()) {
            patient.setZipCode(zipCode);
            changeLog += " zipCode=" + zipCode;
        }
        if (insuranceProvider != null && !insuranceProvider.trim().isEmpty()) {
            patient.setInsuranceProvider(insuranceProvider);
            changeLog += " insurance=" + insuranceProvider;
        }
        if (insuranceNumber != null && !insuranceNumber.trim().isEmpty()) {
            patient.setInsuranceNumber(insuranceNumber);
            changeLog += " insuranceNum=" + insuranceNumber;
        }
        if (emergencyContactName != null && !emergencyContactName.trim().isEmpty()) {
            patient.setEmergencyContactName(emergencyContactName);
            changeLog += " emergencyName=" + emergencyContactName;
        }
        if (emergencyContactPhone != null && !emergencyContactPhone.trim().isEmpty()) {
            patient.setEmergencyContactPhone(emergencyContactPhone);
            changeLog += " emergencyPhone=" + emergencyContactPhone;
        }
        if (medicalHistory != null && !medicalHistory.trim().isEmpty()) {
            patient.setMedicalHistory(medicalHistory);
            changeLog += " medicalHistory=" + medicalHistory.substring(0, Math.min(50, medicalHistory.length()));
        }
        if (allergies != null && !allergies.trim().isEmpty()) {
            patient.setAllergies(allergies);
            changeLog += " allergies=" + allergies;
        }
        if (medications != null && !medications.trim().isEmpty()) {
            patient.setCurrentMedications(medications);
            changeLog += " medications=" + medications;
        }
        if (bloodType != null && isValidBloodType(bloodType)) {
            patient.setBloodType(bloodType);
            changeLog += " bloodType=" + bloodType;
        }
        if (weight > 0) {
            patient.setWeight(weight);
            changeLog += " weight=" + weight;
        }
        if (height > 0) {
            patient.setHeight(height);
            changeLog += " height=" + height;
        }

        patient.setStatus("UPDATED");
        lastModifiedTime.put(patientId, System.currentTimeMillis());

        // SMELL: Tightly coupled notification logic
        notificationQueue.add("Patient information updated: " + patientId + " - " + firstName + " " + lastName);
        if (medicalHistory != null) {
            notificationQueue.add("Medical history updated for patient: " + patientId);
        }
        if (allergies != null) {
            notificationQueue.add("Allergies recorded for patient: " + patientId);
        }

        // SMELL: Audit log with mixed concerns
        auditLog.add(changeLog);
        patientDatabase.put(patientId, patient);

        System.out.println("Patient information updated successfully");
        return true;
    }

    /**
     * Get patient by ID with access tracking
     */
    public Patient getPatient(String patientId) {
        if (patientDatabase.containsKey(patientId)) {
            // SMELL: Side effect in getter method - access count tracking
            patientAccessCount.put(patientId, patientAccessCount.getOrDefault(patientId, 0) + 1);
            auditLog.add("[" + System.currentTimeMillis() + "] PATIENT_ACCESSED " + patientId);
            return patientDatabase.get(patientId);
        }
        return null;
    }

    /**
     * Search patients by various criteria - SMELL: Multiple parameters, should use query object
     */
    public List<Patient> searchPatients(String firstName, String lastName, String contactNumber,
                                        String insuranceProvider, String city, String status) {
        List<Patient> results = new ArrayList<>();
        
        for (Patient patient : patientDatabase.values()) {
            boolean matches = true;
            
            if (firstName != null && !firstName.isEmpty() && 
                !patient.getFirstName().toLowerCase().contains(firstName.toLowerCase())) {
                matches = false;
            }
            if (lastName != null && !lastName.isEmpty() && 
                !patient.getLastName().toLowerCase().contains(lastName.toLowerCase())) {
                matches = false;
            }
            if (contactNumber != null && !contactNumber.isEmpty() && 
                !patient.getContactNumber().equals(contactNumber)) {
                matches = false;
            }
            if (insuranceProvider != null && !insuranceProvider.isEmpty() && 
                !patient.getInsuranceProvider().equalsIgnoreCase(insuranceProvider)) {
                matches = false;
            }
            if (city != null && !city.isEmpty() && 
                !patient.getCity().equalsIgnoreCase(city)) {
                matches = false;
            }
            if (status != null && !status.isEmpty() && 
                !patient.getStatus().equalsIgnoreCase(status)) {
                matches = false;
            }
            
            if (matches) {
                results.add(patient);
            }
        }
        
        auditLog.add("[" + System.currentTimeMillis() + "] PATIENT_SEARCH performed with criteria");
        return results;
    }

    /**
     * Delete patient - performs soft delete with status change
     */
    public boolean deletePatient(String patientId, String reason) {
        if (!patientDatabase.containsKey(patientId)) {
            return false;
        }
        
        Patient patient = patientDatabase.get(patientId);
        patient.setStatus("DELETED");
        patient.setActive(false);
        deletedPatients.add(patientId);
        
        auditLog.add("[" + System.currentTimeMillis() + "] PATIENT_DELETED " + patientId + 
                     " reason=" + reason);
        notificationQueue.add("Patient marked as deleted: " + patientId);
        
        return true;
    }

    /**
     * Get all patients
     */
    public List<Patient> getAllPatients() {
        return new ArrayList<>(patientDatabase.values());
    }

    /**
     * Get audit logs
     */
    public List<String> getAuditLog() {
        return new ArrayList<>(auditLog);
    }

    /**
     * Get notifications
     */
    public List<String> getNotifications() {
        List<String> notifications = new ArrayList<>(notificationQueue);
        notificationQueue.clear();
        return notifications;
    }

    /**
     * Get patient statistics
     */
    public Map<String, Integer> getPatientStatistics() {
        Map<String, Integer> stats = new HashMap<>();
        stats.put("totalPatients", patientDatabase.size());
        stats.put("activePatients", (int) patientDatabase.values().stream()
                .filter(Patient::isActive).count());
        stats.put("deletedPatients", deletedPatients.size());
        stats.put("auditLogSize", auditLog.size());
        return stats;
    }

    /**
     * Helper method for blood type validation
     */
    private boolean isValidBloodType(String bloodType) {
        return bloodType != null && (bloodType.equals("O+") || bloodType.equals("O-") ||
                bloodType.equals("A+") || bloodType.equals("A-") ||
                bloodType.equals("B+") || bloodType.equals("B-") ||
                bloodType.equals("AB+") || bloodType.equals("AB-"));
    }

    /**
     * Get patient access statistics
     */
    public Map<String, Integer> getAccessStatistics() {
        return new HashMap<>(patientAccessCount);
    }

    /**
     * Get last modified time for patient
     */
    public long getLastModifiedTime(String patientId) {
        return lastModifiedTime.getOrDefault(patientId, 0L);
    }

    public int getTotalPatients() {
        return patientDatabase.size();
    }

    public boolean patientExists(String patientId) {
        return patientDatabase.containsKey(patientId);
    }
}
