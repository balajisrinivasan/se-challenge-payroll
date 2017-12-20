package com.wave.payroll.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.wave.payroll.dao.IJobGroupRepository;
import com.wave.payroll.domain.Payroll;
import com.wave.payroll.domain.Report;
import com.wave.payroll.exception.ReportGeneratorException;

@Component
public class PayrollDataProcessor {
	
	@Autowired
	private IJobGroupRepository jobGroupDao;

	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy");
	
	//reads the trailer line and returns the report id
	public String getReportId(MultipartFile file) throws ReportGeneratorException {
		Stream<String> lines = createFileStream(file);
		String trailerLine = lines.filter(line -> line.startsWith("report id")).findFirst().get();
		return trailerLine.split(",")[1];
	}
	//converts input rows to Payroll POJOs
	public List<Payroll> mapSourceToObject(MultipartFile file, String reportId) throws ReportGeneratorException {
		Stream<String> lines = createFileStream(file);
		return lines.parallel().skip(1).filter(line -> isValid(line))
				.map(line -> mapToPayroll(line, reportId)).collect(Collectors.toList());
	}
	
	public List<Report> generateReport(List<Payroll> payrollList) {
		//group payroll report by combination of employee id + payperiod
		// and calculate the total amount
		Map<String, Report> reportMap = new HashMap<>();
		for(Payroll payroll:payrollList) {
			String payperiod = getPayPeriod(payroll.getDate());
			String key = payroll.getEmployeeId() + "|" + payperiod;
			if(reportMap.containsKey(key)) {
				Report report = reportMap.get(key);
				report.setAmount(report.getAmount() + (payroll.getHours() * payroll.getJobGroup().getBillingRate()));
				reportMap.put(key, report);
			} else {
				Report report = new Report();
				report.setAmount(payroll.getHours() * payroll.getJobGroup().getBillingRate());
				report.setPeriod(payperiod);
				report.setEmployeeId(payroll.getEmployeeId());
				report.setReportId(payroll.getReportId());
				reportMap.put(key, report);
			}
		}
		return (new ArrayList<Report>(reportMap.values()));
	}
	
	//for a given date, returns the pay period
	// for input 12/12/2016, thee method will return 1/12/2016-15/12/2016
	public String getPayPeriod(java.sql.Date date) {
		LocalDate localDate = date.toLocalDate();
		LocalDate startDate = null;
		LocalDate endDate = null;
		if(localDate.getDayOfMonth() > 15) {
			startDate = localDate.withDayOfMonth(16);
			endDate = localDate.withDayOfMonth(localDate.lengthOfMonth());
		} else {
			startDate = localDate.withDayOfMonth(1);
			endDate = localDate.withDayOfMonth(15);
		}
		return startDate.format(formatter) +"-" + endDate.format(formatter);
	}
	
	// convert row to Payroll object
	private Payroll mapToPayroll(String line, String reportId) {
		Payroll payroll = new Payroll();
		String[] values = line.split(",");
		payroll.setDate(formatDate(values[0]));
		payroll.setHours(Double.parseDouble(values[1]));
		payroll.setEmployeeId(values[2]);
		payroll.setJobGroup(jobGroupDao.findOneByName(values[3]));
		payroll.setReportId(reportId);
		return payroll;
	}

	private boolean isValid(String line) {
		// add other validation rules for date, hours entered etc
		String[] values = line.split(",");
		if (values.length != 4 || "report id".equals(values[0]))
			return false;
		return true;
	}
	
	public Date formatDate(String date) {
		LocalDate formatDate = LocalDate.parse(date, formatter);
		return Date.valueOf(formatDate);
	}
	
	private Stream<String> createFileStream(MultipartFile file) throws ReportGeneratorException {
		try {
			Stream<String> lines = (new BufferedReader(new InputStreamReader(file.getInputStream()))).lines();
			return lines;
		} catch (IOException e) {
			throw new ReportGeneratorException("Failed to process input file:" + e);
		}
	}
}
