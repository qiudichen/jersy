package com.iex.tv.caching.services.test;

import javax.annotation.Resource;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.iex.tv.caching.spi.CacheManager;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/app-service-test-context.xml"})
public class BaseCachingServiceTest {
	@Resource(name="cacheManager")
	protected CacheManager<?> cacheManager;
	
	
}
