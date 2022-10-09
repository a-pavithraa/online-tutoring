CREATE TABLE subject(
   id bigint AUTO_INCREMENT PRIMARY KEY,
   name varchar(255) NOT NULL,
   created_at timestamp ,
   updated_at timestamp  
);

insert
	into
	subject(id,
	name)
values(1,
'Math');

insert
	into
	subject(id,
	name)
values(2,
'Science');

CREATE TABLE grade(
   id bigint AUTO_INCREMENT PRIMARY KEY,
   name varchar(255) NOT NULL,
   created_at timestamp ,
   updated_at timestamp  
);

insert
	into
	grade(id,
	name)
values(1,
'VI');

insert
	into
	grade(id,
	name)
values(2,
'VII');

insert
	into
	grade(id,
	name)
values(3,
'VIII');


CREATE TABLE student(
id bigint AUTO_INCREMENT PRIMARY KEY,
email varchar(255) NOT NULL, 
user_name varchar(200) NOT NULL,
full_name varchar(200) NOT NULL,
   
address varchar(500),
phone_no varchar(30),
cognito_id varchar(100),
grade_id bigint,
parent_name varchar(200),
created_at timestamp ,
updated_at timestamp ,
FOREIGN KEY (grade_id)
   REFERENCES grade(id)

);

create table student_subject_map(
id bigint AUTO_INCREMENT PRIMARY KEY,
   student_id bigint,
   subject_id bigint,
   FOREIGN KEY (student_id)
           REFERENCES student(id)
           ON
DELETE
	CASCADE,
	FOREIGN KEY (subject_id)
   	        REFERENCES subject(id)
           ON
	DELETE
		CASCADE
);


CREATE TABLE teacher(
id bigint AUTO_INCREMENT PRIMARY KEY,
email varchar(255) NOT NULL, 
user_name varchar(200) NOT NULL,
full_name varchar(200) NOT NULL,
address varchar(500),
phone_no varchar(30),
cognito_id varchar(100),
created_at timestamp ,
updated_at timestamp 

);

create table teacher_subject_grade_map(
   teacher_id bigint,
    grade_id bigint,
   subject_id bigint,
   FOREIGN KEY (teacher_id)
           REFERENCES teacher(id)
           ON
DELETE
	CASCADE,
	
		
		   FOREIGN KEY (grade_id)
		           REFERENCES grade(id)
		           ON
		DELETE
			CASCADE,
			FOREIGN KEY (subject_id)
		   	        REFERENCES subject(id)
		           ON
			DELETE
		CASCADE,
		primary key (teacher_id,subject_id,grade_id)
); 
create table assessment(
id bigint AUTO_INCREMENT PRIMARY KEY,
   subject_id bigint,
   grade_id bigint,
   teacher_id bigint,
   assessment_date timestamp,
   created_at timestamp ,
   updated_at timestamp ,
   question_paper_document varchar(500),
   FOREIGN KEY (subject_id)
           REFERENCES subject(id)
           ON
DELETE
	CASCADE,
	FOREIGN KEY (grade_id)
   	        REFERENCES grade(id)
           ON
	DELETE
		CASCADE,
		 FOREIGN KEY (teacher_id)
                   REFERENCES teacher(id)
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
create table assessment_notification(
id bigint AUTO_INCREMENT PRIMARY KEY,
   assessment_id bigint,
   user_id bigint,
   user_role varchar(20),
  description varchar(300),
   created_at timestamp ,
   updated_at timestamp ,

    FOREIGN KEY (assessment_id)
            REFERENCES assessment(id)
           ON
    DELETE
        CASCADE
);

INSERT INTO teacher (email,user_name,full_name,address,phone_no,cognito_id,created_at,updated_at) VALUES
	 ('a.pavithraa@gmail.com','teachernew2','Teachernew',NULL,'a.pavithraa@gmail.com','21cea790-a54e-476a-8d80-574c43998450','2022-09-22 08:08:45','2022-09-22 08:08:45'),
	 ('a.pavithraa@gmail.com','teachernew3','Teachernew3',NULL,'a.pavithraa@gmail.com','27f8b488-f852-4067-bf4e-a6a6e8cf0f4e','2022-09-29 08:45:57','2022-09-29 08:45:57');
