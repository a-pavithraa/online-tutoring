

variable "region" {
  default = "us-east-1"
}


variable "prefix" {
  default = "shiksha-student"
}


variable "domain_name" {

}
variable "bucket_name" {
  type        = string
  description = "The name of the bucket without the www. prefix. Normally domain_name."
}

