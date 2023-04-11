package com.WebFlux.WebFlux.service.impl;

import com.WebFlux.WebFlux.contant.Constant;
import com.WebFlux.WebFlux.dto.EmployeeDto;
import com.WebFlux.WebFlux.entity.Employee;
import com.WebFlux.WebFlux.exception.UserException;
import com.WebFlux.WebFlux.mapper.EmployeeMapper;
import com.WebFlux.WebFlux.repository.EmployeeRepository;
import com.WebFlux.WebFlux.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;

import static com.WebFlux.WebFlux.contant.Constant.EMPLOYEE_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Override
    public Mono<EmployeeDto> saveEmployee(EmployeeDto employeeDto) {
        return employeeRepository.save(EmployeeMapper.mapToEmployee(employeeDto))
                .map(EmployeeMapper::mapToEmployeeDto).
                switchIfEmpty(Mono.error(new UserException(Constant.EMPLOYEE_NOT_CREATE, HttpStatus.INTERNAL_SERVER_ERROR)));
    }

    @Override
    public Mono<EmployeeDto> getEmployee(String employeeId) {
        return employeeRepository.findById(employeeId).filter(Objects::nonNull)
                .map((EmployeeMapper::mapToEmployeeDto))
                .switchIfEmpty(Mono.error(new UserException(String.format("%s for this given id :%s", EMPLOYEE_NOT_FOUND, employeeId), HttpStatus.BAD_REQUEST)));
    }

    @Override
    public Flux<EmployeeDto> getAllEmployees() {
        Flux<Employee> employeeFlux = employeeRepository.findAll()
                .switchIfEmpty(Mono.error(new UserException(EMPLOYEE_NOT_FOUND, HttpStatus.NO_CONTENT)));
        return employeeFlux
                .map(EmployeeMapper::mapToEmployeeDto)
                .switchIfEmpty(Flux.empty());
    }

    @Override
    public Mono<EmployeeDto> updateEmployee(EmployeeDto employeeDto, String employeeId) {
        Mono<Employee> employeeMono = employeeRepository.findById(employeeId)
                .switchIfEmpty(Mono.error(new UserException(EMPLOYEE_NOT_FOUND, HttpStatus.NO_CONTENT)));

        return employeeMono.flatMap(existingEmployee -> {
            existingEmployee.setFirstName(employeeDto.getFirstName());
            existingEmployee.setLastName(employeeDto.getLastName());
            existingEmployee.setEmail(employeeDto.getEmail());
            return employeeRepository.save(existingEmployee);
        }).map((EmployeeMapper::mapToEmployeeDto));
    }

    @Override
    public Mono<?> deleteEmployee(String employeeId) {
        return employeeRepository.findById(employeeId)
                .map(employeeRepository::delete)
                .switchIfEmpty(Mono.error(new UserException(EMPLOYEE_NOT_FOUND, HttpStatus.NO_CONTENT)));

    }
}
