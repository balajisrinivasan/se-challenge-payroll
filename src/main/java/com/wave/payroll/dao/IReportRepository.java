package com.wave.payroll.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.wave.payroll.domain.Report;

public interface IReportRepository extends CrudRepository<Report, Long> {
	List<Report> findByReportId(String reportId);
}
