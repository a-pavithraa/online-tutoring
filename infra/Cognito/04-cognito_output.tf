output "client_pool_id"{
    description = "Replace USER_POOL_ID with this value in constants.js"
    value=module.cognito_user_pool.id
}

output "client_endpoint"{
    description = "REplace COGNITO_ENDPOINT with this value  in constants.js"
    
    value=module.cognito_user_pool.endpoint
}
output "client_id" {
    description = "Replace CLIENT_ID with this value in constants.js"
    value = module.cognito_user_pool.client_ids
  
}

output "identity_pool_id" {
    description = "Replace IDENTITY_POOL_ID with this value in constants.js (only for student frontend code)"
    value = aws_cognito_identity_pool.identity_pool.id
  
}