package com.adminservice.model;

public record StudentRecord(
        long id,
        String userName,
        String fullName,
        String email,
        String parentName,
        long gradeId
) {
}
