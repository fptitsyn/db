package com.example.application.services;

import com.example.application.data.CategoryOfComponent;
import com.example.application.data.CategoryOfComponentRepository;
import com.example.application.data.TypeOfDevice;
import com.example.application.data.TypeOfDeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryOfComponentService {

    @Autowired
    private CategoryOfComponentRepository repository;

    @Autowired
    private TypeOfDeviceRepository typeOfDeviceRepository;

    public List<CategoryOfComponent> findAll() {
        return repository.findAll();
    }

    public CategoryOfComponent save(CategoryOfComponent categoryOfComponent) {
        return repository.save(categoryOfComponent);
    }

    public void delete(CategoryOfComponent categoryOfComponent) {
        repository.delete(categoryOfComponent);
    }

    public List<TypeOfDevice> findAllTypesOfDevices() {
        return typeOfDeviceRepository.findAll();
    }
}
