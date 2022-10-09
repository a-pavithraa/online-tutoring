package com.studentassessment.exception;

public class InvalidMetadataException extends RuntimeException{
    public InvalidMetadataException(){
        super("Metadata is corrupted");
    }
}
