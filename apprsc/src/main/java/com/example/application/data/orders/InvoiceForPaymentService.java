package com.example.application.data.orders;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InvoiceForPaymentService {
    private final InvoiceForPaymentRepository repository;

    public InvoiceForPaymentService(InvoiceForPaymentRepository repository) {
        this.repository = repository;
    }

    public InvoiceForPayment save(InvoiceForPayment invoice) {
        return repository.save(invoice);
    }

    public List<InvoiceForPayment> findByOrderId(Long orderId) {
        return repository.findByOrdersId(orderId);
    }
}