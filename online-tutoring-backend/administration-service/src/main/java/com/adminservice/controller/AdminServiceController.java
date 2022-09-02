package com.adminservice.controller;

import javax.validation.Valid;

import com.adminservice.model.CreateTeacherRequest;
import com.adminservice.model.RegisterTeacherMappingRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.adminservice.model.CreateStudentRequest;
import com.adminservice.service.UserService;

@RestController
@RequestMapping("/admin")

public class AdminServiceController {
	
	
	private UserService userService;
	
	public AdminServiceController(UserService userService) {
		this.userService=userService;
	}
	
	@PostMapping("/student")
	@ResponseStatus(HttpStatus.CREATED)
	public void createStudent(@RequestBody @Valid CreateStudentRequest createStudentRequest) {
		userService.createStudent(createStudentRequest);
		
	}

	@PostMapping("/teacher")
	@ResponseStatus(HttpStatus.CREATED)
	public void createTeacher(@RequestBody @Valid CreateTeacherRequest createTeacherRequest) {
		userService.createTeacher(createTeacherRequest);

	}

	@PostMapping("/teacherMapping")
	@ResponseStatus(HttpStatus.CREATED)
	public void mapTeacherToSubject(@RequestBody @Valid RegisterTeacherMappingRequest registerTeacherMappingRequest) {
		userService.mapTeacherToSubjectAndGrade(registerTeacherMappingRequest);

	}

}
