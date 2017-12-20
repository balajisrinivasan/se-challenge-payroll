package com.wave.payroll.exception;

public class DuplicateReportException extends Exception{
	
	private static final long serialVersionUID = 4558032692429457516L;

	public DuplicateReportException(String message)
    {
       super(message);
    }
	
}
