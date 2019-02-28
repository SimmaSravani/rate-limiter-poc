package com.poc.rate.limiter.service.implementations;

import org.springframework.core.annotation.Order;

import com.poc.rate.limiter.service.AbstractRateLimiter;

//@Service
@Order(1)
public class WithoutRateLimiter extends AbstractRateLimiter {
	
	@Override
	public void performTaskYourWay(int taskNum) {
		util.performOperation(taskNum);
	}


}
