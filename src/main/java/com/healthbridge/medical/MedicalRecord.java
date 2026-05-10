package com.healthbridge.medical;

import com.healthbridge.patient.Patient;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Medical Records management class
 * Handles patient medical records, test results, and clinical notes
 */
public class MedicalRecord {
    private String recordId;
    private String patientId;
    private LocalDateTime recordDate;
    private String recordType; // DIAGNOSIS, TEST_RESULT, CLINICAL_NOTE, PRESCRIPTION
    private String description;
    private String doctorName;
    private String department;
    private Map<String, String> testResults;
    private List<String> prescriptions;
    private String diagnosis;
    private String treatment;
    private String followUpInstructions;
    private boolean isConfidential;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String status; // DRAFT, COMPLETED, ARCHIVED

    public MedicalRecord() {
        this.testResults = new HashMap<>();
        this.prescriptions = new ArrayList<>();
    }

    public MedicalRecord(String recordId, String patientId, String recordType) {
        this.recordId = recordId;
        this.patientId = patientId;
        this.recordType = recordType;
        this.recordDate = LocalDateTime.now();
        this.createdAt = LocalDateTime.now();
        this.status = "DRAFT";
        this.testResults = new HashMap<>();
        this.prescriptions = new ArrayList<>();
    }

    // Getters and Setters
    public String getRecordId() { return recordId; }
    public void setRecordId(String recordId) { this.recordId = recordId; }

    public String getPatientId() { return patientId; }
    public void setPatientId(String patientId) { this.patientId = patientId; }

    public LocalDateTime getRecordDate() { return recordDate; }
    public void setRecordDate(LocalDateTime recordDate) { this.recordDate = recordDate; }

    public String getRecordType() { return recordType; }
    public void setRecordType(String recordType) { this.recordType = recordType; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getDoctorName() { return doctorName; }
    public void setDoctorName(String doctorName) { this.doctorName = doctorName; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public Map<String, String> getTestResults() { return testResults; }
    public void setTestResults(Map<String, String> testResults) { this.testResults = testResults; }

    public List<String> getPrescriptions() { return prescriptions; }
    public void setPrescriptions(List<String> prescriptions) { this.prescriptions = prescriptions; }

    public String getDiagnosis() { return diagnosis; }
    public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }

    public String getTreatment() { return treatment; }
    public void setTreatment(String treatment) { this.treatment = treatment; }

    public String getFollowUpInstructions() { return followUpInstructions; }
    public void setFollowUpInstructions(String followUpInstructions) { this.followUpInstructions = followUpInstructions; }

    public boolean isConfidential() { return isConfidential; }
    public void setConfidential(boolean confidential) { isConfidential = confidential; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public void addTestResult(String testName, String result) {
        testResults.put(testName, result);
        this.updatedAt = LocalDateTime.now();
    }

    public void addPrescription(String prescription) {
        prescriptions.add(prescription);
        this.updatedAt = LocalDateTime.now();
    }

    public void markAsCompleted() {
        this.status = "COMPLETED";
        this.updatedAt = LocalDateTime.now();
    }

    public void archiveRecord() {
        this.status = "ARCHIVED";
        this.updatedAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "MedicalRecord{" +
                "recordId='" + recordId + '\'' +
                ", patientId='" + patientId + '\'' +
                ", recordType='" + recordType + '\'' +
                ", department='" + department + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
