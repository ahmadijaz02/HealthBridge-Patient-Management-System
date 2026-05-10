package com.healthbridge.notification;

import com.healthbridge.patient.Patient;
import com.healthbridge.patient.PatientManager;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Notification Service - handles notifications to patients
 * Demonstrates dependency on PatientManager
 */
public class NotificationService {
    private List<Notification> notifications = new ArrayList<>();
    private PatientManager patientManager;
    private int notificationCounter = 1000;
    private List<String> auditLog = new ArrayList<>();
    private Map<String, LocalDateTime> lastNotificationSent = new HashMap<>();
    private Map<String, Integer> notificationCount = new HashMap<>();

    public NotificationService(PatientManager patientManager) {
        this.patientManager = patientManager;
    }

    /**
     * Send notification to patient
     */
    public Notification sendNotification(String patientId, String notificationType, 
                                        String title, String message, String channel) {
        
        Patient patient = patientManager.getPatient(patientId);
        if (patient == null) {
            System.out.println("ERROR: Patient not found: " + patientId);
            return null;
        }

        String notificationId = "NOT" + String.format("%06d", notificationCounter++);
        Notification notification = new Notification(notificationId, patientId, notificationType);
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setChannel(channel);
        notification.setRecipientEmail(patient.getEmailAddress());
        notification.setRecipientPhone(patient.getContactNumber());

        notifications.add(notification);
        lastNotificationSent.put(patientId, LocalDateTime.now());
        notificationCount.put(patientId, notificationCount.getOrDefault(patientId, 0) + 1);

        auditLog.add("[" + System.currentTimeMillis() + "] NOTIFICATION_SENT " + notificationId + 
                     " patient=" + patientId + " type=" + notificationType + " channel=" + channel);

        System.out.println("Notification sent: " + notificationId + " to patient: " + patientId);
        return notification;
    }

    /**
     * Send SMS notification
     */
    public boolean sendSMS(String patientId, String message) {
        Patient patient = patientManager.getPatient(patientId);
        if (patient == null || patient.getContactNumber() == null) {
            System.out.println("ERROR: Cannot send SMS - patient or phone not found");
            return false;
        }

        sendNotification(patientId, "SMS", "Alert", message, "SMS");
        return true;
    }

    /**
     * Send email notification
     */
    public boolean sendEmail(String patientId, String subject, String message) {
        Patient patient = patientManager.getPatient(patientId);
        if (patient == null || patient.getEmailAddress() == null) {
            System.out.println("ERROR: Cannot send email - patient or email not found");
            return false;
        }

        sendNotification(patientId, "EMAIL", subject, message, "EMAIL");
        return true;
    }

    /**
     * Send appointment reminder
     */
    public boolean sendAppointmentReminder(String patientId, String appointmentId, 
                                          String appointmentTime, String doctorName) {
        
        String subject = "Appointment Reminder";
        String message = "You have an appointment with " + doctorName + " at " + appointmentTime;
        
        return sendEmail(patientId, subject, message) && sendSMS(patientId, message);
    }

    /**
     * Send billing notification
     */
    public boolean sendBillingNotification(String patientId, String invoiceId, double amount) {
        String subject = "Invoice Created";
        String message = "A new invoice has been created for amount: $" + amount;
        
        return sendEmail(patientId, subject, message);
    }

    /**
     * Send prescription notification
     */
    public boolean sendPrescriptionNotification(String patientId, String prescriptionDetails) {
        String subject = "Prescription Notification";
        String message = "Your prescription is ready: " + prescriptionDetails;
        
        return sendEmail(patientId, subject, message) && sendSMS(patientId, message);
    }

    /**
     * Get notification by ID
     */
    public Notification getNotification(String notificationId) {
        for (Notification notification : notifications) {
            if (notification.getNotificationId().equals(notificationId)) {
                return notification;
            }
        }
        return null;
    }

    /**
     * Get all notifications for a patient
     */
    public List<Notification> getPatientNotifications(String patientId) {
        List<Notification> result = new ArrayList<>();
        for (Notification notification : notifications) {
            if (notification.getPatientId().equals(patientId)) {
                result.add(notification);
            }
        }
        return result;
    }

    /**
     * Get unread notifications for a patient
     */
    public List<Notification> getUnreadNotifications(String patientId) {
        List<Notification> result = new ArrayList<>();
        for (Notification notification : notifications) {
            if (notification.getPatientId().equals(patientId) && !notification.isRead()) {
                result.add(notification);
            }
        }
        return result;
    }

    /**
     * Mark notification as read
     */
    public boolean markAsRead(String notificationId) {
        Notification notification = getNotification(notificationId);
        if (notification == null) {
            return false;
        }
        
        notification.setRead(true);
        notification.setReadAt(LocalDateTime.now());
        
        auditLog.add("[" + System.currentTimeMillis() + "] NOTIFICATION_READ " + notificationId);
        return true;
    }

    /**
     * Delete notification
     */
    public boolean deleteNotification(String notificationId) {
        Notification notification = getNotification(notificationId);
        if (notification == null) {
            return false;
        }
        
        notifications.remove(notification);
        auditLog.add("[" + System.currentTimeMillis() + "] NOTIFICATION_DELETED " + notificationId);
        return true;
    }

    /**
     * Get notification statistics
     */
    public Map<String, Object> getNotificationStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalNotifications", notifications.size());
        stats.put("sentNotifications", (int) notifications.stream()
                .filter(n -> n.getStatus().equals("SENT")).count());
        stats.put("readNotifications", (int) notifications.stream()
                .filter(Notification::isRead).count());
        stats.put("unreadNotifications", (int) notifications.stream()
                .filter(n -> !n.isRead()).count());
        stats.put("emailCount", (int) notifications.stream()
                .filter(n -> n.getChannel().equals("EMAIL")).count());
        stats.put("smsCount", (int) notifications.stream()
                .filter(n -> n.getChannel().equals("SMS")).count());
        return stats;
    }

    /**
     * Get audit log
     */
    public List<String> getAuditLog() {
        return new ArrayList<>(auditLog);
    }

    /**
     * Get last notification sent time for patient
     */
    public LocalDateTime getLastNotificationSentTime(String patientId) {
        return lastNotificationSent.get(patientId);
    }

    /**
     * Get notification count for patient
     */
    public int getNotificationCount(String patientId) {
        return notificationCount.getOrDefault(patientId, 0);
    }
}
