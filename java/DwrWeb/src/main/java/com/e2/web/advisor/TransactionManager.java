package com.e2.web.advisor;

import java.util.Arrays;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import com.e2.web.annotation.TransactionMethod;

@Aspect
public class TransactionManager {
	
	/*
	 * define point cut in method level
	 */
	@Pointcut("execution(@com.e2.web.annotation.TransactionMethod * *(..))")
	public void transactionMethod() {}
	
	/*
	 * define point cut in class level
	 */	
	@Pointcut("within(@com.e2.web.annotation.TransactionMethod *)")
	private void transactionClass() {}
	
	@Pointcut(value="@annotation(transactionMethod)",argNames="transactionMethod")
	public void transactionMethod1(TransactionMethod transactionMethod) {}
	
	//@Around(value = "(transactionClass() || transactionMethod())", argNames="transactionMethod")
	@Around(value = "transactionMethod1(transactionMethod)", argNames="transactionMethod")
	public void begin(final ProceedingJoinPoint joinPoint, final TransactionMethod transactionMethod) throws Throwable {
		System.out.println("logAround() is running!");
		System.out.println("hijacked method : " + joinPoint.getSignature().getName());
		System.out.println("hijacked arguments : " + Arrays.toString(joinPoint.getArgs()));
		System.out.println("Around before is running!");
		joinPoint.proceed(); // continue on the intercepted method
		System.out.println("Around after is running!");
		System.out.println("******");
	}
}
