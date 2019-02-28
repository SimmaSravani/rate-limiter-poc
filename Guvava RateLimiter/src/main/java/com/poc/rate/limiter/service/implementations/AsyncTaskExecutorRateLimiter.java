package com.poc.rate.limiter.service.implementations;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.springframework.core.annotation.Order;

import com.google.common.util.concurrent.RateLimiter;
import com.poc.rate.limiter.service.AbstractRateLimiter;

//@Service
@Order(4)
public class AsyncTaskExecutorRateLimiter extends AbstractRateLimiter {

	private RateLimiter rateLimiter;
	private ExecutorService taskExecutor;

	/*
	 * This style defeats the purpose of rate Limiting if no of threads to be opened
	 * aren't too high. Submits the task in rate limited manner.
	 */
	@Override
	public void performTaskYourWay(int taskNum) {
		
		rateLimiter.acquire();
		taskExecutor.submit(() -> util.performOperation(taskNum));
	}

	@Override
	public void terminateAllTasks() {
		
		try {
			
			taskExecutor.shutdown();
			taskExecutor.awaitTermination(20, TimeUnit.SECONDS);
			LOGGER.info("All tasks terminated gracefully.");
			
		} catch (InterruptedException e) {
			LOGGER.error("Couldn't terminate gracefully.");
		}
		
	}

	@Override
	public void initializeRateLimiter() {
		
		taskExecutor = Executors.newFixedThreadPool(10);
		rateLimiter = RateLimiter.create(util.heavyOpAllowedPerSecond);
	}

}
