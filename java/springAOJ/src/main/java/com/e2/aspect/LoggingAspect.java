package com.e2.aspect;

import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class LoggingAspect {

	@Before("execution(* com.e2.customer.bo.CustomerBo.addCustomer(..))")
	public void logBefore(JoinPoint joinPoint) {
		System.out.println("logBefore() is running!");
		System.out.println("hijacked : " + joinPoint.getSignature().getName());
		System.out.println("******");
	}

	@After("execution(* com.e2.customer.bo.CustomerBo.addCustomer(..))")
	public void logAfter(JoinPoint joinPoint) {

		System.out.println("logAfter() is running!");
		System.out.println("hijacked : " + joinPoint.getSignature().getName());
		System.out.println("******");

	}

	@AfterReturning(pointcut = "execution(* com.e2.customer.bo.CustomerBo.addCustomerReturnValue(..))", returning = "result")
	public void logAfterReturning(JoinPoint joinPoint, Object result) {

		System.out.println("logAfterReturning() is running!");
		System.out.println("hijacked : " + joinPoint.getSignature().getName());
		System.out.println("Method returned value is : " + result);
		System.out.println("******");
	}

	@AfterThrowing(pointcut = "execution(* com.e2.customer.bo.CustomerBo.addCustomerThrowException(..))", throwing = "error")
	public void logAfterThrowing(JoinPoint joinPoint, Throwable error) {

		System.out.println("logAfterThrowing() is running!");
		System.out.println("hijacked : " + joinPoint.getSignature().getName());
		System.out.println("Exception : " + error);
		System.out.println("******");

	}
	
	/*
	 * Type point cut
	 * call - The pointcut will find all methods that calls a method in the package
	 * execution - The pointcut will find all methods in the package
	 * withincode - All the statements inside the methods in the package  
	 */
	
	//  anyreturn type, package, anyclass, any method, any type and number of arguments
	@Pointcut("execution(* com.e2.customer.bo.*.*(..))")
	public void traceMethodsInPackage() {}
	
	
	/*
	 * Field pointcut
	 * get - all reads to jdbcTemplate fields of type JdbcTemplate in the integration.db package. 
	 * 		Includes all methods on this field if it’s an object.
	 * set – when you set the jdbcTemplate field of type JdbcTemplate in the integration.db package to a new value. 
	 */
	//@Pointcut("get(private org.springframework.jdbc.core.JdbcTemplate " + 
	//			"integration.db.*.jdbcTemplate)")
	//public void jdbcTemplateGetField() {}
	
	
	@Pointcut("within(*..*Test)")
	public void inTestClass() {}
	
	/*
	 * A pointcut that finds all methods marked with the @PerformanceLogable on the classpath
	 */
	@Pointcut("execution(@com.e2.aspect.PerformanceLogable * *(..))")
	public void performanceLogableMethod() {}
	
	//@Before("performanceLogableMethod() ")
    public void beforeMethod() {    
        System.out.println("At least one of the parameters are " 
                  + "annotated with @MyParamAnnotation");
    }

	//@Around("execution(* com.e2.customer.bo.CustomerBo.addCustomerAround(..))")
	@Around("performanceLogableMethod() ")
	//@Around(value = "anyPrivilegedServiceMethod() && propagateContext(propagate) && this(service)", argNames = "service")
	public void logAround(ProceedingJoinPoint joinPoint) throws Throwable {

		System.out.println("logAround() is running!");
		System.out.println("hijacked method : "
				+ joinPoint.getSignature().getName());
		System.out.println("hijacked arguments : "
				+ Arrays.toString(joinPoint.getArgs()));
		System.out.println("Around before is running!");
		joinPoint.proceed(); // continue on the intercepted method
		System.out.println("Around after is running!");
		System.out.println("******");
	}

}
