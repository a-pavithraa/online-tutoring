module "service_account_roles" {

  for_each    = { for i in local.service_account_policies : i.name => i }
  source      = "./service_account_roles"
  name        = each.value.name
  description = each.value.description

  path                                           = each.value.path
  policy                                         = each.value.policy
  k8s_service_account_namespace                  = each.value.service_account_namespace
  k8s_service_account_name                       = each.value.service_account_name
  role_name                                      = each.value.role_name
  oidc_arn                                       = module.eks.oidc_provider_arn
  aws_iam_oidc_connect_provider_extract_from_arn = local.aws_iam_oidc_connect_provider_extract_from_arn

}

locals {
  role_arn_mappings = {
    for role in module.service_account_roles : role.role_arns.name => role.role_arns.arn
  }
}
output "external_dns_role_arn" {
  value = local.role_arn_mappings["${var.prefix}_externaldns_sa_role"]

}
output "lb_controllor_role_arn" {
  value = local.role_arn_mappings["${var.prefix}_albcontroller_sa_role"]

}
output "cognito_role_arn" {
  value = local.role_arn_mappings["${var.prefix}_cognito_sa_role"]
}
