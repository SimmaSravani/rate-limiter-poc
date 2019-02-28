package com.poc.rate.limiter.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.poc.rate.limiter.utility.Utility;

public interface RateLimiterService {
	
	public static final Logger LOGGER  = LoggerFactory.getLogger(RateLimiterService.class);
	
	default public void performExperiment() {
		
		long startTime = System.currentTimeMillis();
		
		int taskPerformedTillNow = 0 ;
		int millisConsumed = 0 ;
		int observationPeriodMillis = getUtility().totalObservationSeconds * 1000 ;
		
		int maxTasksCanBePerformed = getUtility().maxTasksOverObservationPeriod();
		
		initializeRateLimiter();
		
		while ((millisConsumed < observationPeriodMillis) && (taskPerformedTillNow < maxTasksCanBePerformed) ) {

			long taskStartTime = System.currentTimeMillis();

			performTaskYourWay(taskPerformedTillNow);

			long taskSubmissionTime = System.currentTimeMillis();
			long taskDuration = taskSubmissionTime - taskStartTime;

			LOGGER.info("Task {} submission time {}", taskPerformedTillNow, taskDuration);

			millisConsumed += taskDuration;
			taskPerformedTillNow++;
		}

		terminateAllTasks();
		
		long finishTime = System.currentTimeMillis();
		
		LOGGER.info("Theoretically Total Tasks performed {} in {} millis.", taskPerformedTillNow, millisConsumed);
		
		LOGGER.info("Actual Time Consumed {} millis", finishTime - startTime);
		
		LOGGER.info("Ideally {} tasks should have been performed over {} millis.",
				getUtility().idealTasksOverObservationPeriod(), observationPeriodMillis);
		
	}
	
	public void performTaskYourWay(int taskNum);

	default public String getServiceName() {
		return this.getClass().getSimpleName();
	}
	
	Utility getUtility() ;

	public void terminateAllTasks();

	public void initializeRateLimiter();
	
}
