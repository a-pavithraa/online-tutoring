package com.adminservice.model;

public record StudentDetails(
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
