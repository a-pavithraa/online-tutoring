resource "kubernetes_service_account" "sa_cognito_service_role" {
  metadata {
    name      = local.cognito_service_account_name
    namespace = "default"
    annotations = {
      "eks.amazonaws.com/role-arn" = local.role_arn_mappings["${var.prefix}_cognito_sa_role"]
    }
  }
}

resource "kubernetes_service_account" "sa_sqs_service_role" {
  metadata {
    name      = local.app_service_account_name
    namespace = "default"
    annotations = {
      "eks.amazonaws.com/role-arn" = local.role_arn_mappings["${var.prefix}_sqs_sa_role"]
    }
  }
}