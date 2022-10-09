# Datasource: AWS Load Balancer Controller IAM Policy get from aws-load-balancer-controller/ GIT Repo (latest)

provider "helm" {

  kubernetes {

    host                   = data.aws_eks_cluster.cluster.endpoint
    token                  = data.aws_eks_cluster_auth.cluster.token
    cluster_ca_certificate = base64decode(data.aws_eks_cluster.cluster.certificate_authority.0.data)
  }
}

locals {
 
  aws_lb_values = templatefile(
  "${path.module}/helm_chart_config/aws_lb_custom_values.yml",{
    repositoryName="602401143452.dkr.ecr.us-east-1.amazonaws.com/amazon/aws-load-balancer-controller"
    serviceAccountName = local.aws_lb_service_account_name
    roleArn= local.role_arn_mappings["${var.prefix}_albcontroller_sa_role"]
    vpcId= module.vpc.vpc_id
    region= var.region
    clusterName= module.eks.cluster_id
  }
  )

}

resource "helm_release" "loadbalancer_controller" {
  depends_on = [module.service_account_roles]
  name       = "aws-load-balancer-controller"
  verify     = false
  repository = "https://aws.github.io/eks-charts"
  chart      = "aws-load-balancer-controller"
  namespace = "kube-system"
  values     = [local.aws_lb_values]

 

}

output "lbc_helm_metadata" {
  description = "Metadata Block outlining status of the deployed release."
  value       = helm_release.loadbalancer_controller.metadata
}

resource "kubernetes_ingress_class_v1" "ingress_class_default" {
  depends_on = [helm_release.loadbalancer_controller]
  metadata {
    name = "my-aws-ingress-class"
    annotations = {
      "ingressclass.kubernetes.io/is-default-class" = "true"
    }
  }
  spec {
    controller = "ingress.k8s.aws/alb"
  }
}
