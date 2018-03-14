package com.demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class ClientConfig {
	
	@Value("${client.default-uri:/CalloutAstWebService/service/*}")
	private String defaultUri;
}
