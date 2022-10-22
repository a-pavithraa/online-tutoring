module "acm" {
 
  source  = "terraform-aws-modules/acm/aws"
  version = "~>4.1"
  domain_name = var.domain_name
  zone_id     = local.zone_id
  subject_alternative_names = [
    "*.${var.domain_name}"
  ]
}
