package com.example.application.data.services;

import com.example.application.data.components.TypeOfDevice;
import com.example.application.data.components.TypeOfDeviceRepository;
import com.example.application.data.employees.Employees;
import com.example.application.data.employees.EmployeesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServicesService {
    private final ServicesRepository servicesRepository;
    private final TypeOfDeviceRepository typeOfDeviceRepository;

    @Autowired
    public ServicesService(ServicesRepository servicesRepository,
                          TypeOfDeviceRepository typeOfDeviceRepository) {
        this.servicesRepository = servicesRepository;
        this.typeOfDeviceRepository = typeOfDeviceRepository;
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

}
