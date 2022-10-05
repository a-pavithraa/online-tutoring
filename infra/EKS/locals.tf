locals {
  cluster_name = "${var.prefix}_cluster"
  service_account_policies = [{
    name                      = "cognito_access_policy"
    description               = "Cognito Access Policy"
    service_account_name      = "cognito-access"
    service_account_namespace = "default"
    role_name                 = "${var.prefix}_cognito_sa_role"
    path                      = "/"
    policy                    = templatefile("${path.module}/service_account_policies/cognito.json", { region = "${var.region}", account_id = "${var.account_id}", user_pool_id = "${var.user_pool_id}" })

    }, {
    name                      = "sqs_access_policy"
    description               = "SQS Access Policy"
    service_account_name      = "sqs-access"
    service_account_namespace = "default"
    role_name                 = "${var.prefix}_sqs_sa_role"
    path                      = "/"
    policy                    = file("${path.module}/service_account_policies/sqs.json")

    }, {
    name                      = "externaldns_access_policy"
    service_account_name      = "external-dns"
    service_account_namespace = "default"
    role_name                 = "${var.prefix}_externaldns_sa_role"
    description               = "External DNS Access Policy"
    path                      = "/"
    policy                    = file("${path.module}/service_account_policies/external_dns.json")

    }, {
    name                      = "aws_lb_controller_access_policy"
    service_account_name      = "aws-load-balancer-controller"
    service_account_namespace = "kube-system"
    role_name                 = "${var.prefix}_albcontroller_sa_role"
    description               = "AWS Load Balancer Controller Access Policy"
    path                      = "/"
    policy                    = file("${path.module}/service_account_policies/lb_controllor.json")
  }]
}
