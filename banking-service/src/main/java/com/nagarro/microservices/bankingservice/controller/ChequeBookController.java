package com.nagarro.microservices.bankingservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nagarro.microservices.bankingservice.beans.Account;
import com.nagarro.microservices.bankingservice.exception.ResourceNotFoundException;
import com.nagarro.microservices.bankingservice.repository.AccountRepository;

@RefreshScope
@RestController
public class ChequeBookController {

	Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private AccountRepository accountRepository;
	
	@PostMapping("/cheque/{accountNo}/issue")
	public Account issueCheque(@PathVariable(value = "accountNo") String accountNo) {
		try {
	        return accountRepository.findByAccountNo(accountNo).map(account -> {
	            account.setChequeIssued(true);
	            LOGGER.info("Cheque issued.");
	            return accountRepository.save(account);
	        }).orElseThrow(() -> new ResourceNotFoundException("AccountNo: " + accountNo + "not found"));
		} catch (ResourceNotFoundException e) {
			throw new ResourceNotFoundException("AccountNo " + accountNo + " not found");
		}
	}
	
	@PostMapping("/cheque/{accountNo}/block")
	public Account blockCheque(@PathVariable(value = "accountNo") String accountNo) {
		try {
	        return accountRepository.findByAccountNo(accountNo).map(account -> {
	            account.setBlockCheque(true);
	            LOGGER.info("Cheque blocked.");
	            return accountRepository.save(account);
	        }).orElseThrow(() -> new ResourceNotFoundException("AccountNo: " + accountNo + "not found"));
		} catch (ResourceNotFoundException e) {
			throw new ResourceNotFoundException("AccountNo " + accountNo + " not found");
		}
	}
	
}
