package com.wave.payroll.rest.client;

import java.io.File;
import java.util.List;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;



public class PayrollRestClient {
	
	public static void main(String args[]) {
		
		LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        FileSystemResource value = new FileSystemResource(new File("src/test/resources/sample.csv"));
        map.add("file", value);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = new HttpEntity<>(map, headers);
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.exchange("http://localhost:8080/payroll/upload", HttpMethod.POST, requestEntity, List.class);
       
	}

}
