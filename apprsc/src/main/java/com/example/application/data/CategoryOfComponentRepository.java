package com.example.application.data;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryOfComponentRepository extends JpaRepository<CategoryOfComponent, Long> {

    List<CategoryOfComponent> findByTypeOfDevice(TypeOfDevice typeOfDevice);
}
