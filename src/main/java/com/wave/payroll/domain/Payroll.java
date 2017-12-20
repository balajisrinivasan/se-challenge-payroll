package com.wave.payroll.domain;

import java.sql.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="Payroll")
public class Payroll {
	
	@Id @GeneratedValue
	@Column(name="id")
	private Long id;
	
	@Column(name="report_id")
	private String reportId;
	
	@Column(name="date")
	private Date date;
	
	@Column(name="hours")
	private double hours;
	
	@Column(name="employee_id")
	private String employeeId;
	
	@ManyToOne(fetch = FetchType.EAGER, cascade=CascadeType.MERGE)
    @JoinColumn(name="job_group_id", nullable=false)
	private JobGroup jobGroup;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getReportId() {
		return reportId;
	}
	public void setReportId(String reportId) {
		this.reportId = reportId;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public double getHours() {
		return hours;
	}
	public void setHours(double hours) {
		this.hours = hours;
	}
	public String getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
	public JobGroup getJobGroup() {
		return jobGroup;
	}
	public void setJobGroup(JobGroup jobGroup) {
		this.jobGroup = jobGroup;
	}
	
}