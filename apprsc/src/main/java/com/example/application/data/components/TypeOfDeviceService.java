package com.example.application.data.components;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class TypeOfDeviceService {

    private final TypeOfDeviceRepository typeOfDeviceRepository;

    @Autowired
    public TypeOfDeviceService(TypeOfDeviceRepository typeOfDeviceRepository) {
        this.typeOfDeviceRepository = typeOfDeviceRepository;
    }

    // Основной метод сохранения (создание и обновление)
    public TypeOfDeviceDTO saveDeviceType(TypeOfDeviceDTO dto) {
        TypeOfDevice type = dto.getId() != null ?
                typeOfDeviceRepository.findById(dto.getId())
                        .orElse(new TypeOfDevice()) :
                new TypeOfDevice();

        type.setTypeOfDeviceName(dto.getName());
        TypeOfDevice saved = typeOfDeviceRepository.save(type);
        return convertToDTO(saved);
    }

    // Получение по имени
    public Optional<TypeOfDeviceDTO> getByName(String name) {
        return typeOfDeviceRepository.findByTypeOfDeviceName(name)
                .map(this::convertToDTO);
    }

    // Получение всех DTO
    public List<TypeOfDeviceDTO> getAllDeviceTypes() {
        return typeOfDeviceRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Получение только имен
    public List<String> getAllDeviceTypeNames() {
        return typeOfDeviceRepository.findAll().stream()
                .map(TypeOfDevice::getTypeOfDeviceName)
                .collect(Collectors.toList());
    }

    // Удаление
    @Transactional
    public void deleteDeviceType(Long id) {
        typeOfDeviceRepository.deleteById(id);
    }

    // Поиск или создание
    public TypeOfDevice findOrCreate(String deviceTypeName) {
        return typeOfDeviceRepository.findByTypeOfDeviceName(deviceTypeName)
                .orElseGet(() -> {
                    TypeOfDevice newType = new TypeOfDevice();
                    newType.setTypeOfDeviceName(deviceTypeName);
                    return typeOfDeviceRepository.save(newType);
                });
    }

    // Конвертер в DTO
    private TypeOfDeviceDTO convertToDTO(TypeOfDevice entity) {
        return new TypeOfDeviceDTO(
                entity.getTypeOfDeviceId(),
                entity.getTypeOfDeviceName()
        );
    }

    // Конвертер из DTO (если нужен)
    private TypeOfDevice convertToEntity(TypeOfDeviceDTO dto) {
        TypeOfDevice entity = new TypeOfDevice();
        entity.setTypeOfDeviceId(dto.getId());
        entity.setTypeOfDeviceName(dto.getName());
        return entity;
    }
}