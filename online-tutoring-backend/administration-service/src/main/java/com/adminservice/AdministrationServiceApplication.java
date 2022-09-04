package com.adminservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;
import org.zalando.problem.ProblemModule;
import org.zalando.problem.validation.ConstraintViolationProblemModule;

@SpringBootApplication
@EnableAutoConfiguration(exclude = ErrorMvcAutoConfiguration.class)
@EnableJpaRepositories(
		basePackages = {
				"com.vladmihalcea.spring.repository",
				"com.adminservice.repo"
		}
)
public class AdministrationServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AdministrationServiceApplication.class, args);
	}
	@Bean
	public ObjectMapper objectMapper() {
		// In this example, stack traces support is enabled by default.
		// If you want to disable stack traces just use new ProblemModule() instead of new ProblemModule().withStackTraces()
		return new ObjectMapper().registerModules(new ProblemModule(), new ConstraintViolationProblemModule());
	}
}



