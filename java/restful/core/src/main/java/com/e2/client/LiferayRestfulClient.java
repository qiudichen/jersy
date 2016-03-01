package com.e2.client;

import java.util.List;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.nice.service.client.NiceRestTemplate;

public class LiferayRestfulClient implements Runnable {
	
	private NiceRestTemplate restTemplate;

	private Object lockObject;
	
	public static void main(String[] args) {
		AnnotationConfigApplicationContext beanFactory =
			     new AnnotationConfigApplicationContext(SpringConfig.class);

		NiceRestTemplate restTemplate = beanFactory.getBean(NiceRestTemplate.class, "restTemplate");
		Object lockObject = new Object();
		
		int threadSize = 10;
		Thread[] threads = new Thread[threadSize];
		for(int i = 0; i < threadSize; i++) {
			threads[i] = new Thread(new LiferayRestfulClient(restTemplate, lockObject));
		}

		long start = System.currentTimeMillis();
		for(int i = 0; i < threadSize; i++) {
			threads[i].start();;
		}
		
		synchronized( lockObject )
		{ 
		    while(true)
		    { 
		        try {
					lockObject.wait(10000);
					if(!isRunning(threads)) {
						break;
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
		    }
		}
		
		beanFactory.close();
	}

	private static boolean isRunning(Thread[] threads) {
		for(Thread thread : threads) {
			if(thread.isAlive()) {
				return true;
			}
		}
		return false;
	}
	
	public LiferayRestfulClient(NiceRestTemplate restTemplate, Object lockObject) {
		this.restTemplate = restTemplate;
		this.lockObject = lockObject;
	}
	
	public void run() {
		
		for(int i = 0; i < 10; i++) {
			try {
				List<String> niceRoles = restTemplate.getForObject("http://localhost:8080/delegate/nicerole/listNames", List.class);
				System.out.println("loop " + i + ", result ==  " + niceRoles);
			} catch(Throwable e) {
				e.printStackTrace();
			}
		}
		try {
			synchronized( lockObject ) 
			{ 
				lockObject.notify();
			} 
		} catch(Exception e) {
			e.printStackTrace();
		}		
	}
}
