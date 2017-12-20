package com.wave.payroll.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.google.gson.Gson;
import com.wave.payroll.domain.ApiError;
import com.wave.payroll.exception.DuplicateReportException;
import com.wave.payroll.exception.ReportGeneratorException;

@ControllerAdvice
public class PayrollControllerAdvice {
	
	private Gson gson = new Gson();
	
	@ExceptionHandler(DuplicateReportException.class) 
	public ResponseEntity<String> handleDuplicateReport(DuplicateReportException e) {
		ApiError error = new ApiError(HttpStatus.BAD_REQUEST.toString(), "Report already generated for this ID");
		return new ResponseEntity<String>(gson.toJson(error), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(ReportGeneratorException.class) 
	public ResponseEntity<String> handleDuplicateReport(ReportGeneratorException e) {
		ApiError error = new ApiError(HttpStatus.BAD_REQUEST.toString(), "Invalid file or processing exception");
		return new ResponseEntity<String>(gson.toJson(error), HttpStatus.BAD_REQUEST);
	}

}
