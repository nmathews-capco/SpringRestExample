package com.backbase.spring;

import org.springframework.web.client.RestTemplate;

public class TestSpringRestExample {

	public static final String SERVER_URI1 = "https://apisandbox.openbankproject.com/obp/v1.2.1/banks";

	
	public static void main(String args[]) {

		testgetallTransaction();
	}

	private static void testgetallTransaction() {
		RestTemplate restTemplate = new RestTemplate();
		String str = restTemplate.getForObject(SERVER_URI1, String.class);
		System.out.println(str);
	}
	

}
