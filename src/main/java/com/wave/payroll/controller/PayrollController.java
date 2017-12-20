package com.wave.payroll.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.wave.payroll.exception.DuplicateReportException;
import com.wave.payroll.exception.ReportGeneratorException;
import com.wave.payroll.service.IPayrollService;

@RestController
public class PayrollController {	
	
	@Autowired
	private IPayrollService payrollService;
	
	private Gson gson = new Gson();

	@RequestMapping(value="/payroll/upload", method=RequestMethod.POST)
	public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) throws ReportGeneratorException, DuplicateReportException {
		return new ResponseEntity<String> (gson.toJson(payrollService.uploadFileAndGenrateReport(file)), HttpStatus.OK);
	}
	
	@RequestMapping(value="/payroll/report", method=RequestMethod.GET)
	public ResponseEntity<String> getReport(@RequestParam("reportId") String reportId) {
		System.out.println(gson.toJson(payrollService.fetchReport(reportId)));
		return new ResponseEntity<String> (gson.toJson(payrollService.fetchReport(reportId)), HttpStatus.OK);
	}
}
