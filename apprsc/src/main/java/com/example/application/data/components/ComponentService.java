package com.example.application.data.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ComponentService {
    private final ComponentRepository componentRepo;
    private final TypeOfDeviceService typeService;
    private final CategoryOfComponentService categoryService;

    @Autowired
    public ComponentService(
            ComponentRepository componentRepo,
            TypeOfDeviceService typeService,
            CategoryOfComponentService categoryService
    ) {
        this.componentRepo = componentRepo;
        this.typeService = typeService;
        this.categoryService = categoryService;
    }

    public List<ComponentDTO> findAllComponents() {
        return componentRepo.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public void saveComponent(ComponentDTO dto) {
        // 1. Найти или создать категорию
        TypeOfDevice deviceType = typeService.findOrCreate(dto.getTypeOfDeviceName());
        CategoryOfComponent category = categoryService.findOrCreate(dto.getTypeOfPartName(), deviceType);

        // 2. Найти существующий компонент или создать новый
        Component component = dto.getComponentId() != null ?
                componentRepo.findById(dto.getComponentId()).orElse(new Component()) :
                new Component();

        // 3. Обновить поля
        component.setName(dto.getComponentName());
        component.setCost(dto.getCost());
        component.setCategory(category);

        // 4. Сохранить
        componentRepo.save(component);
    }
    public void deleteComponent(Long componentId) {
        componentRepo.deleteById(componentId);
    }

    private ComponentDTO convertToDTO(Component component) {
        ComponentDTO dto = new ComponentDTO();
        dto.setComponentId(component.getComponentId());
        dto.setComponentName(component.getName());
        dto.setCost(component.getCost());
        dto.setTypeOfDeviceName(component.getCategory().getTypeOfDevice().getTypeOfDeviceName());
        dto.setTypeOfPartName(component.getCategory().getTypeOfPartName());
        return dto;
    }

    public ComponentDTO getComponentById(Long id) {
        return componentRepo.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new RuntimeException("Component not found"));
    }

}