package com.nagarro.microservices.bankingservice.proxy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table
public class Customer {

	@Id
	@GeneratedValue
	@Column(name = "UserId")
	private Long userId;
	
	@Column(name = "Name", nullable = false)
	@NotNull
	private String name;

	@Column(name = "Contact", unique = true, nullable = false)
	private long contactNumber;
	
	public Customer() {
		super();
	}
	
	public Customer(String name, long contactNumber) {
		super();
		this.name = name;
		this.contactNumber = contactNumber;
	}

	public long getContactNumber() {
		return contactNumber;
	}

	public String getName() {
		return name;
	}
	
	public Long getUserId() {
		return userId;
	}
	
	public void setContactNumber(long contactNumber) {
		this.contactNumber = contactNumber;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setUserId(Long userId) {
		this.userId = userId;
	}

}
