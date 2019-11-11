package com.nagarro.microservices.customerservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nagarro.microservices.customerservice.beans.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

}
