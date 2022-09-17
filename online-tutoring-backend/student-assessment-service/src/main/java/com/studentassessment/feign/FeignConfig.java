package com.studentassessment.feign;

import static java.util.concurrent.TimeUnit.SECONDS;

import java.util.HashSet;
import java.util.Set;

import org.springframework.context.annotation.Bean;

import feign.RequestInterceptor;
import feign.Retryer;
import feign.codec.ErrorDecoder;

public class FeignConfig {
	
	 @Bean
	  public Retryer retryer() {
	    return new Retryer.Default(100, SECONDS.toMillis(1), 3);
	  }

	
	  @Bean
	  public ErrorDecoder errorDecoder() {
		
	    Set<Integer> retryableStatusCodes = new HashSet<>();
	    retryableStatusCodes.add(500);
	    retryableStatusCodes.add(503);
	    
	    return new FeignErrorDecoder(retryableStatusCodes);
	  }

}
