package com.studentassessment.model;

public record StudentRecord(
        long id,
        String userName,
        String fullName,
        String email,
        String parentName,
        String contactNo,
        String address,
        String cognitoId
) {
}
