package com.example.application.data.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryOfComponentService {

    private final CategoryOfComponentRepository categoryOfComponentRepository;
    private TypeOfDeviceRepository typeOfDeviceRepository;
    private final TypeOfDeviceService typeOfDeviceService;
    @Autowired
    public CategoryOfComponentService(
            CategoryOfComponentRepository categoryOfComponentRepository,
            TypeOfDeviceService typeOfDeviceService // Внедряем через конструктор
    ) {
        this.categoryOfComponentRepository = categoryOfComponentRepository;
        this.typeOfDeviceService = typeOfDeviceService;
    }
    public List<String> getPartNamesByDeviceType(String deviceTypeName) {
        return categoryOfComponentRepository.findByTypeOfDevice_TypeOfDeviceName(deviceTypeName)
                .stream()
                .map(CategoryOfComponent::getTypeOfPartName)
                .collect(Collectors.toList());
    }

    public CategoryOfComponent findOrCreate(String partTypeName, TypeOfDevice deviceType) {
        return categoryOfComponentRepository
                .findByTypeOfPartNameAndTypeOfDevice(partTypeName, deviceType)
                .orElseGet(() -> {
                    CategoryOfComponent newCategory = new CategoryOfComponent();
                    newCategory.setTypeOfPartName(partTypeName);
                    newCategory.setTypeOfDevice(deviceType);
                    return categoryOfComponentRepository.save(newCategory);
                });
    }
    public List<CategoryOfComponent> findAll() {
        return categoryOfComponentRepository.findAll();
    }

    public CategoryOfComponent save(CategoryOfComponent categoryOfComponent) {
        return categoryOfComponentRepository.save(categoryOfComponent);
    }

    public void delete(CategoryOfComponent categoryOfComponent) {
        categoryOfComponentRepository.delete(categoryOfComponent);
    }

    public List<TypeOfDevice> findAllTypesOfDevices() {
        return typeOfDeviceRepository.findAll();
    }

    public void saveCategory(CategoryDTO dto) {
        TypeOfDevice type = typeOfDeviceService.findOrCreate(dto.getDeviceType());
        CategoryOfComponent category = new CategoryOfComponent();
        category.setTypeOfDevice(type);
        category.setTypeOfPartName(dto.getName());
        categoryOfComponentRepository.save(category);
    }
}