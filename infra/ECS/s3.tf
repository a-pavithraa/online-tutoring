module "s3_bucket" {
  source = "./s3"
 
  for_each={for bucket in local.assignment_buckets : bucket.bucket_name  => bucket}
  bucket_name = each.value.bucket_name
  expiration_days = each.value.expiration_days
  tags = local.common_tags
}
output "s3_bucket_name" {
  value = module.s3_bucket
  
}
