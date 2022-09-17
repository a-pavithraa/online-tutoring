package com.studentassessment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
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

}
