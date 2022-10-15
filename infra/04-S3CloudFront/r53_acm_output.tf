
output "customdomain_zoneid" {
  description = "The Hosted Zone id of the desired Hosted Zone"
  value       = data.aws_route53_zone.customdomain.zone_id
}

output "customdomain_name" {
  description = " The Hosted Zone name of the desired Hosted Zone."
  value       = data.aws_route53_zone.customdomain.name
}

output "this_acm_certificate_arn" {
  description = "The ARN of the certificate"
  value       = module.acm.acm_certificate_arn
}