variable "region" {
    default = "us-east-1"
  
}

variable "prefix" {
  default = "shiksha"
}

variable "redirect_url"{
    default="http://localhost:3000/"
}
variable "callback_url"{
     default="http://localhost:3000/"
}
variable "logout_url"{
    default="http://localhost:3000/"
}

variable "reply_to_email_address"{
    default="a.pavithraa@gmail.com"
}
variable "domain_name" {
    default="saaralkaatru.com"
  
}
variable "custom_domain_name"{
    default="shiksha.saaralkaatru.com"
}

variable "certificate_arn"{

}

variable "assessment_bucket_name"{
    default = "shiksha-answersheets.612efef2"
}
variable "dynamodb_table_name" {
    default = "StudentNotification"
  
}