data "aws_availability_zones" "available" {}
data "aws_eks_cluster" "cluster" {
  name = module.eks.cluster_id
}

data "aws_eks_cluster_auth" "cluster" {
  name = module.eks.cluster_id
}
locals {
  aws_iam_oidc_connect_provider_extract_from_arn = element(split("oidc-provider/", "${module.eks.oidc_provider_arn}"), 1)
}