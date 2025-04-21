package com.example.application.data.employees;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public interface EmployeesRepository extends JpaRepository<Employees, Long>, JpaSpecificationExecutor<Employees> {

    List<Employees> findByLastNameContainingIgnoreCaseOrFirstNameContainingIgnoreCaseOrMiddleNameContainingIgnoreCase(
            String lastNamePart,
            String firstNamePart,
            String middleNamePart
    );

    @Query(value = "SELECT * FROM get_available_employees_by_city(:city, :staffingTableId)",
            nativeQuery = true)
    List<Object[]> findAvailableEmployeesByCityAndOffice(
            @Param("city") String city,
            @Param("staffingTableId") Long staffingTableId
    );

    default List<Employees> findAvailableEmployees(String city, Long staffingTableId) {
        return findAvailableEmployeesByCityAndOffice(city, staffingTableId).stream()
                .map(arr -> {
                    Employees employee = new Employees();
                    employee.setId(((Number) arr[0]).longValue());
                    employee.setFirstName((String) arr[1]);
                    employee.setLastName((String) arr[2]);
                    employee.setMiddleName((String) arr[3]);
                    return employee;
                })
                .collect(Collectors.toList());
    }

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