package com.healthbridge.billing;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Invoice entity class
 */
public class Invoice {
    private String invoiceId;
    private String patientId;
    private String patientName;
    private String invoiceType;
    private double amount;
    private double totalPaymentReceived = 0;
    private String department;
    private String description;
    private LocalDateTime invoiceDate;
    private LocalDateTime dueDate;
    private LocalDateTime paidDate;
    private String status; // PENDING, PARTIALLY_PAID, PAID, OVERDUE, CANCELLED
    private List<Payment> payments;
    private String notes;

    public Invoice() {
        this.payments = new ArrayList<>();
    }

    public Invoice(String invoiceId, String patientId, String invoiceType, double amount) {
        this.invoiceId = invoiceId;
        this.patientId = patientId;
        this.invoiceType = invoiceType;
        this.amount = amount;
        this.status = "PENDING";
        this.invoiceDate = LocalDateTime.now();
        this.dueDate = LocalDateTime.now().plusDays(30);
        this.payments = new ArrayList<>();
    }

    // Getters and Setters
    public String getInvoiceId() { return invoiceId; }
    public void setInvoiceId(String invoiceId) { this.invoiceId = invoiceId; }

    public String getPatientId() { return patientId; }
    public void setPatientId(String patientId) { this.patientId = patientId; }

    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }

    public String getInvoiceType() { return invoiceType; }
    public void setInvoiceType(String invoiceType) { this.invoiceType = invoiceType; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public double getTotalPaymentReceived() { return totalPaymentReceived; }
    public void setTotalPaymentReceived(double totalPaymentReceived) { this.totalPaymentReceived = totalPaymentReceived; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getInvoiceDate() { return invoiceDate; }
    public void setInvoiceDate(LocalDateTime invoiceDate) { this.invoiceDate = invoiceDate; }

    public LocalDateTime getDueDate() { return dueDate; }
    public void setDueDate(LocalDateTime dueDate) { this.dueDate = dueDate; }

    public LocalDateTime getPaidDate() { return paidDate; }
    public void setPaidDate(LocalDateTime paidDate) { this.paidDate = paidDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public List<Payment> getPayments() { return new ArrayList<>(payments); }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public void addPayment(Payment payment) {
        payments.add(payment);
        totalPaymentReceived += payment.getAmount();
        if (totalPaymentReceived >= amount) {
            this.paidDate = LocalDateTime.now();
        }
    }

    public double getRemainingAmount() {
        return Math.max(0, amount - totalPaymentReceived);
    }

    public boolean isPaid() {
        return totalPaymentReceived >= amount;
    }

    public boolean hasPartialPayment() {
        return totalPaymentReceived > 0 && totalPaymentReceived < amount;
    }

    @Override
    public String toString() {
        return "Invoice{" +
                "invoiceId='" + invoiceId + '\'' +
                ", patientId='" + patientId + '\'' +
                ", amount=" + amount +
                ", status='" + status + '\'' +
                '}';
    }
}
