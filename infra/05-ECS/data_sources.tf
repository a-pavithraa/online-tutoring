data "aws_availability_zones" "available" {}
data "aws_route53_zone" "this" {
  name = var.domain_name
}
data "aws_caller_identity" "current" {}
locals {
 

  zone_id = data.aws_route53_zone.this.zone_id
}