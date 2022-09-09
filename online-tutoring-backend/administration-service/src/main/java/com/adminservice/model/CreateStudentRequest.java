package com.adminservice.model;

import javax.validation.constraints.NotEmpty;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateStudentRequest {
	@NotEmpty(message = "Use Name should not be empty")
	private String userName;
	@NotEmpty(message = "Full Name should not be empty")
	private String fullName;
	@NotEmpty(message = "Email should not be empty")
	private String emailId;
	@NotEmpty(message = "Password should not be empty")
	private String password;
	private String address;
	private String phoneNo;
	private long grade;
	private List<Long> subjects;
	private String parentName;
	

}
