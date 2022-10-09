package com.studentassessment.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.auth.credentials.WebIdentityTokenFileCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.sqs.SqsClient;

@Configuration
@Profile("production")
public class AWSServicesClientConfiguration {
	@Value("${kubernetes.profile}")
	private String kubernetesProfile;
	private static final Logger logger = LoggerFactory.getLogger(AWSServicesClientConfiguration.class);


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

	@Bean
	public DynamoDbClient getDynamoDBClient(){
		logger.info("get getSQSClient");
		return "Y".equals(kubernetesProfile)
				? DynamoDbClient.builder()
				.credentialsProvider(WebIdentityTokenFileCredentialsProvider.create()).build()
				: DynamoDbClient.builder().build();


	}

	@Bean
	public DynamoDbEnhancedClient getDynamoDBEnhancedClient(){
		DynamoDbClient dynamoDBClient = getDynamoDBClient();
		DynamoDbEnhancedClient enhancedClient = DynamoDbEnhancedClient.builder()
				.dynamoDbClient(dynamoDBClient)
				.build();
		return enhancedClient;
	}



}
