package com.poc.rate.limiter.service.implementations;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import com.google.common.util.concurrent.RateLimiter;
import com.poc.rate.limiter.service.AbstractRateLimiter;

@Service
@Order(3)
public class GuvavaRateLimiter extends AbstractRateLimiter {
	
	private RateLimiter rateLimiter ;
	
	@Override
	public void performTaskYourWay(int taskNum) {

		rateLimiter.acquire();
		util.performOperation(taskNum);
	}
	

	@Override
	public void initializeRateLimiter() {
		rateLimiter = RateLimiter.create(util.heavyOpAllowedPerSecond);
	}

}
