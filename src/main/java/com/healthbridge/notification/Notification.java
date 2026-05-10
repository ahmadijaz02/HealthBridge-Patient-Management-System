package com.healthbridge.notification;

import java.time.LocalDateTime;

/**
 * Notification entity class
 */
public class Notification {
    private String notificationId;
    private String patientId;
    private String notificationType; // SMS, EMAIL, IN_APP, PUSH
    private String title;
    private String message;
    private String channel; // SMS, EMAIL, IN_APP, PUSH
    private String recipientEmail;
    private String recipientPhone;
    private LocalDateTime sentAt;
    private LocalDateTime readAt;
    private boolean isRead;
    private String status; // SENT, FAILED, PENDING
    private String errorMessage;
    private int retryCount;
    private LocalDateTime createdAt;

    public Notification() {
    }

    public Notification(String notificationId, String patientId, String notificationType) {
        this.notificationId = notificationId;
        this.patientId = patientId;
        this.notificationType = notificationType;
        this.status = "SENT";
        this.sentAt = LocalDateTime.now();
        this.createdAt = LocalDateTime.now();
        this.isRead = false;
        this.retryCount = 0;
    }

    // Getters and Setters
    public String getNotificationId() { return notificationId; }
    public void setNotificationId(String notificationId) { this.notificationId = notificationId; }

    public String getPatientId() { return patientId; }
    public void setPatientId(String patientId) { this.patientId = patientId; }

    public String getNotificationType() { return notificationType; }
    public void setNotificationType(String notificationType) { this.notificationType = notificationType; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getChannel() { return channel; }
    public void setChannel(String channel) { this.channel = channel; }

    public String getRecipientEmail() { return recipientEmail; }
    public void setRecipientEmail(String recipientEmail) { this.recipientEmail = recipientEmail; }

    public String getRecipientPhone() { return recipientPhone; }
    public void setRecipientPhone(String recipientPhone) { this.recipientPhone = recipientPhone; }

    public LocalDateTime getSentAt() { return sentAt; }
    public void setSentAt(LocalDateTime sentAt) { this.sentAt = sentAt; }

    public LocalDateTime getReadAt() { return readAt; }
    public void setReadAt(LocalDateTime readAt) { this.readAt = readAt; }

    public boolean isRead() { return isRead; }
    public void setRead(boolean read) { isRead = read; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }

    public int getRetryCount() { return retryCount; }
    public void setRetryCount(int retryCount) { this.retryCount = retryCount; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return "Notification{" +
                "notificationId='" + notificationId + '\'' +
                ", patientId='" + patientId + '\'' +
                ", notificationType='" + notificationType + '\'' +
                ", channel='" + channel + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
