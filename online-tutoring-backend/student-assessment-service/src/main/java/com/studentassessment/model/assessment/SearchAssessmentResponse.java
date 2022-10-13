package com.studentassessment.model.assessment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SearchAssessmentResponse {
    private List<AssessmentDetails> assessmentDetailsRecordList;
}
