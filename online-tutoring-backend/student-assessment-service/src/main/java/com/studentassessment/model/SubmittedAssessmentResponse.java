package com.studentassessment.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SubmittedAssessmentResponse {
    //Need to add pagination specific properties in the next iteration
    private List<SubmittedAssessmentsRecord> submittedAssessmentsRecords;
}
