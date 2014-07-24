package com.iex.tv.caching.jms;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.iex.tv.caching.spi.CacheNotifier;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/app-jms-notify-test-context.xml"})
public class JMSCacheNotifierImplTest {
	@Resource(name="notifier")
	CacheNotifier notifier;
	
	@Test
	public void remove() {
		String keyValue = "123";
		notifier.removeObject("agent", keyValue);
		notifier.removeObject("supervisor", keyValue);
	}
}
