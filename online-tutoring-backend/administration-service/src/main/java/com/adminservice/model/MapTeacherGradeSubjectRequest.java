package com.adminservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MapTeacherGradeSubjectRequest {
    @NotEmpty
    private Long teacherId;
    @NotEmpty
    private List<GradeAndSubjectMappingRequest> gradeAndSubjectMappingRequestList;
}
