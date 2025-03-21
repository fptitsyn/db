package com.example.application.services;

import com.example.application.data.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ComponentService {

    @Autowired
    private ComponentRepository componentRepository;

    @Autowired
    private CategoryOfComponentRepository categoryOfComponentRepository;

    @Autowired
    private TypeOfDeviceRepository typeOfDeviceRepository;

    public List<Component> findAll() {
        return componentRepository.findAll();
    }

    public Component save(Component component) {
        return componentRepository.save(component);
    }

    public void delete(Component component) {
        componentRepository.delete(component);
    }

    public List<CategoryOfComponent> findCategoriesByTypeOfDevice(TypeOfDevice typeOfDevice) {
        return categoryOfComponentRepository.findByTypeOfDevice(typeOfDevice);
    }

    public List<TypeOfDevice> findAllTypesOfDevices() {
        return typeOfDeviceRepository.findAll();
    }
}