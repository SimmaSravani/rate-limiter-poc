package com.poc.rate.limiter.utility;

import java.util.Random;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Utility {

	private static final Logger LOGGER = LoggerFactory.getLogger(Utility.class);
	
	@Value("${heavy.operations.allowed.per.sec}")
	public double heavyOpAllowedPerSecond ;
	
	@Value("${total.observations.seconds}")
	public int totalObservationSeconds ;
	
	@Value("${heavy.operation.min.latency}")
	public int heavyOperationMinLatency ;
	
	@Value("${heavy.operation.max.latency}")
	public int heavyOperationMaxLatency ;
	
//	This is added to serve all kinds of rate limiters same latencies, so comparison could be more accurate.
	private int [] taskLatencies ;
	
	@PostConstruct
	private void init() {
		
		taskLatencies =  new int[maxTasksOverObservationPeriod()];
		
		for(int i =0 ; i< maxTasksOverObservationPeriod(); i++) {
			taskLatencies[i] =  generateRandomLatency();
		}
	}
	
	public int performOperation(int taskNum) {
		
		int randomMillis = taskLatencies[taskNum];

		try {
			Thread.sleep(randomMillis);
		} catch (InterruptedException e) {
			LOGGER.error("Error Performing Task {}.",taskNum);
		}
		
		LOGGER.info("Task {} Latency {}.", taskNum, randomMillis);
		return randomMillis ;
	}

	public int performRandomHeavyOperation() {
		
		int randomMillis = generateRandomLatency();
		LOGGER.info("Random Latency Generated {}.", randomMillis);

		try {
			Thread.sleep(randomMillis);
		} catch (InterruptedException e) {
			LOGGER.error("Error Performing heavy operation.");
		}
		
		return randomMillis ;
	}
	
	/**
	 * @return generates random number between heavyOperationMinLatency to heavyOperationMaxLatency.
	 */
	private int generateRandomLatency() {
		Random random = new Random();
		
		int milliSeconds = random.nextInt(heavyOperationMaxLatency-heavyOperationMinLatency);
		
		return milliSeconds + heavyOperationMinLatency ;
	}
	
	
	public double idealTasksOverObservationPeriod() {
		return totalObservationSeconds * heavyOpAllowedPerSecond ;
	}
	
	public int maxTasksOverObservationPeriod() {
		
		return totalObservationSeconds * 1000 / heavyOperationMinLatency ;
	}


}
