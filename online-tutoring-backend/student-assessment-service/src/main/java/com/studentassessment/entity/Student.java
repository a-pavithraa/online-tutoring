package com.studentassessment.entity;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "student")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Student {
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	private Long id;
	private String fullName;
	private String userName;
	private String email;
	private String address;
	private String phoneNo;
	private String cognitoId;
	private String parentName;
	@OneToMany(
			mappedBy = "student",
			fetch = FetchType.LAZY,
			cascade = CascadeType.ALL,
			orphanRemoval = true
	)
	private Set<StudentAssessmentMapping> assessmentMappings = new HashSet<>();

}
