package com.appauth.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="CUSTOMERS")
public class Customer {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	long Id;
	@Column(name="CUSTOMER_NAME")
	private String name;
	@Column(name="PASSWORD")
	private String password;
	@Column(name="EMAIL")
	private String email;
	
	public Customer() {}
	
	public Customer(String name, String password, String email) {
		this.name = name;
		this.password = password;
		this.email = email;
	}
	
	public long getId() {
		return Id;
	}
	public void setId(long id) {
		this.Id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
}
