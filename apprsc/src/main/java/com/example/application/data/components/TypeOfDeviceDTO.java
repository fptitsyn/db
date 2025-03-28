package com.example.application.data.components;

public class TypeOfDeviceDTO {
    private Long id;
    private String name;

    public TypeOfDeviceDTO() {}

    public TypeOfDeviceDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    // Геттеры и сеттеры
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}