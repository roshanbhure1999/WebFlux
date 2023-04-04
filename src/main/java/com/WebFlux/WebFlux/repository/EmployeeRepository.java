package com.WebFlux.WebFlux.repository;

import com.WebFlux.WebFlux.entity.Employee;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface EmployeeRepository extends ReactiveCrudRepository<Employee, String> {
}
