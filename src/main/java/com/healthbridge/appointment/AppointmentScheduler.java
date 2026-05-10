package com.healthbridge.appointment;

import com.healthbridge.patient.Patient;
import com.healthbridge.patient.PatientManager;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Appointment Scheduler - manages appointment scheduling with dependencies on PatientManager
 * This class demonstrates inter-class dependencies
 */
public class AppointmentScheduler {
    private Map<String, Appointment> appointments = new HashMap<>();
    private PatientManager patientManager;
    private int appointmentCounter = 5000;
    private List<String> auditLog = new ArrayList<>();
    private Map<String, LocalDateTime> appointmentReminderSentTime = new HashMap<>();
    private List<String> cancelledAppointments = new ArrayList<>();

    public AppointmentScheduler(PatientManager patientManager) {
        this.patientManager = patientManager;
    }

    /**
     * Schedule a new appointment
     * SMELL: No validation that patient/doctor exist
     */
    public Appointment scheduleAppointment(String patientId, String doctorId, 
                                          String doctorName, String department,
                                          LocalDateTime appointmentDateTime,
                                          String appointmentType, String reason) {
        
        // SMELL: Not checking if patient exists in PatientManager
        Patient patient = patientManager.getPatient(patientId);
        if (patient == null) {
            System.out.println("ERROR: Patient not found: " + patientId);
            auditLog.add("[" + System.currentTimeMillis() + "] APPOINTMENT_SCHEDULE_FAILED patient not found: " + patientId);
            return null;
        }

        String appointmentId = "APT" + String.format("%05d", appointmentCounter++);
        Appointment appointment = new Appointment(appointmentId, patientId, doctorId, appointmentDateTime);
        appointment.setDoctorName(doctorName);
        appointment.setDepartment(department);
        appointment.setAppointmentType(appointmentType);
        appointment.setReason(reason);

        appointments.put(appointmentId, appointment);
        
        auditLog.add("[" + System.currentTimeMillis() + "] APPOINTMENT_SCHEDULED " + appointmentId + 
                     " patient=" + patientId + " doctor=" + doctorName + " time=" + appointmentDateTime);

        System.out.println("Appointment scheduled successfully: " + appointmentId);
        return appointment;
    }

    /**
     * Get appointment by ID
     */
    public Appointment getAppointment(String appointmentId) {
        return appointments.get(appointmentId);
    }

    /**
     * Get all appointments for a patient
     */
    public List<Appointment> getPatientAppointments(String patientId) {
        return appointments.values().stream()
                .filter(apt -> apt.getPatientId().equals(patientId))
                .collect(Collectors.toList());
    }

    /**
     * Get upcoming appointments for a patient
     */
    public List<Appointment> getUpcomingAppointments(String patientId, int days) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime futureDate = now.plusDays(days);
        
        return appointments.values().stream()
                .filter(apt -> apt.getPatientId().equals(patientId))
                .filter(apt -> apt.getStatus().equals("SCHEDULED"))
                .filter(apt -> apt.getAppointmentDateTime().isAfter(now) && 
                               apt.getAppointmentDateTime().isBefore(futureDate))
                .collect(Collectors.toList());
    }

    /**
     * Get appointments by doctor
     */
    public List<Appointment> getAppointmentsByDoctor(String doctorId) {
        return appointments.values().stream()
                .filter(apt -> apt.getDoctorId().equals(doctorId))
                .collect(Collectors.toList());
    }

    /**
     * Get available slots for a doctor on a given date
     * SMELL: Hard-coded slot calculation
     */
    public List<LocalDateTime> getAvailableSlots(String doctorId, LocalDateTime date) {
        List<LocalDateTime> availableSlots = new ArrayList<>();
        LocalDateTime startTime = date.withHour(9).withMinute(0);
        LocalDateTime endTime = date.withHour(17).withMinute(0);

        Set<LocalDateTime> bookedTimes = appointments.values().stream()
                .filter(apt -> apt.getDoctorId().equals(doctorId))
                .filter(apt -> apt.getAppointmentDateTime().toLocalDate().equals(date.toLocalDate()))
                .map(Appointment::getAppointmentDateTime)
                .collect(Collectors.toSet());

        while (startTime.isBefore(endTime)) {
            if (!bookedTimes.contains(startTime)) {
                availableSlots.add(startTime);
            }
            startTime = startTime.plusMinutes(30);
        }

        return availableSlots;
    }

    /**
     * Confirm appointment
     */
    public boolean confirmAppointment(String appointmentId) {
        Appointment appointment = appointments.get(appointmentId);
        if (appointment == null) {
            return false;
        }
        
        appointment.confirmAppointment();
        auditLog.add("[" + System.currentTimeMillis() + "] APPOINTMENT_CONFIRMED " + appointmentId);
        return true;
    }

    /**
     * Cancel appointment
     */
    public boolean cancelAppointment(String appointmentId, String reason) {
        Appointment appointment = appointments.get(appointmentId);
        if (appointment == null) {
            return false;
        }
        
        appointment.cancelAppointment(reason);
        cancelledAppointments.add(appointmentId);
        
        auditLog.add("[" + System.currentTimeMillis() + "] APPOINTMENT_CANCELLED " + appointmentId + 
                     " reason=" + reason);
        return true;
    }

    /**
     * Reschedule appointment
     */
    public boolean rescheduleAppointment(String appointmentId, LocalDateTime newDateTime) {
        Appointment appointment = appointments.get(appointmentId);
        if (appointment == null) {
            return false;
        }
        
        appointment.rescheduleAppointment(newDateTime);
        auditLog.add("[" + System.currentTimeMillis() + "] APPOINTMENT_RESCHEDULED " + appointmentId + 
                     " newTime=" + newDateTime);
        return true;
    }

    /**
     * Mark appointment as completed
     */
    public boolean completeAppointment(String appointmentId, String notes, String feedback, int rating) {
        Appointment appointment = appointments.get(appointmentId);
        if (appointment == null) {
            return false;
        }
        
        appointment.completeAppointment();
        appointment.setNotes(notes);
        appointment.setPatientFeedback(feedback);
        appointment.setPatientRating(rating);
        
        auditLog.add("[" + System.currentTimeMillis() + "] APPOINTMENT_COMPLETED " + appointmentId + 
                     " rating=" + rating);
        return true;
    }

    /**
     * Send reminder notification
     */
    public boolean sendReminder(String appointmentId) {
        Appointment appointment = appointments.get(appointmentId);
        if (appointment == null) {
            return false;
        }
        
        appointment.setReminderSent(true);
        appointment.setLastReminderTime(LocalDateTime.now());
        appointmentReminderSentTime.put(appointmentId, LocalDateTime.now());
        
        System.out.println("Reminder sent for appointment: " + appointmentId + 
                          " to patient: " + appointment.getPatientId());
        auditLog.add("[" + System.currentTimeMillis() + "] REMINDER_SENT " + appointmentId);
        return true;
    }

    /**
     * Get all appointments
     */
    public List<Appointment> getAllAppointments() {
        return new ArrayList<>(appointments.values());
    }

    /**
     * Get appointment statistics
     */
    public Map<String, Integer> getAppointmentStatistics() {
        Map<String, Integer> stats = new HashMap<>();
        stats.put("totalAppointments", appointments.size());
        stats.put("scheduledAppointments", (int) appointments.values().stream()
                .filter(apt -> apt.getStatus().equals("SCHEDULED")).count());
        stats.put("completedAppointments", (int) appointments.values().stream()
                .filter(apt -> apt.getStatus().equals("COMPLETED")).count());
        stats.put("cancelledAppointments", cancelledAppointments.size());
        return stats;
    }

    /**
     * Get audit log
     */
    public List<String> getAuditLog() {
        return new ArrayList<>(auditLog);
    }

    public int getTotalAppointments() {
        return appointments.size();
    }
}
