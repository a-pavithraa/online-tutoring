create table assessment(
id bigint AUTO_INCREMENT PRIMARY KEY,
   subject_id bigint,
   grade_id bigint,
   assessment_date timestamp,
   created_at timestamp ,
   updated_at timestamp ,
   document_url varchar(500),
   FOREIGN KEY (subject_id)
           REFERENCES subject(id)
           ON
DELETE
	CASCADE,
	FOREIGN KEY (grade_id)
   	        REFERENCES grade(id)
           ON
	DELETE
		CASCADE
);
create table student_assessment_mapping(

assessment_id bigint,
student_id bigint,
marks double,
uploaded_document varchar(200),
corrected_document varchar(200),
  FOREIGN KEY (assessment_id)
           REFERENCES assessment(id)
           ON
DELETE
	CASCADE,
	FOREIGN KEY (student_id)
   	        REFERENCES student(id)
           ON
	DELETE
		CASCADE,
		primary key (assessment_id,student_id)
);
