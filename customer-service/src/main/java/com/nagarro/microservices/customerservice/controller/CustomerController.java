package com.nagarro.microservices.customerservice.controller;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.nagarro.microservices.customerservice.beans.Customer;
import com.nagarro.microservices.customerservice.exception.ResourceNotFoundException;
import com.nagarro.microservices.customerservice.repository.CustomerRepository;

@RefreshScope
@RestController
public class CustomerController {

	Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private CustomerRepository customerRepository;

	@Value("${message:Hello default}")
	private String message;
	
	@GetMapping("/message")
	public String getMessage() {
		return this.message;
	}
	
	@GetMapping(path = "/")
	public String hello() {
		return "RestAPI for customer panel.";
	}

	@GetMapping("/customers")
	public List<Customer> getAllCustomers() {
		return customerRepository.findAll();
	}

	@GetMapping("/customers/{userId}")
	public Customer getCustomerById(@PathVariable("userId") Long userId) {
		return customerRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("UserId: " + userId + " not found"));
	}
	
	@GetMapping("/customers/exits/{userId}")
	public boolean customerExitsById(@PathVariable("userId") Long userId) {
		return customerRepository.existsById(userId);
	}

	@PostMapping(path = "/addCustomer", consumes = "application/json")
	public void addCustomer(@Valid @RequestBody Customer customer) {
		try {
			customerRepository.save(customer);
			LOGGER.info("New Customer created");
		} catch (Exception e) {
			LOGGER.error("Exception occured: ", e);
		}
	}

	/**
	 * @param id
	 * @param customerRequest
	 */
	@PutMapping(value = "/customers/{userId}", consumes = "application/json")
	public Customer updateCustomer(@PathVariable("userId") Long userId, @RequestBody Customer customerRequest) {
		return customerRepository.findById(userId).map(customer -> {
			customer.setName(customerRequest.getName());
			customer.setContactNumber(customerRequest.getContactNumber());
			return customerRepository.save(customer);
		}).orElseThrow(() -> new ResourceNotFoundException("UserId: " + userId + " not found"));
	}

	@DeleteMapping("/customers/{userId}")
	public ResponseEntity<Object> deleteCustomer(@PathVariable("userId") Long userId) {
		return customerRepository.findById(userId).map(customer -> {
			customerRepository.delete(customer);
			return ResponseEntity.ok().build();
		}).orElseThrow(() -> new ResourceNotFoundException("UserId " + userId + " not found"));
	}

}
