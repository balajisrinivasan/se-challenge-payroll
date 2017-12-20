package com.wave.payroll.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.wave.payroll.domain.Payroll;

public interface IPayrollRepository extends CrudRepository<Payroll, Long>{
	public List<Payroll> findByReportId(String reportId);
	public boolean existsByReportId(String reportId);
}
