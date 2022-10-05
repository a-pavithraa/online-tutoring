resource "aws_dynamodb_table"  "dynamodb_table" {
  
  name     = var.dynamodb_table_name
  hash_key = "cognitoId"
  
  billing_mode   = "PROVISIONED"
  read_capacity  = 5
  write_capacity = 5
   ttl {
    attribute_name = "ttl"
    enabled        = true
  }
   attribute {
    name = "cognitoId"
    type = "S"
  }

  

  tags = local.common_tags

  
 


}

