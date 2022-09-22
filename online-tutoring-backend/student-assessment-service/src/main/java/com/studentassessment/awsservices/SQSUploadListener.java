package com.studentassessment.awsservices;


import com.studentassessment.model.S3EventNotification;
import com.studentassessment.service.AssessmentService;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SQSUploadListener {
    private static final Logger LOG = LoggerFactory.getLogger(SQSUploadListener.class);

    private final AssessmentService assessmentService;
    private final AWSUtilityService AWSUtilityService;

    @SqsListener("${questionpaper.queue.name}")
    public void questionPaperUploadHandler(S3EventNotification s3EventNotificationRecord) {
        if (s3EventNotificationRecord != null && s3EventNotificationRecord.getRecords() != null) {
            S3EventNotification.S3 s3Entity = s3EventNotificationRecord.getRecords().get(0).getS3();
            LOG.info("Bucket Name {}", s3Entity.getBucket().getName());
            LOG.info("Key Name {}", s3Entity.getObject().getKey());
            assessmentService.sendMailToStudentsOnQuestionsUpload(s3Entity);


        }
    }

    @SqsListener("${answersheet.queue.name}")
    public void answerSheetUploadHandler(S3EventNotification s3EventNotificationRecord) {
        if (s3EventNotificationRecord != null && s3EventNotificationRecord.getRecords() != null) {
            S3EventNotification.S3 s3Entity = s3EventNotificationRecord.getRecords().get(0).getS3();
            LOG.info("Bucket Name {}", s3Entity.getBucket().getName());
            LOG.info("Key Name {}", s3Entity.getObject().getKey());
            assessmentService.sendMailToTeacherOnAnswerSheetUpload(s3Entity);


        }
    }

}
