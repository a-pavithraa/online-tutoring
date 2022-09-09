package com.adminservice.service;

import com.adminservice.util.Utilties;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.*;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CognitoService {

    private final  CognitoIdentityProviderClient identityProviderClient;
    // private UserRepository userRepo;
    @Value("${cognito.app.clientid}")
    private String clientId;
    @Value("${jwt.aws.userPoolId}")
    private String userPoolId;
    @Value("${student.group.name}")
    private String studentGroup;
    @Value("${teacher.group.name}")
    private String teacherGroup;

    private static final Logger logger = LoggerFactory.getLogger(CognitoService.class);



    public String createCognitoUser(String userName,String password,String emailId, Utilties.Roles role) {
        AttributeType attributeType = AttributeType.builder().name("email").value(emailId)
                .build();

        List<AttributeType> attrs = new ArrayList<>();
        attrs.add(attributeType);
        SignUpRequest signUpRequest = SignUpRequest.builder().userAttributes(attrs)
                .username(userName).clientId(clientId).password(password)
                .build();

        SignUpResponse response = identityProviderClient.signUp(signUpRequest);

        logger.debug("user confirmed=={}", response);

        String groupName = Utilties.Roles.TEACHER == role ? teacherGroup : studentGroup;
        AdminAddUserToGroupRequest adminGroupRequest = AdminAddUserToGroupRequest.builder()
                .username(userName).userPoolId(userPoolId).groupName(groupName).build();
        identityProviderClient.adminAddUserToGroup(adminGroupRequest);

        return response.userSub();

    }

    public void deleteCognitoUser(String userName){
       AdminDeleteUserRequest adminDeleteUserRequest = AdminDeleteUserRequest.builder()
               .userPoolId(userPoolId)
               .username(userName).build();
       identityProviderClient.adminDeleteUser(adminDeleteUserRequest);
    }
}
