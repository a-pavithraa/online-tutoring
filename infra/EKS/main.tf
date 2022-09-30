module "eks" {
  source                          = "terraform-aws-modules/eks/aws"
  version                         = "18.29.1"
  cluster_name                    = local.cluster_name
  cluster_version                 = "1.22"
  cluster_endpoint_private_access = true
  cluster_endpoint_public_access  = true

  cluster_addons = {
    vpc-cni = {
      resolve_conflicts        = "OVERWRITE"
      service_account_role_arn = module.vpc_cni_irsa.iam_role_arn
    }
  }

  vpc_id     = module.vpc.vpc_id
  subnet_ids = module.vpc.private_subnets
  
  node_security_group_additional_rules = {
    ingress_self_all = {
      description = "Node to node all ports/protocols"
      protocol    = "-1"
      from_port   = 0
      to_port     = 0
      type        = "ingress"
      self        = true
    }
    egress_all = {
      description = "Node all egress"
      protocol    = "-1"
      from_port   = 0
      to_port     = 0
      type        = "egress"
      cidr_blocks = ["0.0.0.0/0"]
    }
    ingress_allow_access_from_control_plane = {
  type                          = "ingress"
  protocol                      = "tcp"
  from_port                     = 9443
  to_port                       = 9443
  source_cluster_security_group = true
  description                   = "Allow access from control plane to webhook port of AWS load balancer controller"
}
  }

  eks_managed_node_group_defaults = {
    # We are using the IRSA created below for permissions
    # However, we have to provision a new cluster with the policy attached FIRST
    # before we can disable. Without this initial policy,
    # the VPC CNI fails to assign IPs and nodes cannot join the new cluster
    iam_role_attach_cni_policy = true
    instance_types             = [var.instance_type]
  }

  eks_managed_node_groups = {
    default = {}
  }

  tags = {
    Environment = "dev"
    Terraform   = "true"
  }
}

module "vpc_cni_irsa" {
  source = "terraform-aws-modules/iam/aws//modules/iam-role-for-service-accounts-eks"

  role_name             = "vpc_cni"
  attach_vpc_cni_policy = true
  vpc_cni_enable_ipv4   = true

  oidc_providers = {
    main = {
      provider_arn               = module.eks.oidc_provider_arn
      namespace_service_accounts = ["kube-system:aws-node"]
    }
  }

  tags = {
    Environment = "dev"
    Terraform   = "true"
  }
}

data "aws_eks_cluster" "cluster" {
  name = module.eks.cluster_id
}

data "aws_eks_cluster_auth" "cluster" {
  name = module.eks.cluster_id
}

provider "kubernetes" {
  host                   = data.aws_eks_cluster.cluster.endpoint
  token                  = data.aws_eks_cluster_auth.cluster.token
  cluster_ca_certificate = base64decode(data.aws_eks_cluster.cluster.certificate_authority.0.data)
}
locals {
  aws_iam_oidc_connect_provider_extract_from_arn = element(split("oidc-provider/", "${module.eks.oidc_provider_arn}"), 1)
}
module "service_account_roles" {

  for_each    = { for i in local.service_account_policies : i.name => i }
  source      = "./service_account_roles"
  name        = each.value.name
  description = each.value.description

  path                                           = each.value.path
  policy                                         = each.value.policy
  k8s_service_account_namespace                  = each.value.service_account_namespace
  k8s_service_account_name                       = each.value.service_account_name
  role_name = each.value.role_name
  oidc_arn                                       = module.eks.oidc_provider_arn
  aws_iam_oidc_connect_provider_extract_from_arn = local.aws_iam_oidc_connect_provider_extract_from_arn

}

locals {
  role_arn_mappings={
    for  role in module.service_account_roles : role.role_arns.name => role.role_arns.arn
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
