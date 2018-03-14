package com.demo.webservice.jersey;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;

@Configuration
@ApplicationPath("/jersey")
public class JerseyConfig extends ResourceConfig {

    JerseyConfig() {
        register(CalloutRest.class);
        packages("restpackagename");
    }
}
