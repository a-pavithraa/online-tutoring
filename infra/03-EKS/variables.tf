variable "prefix" {
  default = "shiksha"
}
variable "oidc_thumbprint_list" {
  type    = list(any)
  default = []
}

variable "region" {
  default = "us-east-1"
}
variable "account_id" {
  description = "Account ID"
}
variable "instance_type" {
  default = "t2.medium"
}

variable "vpc_cidr_block" {
  description = "VPC CIDR Block"
  type        = string
  default     = "10.16.0.0/16"
}

variable "hosted_zone_id" {
  description = "Hosted Zone Id for External DNS service account"
  type        = string
}
variable "user_pool_id" {
  type = string
}

variable "dynamodb_table_name"{
  default = "StudentNotification"
}
variable "use_helm" {
  type = bool
  default = false
  
}
variable "question_paper_bucket_name" {
  default = "shiksha-questionpapers.bf56ce00"
  
}
variable "answer_sheet_bucket_name" {
  default = "shiksha-answersheets.612efef2"
  
}