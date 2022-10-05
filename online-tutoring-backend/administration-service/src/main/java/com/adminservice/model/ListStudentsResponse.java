package com.adminservice.model;

import com.adminservice.entity.Student;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ListStudentsResponse {

    private List<StudentRecord> studentRecords;
}
