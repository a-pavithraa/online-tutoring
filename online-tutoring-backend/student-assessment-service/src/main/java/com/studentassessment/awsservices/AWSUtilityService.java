package com.studentassessment.awsservices;

import com.studentassessment.model.s3.S3UploadDocDetails;
import io.awspring.cloud.s3.ObjectMetadata;
import io.awspring.cloud.s3.S3Resource;
import io.awspring.cloud.s3.S3Template;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.time.Duration;
import java.util.Map;
import static com.studentassessment.api.mdm.Constants.Metadata;

@Service
@RequiredArgsConstructor
public class AWSUtilityService {

    private final S3Presigner s3Presigner;
    private final S3Client s3Client;

    private static final Logger LOG = LoggerFactory.getLogger(AWSUtilityService.class);
    private final MailSender mailSender;

    private final S3Template s3Template;
    @Value("${mail.address.from}")
    private String fromMailAddress;
    private final static int PRE_SIGNED_URL_VALIDITY = 120;


    public S3UploadDocDetails parseS3MetadataForIds(GetObjectRequest getObjectRequest, String key){

        GetObjectResponse getObjectResponse = s3Client.getObject(getObjectRequest).response();
        long assessmentId = 0;
        long teacherId =0;
        long studentId=0;
        String cognitoId=null;

        if (getObjectResponse.metadata().containsKey(Metadata.ASSESSMENT_ID.toString()))
            assessmentId= Long.valueOf(getObjectResponse.metadata().get(Metadata.ASSESSMENT_ID.toString()));

        if (getObjectResponse.metadata().containsKey(Metadata.TEACHER_ID.toString()))
            teacherId= Long.valueOf(getObjectResponse.metadata().get(Metadata.TEACHER_ID.toString()));
        if (getObjectResponse.metadata().containsKey(Metadata.STUDENT_ID.toString()))
            studentId= Long.valueOf(getObjectResponse.metadata().get(Metadata.STUDENT_ID.toString()));
        if (getObjectResponse.metadata().containsKey(Metadata.COGNITO_ID.toString()))
            cognitoId= String.valueOf(getObjectResponse.metadata().get(Metadata.COGNITO_ID.toString()));
         /* if(assessmentId==0 || teacherId==0|| studentId==0)
            throw new InvalidMetadataException();*/
        S3UploadDocDetails s3UploadDocDetailsRecords= new S3UploadDocDetails(assessmentId,teacherId,studentId,cognitoId,key);
        return s3UploadDocDetailsRecords;

    }

    public String getSignedUrl(GetObjectRequest getObjectRequest){


        GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(PRE_SIGNED_URL_VALIDITY))
                .getObjectRequest(getObjectRequest)
                .build();


        PresignedGetObjectRequest presignedGetObjectRequest = s3Presigner.presignGetObject(getObjectPresignRequest);
        String theUrl = presignedGetObjectRequest.url().toString();
        return theUrl;

    }

    public String getPresignedUrl(String bucketName,String key){
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();
        GetObjectResponse getObjectResponse = s3Client.getObject(getObjectRequest).response();
        GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(60))
                .getObjectRequest(getObjectRequest)
                .build();


        PresignedGetObjectRequest presignedGetObjectRequest = s3Presigner.presignGetObject(getObjectPresignRequest);
        String theUrl = presignedGetObjectRequest.url().toString();

        return theUrl;

    }

    public void sendMail(String recipientAddress, String subject, String body){
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(fromMailAddress);
        simpleMailMessage.setTo(recipientAddress);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(body);
        mailSender.send(simpleMailMessage);

    }

    @SneakyThrows
    public void uploadToBucket(String bucketName, String key, Map<String,String> metadataMap, MultipartFile file) {

        ObjectMetadata.Builder metadataBuilder = ObjectMetadata.builder();
        metadataMap.entrySet().forEach(m->metadataBuilder.metadata(m.getKey(),m.getValue()));
        ObjectMetadata metadata =metadataBuilder.build();
        s3Template.upload(bucketName, key, file.getInputStream(),metadata);


    }

    public void uploadJSONToBucket(String bucketName,String key, String jsonStr){
        s3Template.store(bucketName,key,jsonStr);
    }

    @SneakyThrows
    public InputStreamResource downloadFile(String bucket, String key)  {
       S3Resource s3Resource = s3Template.download(bucket,key);
        InputStreamResource inputStreamResource=new InputStreamResource(s3Resource.getInputStream());
        return inputStreamResource;
    }


}
