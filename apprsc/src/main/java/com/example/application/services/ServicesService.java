package com.example.application.services;

import com.example.application.data.*;
import com.example.application.data.Services;
import com.example.application.data.ServicesRepository;
import com.example.application.data.components.TypeOfDevice;
import com.example.application.data.components.TypeOfDeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServicesService {
    private final ServicesRepository servicesRepository;
    private final TypeOfDeviceRepository typeOfDeviceRepository;
    private final EmployeesRepository employeesRepository;

    @Autowired
    public ServicesService(ServicesRepository servicesRepository,
                          TypeOfDeviceRepository typeOfDeviceRepository,
                          EmployeesRepository employeesRepository) {
        this.servicesRepository = servicesRepository;
        this.typeOfDeviceRepository = typeOfDeviceRepository;
        this.employeesRepository = employeesRepository;
    }

    public List<Services> findAll() {
        return servicesRepository.findAll();
    }

    public Services save(Services services) {
        return servicesRepository.save(services);
    }

    public void delete(Services services) {
        servicesRepository.delete(services);
    }

    public List<TypeOfDevice> findAllTypesOfDevices() {
        return typeOfDeviceRepository.findAll();
    }

    public List<Employees> findAllEmployees() {
        return employeesRepository.findAll();
    }
}
