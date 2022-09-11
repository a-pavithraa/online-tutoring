package com.adminservice.exception;

public class MappingAlreadyExistsException extends RuntimeException{
    public MappingAlreadyExistsException(long gradeId,long subjectId){
        super("Mapped with Grade Id "+gradeId+" and Subject Id "+subjectId+" already exists");
    }
}
