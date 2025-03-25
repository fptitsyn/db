package com.example.application.services;

import com.example.application.data.Services;
import com.example.application.data.ServicesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServicesService {
    private final ServicesRepository repository;

    @Autowired
    public ServicesService(ServicesRepository repository) {this.repository = repository;}

    public Services save(Services service) {return repository.save(service);}

    public void delete(Services service) {repository.delete(service);}

    public List<Services> findAllServices() {return repository.findAll();}
}
