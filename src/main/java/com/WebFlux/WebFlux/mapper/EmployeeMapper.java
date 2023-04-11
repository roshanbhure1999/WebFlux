package com.WebFlux.WebFlux.mapper;

import com.WebFlux.WebFlux.dto.EmployeeDto;
import com.WebFlux.WebFlux.entity.Employee;

import java.util.Objects;

public class EmployeeMapper {
    public static EmployeeDto mapToEmployeeDto(Employee employee) {
        EmployeeDto employeeDto = null;
        if (Objects.nonNull(employee)) {
            employeeDto = new EmployeeDto(
                    employee.getId(),
                    employee.getFirstName(),
                    employee.getLastName(),
                    employee.getEmail()
            );
        }
        return employeeDto;
    }

    public static Employee mapToEmployee(EmployeeDto employeeDto) {
        Employee employee = null;
        if (Objects.nonNull(employeeDto)) {
            employee = new Employee(
                    employeeDto.getId(),
                    employeeDto.getFirstName(),
                    employeeDto.getLastName(),
                    employeeDto.getEmail()
            );

        }

        return employee;
    }
}
