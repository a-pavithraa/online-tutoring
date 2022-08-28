package com.adminservice.model;

import javax.validation.constraints.NotEmpty;


import com.adminservice.util.Utilties.RoleGroupMapping;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserRequest {
	@NotEmpty(message = "Use Name should not be empty")
	private String userName;
	@NotEmpty(message = "Email should not be empty")
	private String emailId;
	@NotEmpty(message = "Password should not be empty")
	private String password;
	@NotEmpty(message = "Role should not be empty")
	private String role;
	private String address;
	private String phoneNo;
	

}
