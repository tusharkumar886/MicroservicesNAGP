package com.nagarro.microservices.bankingservice.beans;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.nagarro.microservices.bankingservice.proxy.Customer;

@Entity
@Table
public class Account {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true)
	private String accountNo;
	
	@Column
	@NotNull
	private Double balance;
	
	@Column
	private String branch;
	
	@Column
	private boolean chequeIssued;
	
	@Column
	private boolean blockCheque;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "customer_userId", nullable = false)
	private Customer customer;
	
	public Account() {
		super();
	}
	
	public Account(Double balance, Customer customer) {
		this.balance = balance;
		this.customer = customer;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}
	
	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public boolean isChequeIssued() {
		return chequeIssued;
	}

	public void setChequeIssued(boolean chequeIssued) {
		this.chequeIssued = chequeIssued;
	}

	public boolean isBlockCheque() {
		return blockCheque;
	}

	public void setBlockCheque(boolean blockCheque) {
		this.blockCheque = blockCheque;
	}
	
}
