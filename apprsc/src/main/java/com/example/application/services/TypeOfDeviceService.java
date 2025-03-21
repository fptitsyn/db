package com.example.application.services;

import com.example.application.data.TypeOfDevice;
import com.example.application.data.TypeOfDeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TypeOfDeviceService {

    @Autowired
    private TypeOfDeviceRepository repository;

    public List<TypeOfDevice> findAll() {
        return repository.findAll();
    }

    public TypeOfDevice save(TypeOfDevice typeOfDevice) {
        return repository.save(typeOfDevice);
    }

    public void delete(TypeOfDevice typeOfDevice) {
        repository.delete(typeOfDevice);
    }
}
