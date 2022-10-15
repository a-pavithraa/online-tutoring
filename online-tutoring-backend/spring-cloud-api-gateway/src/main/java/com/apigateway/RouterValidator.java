package com.apigateway;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class RouterValidator {


    //Uncomment this for unsecured access to create teacher
    //public static final List<String> openApiEndpoints = List.of("**/health","/mdm/admin/teacher");

    public static final List<String> openApiEndpoints = List.of(
            "**/health"

    );

    public Predicate<ServerHttpRequest> isSecured =
            request -> openApiEndpoints
                    .stream()
                    .noneMatch(uri -> request.getURI().getPath().contains(uri));



}