resource "aws_dynamodb_table"  "dynamodb_table" {
  
  name     = var.dynamodb_table_name
  hash_key = "cognitoId"
  
  billing_mode   = "PAY_PER_REQUEST"
#read_capacity  = 20
 # write_capacity = 20
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

