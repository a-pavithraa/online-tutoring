package com.studentassessment.model.s3;

public record S3UploadDocDetails(long assessmentId, long teacherId, long studentId, String cognitoId, String key) {
}
