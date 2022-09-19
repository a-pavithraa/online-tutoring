package com.studentassessment.awsservices;


import com.studentassessment.model.AssessmentRecord;
import com.studentassessment.model.S3EventNotification;
import com.studentassessment.model.S3UploadDocDetailsRecord;
import com.studentassessment.service.AssessmentService;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class SQSUploadListener {
    private static final Logger LOG = LoggerFactory.getLogger(SQSUploadListener.class);

    private final AssessmentService assessmentService;
    private final S3ActionsService s3ActionsService;

    @SqsListener("${questionpaper.queue.name}")
    public void processOrder(S3EventNotification s3EventNotificationRecord) {
        if (s3EventNotificationRecord != null && s3EventNotificationRecord.getRecords() != null) {
            S3EventNotification.S3 s3Entity = s3EventNotificationRecord.getRecords().get(0).getS3();
            LOG.info("Bucket Name {}", s3Entity.getBucket().getName());
            LOG.info("Key Name {}", s3Entity.getObject().getKey());
           assessmentService.sendMailToStudentsOnQuestionsUpload(s3Entity);


        }


    }

}
