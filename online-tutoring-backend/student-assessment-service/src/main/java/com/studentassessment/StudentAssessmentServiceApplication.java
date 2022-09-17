package com.studentassessment;

import com.studentassessment.model.S3EventNotification;
import io.awspring.cloud.sqs.support.converter.SqsMessagingMessageConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableFeignClients
@EnableJpaRepositories(
		basePackages = {
				"com.vladmihalcea.spring.repository",
				"com.studentassessment.repo"
		}
)
public class StudentAssessmentServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(StudentAssessmentServiceApplication.class, args);
	}
	@Bean
	public SqsMessagingMessageConverter getSQSMessagingConverter(){
		SqsMessagingMessageConverter messageConverter = new SqsMessagingMessageConverter();
		messageConverter.setPayloadTypeMapper(message -> S3EventNotification.class);
		return messageConverter;
	}


}
