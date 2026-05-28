package com.timora.app.controller;

import com.timora.app.model.Customer;
import com.timora.app.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

//    private final CustomerService customerService;
//
//    @GetMapping
//    public List<Customer> getAll() {
//        return customerService.findAll();
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<Customer> getById(@PathVariable Long id) {
//
//        return customerService.findById(id)
//                .map(ResponseEntity::ok)
//                .orElse(ResponseEntity.notFound().build());
//    }
//
//    @PostMapping
//    public Customer create(@RequestBody Customer customer) {
//        return customerService.save(customer);
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<Customer> update(
//            @PathVariable Long id,
//            @RequestBody Customer customer
//    ) {
//
//        return ResponseEntity.ok(
//                customerService.update(id, customer)
//        );
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> delete(@PathVariable Long id) {
//
//        customerService.delete(id);
//
//        return ResponseEntity.noContent().build();
//    }

}
