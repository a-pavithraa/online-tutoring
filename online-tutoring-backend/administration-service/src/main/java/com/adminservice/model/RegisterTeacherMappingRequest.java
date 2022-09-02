package com.adminservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterTeacherMappingRequest {
    @NotEmpty
    private String userName;
    @NotEmpty
    private Long grade;
    @NotEmpty
    private Long subject;
}
