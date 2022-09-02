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
name varchar(200) NOT NULL,
   
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
name varchar(200) NOT NULL,   
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
