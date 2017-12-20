package com.wave.payroll.domain;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.NaturalId;

@Entity
@Table(name="JobGroup")
public class JobGroup {
	
	public JobGroup() {
		
	}
	
	public JobGroup(String name, double billingRate) {
		this.name = name;
		this.billingRate = billingRate;
	}
	
	@Id @GeneratedValue
	@Column(name="id")
	private Long id;
	
	@NaturalId
	@Column(name="name")
	private String name;
	
	@Column(name="billing_rate")
	private double billingRate;
	
	@OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER, mappedBy = "jobGroup")
	private Set<Payroll> payrolls;
	

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getBillingRate() {
		return billingRate;
	}
	public void setBillingRate(double billingRate) {
		this.billingRate = billingRate;
	}
	public Set<Payroll> getPayrolls() {
		return payrolls;
	}
	public void setPayrolls(Set<Payroll> payrolls) {
		this.payrolls = payrolls;
	}	
	
}
