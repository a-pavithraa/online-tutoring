CREATE TABLE users(
   id bigint  AUTO_INCREMENT PRIMARY KEY,
   email varchar(255) NOT NULL, 
   user_name varchar(200) NOT NULL,
   
address varchar(500),
phone_no varchar(30),
   created_at  timestamp ,
   updated_at  timestamp 

);

CREATE TABLE  roles  (
   id  bigint AUTO_INCREMENT PRIMARY KEY,
   name  varchar(255) NOT NULL,
   created_at  timestamp ,
   updated_at  timestamp  
);

CREATE TABLE  user_role  (
   user_id  bigint NOT NULL,
   role_id  bigint NOT NULL,
FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE,
        FOREIGN KEY (role_id)
	        REFERENCES roles(id)
        ON DELETE CASCADE
);

insert into roles(id,name)values(1,'Admin');
insert into roles(id,name)values(2,'Teacher');
insert into roles(id,name)values(3,'Student');


CREATE TABLE  subjects(
   id  bigint AUTO_INCREMENT PRIMARY KEY,
   name  varchar(255) NOT NULL,
   created_at  timestamp ,
   updated_at  timestamp  
);

insert into subjects(id,name)values(1,'Math');
insert into subjects(id,name)values(2,'Science');


CREATE TABLE  grades(
   id  bigint AUTO_INCREMENT PRIMARY KEY,
   name  varchar(255) NOT NULL,
   created_at  timestamp ,
   updated_at  timestamp  
);
insert into grades(id,name)values(1,'VI');
insert into grades(id,name)values(2,'VII');
insert into grades(id,name)values(3,'VIII');

CREATE TABLE students(
   id bigint AUTO_INCREMENT PRIMARY KEY,
   user_id bigint NOT NULL, 
  subject_id bigint  not null,
grade_id bigint not null,
   created_at  timestamp ,
   updated_at  timestamp ,
   FOREIGN KEY (user_id)
           REFERENCES users(id)
           ON DELETE CASCADE,
FOREIGN KEY (subject_id)
        REFERENCES subjects(id)
        ON DELETE CASCADE,
FOREIGN KEY (grade_id)
        REFERENCES grades(id)
        ON DELETE CASCADE
);

CREATE TABLE teachers(
   id INT AUTO_INCREMENT PRIMARY KEY,
   user_id bigint NOT NULL, 
  subject_id bigint  not null,
grade_id bigint not null,
   created_at  timestamp ,
   updated_at  timestamp ,
    FOREIGN KEY (user_id)
              REFERENCES users(id)
              ON DELETE CASCADE,
   FOREIGN KEY (subject_id)
           REFERENCES subjects(id)
           ON DELETE CASCADE,
   FOREIGN KEY (grade_id)
           REFERENCES grades(id)
        ON DELETE CASCADE
);
