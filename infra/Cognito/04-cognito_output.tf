output "client_pool_id"{
    description = "User Pool Id to be copied for authentication"
    value=module.cognito_user_pool.id
}

output "client_endpoint"{
    
    value=module.cognito_user_pool.endpoint
}
output "client_id" {
    value = module.cognito_user_pool.client_ids
  
}