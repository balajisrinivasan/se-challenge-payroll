package com.wave.payroll.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.wave.payroll.domain.Report;
import com.wave.payroll.service.PayrollService;

@RunWith(SpringRunner.class)
@WebMvcTest(value = PayrollController.class, secure = false)
public class PayrollControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private PayrollService payrollService;
	
	private MockMultipartFile multipart;
	
	@Before
	public void intiliaze() throws Exception {
		List<Report> expectedResult = getExepectedResults();
		FileInputStream fis = new FileInputStream(new File("/Users/balaji/Development/workspace/se-wave-payroll/src/test/resources/sample.csv"));
		multipart = new MockMultipartFile("file", "sample.csv", "multipart/form-data", fis);
		
		Mockito.when(payrollService.uploadFileAndGenrateReport(multipart)).thenReturn(expectedResult);
		Mockito.when(payrollService.fetchReport(Mockito.anyString())).thenReturn(expectedResult);
	}

	@Test
	public void uploadFile() throws Exception {
		MvcResult mvcResult = mockMvc.perform(fileUpload("/payroll/upload").file(multipart)).andReturn();
		Assert.assertEquals(getExpectedJsonOutput(), mvcResult.getResponse().getContentAsString());
	}
	
	@Test
	public void getReport() throws Exception {
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/payroll/report").param("reportId", "1234");
		MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
		Assert.assertEquals(getExpectedJsonOutput(), mvcResult.getResponse().getContentAsString());
	}

	private List<Report> getExepectedResults() {
		List<Report> expectedResult = new ArrayList<>();
		expectedResult.add(createReport("1", "01/01/2105-01/15/2015", 300));
		expectedResult.add(createReport("1", "16/01/2105-31/15/2015", 300));
		return expectedResult;
	}

	private Report createReport(String empId, String period, double amount) {
		Report report = new Report();
		report.setAmount(amount);
		report.setEmployeeId(empId);
		report.setPeriod(period);
		return report;
	}
	
	private String getExpectedJsonOutput() {
		return "[{\"employeeId\":\"1\",\"period\":\"01/01/2105-01/15/2015\",\"amount\":300.0},{\"employeeId\":\"1\",\"period\":\"16/01/2105-31/15/2015\",\"amount\":300.0}]";
	}

}
