package com.nagarro.microservices.bankingservice.controller;

import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.nagarro.microservices.bankingservice.beans.Account;
import com.nagarro.microservices.bankingservice.exception.ResourceNotFoundException;
import com.nagarro.microservices.bankingservice.proxy.Customer;
import com.nagarro.microservices.bankingservice.proxy.CustomerServiceProxy;
import com.nagarro.microservices.bankingservice.repository.AccountRepository;

@RefreshScope
@RestController
public class AccountController {

	Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private CustomerServiceProxy customerServiceProxy;

	/**
	 * @param postId
	 * @param pageable
	 * @return Account details
	 */
	@GetMapping("/users/{userId}/accounts")
	public List<Account> getAllCommentsByPostId(@PathVariable(value = "userId") Long userId) {
		return accountRepository.findByCustomerUserId(userId);
	}

	/**
	 * @param userId
	 * @param account
	 * @return created Account object
	 */
	@PostMapping("/users/{userId}/createAccount")
	public Account createAccount(@PathVariable(value = "userId") Long userId, @Valid @RequestBody Account account) {
		try {
			Customer customer = customerServiceProxy.getCustomerById(userId);
			account.setCustomer(customer);
			account.setAccountNo(UUID.randomUUID().toString());
			LOGGER.info("Account created.");
			return accountRepository.save(account);
		} catch (ResourceNotFoundException e) {
			throw new ResourceNotFoundException("UserId " + userId + " not found");
		}
	}

	/**
	 * @param userId
	 * @param accountNo
	 * @param branchName
	 * @return Account
	 */
	@PutMapping("/users/{userId}/branch/{accountNo}")
    public Account updateBranch(@PathVariable (value = "userId") Long userId,
                                 @PathVariable (value = "accountNo") String accountNo,
                                 @Valid @RequestBody String branchName) {
		if(!customerServiceProxy.customerExitsById(userId)) {
            throw new ResourceNotFoundException("UserId " + userId + " not found");
        }
		
        return accountRepository.findByAccountNo(accountNo).map(account -> {
            account.setBranch(branchName);
            LOGGER.info("Branch changed");
            return accountRepository.save(account);
        }).orElseThrow(() -> new ResourceNotFoundException("AccountNo: " + accountNo + "not found"));
    }
	
	/**
	 * @param userId
	 * @param accountNo
	 * @param amount
	 * @return Account
	 */
	@PutMapping("/users/{userId}/balance/{accountNo}")
    public Account updateBalance(@PathVariable (value = "userId") Long userId,
                                 @PathVariable (value = "accountNo") String accountNo,
                                 @Valid @RequestBody Double amount) {
        if(!customerServiceProxy.customerExitsById(userId)) {
            throw new ResourceNotFoundException("UserId " + userId + " not found");
        }

        return accountRepository.findByAccountNo(accountNo).map(account -> {
            account.setBalance(account.getBalance() + amount);
            return accountRepository.save(account);
        }).orElseThrow(() -> new ResourceNotFoundException("AccountNo: " + accountNo + "not found"));
    }

	/**
	 * @param userId
	 * @param accountNo
	 */
	@DeleteMapping("/users/{userId}/accounts/{accountNo}")
	public ResponseEntity<?> deleteAccount(@PathVariable(value = "userId") Long userId,
			@PathVariable(value = "accountNo") String accountNo) {
		return accountRepository.findByAccountNoAndCustomerUserId(accountNo, userId).map(account -> {
			accountRepository.delete(account);
			return ResponseEntity.ok().build();
		}).orElseThrow(() -> new ResourceNotFoundException(
				"Account not found with no: " + accountNo + " and UserId: " + userId));
	}
}
