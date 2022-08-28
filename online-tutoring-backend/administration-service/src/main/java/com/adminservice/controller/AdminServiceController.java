package com.adminservice.controller;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.adminservice.model.CreateUserRequest;
import com.adminservice.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin")

public class AdminServiceController {
	
	
	private UserService userService;
	
	public AdminServiceController(UserService userService) {
		this.userService=userService;
	}
	
	@PostMapping("/user")
	@ResponseStatus(HttpStatus.CREATED)
	public void createUser(@RequestBody @Valid CreateUserRequest createUserRequest) {
		userService.createUser(createUserRequest);
		
	}

}
