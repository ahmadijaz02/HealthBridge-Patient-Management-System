package com.healthbridge.appointment;

import java.time.LocalDateTime;

/**
 * Appointment entity representing a patient-doctor appointment
 */
public class Appointment {
    private String appointmentId;
    private String patientId;
    private String doctorId;
    private String doctorName;
    private String department;
    private LocalDateTime appointmentDateTime;
    private LocalDateTime createdDateTime;
    private int durationMinutes;
    private String status; // SCHEDULED, COMPLETED, CANCELLED, NO_SHOW, RESCHEDULED
    private String reason;
    private String notes;
    private String appointmentType; // CONSULTATION, FOLLOW_UP, ROUTINE_CHECKUP, PROCEDURE
    private String locationRoom;
    private boolean isConfirmed;
    private boolean reminderSent;
    private LocalDateTime lastReminderTime;
    private String patientFeedback;
    private int patientRating;

    public Appointment() {
    }

    public Appointment(String appointmentId, String patientId, String doctorId, 
                       LocalDateTime appointmentDateTime) {
        this.appointmentId = appointmentId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.appointmentDateTime = appointmentDateTime;
        this.createdDateTime = LocalDateTime.now();
        this.status = "SCHEDULED";
        this.durationMinutes = 30;
        this.isConfirmed = false;
        this.reminderSent = false;
    }

    // Getters and Setters
    public String getAppointmentId() { return appointmentId; }
    public void setAppointmentId(String appointmentId) { this.appointmentId = appointmentId; }

    public String getPatientId() { return patientId; }
    public void setPatientId(String patientId) { this.patientId = patientId; }

    public String getDoctorId() { return doctorId; }
    public void setDoctorId(String doctorId) { this.doctorId = doctorId; }

    public String getDoctorName() { return doctorName; }
    public void setDoctorName(String doctorName) { this.doctorName = doctorName; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public LocalDateTime getAppointmentDateTime() { return appointmentDateTime; }
    public void setAppointmentDateTime(LocalDateTime appointmentDateTime) { this.appointmentDateTime = appointmentDateTime; }

    public LocalDateTime getCreatedDateTime() { return createdDateTime; }
    public void setCreatedDateTime(LocalDateTime createdDateTime) { this.createdDateTime = createdDateTime; }

    public int getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(int durationMinutes) { this.durationMinutes = durationMinutes; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public String getAppointmentType() { return appointmentType; }
    public void setAppointmentType(String appointmentType) { this.appointmentType = appointmentType; }

    public String getLocationRoom() { return locationRoom; }
    public void setLocationRoom(String locationRoom) { this.locationRoom = locationRoom; }

    public boolean isConfirmed() { return isConfirmed; }
    public void setConfirmed(boolean confirmed) { isConfirmed = confirmed; }

    public boolean isReminderSent() { return reminderSent; }
    public void setReminderSent(boolean reminderSent) { this.reminderSent = reminderSent; }

    public LocalDateTime getLastReminderTime() { return lastReminderTime; }
    public void setLastReminderTime(LocalDateTime lastReminderTime) { this.lastReminderTime = lastReminderTime; }

    public String getPatientFeedback() { return patientFeedback; }
    public void setPatientFeedback(String patientFeedback) { this.patientFeedback = patientFeedback; }

    public int getPatientRating() { return patientRating; }
    public void setPatientRating(int patientRating) { this.patientRating = patientRating; }

    public void confirmAppointment() {
        this.isConfirmed = true;
    }

    public void completeAppointment() {
        this.status = "COMPLETED";
    }

    public void cancelAppointment(String reason) {
        this.status = "CANCELLED";
        this.reason = reason;
    }

    public void markAsNoShow() {
        this.status = "NO_SHOW";
    }

    public void rescheduleAppointment(LocalDateTime newDateTime) {
        this.appointmentDateTime = newDateTime;
        this.status = "RESCHEDULED";
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "appointmentId='" + appointmentId + '\'' +
                ", patientId='" + patientId + '\'' +
                ", doctorName='" + doctorName + '\'' +
                ", appointmentDateTime=" + appointmentDateTime +
                ", status='" + status + '\'' +
                '}';
    }
}
