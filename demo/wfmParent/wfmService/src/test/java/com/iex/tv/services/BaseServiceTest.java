package com.iex.tv.services;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/app-test-context.xml"})
@Ignore
public class BaseServiceTest {
	@Before
	public void setup() {
		try {
			System.out.println(" Start Test ");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	@After
	public void release() {

	}	
}
