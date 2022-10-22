variable "region" {
  default = "us-east-1"

}
variable "prefix" {
  default = "shiksha"
}
variable "vpc_cidr_block" {
  description = "VPC CIDR Block"
  type        = string
  default     = "10.16.0.0/16"
}
variable "domain_name" {
  default = "saaralkaatru.com"

}
variable "db_password" {

}
variable "dynamodb_table_name"{
  default = "StudentNotification"
}

variable "question_paper_bucket_name" {
  default = "shiksha-questionpapers.bf56ce00"
  
}
variable "answer_sheet_bucket_name" {
  default = "shiksha-answersheets.612efef2"
  
}