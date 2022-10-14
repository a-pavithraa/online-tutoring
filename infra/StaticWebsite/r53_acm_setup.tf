data "aws_route53_zone" "customdomain" {
  name = var.domain_name
}



resource "aws_route53_record" "apps_dns" {
  zone_id = data.aws_route53_zone.customdomain.zone_id
  name    = "${var.prefix}.${var.domain_name}"
  type    = "A"
  alias {
    name                   = aws_cloudfront_distribution.shiksha_distribution.domain_name
    zone_id                = aws_cloudfront_distribution.shiksha_distribution.hosted_zone_id
    evaluate_target_health = false
  }
}


module "acm" {
  source      = "terraform-aws-modules/acm/aws"
  version = "~> 4.0"
  domain_name = trimsuffix(data.aws_route53_zone.customdomain.name, ".")
  zone_id     = data.aws_route53_zone.customdomain.zone_id
  subject_alternative_names = [
    "*.${var.domain_name}"
  ]
  tags = local.common_tags
}

