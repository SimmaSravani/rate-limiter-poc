package com.poc.rate.limiter.service.implementations;

import javax.annotation.PostConstruct;

import org.springframework.core.annotation.Order;

import com.poc.rate.limiter.service.AbstractRateLimiter;

//@Service
@Order(2)
public class CustomThreadSleepRateLimiter extends AbstractRateLimiter  {
	
	private double oneTaskMustTakeAtleastLatency ;
	
	@PostConstruct
	private void init() {
		oneTaskMustTakeAtleastLatency = 1000/util.heavyOpAllowedPerSecond ;
	}
	
	@Override
	public void performTaskYourWay(int taskNum) { 
		int timeTakenInTask = util.performOperation(taskNum);
		rateLimit(timeTakenInTask);
	}


	public void rateLimit(int timeTakenInTask) {

		if (timeTakenInTask < oneTaskMustTakeAtleastLatency) {

			double SleepFor = oneTaskMustTakeAtleastLatency - timeTakenInTask;

			try {
				Thread.sleep((long)SleepFor);
			} catch (InterruptedException e) {
				LOGGER.error("Couldn't Sleep Properly. Anxiety issues :p {}", e);
			}
			
			LOGGER.info("Slept for {} millis.", SleepFor);
		}
	}
	

}
