module "s3_bucket" {
  source = "./s3"

  for_each           = { for bucket in local.assignment_buckets : bucket.bucket_name => bucket }
  bucket_name        = each.value.bucket_name
  lifecycle_policies = each.value.lifecycle_rules
  s3_prefix=each.value.s3_notification_prefix
  tags               = local.common_tags
}
output "s3_bucket_name" {
  value = module.s3_bucket

}
