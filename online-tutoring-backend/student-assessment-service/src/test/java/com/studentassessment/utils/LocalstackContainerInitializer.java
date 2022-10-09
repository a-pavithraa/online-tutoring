package com.studentassessment.utils;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.MountableFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.sqs.SqsClient;

import static org.testcontainers.containers.localstack.LocalStackContainer.Service.SECRETSMANAGER;

public class LocalstackContainerInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    static final DockerImageName imageName = DockerImageName.parse("localstack/localstack:1.1.0");



    static final LocalStackContainer localstack = new LocalStackContainer(imageName)
            .withServices(
                    LocalStackContainer.Service.S3,
                    LocalStackContainer.Service.SQS,

                    LocalStackContainer.Service.DYNAMODB,
                    LocalStackContainer.Service.SES,
                    SECRETSMANAGER

            )
           .withClasspathResourceMapping("/localstack", "/docker-entrypoint-initaws.d", BindMode.READ_ONLY)
            .waitingFor(Wait.forLogMessage(".*Initialized\\.\n", 1));



    static {
        localstack.start();
        //.withCopyFileToContainer(MountableFile.forHostPath("/localstack/"), "/docker-entrypoint-initaws.d/");

        System.setProperty("spring.config.import","optional:aws-secretsmanager:/secrets/api-secrets");
    }

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {

        System.out.println("Inside initialiuze of app context");
        String awsEndpoint = "http://" + localstack.getHost() + ":" + localstack.getMappedPort(4566);
        TestPropertyValues.of(
                "spring.cloud.aws.endpoint=" + awsEndpoint,
                "spring.cloud.aws.s3.path-style-access-enabled=true"


        );




    }

    @TestConfiguration
    @Profile("test")
    public static class AWSConfiguration {

        @Bean
        public S3Client getS3Client() {
            System.out.println("Inside s3 client in test configuration!!!");
            return   S3Client
                    .builder()
                    .endpointOverride(localstack.getEndpointOverride(LocalStackContainer.Service.S3))
                    .credentialsProvider(
                            StaticCredentialsProvider.create(
                                    AwsBasicCredentials.create(localstack.getAccessKey(), localstack.getSecretKey())
                            )
                    )
                    .region(Region.of(localstack.getRegion()))
                    .build();
        }

        @Bean
        public S3Presigner getS3PresignerClient() {
            System.out.println("Inside s3 client in test configuration!!!");
            return   S3Presigner
                    .builder()
                    .endpointOverride(localstack.getEndpointOverride(LocalStackContainer.Service.S3))
                    .credentialsProvider(
                            StaticCredentialsProvider.create(
                                    AwsBasicCredentials.create(localstack.getAccessKey(), localstack.getSecretKey())
                            )
                    )
                    .region(Region.of(localstack.getRegion()))
                    .build();
        }

        @Bean
        public SqsClient getSQSClient() {
            return  SqsClient
                    .builder()
                    .endpointOverride(localstack.getEndpointOverride(LocalStackContainer.Service.SQS))
                    .credentialsProvider(
                            StaticCredentialsProvider.create(
                                    AwsBasicCredentials.create(localstack.getAccessKey(), localstack.getSecretKey())
                            )
                    )
                    .region(Region.of(localstack.getRegion()))
                    .build();
        }

        @Bean
        public DynamoDbClient getDynamoDBClient(){
            return DynamoDbClient.builder()
                    .endpointOverride(localstack.getEndpointOverride(LocalStackContainer.Service.DYNAMODB))
                    .credentialsProvider(
                            StaticCredentialsProvider.create(
                                    AwsBasicCredentials.create(localstack.getAccessKey(), localstack.getSecretKey())
                            )
                    )
                    .region(Region.of(localstack.getRegion()))
                    .build();
        }
        }

    }




