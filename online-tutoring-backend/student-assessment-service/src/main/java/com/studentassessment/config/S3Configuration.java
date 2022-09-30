package com.studentassessment.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.auth.credentials.WebIdentityTokenFileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.sqs.SqsClient;

@Configuration
public class S3Configuration {
	@Value("${kubernetes.profile}")
	private String kubernetesProfile;
	private static final Logger logger = LoggerFactory.getLogger(S3Configuration.class);


	@Bean
	public S3Client getS3Client() {
		logger.info("get S3 Client");
		logger.info(kubernetesProfile);

		return "Y".equals(kubernetesProfile)
				? S3Client.builder()
						.credentialsProvider(WebIdentityTokenFileCredentialsProvider.create()).build()
				: S3Client.builder().build();


	}
	@Bean
	public SqsClient getSQSClient(){
		logger.info("get getSQSClient");
		return "Y".equals(kubernetesProfile)
				? SqsClient.builder()
				.credentialsProvider(WebIdentityTokenFileCredentialsProvider.create()).build()
				: SqsClient.builder().build();


	}

	@Bean
	public S3Presigner getS3Presigner(){
		return "Y".equals(kubernetesProfile)?S3Presigner.builder()
				.credentialsProvider(WebIdentityTokenFileCredentialsProvider.create())
				.build(): S3Presigner.builder().build();
	}

}
