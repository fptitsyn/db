package com.example.application.data.components;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CategoryOfComponentRepository extends JpaRepository<CategoryOfComponent, Long> {
    @Query("SELECT c FROM CategoryOfComponent c WHERE c.typeOfPartName = :partName AND c.typeOfDevice = :deviceType")
    Optional<CategoryOfComponent> findByTypeOfPartNameAndTypeOfDevice(
            @Param("partName") String partName,
            @Param("deviceType") TypeOfDevice deviceType
    );

    @Query("SELECT c FROM CategoryOfComponent c WHERE c.typeOfDevice.typeOfDeviceName = :deviceTypeName")
    List<CategoryOfComponent> findByTypeOfDevice_TypeOfDeviceName(
            @Param("deviceTypeName") String deviceTypeName
    );

    List<CategoryOfComponent> findByTypeOfDevice(TypeOfDevice typeOfDevice);

}
