package com.adminservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateTeacherRequest {
    @NotEmpty(message = "Use Name should not be empty")
    private String userName;
    @NotEmpty(message = "Email should not be empty")
    private String emailId;
    @NotEmpty(message = "Password should not be empty")
    private String password;
    private String address;
    private String phoneNo;

}
