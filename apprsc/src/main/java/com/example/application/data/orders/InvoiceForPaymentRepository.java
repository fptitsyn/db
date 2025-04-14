package com.example.application.data.orders;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoiceForPaymentRepository extends JpaRepository<InvoiceForPayment, Long> {
    List<InvoiceForPayment> findByOrdersId(Long orderId);
}
