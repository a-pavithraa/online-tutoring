package com.adminservice.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import com.adminservice.model.CreateUserRequest;

import com.adminservice.util.Utilties.Roles;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminAddUserToGroupRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AttributeType;
import software.amazon.awssdk.services.cognitoidentityprovider.model.SignUpRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.SignUpResponse;

@Service
@RequiredArgsConstructor
public class UserService {

	private CognitoIdentityProviderClient identityProviderClient;
	//private UserRepository userRepo;
	@Value("${cognito.app.clientid}")
	private String clientId;
	@Value("${jwt.aws.userPoolId}")
	private String userPoolId;
	@Value("${student.group.name}")
	private  String studentGroup ;
	@Value("${teacher.group.name}")
	private String teacherGroup ;
	
	private static final Logger logger = LoggerFactory.getLogger(UserService.class);
	
	@Autowired
	public UserService(CognitoIdentityProviderClient identityProviderClient) {
		this.identityProviderClient=identityProviderClient;
		//this.userRepo = userRepo;
	}

	public void createUser(CreateUserRequest createUserRequest) {
		
		  AttributeType attributeType = AttributeType.builder()
		            .name("email")
		            .value(createUserRequest.getEmailId())
		            .build();

		        List<AttributeType> attrs = new ArrayList<>();
		        attrs.add(attributeType);
		        SignUpRequest signUpRequest = SignUpRequest.builder()
		                .userAttributes(attrs)
		                .username(createUserRequest.getUserName())
		                .clientId(clientId)           
		                
		                .password(createUserRequest.getPassword())		               
		                .build();

		        SignUpResponse response= identityProviderClient.signUp(signUpRequest);
		        logger.debug("user confirmed=={}",response);
		        Roles passedRole = Roles.valueOf(createUserRequest.getRole());
		        String groupName = Roles.TEACHER==passedRole?teacherGroup:studentGroup;
		        AdminAddUserToGroupRequest adminGroupRequest = AdminAddUserToGroupRequest.builder()
		        		.username(createUserRequest.getUserName())
		        		.userPoolId(userPoolId)
		        		.groupName(groupName).build();
		        identityProviderClient.adminAddUserToGroup(adminGroupRequest);
				/*
				 * Users user = Users.builder() .userName(createUserRequest.getUserName())
				 * .email(createUserRequest.getEmailId()) .cognitoId(response.userSub())
				 * .phoneNo(createUserRequest.getEmailId())
				 * .address(createUserRequest.getAddress()) .build();
				 */
		       // userRepo.persistAndFlush(user);
		     
		        
		     

	}
}
