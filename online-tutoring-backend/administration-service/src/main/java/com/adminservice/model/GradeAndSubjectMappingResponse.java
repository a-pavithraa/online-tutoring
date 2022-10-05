package com.adminservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GradeAndSubjectMappingResponse {

    private List<GradeAndSubjectMappingRecord> gradeAndSubjectMappingRecords;
}
