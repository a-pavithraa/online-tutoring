package com.studentassessment.model.assessment;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StudentPerformance {
    private double marks;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MMM")
    private LocalDateTime assessmentDate;
}
