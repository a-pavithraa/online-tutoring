output "bucket_name" {
    value = aws_s3_bucket.s3_bucket.bucket
  
}
output "queue_arn" {
    value = aws_sqs_queue.event_notification_queue.arn
  
}