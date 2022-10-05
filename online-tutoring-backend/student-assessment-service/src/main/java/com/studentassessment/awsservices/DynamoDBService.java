package com.studentassessment.awsservices;

import com.studentassessment.entity.dynamodb.StudentNotification;
import io.awspring.cloud.dynamodb.DynamoDbTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.MappedTableResource;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.BatchWriteItemEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.TransactWriteItemsEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.WriteBatch;
import software.amazon.awssdk.services.dynamodb.model.UpdateTimeToLiveRequest;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DynamoDBService {

    @Value("${dynamodb.table.name}")
    private String dynamoDBTableName;

    private final DynamoDbTemplate dynamoDbTemplate;
    private final DynamoDbEnhancedClient dynamoDbEnhancedClient;

    public void insertStudentNotificationRecords(List<StudentNotification> studentNotificationList){

        DynamoDbTable<StudentNotification> mappedTable = dynamoDbEnhancedClient.table(dynamoDBTableName, TableSchema.fromBean(StudentNotification.class));

        WriteBatch.Builder<StudentNotification> batchBuilder = WriteBatch.builder(StudentNotification.class)
                .mappedTableResource(mappedTable);
        studentNotificationList.forEach(studentNotification -> batchBuilder.addPutItem(studentNotification));

        BatchWriteItemEnhancedRequest batchWriteItemEnhancedRequest = BatchWriteItemEnhancedRequest.builder()
                .writeBatches(batchBuilder.build()).build();

        dynamoDbEnhancedClient.batchWriteItem(batchWriteItemEnhancedRequest);



    }


}
