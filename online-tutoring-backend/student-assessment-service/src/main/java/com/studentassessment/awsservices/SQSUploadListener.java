package com.studentassessment.awsservices;


import com.studentassessment.model.s3.S3EventNotification;
import com.studentassessment.model.s3.S3UploadDocDetails;
import com.studentassessment.service.AssessmentService;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;

@Component
@RequiredArgsConstructor
public class SQSUploadListener {
    private static final Logger LOG = LoggerFactory.getLogger(SQSUploadListener.class);

    private final AssessmentService assessmentService;
    private final AWSUtilityService awsUtilityService;
    private final com.studentassessment.awsservices.AWSUtilityService AWSUtilityService;

    @SqsListener("${questionpaper.queue.name}")
    public void questionPaperUploadHandler(S3EventNotification s3EventNotificationRecord) {
        if (s3EventNotificationRecord != null && s3EventNotificationRecord.getRecords() != null) {
            S3EventNotification.S3 s3Entity = s3EventNotificationRecord.getRecords().get(0).getS3();
            LOG.info("Bucket Name {}", s3Entity.getBucket().getName());
            LOG.info("Key Name {}", s3Entity.getObject().getKey());
            GetObjectRequest getObjectRequest = getS3RequestObject(s3Entity);
            S3UploadDocDetails s3UploadDocDetailsRecord = awsUtilityService.parseS3MetadataForIds(getObjectRequest, s3Entity.getObject().getKey());
            String preSignedUrl = awsUtilityService.getSignedUrl(getObjectRequest);
            assessmentService.processQuestionPaperUpload(s3UploadDocDetailsRecord,preSignedUrl);


        }
    }

    @SqsListener("${answersheet.queue.name}")
    public void answerSheetUploadHandler(S3EventNotification s3EventNotificationRecord) {
        if (s3EventNotificationRecord != null && s3EventNotificationRecord.getRecords() != null) {
            S3EventNotification.S3 s3Entity = s3EventNotificationRecord.getRecords().get(0).getS3();
            LOG.info("Bucket Name {}", s3Entity.getBucket().getName());
            LOG.info("Key Name {}", s3Entity.getObject().getKey());
            if (s3Entity.getObject().getKey().contains("Uploads/")) {
                GetObjectRequest getObjectRequest = getS3RequestObject(s3Entity);
                S3UploadDocDetails s3UploadDocDetailsRecord = awsUtilityService.parseS3MetadataForIds(getObjectRequest, s3Entity.getObject().getKey());
                String preSignedUrl = awsUtilityService.getSignedUrl(getObjectRequest);
                assessmentService.processAnswerSheetUpload(s3UploadDocDetailsRecord, preSignedUrl);
            }


        }
    }

    private GetObjectRequest getS3RequestObject(S3EventNotification.S3 s3Entity) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(s3Entity.getBucket().getName())
                .key(s3Entity.getObject().getKey())
                .build();
        return getObjectRequest;
    }

}
