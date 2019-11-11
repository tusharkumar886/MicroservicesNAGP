package com.nagarro.microservices.bankingservice.proxy;

import java.util.List;

import javax.validation.Valid;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Component
// @FeignClient(name = "product-service", url="localhost:8000")
// @FeignClient(name = "product-service")
@FeignClient(name = "netflix-api-zuul-gateway-server")
@RibbonClient(name = "customer-service")
public interface CustomerServiceProxy {

	@GetMapping("/customer-service/customers")
	public List<Customer> getAllCustomers();
	
	@GetMapping("/customer-service/customers/{userId}")
	public Customer getCustomerById(@PathVariable("userId") Long userId);
	
	@GetMapping("/customer-service/customers/exits/{userId}")
	public boolean customerExitsById(@PathVariable("userId") Long userId);

	@PostMapping(path = "/customer-service/addCustomer")
	public void addCustomer(@Valid @RequestBody Customer customer);

	@PutMapping(value = "/customer-service/customers/{userId}", consumes = "application/json")
	public Customer updateCustomer(@PathVariable("userId") Long userId, @RequestBody Customer customerRequest);

	@DeleteMapping("/customer-service/customers/{userId}")
	public ResponseEntity<Object> deleteCustomer(@PathVariable("userId") Long userId);
}
