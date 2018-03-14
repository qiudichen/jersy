package com.demo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

//@RunWith(SpringRunner.class)
//@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
public class SpringWsApplicationTests {
	//@Autowired
	//private HelloWorldClient helloWorldClient;

	@LocalServerPort
	private int port;
	
	//@Test
	public void testSayHello() {
		System.out.println("");
		//assertThat(helloWorldClient.sayHello("John", "Doe")).isEqualTo("Hello John Doe!");
	  
	}
}
