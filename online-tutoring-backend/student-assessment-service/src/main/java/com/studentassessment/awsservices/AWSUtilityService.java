package com.studentassessment.awsservices;

import com.studentassessment.model.S3EventNotification;
import com.studentassessment.model.S3UploadDocDetailsRecord;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AWSUtilityService {

    private final S3Presigner s3Presigner;
    private final S3Client s3Client;

    private static final Logger LOG = LoggerFactory.getLogger(AWSUtilityService.class);
    private final MailSender mailSender;
    @Value("${mail.address.from}")
    private String fromMailAddress;

    public S3UploadDocDetailsRecord getSignedUrlAndAssignmentId(S3EventNotification.S3 s3Entity){

        Map<String,String> resultMap = new HashMap<>();
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(s3Entity.getBucket().getName())
                .key(s3Entity.getObject().getKey())
                .build();
        GetObjectResponse getObjectResponse = s3Client.getObject(getObjectRequest).response();
        long assignmentId = 0;
        long teacherId =0;
        long studentId=0;

        if (getObjectResponse.metadata().containsKey("assignmentid"))
            assignmentId= Long.valueOf(getObjectResponse.metadata().get("assignmentid"));

        if (getObjectResponse.metadata().containsKey("teacherid"))
            teacherId= Long.valueOf(getObjectResponse.metadata().get("teacherid"));
        if (getObjectResponse.metadata().containsKey("studentid"))
            studentId= Long.valueOf(getObjectResponse.metadata().get("studentid"));
        GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(60))
                .getObjectRequest(getObjectRequest)
                .build();


        PresignedGetObjectRequest presignedGetObjectRequest = s3Presigner.presignGetObject(getObjectPresignRequest);
        String theUrl = presignedGetObjectRequest.url().toString();
        System.out.println(theUrl);
        S3UploadDocDetailsRecord s3UploadDocDetailsRecords= new S3UploadDocDetailsRecord(assignmentId,teacherId,studentId,theUrl);
        return s3UploadDocDetailsRecords;
    }

    public void sendMail(String recipientAddress, String subject, String body){
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(fromMailAddress);
        simpleMailMessage.setTo(recipientAddress);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(body);
        mailSender.send(simpleMailMessage);

    }


}
