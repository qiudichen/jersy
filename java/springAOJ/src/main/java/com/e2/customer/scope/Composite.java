package com.e2.customer.scope;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import com.e2.scope.IterationScope;
import com.e2.scope.TestStatefulClass;

@Component("composite")
public class Composite {
	private List<TestStatefulClass> beans = new ArrayList<TestStatefulClass>();

	public void print() {
		for (TestStatefulClass bean : beans) {
			bean.display();
		}
	}

	@Autowired
	public void setBeans(List<TestStatefulClass> beans) {
		this.beans.clear();
		this.beans.addAll(beans);
	}

	public static void main(String[] args) {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring-scope-config.xml");
		System.out.println("Getting 'composite' bean");
		
		Composite composite = (Composite) context.getBean("composite");
		System.out.println("First print");
		composite.print();
		System.out.println("Second print");
		composite.print();
		
		IterationScope scope = (IterationScope) context.getBean("iterationScope");
		
		System.out.println("Changing iteration");
		scope.nextIteration();
		System.out.println("Third print");
		composite.print();
		System.out.println("Forth print");
		composite.print();
	}
}
