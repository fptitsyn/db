package com.example.application.data.employees;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public interface EmployeesRepository extends JpaRepository<Employees, Long>, JpaSpecificationExecutor<Employees> {

    List<Employees> findByLastNameContainingIgnoreCaseOrFirstNameContainingIgnoreCaseOrMiddleNameContainingIgnoreCase(
            String lastNamePart,
            String firstNamePart,
            String middleNamePart
    );

    Optional<Employees> findById(Long id);

    @Query(value = "SELECT employee_id, full_name_with_position FROM get_employees_by_location(:locationId)",
            nativeQuery = true)
    List<Object[]> findEmployeesWithPositionByLocation(@Param("locationId") Long locationId);

    default List<EmployeeWithPositionDTO> findEmployeesWithPosition(Long locationId) {
        return findEmployeesWithPositionByLocation(locationId).stream()
                .map(arr -> new EmployeeWithPositionDTO(
                        ((Number) arr[0]).longValue(),
                        (String) arr[1]
                ))
                .collect(Collectors.toList());
    }
}