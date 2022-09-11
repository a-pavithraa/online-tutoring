package com.adminservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GradeAndSubjectMappingRequest {
    @NotEmpty
    private Long gradeId;
    @NotEmpty
    private Long subjectId;
}
