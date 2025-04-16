package com.example.application.reports.orders;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderInfoService {
    private final OrderInfoRepository repository;

    public OrderInfoService(OrderInfoRepository repository) {
        this.repository = repository;
    }

    public List<OrderInfoDTO> getAllOrderInfo() {
        return repository.findAllOrderInfo();
    }
}