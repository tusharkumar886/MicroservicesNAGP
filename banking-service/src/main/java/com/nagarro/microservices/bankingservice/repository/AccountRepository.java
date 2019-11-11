package com.nagarro.microservices.bankingservice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nagarro.microservices.bankingservice.beans.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
	List<Account> findByCustomerUserId(Long userId);
	Optional<Account> findByAccountNoAndCustomerUserId(String accountNo, Long userId);
	Optional<Account> findByAccountNo(String accountNo);
}