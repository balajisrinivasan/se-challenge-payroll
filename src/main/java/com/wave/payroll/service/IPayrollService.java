package com.wave.payroll.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.wave.payroll.domain.Report;
import com.wave.payroll.exception.DuplicateReportException;
import com.wave.payroll.exception.ReportGeneratorException;


public interface IPayrollService {
	public List<Report> uploadFileAndGenrateReport(MultipartFile file) throws ReportGeneratorException, DuplicateReportException;
	public List<Report> fetchReport(String reportId);
}
