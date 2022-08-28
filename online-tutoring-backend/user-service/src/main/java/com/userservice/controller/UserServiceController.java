package com.userservice.controller;

import java.net.http.HttpRequest;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserServiceController {
	
	@RequestMapping("/")
	public String getUsers(HttpServletRequest request) {
		System.out.println(request.getHeaderNames().toString());
		request.getHeaderNames().asIterator().forEachRemaining(headerName->System.out.println(headerName+"=="+request.getHeader(headerName)));
		return "hello from user service";
	}

}
