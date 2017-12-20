package com.wave.payroll.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.wave.payroll.dao.IJobGroupRepository;
import com.wave.payroll.dao.IPayrollRepository;
import com.wave.payroll.dao.IReportRepository;
import com.wave.payroll.domain.JobGroup;
import com.wave.payroll.domain.Payroll;
import com.wave.payroll.domain.Report;
import com.wave.payroll.exception.DuplicateReportException;
import com.wave.payroll.exception.ReportGeneratorException;
import com.wave.payroll.util.PayrollDataProcessor;

@Component
@Transactional
public class PayrollService implements IPayrollService {
	
	private Logger logger = Logger.getLogger(PayrollService.class);
	
	@Autowired
	private IPayrollRepository payrollRepository;
	
	@Autowired
	private IJobGroupRepository jobGroupRepository;
	
	@Autowired
	private IReportRepository reportRepository;
	
	@Autowired
	private PayrollDataProcessor payrollDataProcessor;
	
	@PostConstruct
	public void initilize() throws Exception{
		//load job group details into table on start up
		loadJobGroupData();
	}

	@Override
	public List<Report> uploadFileAndGenrateReport(MultipartFile file) throws ReportGeneratorException, DuplicateReportException {
		String reportId = payrollDataProcessor.getReportId(file);
		if(payrollRepository.existsByReportId(reportId)) {
			logger.info("Report ID already processed");
			throw new DuplicateReportException("This report is already generated");
		}
		logger.info("Processing report for report id:"+reportId);
		List<Payroll> payrollList = payrollDataProcessor.mapSourceToObject(file, reportId);
		payrollRepository.save(payrollList);
		List<Report> report = payrollDataProcessor.generateReport(payrollList);
		reportRepository.save(report);
		return report;
	}
	
	@Override
	public List<Report> fetchReport(String reportId) {
		return reportRepository.findByReportId(reportId);
	}
	
	private void loadJobGroupData() {
		logger.info("Loading JOB GROUP data into table on start up");
		List<JobGroup> jobGroups = new ArrayList<>();
		jobGroups.add(new JobGroup("A", 20));
		jobGroups.add(new JobGroup("B", 30));
		jobGroupRepository.save(jobGroups);
	}
}
