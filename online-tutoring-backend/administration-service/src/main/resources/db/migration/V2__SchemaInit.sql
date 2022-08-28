CREATE TABLE students(
   id INT AUTO_INCREMENT PRIMARY KEY,
   user_id INT NOT NULL, 
  subject_id bigint  not null,
grade_id bigint not null,
   created_at  timestamp ,
   updated_at  timestamp 
);

CREATE TABLE teachers(
   id INT AUTO_INCREMENT PRIMARY KEY,
   user_id INT NOT NULL, 
  subject_id bigint  not null,
grade_id bigint not null,
   created_at  timestamp ,
   updated_at  timestamp 
);