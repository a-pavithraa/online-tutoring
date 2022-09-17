package com.studentassessment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.WebIdentityTokenFileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Configuration
public class S3Configuration {
	@Value("${kubernetes.profile}")
	private String kubernetesProfile;
	private static final Logger logger = LoggerFactory.getLogger(S3Configuration.class);

	@Bean
	public S3Client getS3Client() {
		logger.info(kubernetesProfile);
		
		return "Y".equals(kubernetesProfile)
				? S3Client.builder().region(Region.US_EAST_1)
						.credentialsProvider(WebIdentityTokenFileCredentialsProvider.create()).build()
				: S3Client.builder().region(Region.US_EAST_1).build();


	}

	@Bean
	public S3Presigner getS3Presigner(){
		return "Y".equals(kubernetesProfile)?S3Presigner.builder()
				.region(Region.US_EAST_1)
				.credentialsProvider(WebIdentityTokenFileCredentialsProvider.create())
				.build(): S3Presigner.builder().region(Region.US_EAST_1).build();
	}

}
