CREATE TABLE users(
   id INT AUTO_INCREMENT PRIMARY KEY,
   email varchar(255) NOT NULL, 
   first_name  varchar(100) NOT NULL,
   last_name  varchar(100) ,
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
   role_id  bigint NOT NULL
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
