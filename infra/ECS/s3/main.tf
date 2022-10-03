resource "random_id" "s3_suffix" {


  byte_length = 4
}
resource "aws_s3_bucket" "s3_bucket" {
  bucket = "${var.bucket_name}.${random_id.s3_suffix.hex}"

  tags = var.tags
}
resource "aws_s3_bucket_cors_configuration" "cors_config" {
  bucket = aws_s3_bucket.s3_bucket.id

  cors_rule {
    allowed_headers = ["Authorization", "Content-Length"]
    allowed_methods = ["GET", "POST", "PUT"]
    allowed_origins = ["*"]
    #allowed_origins = ["https://${var.prefix}.${var.domain_name}"]
    max_age_seconds = 3000
  }
}
resource "aws_s3_bucket_public_access_block" "bucket_public_access" {
  bucket = aws_s3_bucket.s3_bucket.id

  block_public_acls       = true
  block_public_policy     = true
  ignore_public_acls      = true
  restrict_public_buckets = true
}
resource "aws_s3_bucket_lifecycle_configuration" "s3_bucket_lifecycle" {
  bucket = aws_s3_bucket.s3_bucket.bucket

  dynamic "rule" {
    for_each = var.lifecycle_policies.prefix_policies
   
    content {
      id = rule.value.name
      expiration {
        days = rule.value.expiration_days
      }
      filter {
        prefix = rule.value.prefix
      }

      status = "Enabled"

    }

  }

}
resource "aws_sqs_queue" "event_notification_queue" {
  name = "${var.bucket_name}_sqs"

  policy = templatefile("${path.module}/sns_policy.json", { queue = "${var.bucket_name}_sqs", bucket = "${aws_s3_bucket.s3_bucket.arn}" })
}
resource "aws_s3_bucket_notification" "bucket_notification" {
  bucket = aws_s3_bucket.s3_bucket.id
 
  queue {
    queue_arn = aws_sqs_queue.event_notification_queue.arn
    events    = ["s3:ObjectCreated:*"]
    
    filter_prefix =var.s3_prefix
  }
}
