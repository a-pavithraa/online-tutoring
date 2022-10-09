locals {
  cluster_name = "${var.prefix}_cluster"
  cognito_service_account_name="cognito-access"
  app_service_account_name="awsservices-access"
  external_dns_service_account_name="external-dns"
  ebs_csi_service_account_name="ebs-csi-controller-sa"
  aws_lb_service_account_name="aws-load-balancer-controller"

  service_account_policies = [{
    name                      = "cognito_access_policy"
    description               = "Cognito Access Policy"
    service_account_name      = local.cognito_service_account_name
    service_account_namespace = "default"
    role_name                 = "${var.prefix}_cognito_sa_role"
    path                      = "/"
    policy                    = templatefile("${path.module}/service_account_policies/cognito.json", { region = "${var.region}", account_id = "${var.account_id}", user_pool_id = "${var.user_pool_id}" })

    }, {
    name                      = "awsservices_access_policy"
    description               = "SQS,SNS and DDB Access Policy"
    service_account_name      = local.app_service_account_name
    service_account_namespace = "default"
    role_name                 = "${var.prefix}_sqs_sa_role"
    path                      = "/"
    policy                    = templatefile("${path.module}/service_account_policies/sqs.json",{dynamodb_table_name=var.dynamodb_table_name})

    }, {
    name                      = "externaldns_access_policy"
    service_account_name      = local.external_dns_service_account_name
    service_account_namespace = "default"
    role_name                 = "${var.prefix}_externaldns_sa_role"
    description               = "External DNS Access Policy"
    path                      = "/"
    policy                    = file("${path.module}/service_account_policies/external_dns.json")

    }, {
    name                      = "ebscsi_access_policy"
    service_account_name      = local.ebs_csi_service_account_name
    service_account_namespace = "kube-system"
    role_name                 = "${var.prefix}_ebscsi_sa_role"
    description               = "EBS CSI Policy"
    path                      = "/"
    policy                    = file("${path.module}/service_account_policies/ebs_csi_driver.json")

    }, {
    name                      = "aws_lb_controller_access_policy"
    service_account_name      = local.aws_lb_service_account_name
    service_account_namespace = "kube-system"
    role_name                 = "${var.prefix}_albcontroller_sa_role"
    description               = "AWS Load Balancer Controller Access Policy"
    path                      = "/"
    policy                    = file("${path.module}/service_account_policies/lb_controllor.json")
  }]
}
