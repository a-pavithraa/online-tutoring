#!/bin/bash

awslocal s3api create-bucket --bucket testcontainers

echo "########### Setting SQS and S3 names as env variables ###########"
export UPLOAD_FILE_EVENT_SQS=question-papers_sqs
export QN_BUCKET_NAME=question-paper-bucket

echo "########### Creating upload file event SQS ###########"
awslocal sqs create-queue --queue-name $UPLOAD_FILE_EVENT_SQS





echo "########### Listing queues ###########"
awslocal  sqs list-queues

echo "########### Create S3 bucket ###########"
awslocal s3api create-bucket\
    --bucket $QN_BUCKET_NAME
awslocal s3api create-bucket\
    --bucket testbucket
echo "########### List S3 bucket ###########"
awslocal s3api list-buckets

echo "########### ARN for upload file event SQS ###########"
UPLOAD_FILE_EVENT_SQS_ARN=$(awslocal  sqs get-queue-attributes\
                  --attribute-name QueueArn --queue-url=http://localhost:4566/000000000000/"$UPLOAD_FILE_EVENT_SQS"\
                  |  sed 's/"QueueArn"/\n"QueueArn"/g' | grep '"QueueArn"' | awk -F '"QueueArn":' '{print $2}' | tr -d '"' | xargs)


echo "########### Set S3 bucket notification configurations ###########"
awslocal s3api put-bucket-notification-configuration\
    --bucket $QN_BUCKET_NAME\
    --notification-configuration  '{
                                      "QueueConfigurations": [
                                         {
                                           "QueueArn": "'"$UPLOAD_FILE_EVENT_SQS_ARN"'",
                                           "Events": ["s3:ObjectCreated:*"]
                                         }
                                       ]
                                     }'

echo "########### Get S3 bucket notification configurations ###########"
awslocal s3api get-bucket-notification-configuration\
    --bucket $QN_BUCKET_NAME
echo "Initialized."