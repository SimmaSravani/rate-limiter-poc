package com.poc.rate.limiter.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poc.rate.limiter.utility.Utility;

@Service
public abstract class AbstractRateLimiter implements RateLimiterService {
	
	@Autowired
	public Utility util ;
	
	public Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	@Override
	public Utility getUtility() {
		return util;
	}

	@Override
	public void terminateAllTasks() {
		
		LOGGER.info("All tasks terminated before.");
	}

	@Override
	public void initializeRateLimiter() {
			
		LOGGER.info("Rate Limiter Already Initialized.");
	}

}
