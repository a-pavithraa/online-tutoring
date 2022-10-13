package com.studentassessment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.studentassessment.model.s3.S3EventNotification;
import io.awspring.cloud.sqs.support.converter.SqsMessagingMessageConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.zalando.problem.ProblemModule;
import org.zalando.problem.validation.ConstraintViolationProblemModule;

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

	@Bean
	public ObjectMapper objectMapper() {
		ObjectMapper mapper = new ObjectMapper();
		// In this example, stack traces support is enabled by default.
		// If you want to disable stack traces just use new ProblemModule() instead of new ProblemModule().withStackTraces()
		mapper.registerModules(new ProblemModule(), new ConstraintViolationProblemModule(),(new JavaTimeModule()));
		mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);


		return mapper;
	}


}
