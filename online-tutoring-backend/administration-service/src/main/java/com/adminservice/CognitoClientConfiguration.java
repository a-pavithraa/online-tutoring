package com.adminservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import org.zalando.fauxpas.TryWith;

import software.amazon.awssdk.auth.credentials.WebIdentityTokenFileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.CognitoIdentityProviderException;
import software.amazon.awssdk.services.cognitoidentityprovider.model.SignUpRequest;

@Configuration
public class CognitoClientConfiguration {
	@Value("${kubernetes.profile}")
	private String kubernetesProfile;
	private static final Logger logger = LoggerFactory.getLogger(CognitoClientConfiguration.class);

	@Bean
	public  CognitoIdentityProviderClient getCognitoClient() {
		
	return	"Y".equals(kubernetesProfile) ?CognitoIdentityProviderClient.builder()
        .region(Region.US_EAST_1)
        .credentialsProvider(WebIdentityTokenFileCredentialsProvider.create())
        .build():CognitoIdentityProviderClient.builder()
        .region(Region.US_EAST_1)		            
        .build();

		
	}
	

	
	

}
