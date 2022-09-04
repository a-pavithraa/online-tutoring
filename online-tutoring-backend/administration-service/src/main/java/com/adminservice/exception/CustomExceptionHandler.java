package com.adminservice.exception;

import com.adminservice.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.NativeWebRequest;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;
import org.zalando.problem.ThrowableProblem;
import org.zalando.problem.spring.web.advice.AdviceTrait;
import org.zalando.problem.spring.web.advice.ProblemHandling;
import org.zalando.problem.spring.web.advice.custom.CustomAdviceTrait;
import org.zalando.problem.spring.web.advice.general.GeneralAdviceTrait;
import org.zalando.problem.spring.web.advice.http.HttpAdviceTrait;
import org.zalando.problem.spring.web.advice.routing.NoHandlerFoundAdviceTrait;
import org.zalando.problem.spring.web.advice.routing.RoutingAdviceTrait;
import org.zalando.problem.spring.web.advice.validation.ValidationAdviceTrait;

import java.util.NoSuchElementException;

@ControllerAdvice
public class CustomExceptionHandler implements
        AdviceTrait, GeneralAdviceTrait,
        HttpAdviceTrait, RoutingAdviceTrait, ValidationAdviceTrait {

    private static final Logger logger = LoggerFactory.getLogger(CustomExceptionHandler.class);

    @ExceptionHandler
    public ResponseEntity<Problem> handle(NoSuchElementException e, NativeWebRequest request) {

        logger.debug(e.toString());
        ThrowableProblem problem = Problem.builder().withStatus(Status.NOT_FOUND)
                .withTitle("Not Found")
                .withDetail(e.getMessage())
                .build();
        return create(problem, request);
    }

    @Override
    public boolean isCausalChainsEnabled() {
        return false;
    }
}
