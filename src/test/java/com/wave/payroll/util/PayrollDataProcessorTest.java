package com.wave.payroll.util;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;

import com.wave.payroll.dao.IJobGroupRepository;
import com.wave.payroll.domain.JobGroup;
import com.wave.payroll.domain.Payroll;
import com.wave.payroll.domain.Report;
@RunWith(SpringRunner.class)
public class PayrollDataProcessorTest {
	
	private PayrollDataProcessor payrollDataProcessor = new PayrollDataProcessor();
	
	@MockBean
	private IJobGroupRepository jobGroupDao;
	
	private MockMultipartFile multipart; 
	
	@Before
	public void initiliaze() throws Exception
	{
		JobGroup jobGroup = new JobGroup("A", 20);
		Mockito.when(jobGroupDao.findOneByName(Mockito.anyString())).thenReturn(jobGroup);
		FileInputStream fis = new FileInputStream(new File("src/test/resources/sample.csv"));
		multipart = new MockMultipartFile("file", "sample.csv", "multipart/form-data", fis);
	}
	
	@Test
	public void getReportId() throws Exception {
		Assert.assertEquals("43", payrollDataProcessor.getReportId(multipart));
	}
	
	@Ignore
	@Test
	public void parseInputLines() throws Exception {
		List<Payroll> list = payrollDataProcessor.mapSourceToObject(multipart, payrollDataProcessor.getReportId(multipart));
		Assert.assertEquals("1", list.get(0).getEmployeeId());
		Assert.assertEquals("2016-11-14", list.get(0).getDate().toString());
		Assert.assertEquals("A", list.get(0).getJobGroup().getName());
		Assert.assertEquals("20.0", String.valueOf(list.get(0).getJobGroup().getBillingRate()));
		
	}
	
	@Test
	public void getPayPeriod() {
		Assert.assertEquals("1/11/2016-15/11/2016", payrollDataProcessor.getPayPeriod(payrollDataProcessor.formatDate("14/11/2016")));
		Assert.assertEquals("16/2/2016-29/2/2016", payrollDataProcessor.getPayPeriod(payrollDataProcessor.formatDate("17/02/2016")));
	}
	
	@Test
	public void generateReport() {
		List<Payroll> inputList = createInputList();
		Report report = payrollDataProcessor.generateReport(inputList).get(0);
		Assert.assertEquals("1", report.getEmployeeId());
		Assert.assertEquals("300.0", String.valueOf(report.getAmount()));
		Assert.assertEquals("1/11/2016-15/11/2016", report.getPeriod());
	}

	private List<Payroll> createInputList() {
		List<Payroll> list = new ArrayList<>();
		Payroll payroll = new Payroll();
		payroll.setEmployeeId("1");
		payroll.setDate(payrollDataProcessor.formatDate("4/11/2016"));
		payroll.setHours(10);
		payroll.setJobGroup(new JobGroup("A", 20));
		
		Payroll payroll2 = new Payroll();
		payroll2.setEmployeeId("1");
		payroll2.setDate(payrollDataProcessor.formatDate("4/11/2016"));
		payroll2.setHours(5);
		payroll2.setJobGroup(new JobGroup("A", 20));
		
		list.add(payroll);
		list.add(payroll2);
		return list;
	}
}
