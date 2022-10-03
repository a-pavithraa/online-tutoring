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