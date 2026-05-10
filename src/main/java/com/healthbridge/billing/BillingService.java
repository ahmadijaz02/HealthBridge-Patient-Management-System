package com.healthbridge.billing;

import com.healthbridge.patient.Patient;
import com.healthbridge.patient.PatientManager;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Billing Service - handles patient billing and invoices
 * Demonstrates dependency on PatientManager
 */
public class BillingService {
    private Map<String, Invoice> invoices = new HashMap<>();
    private Map<String, Payment> payments = new HashMap<>();
    private PatientManager patientManager;
    private int invoiceCounter = 2000;
    private int paymentCounter = 3000;
    private List<String> auditLog = new ArrayList<>();
    private Map<String, Double> departmentRevenue = new HashMap<>();
    private double consultationCost = 150.0;
    private double diagnosisCost = 75.0;
    private double procedureCost = 500.0;
    private double laboratoryTestCost = 100.0;

    public BillingService(PatientManager patientManager) {
        this.patientManager = patientManager;
    }

    /**
     * Create invoice for patient
     */
    public Invoice createInvoice(String patientId, String invoiceType, String department,
                                String description, double amount) {
        
        Patient patient = patientManager.getPatient(patientId);
        if (patient == null) {
            System.out.println("ERROR: Patient not found: " + patientId);
            return null;
        }

        String invoiceId = "INV" + String.format("%06d", invoiceCounter++);
        Invoice invoice = new Invoice(invoiceId, patientId, invoiceType, amount);
        invoice.setDepartment(department);
        invoice.setDescription(description);
        invoice.setPatientName(patient.getFullName());

        invoices.put(invoiceId, invoice);
        
        // Update department revenue
        departmentRevenue.put(department, departmentRevenue.getOrDefault(department, 0.0) + amount);

        auditLog.add("[" + System.currentTimeMillis() + "] INVOICE_CREATED " + invoiceId + 
                     " patient=" + patientId + " amount=" + amount);

        System.out.println("Invoice created: " + invoiceId + " for patient: " + patientId);
        return invoice;
    }

    /**
     * Record payment for invoice
     */
    public Payment recordPayment(String invoiceId, double amount, String paymentMethod) {
        
        Invoice invoice = invoices.get(invoiceId);
        if (invoice == null) {
            System.out.println("ERROR: Invoice not found: " + invoiceId);
            return null;
        }

        if (amount <= 0 || amount > invoice.getAmount()) {
            System.out.println("ERROR: Invalid payment amount");
            return null;
        }

        String paymentId = "PAY" + String.format("%06d", paymentCounter++);
        Payment payment = new Payment(paymentId, invoiceId, amount, paymentMethod);

        payments.put(paymentId, payment);
        invoice.addPayment(payment);

        if (invoice.isPaid()) {
            invoice.setStatus("PAID");
            auditLog.add("[" + System.currentTimeMillis() + "] INVOICE_PAID " + invoiceId + 
                         " totalAmount=" + invoice.getTotalPaymentReceived());
        } else if (invoice.hasPartialPayment()) {
            invoice.setStatus("PARTIALLY_PAID");
        }

        auditLog.add("[" + System.currentTimeMillis() + "] PAYMENT_RECORDED " + paymentId + 
                     " invoice=" + invoiceId + " amount=" + amount + " method=" + paymentMethod);

        System.out.println("Payment recorded: " + paymentId);
        return payment;
    }

    /**
     * Get invoice by ID
     */
    public Invoice getInvoice(String invoiceId) {
        return invoices.get(invoiceId);
    }

    /**
     * Get all invoices for a patient
     */
    public List<Invoice> getPatientInvoices(String patientId) {
        List<Invoice> result = new ArrayList<>();
        for (Invoice invoice : invoices.values()) {
            if (invoice.getPatientId().equals(patientId)) {
                result.add(invoice);
            }
        }
        return result;
    }

    /**
     * Get outstanding invoices for a patient
     */
    public List<Invoice> getOutstandingInvoices(String patientId) {
        List<Invoice> result = new ArrayList<>();
        for (Invoice invoice : invoices.values()) {
            if (invoice.getPatientId().equals(patientId) && 
                (invoice.getStatus().equals("PENDING") || invoice.getStatus().equals("PARTIALLY_PAID"))) {
                result.add(invoice);
            }
        }
        return result;
    }

    /**
     * Get total outstanding amount for patient
     */
    public double getOutstandingAmount(String patientId) {
        double total = 0;
        for (Invoice invoice : getOutstandingInvoices(patientId)) {
            total += invoice.getRemainingAmount();
        }
        return total;
    }

    /**
     * Send payment reminder
     */
    public boolean sendPaymentReminder(String invoiceId) {
        Invoice invoice = invoices.get(invoiceId);
        if (invoice == null) {
            return false;
        }

        if (invoice.getStatus().equals("PAID")) {
            System.out.println("Invoice already paid: " + invoiceId);
            return false;
        }

        System.out.println("Payment reminder sent for invoice: " + invoiceId + 
                          " to patient: " + invoice.getPatientId());
        auditLog.add("[" + System.currentTimeMillis() + "] PAYMENT_REMINDER_SENT " + invoiceId);
        return true;
    }

    /**
     * Get revenue by department
     */
    public Map<String, Double> getRevenueByDepartment() {
        return new HashMap<>(departmentRevenue);
    }

    /**
     * Get total revenue
     */
    public double getTotalRevenue() {
        return departmentRevenue.values().stream().mapToDouble(Double::doubleValue).sum();
    }

    /**
     * Get billing statistics
     */
    public Map<String, Object> getBillingStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalInvoices", invoices.size());
        stats.put("totalPayments", payments.size());
        stats.put("totalRevenue", getTotalRevenue());
        stats.put("paidInvoices", (int) invoices.values().stream()
                .filter(inv -> inv.getStatus().equals("PAID")).count());
        stats.put("pendingInvoices", (int) invoices.values().stream()
                .filter(inv -> inv.getStatus().equals("PENDING")).count());
        stats.put("partiallyPaidInvoices", (int) invoices.values().stream()
                .filter(inv -> inv.getStatus().equals("PARTIALLY_PAID")).count());
        return stats;
    }

    /**
     * Get audit log
     */
    public List<String> getAuditLog() {
        return new ArrayList<>(auditLog);
    }

    // Getters for costs
    public double getConsultationCost() { return consultationCost; }
    public void setConsultationCost(double cost) { this.consultationCost = cost; }

    public double getDiagnosisCost() { return diagnosisCost; }
    public void setDiagnosisCost(double cost) { this.diagnosisCost = cost; }

    public double getProcedureCost() { return procedureCost; }
    public void setProcedureCost(double cost) { this.procedureCost = cost; }

    public double getLaboratoryTestCost() { return laboratoryTestCost; }
    public void setLaboratoryTestCost(double cost) { this.laboratoryTestCost = cost; }
}
