package com.adminservice.controller;

import javax.validation.Valid;

import com.adminservice.entity.Teacher;
import com.adminservice.model.CreateTeacherRequest;
import com.adminservice.model.RegisterTeacherMappingRequest;
import com.adminservice.service.CognitoService;
import com.adminservice.util.Utilties;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.adminservice.model.CreateStudentRequest;
import com.adminservice.service.UserService;

@RestController
@RequestMapping("/mdm/admin")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AdminServiceController {

	private final UserService userService;
	private final CognitoService cognitoService;
	

	@PostMapping("/student")
	@ResponseStatus(HttpStatus.CREATED)
	public void createStudent( @Valid @RequestBody CreateStudentRequest createStudentRequest) {
		String cognitoId = cognitoService.createCognitoUser(createStudentRequest.getUserName(),createStudentRequest.getPassword(),createStudentRequest.getEmailId(), Utilties.Roles.STUDENT);
		userService.createStudent(createStudentRequest,cognitoId);
		
	}

	@PostMapping("/teacher")
	@ResponseStatus(HttpStatus.CREATED)
	public void createTeacher( @Valid @RequestBody CreateTeacherRequest createTeacherRequest) {
		String cognitoId = cognitoService.createCognitoUser(createTeacherRequest.getUserName(),createTeacherRequest.getPassword(),createTeacherRequest.getEmailId(), Utilties.Roles.TEACHER);
		userService.createTeacher(createTeacherRequest,cognitoId);

	}

	@DeleteMapping("/teacher/{teacherName}")

	public void deleteTeacher(@PathVariable("teacherName") String teacherName){
		userService.deleteTeacher(teacherName);
		cognitoService.deleteCognitoUser(teacherName);
	}

	@DeleteMapping("/student/{studentName}")

	public void deleteStudent(@PathVariable("studentName") String studentName){
		userService.deleteStudent(studentName);
		cognitoService.deleteCognitoUser(studentName);
	}


}
