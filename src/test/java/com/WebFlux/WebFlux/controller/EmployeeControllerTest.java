package com.WebFlux.WebFlux.controller;

import com.WebFlux.WebFlux.dto.EmployeeDto;
import com.WebFlux.WebFlux.repository.EmployeeRepository;
import com.WebFlux.WebFlux.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.Collections;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class EmployeeControllerTest {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Test
    void saveEmployee() {
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setId("test 1");
        employeeDto.setFirstName("John");
        employeeDto.setLastName("Cena");
        employeeDto.setEmail("john@gmail.com");

        webTestClient.post().uri("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(employeeDto), EmployeeDto.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$.firstName").isEqualTo(employeeDto.getFirstName())
                .jsonPath("$.lastName").isEqualTo(employeeDto.getLastName())
                .jsonPath("$.email").isEqualTo(employeeDto.getEmail());
    }


    @Test
    void getEmployee() {
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setId("test");
        employeeDto.setFirstName("Ramesh");
        employeeDto.setLastName("Fadatare");
        employeeDto.setEmail("ramesh@gmail.com");

        employeeService.saveEmployee(employeeDto).block();
        EmployeeDto savedEmployee = employeeService.getEmployee(employeeDto.getId()).block();

        webTestClient.get().uri("/api/employees/{id}", Collections.singletonMap("id", savedEmployee.getId()))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$.id").isEqualTo(savedEmployee.getId())
                .jsonPath("$.firstName").isEqualTo(employeeDto.getFirstName())
                .jsonPath("$.lastName").isEqualTo(employeeDto.getLastName())
                .jsonPath("$.email").isEqualTo(employeeDto.getEmail());
    }

    @Test
    void getAllEmployees() {
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setFirstName("John");
        employeeDto.setLastName("Cena");
        employeeDto.setEmail("john@gmail.com");

        employeeService.saveEmployee(employeeDto).block();

        EmployeeDto employeeDto1 = new EmployeeDto();
        employeeDto1.setFirstName("Meena");
        employeeDto1.setLastName("Fadatare");
        employeeDto1.setEmail("meena@gmail.com");

        // employeeService.saveEmployee(employeeDto1).block();
        employeeService.getAllEmployees();
        webTestClient.get().uri("/api/employees")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(EmployeeDto.class)
                .consumeWith(System.out::println);
    }

    @Test
    void updateEmployee() {
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setFirstName("Ramesh");
        employeeDto.setLastName("Fadatare");
        employeeDto.setEmail("ramesh@gmail.com");

        EmployeeDto savedEmployee = employeeService.saveEmployee(employeeDto).block();

        EmployeeDto updatedEmployee = new EmployeeDto();
        updatedEmployee.setFirstName("Ram");
        updatedEmployee.setLastName("Jadhav");
        updatedEmployee.setEmail("ram@gmail.com");

        webTestClient.put().uri("/api/employees/{id}", Collections.singletonMap("id", savedEmployee.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(updatedEmployee), EmployeeDto.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$.firstName").isEqualTo(updatedEmployee.getFirstName())
                .jsonPath("$.lastName").isEqualTo(updatedEmployee.getLastName())
                .jsonPath("$.email").isEqualTo(updatedEmployee.getEmail());
    }

    @Test
    void deleteEmployee() {
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setId("test");
        employeeDto.setFirstName("Ramesh");
        employeeDto.setLastName("Fadatare");
        employeeDto.setEmail("ramesh@gmail.com");

        EmployeeDto savedEmployee = employeeService.saveEmployee(employeeDto).block();

        webTestClient.delete().uri("/api/employees/{id}", Collections.singletonMap("id", employeeDto.getId()))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(System.out::println);

    }
}