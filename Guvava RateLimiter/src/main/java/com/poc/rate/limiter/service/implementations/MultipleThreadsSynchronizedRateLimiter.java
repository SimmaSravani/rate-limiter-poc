package com.poc.rate.limiter.service.implementations;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.springframework.core.annotation.Order;

import com.google.common.util.concurrent.RateLimiter;
import com.poc.rate.limiter.service.AbstractRateLimiter;

//@Service
@Order(5)
public class MultipleThreadsSynchronizedRateLimiter extends AbstractRateLimiter {

	private RateLimiter rateLimiter;
	private ExecutorService taskExecutor;

	/*
	 * Multiple Threads are requesting a thread safe guvava rate limiter.
	 * 
	 */
	@Override
	public void performTaskYourWay(int taskNum) {

		taskExecutor.submit(() -> {
			
			long rateLimiterAcquireStartTime = System.currentTimeMillis();
			
			rateLimiter.acquire();
			
			long rateLimiterAcquireEndTime = System.currentTimeMillis();
			long rateLimiterAcquireDuration = rateLimiterAcquireEndTime - rateLimiterAcquireStartTime;
			LOGGER.info("Task {} Rate Limiter Acquire time {}", taskNum, rateLimiterAcquireDuration);
			
			util.performOperation(taskNum);
		});
	}

	@Override
	public void terminateAllTasks() {
		
		Boolean isTerminatedSuccessfully;
		try {
			taskExecutor.shutdown();
			isTerminatedSuccessfully = taskExecutor.awaitTermination(getUtility().totalObservationSeconds,
					TimeUnit.SECONDS);
		
		} catch (InterruptedException e) {
			isTerminatedSuccessfully = false;
			LOGGER.error(e.getLocalizedMessage(),e);
		}

		if (!isTerminatedSuccessfully) {
			LOGGER.error("Couldn't terminate within stipulated time. Terminating Forcefully.");
			taskExecutor.shutdownNow();
		}
		
	}

	@Override
	public void initializeRateLimiter() {
		
		taskExecutor = Executors.newFixedThreadPool(10);
		rateLimiter = RateLimiter.create(util.heavyOpAllowedPerSecond);
	}

}
