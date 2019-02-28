package com.poc.rate.limiter;

import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.poc.rate.limiter.service.RateLimiterService;


@SpringBootApplication
@EnableScheduling
@ComponentScan("com.poc.rate.limiter")
public class Application {
	
	@Autowired
	private List<RateLimiterService> rateLimiterServices;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);
	
	@PostConstruct
	public void init() {
		
		for (RateLimiterService rateLimiterService : rateLimiterServices) {
			
			LOGGER.info("--------------{} Service Experiment Started.----------------",
					rateLimiterService.getServiceName());

			rateLimiterService.performExperiment();
			
			LOGGER.info("--------------{} Service Experiment Complete.----------------",
					rateLimiterService.getServiceName());
		}
		
	}


    public static void main(String[] args) {

    	ConfigurableApplicationContext context = 
    			SpringApplication.run(Application.class);
    	
    	SpringApplication.exit(context, ()-> 0 );
        
    }
    
}
