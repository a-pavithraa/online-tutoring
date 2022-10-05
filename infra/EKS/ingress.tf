# Datasource: AWS Load Balancer Controller IAM Policy get from aws-load-balancer-controller/ GIT Repo (latest)

provider "helm" {

  kubernetes {

    host                   = data.aws_eks_cluster.cluster.endpoint
    token                  = data.aws_eks_cluster_auth.cluster.token
    cluster_ca_certificate = base64decode(data.aws_eks_cluster.cluster.certificate_authority.0.data)
  }
}


resource "helm_release" "loadbalancer_controller" {
  depends_on = [module.service_account_roles]
  name       = "aws-load-balancer-controller"
  verify     = false
  repository = "https://aws.github.io/eks-charts"
  chart      = "aws-load-balancer-controller"

  namespace = "kube-system"

  set {
    name  = "image.repository"
    value = "602401143452.dkr.ecr.us-east-1.amazonaws.com/amazon/aws-load-balancer-controller" # Changes based on Region - This is for us-east-1 Additional Reference: https://docs.aws.amazon.com/eks/latest/userguide/add-ons-images.html
  }

  set {
    name  = "serviceAccount.create"
    value = "true"
  }

  set {
    name  = "serviceAccount.name"
    value = "aws-load-balancer-controller"
  }

  set {
    name  = "serviceAccount.annotations.eks\\.amazonaws\\.com/role-arn"
    value = local.role_arn_mappings["${var.prefix}_albcontroller_sa_role"]
  }

  set {
    name  = "vpcId"
    value = module.vpc.vpc_id
  }

  set {
    name  = "region"
    value = var.region
  }

  set {
    name  = "clusterName"
    value = module.eks.cluster_id
  }

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
