locals {  
  common_tags = {
    app     = "Shiksha"
    version = "V1"
  }
  assignment_buckets=[{
    bucket_name="${var.prefix}-questionpapers",
    expiration_days=7
  },{
     bucket_name="${var.prefix}-answersheets",
    expiration_days=15
  }]
}

