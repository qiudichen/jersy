package com.e2.web.util;

import org.springframework.beans.BeansException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringUtil implements ApplicationContextAware {

	private static ApplicationContext applicationContext;
	
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}

	public static <T> T getSpringBean(String beanName, Class<T> cls) {
        return (T)applicationContext.getBean(beanName);
    }
}
