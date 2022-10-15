locals {
 
  ebs_csi_values = templatefile(
  "${path.module}/helm_chart_config/ebs_csi_custom_values.yml", {
    repositoryName="602401143452.dkr.ecr.us-east-1.amazonaws.com/eks/aws-ebs-csi-driver"
    serviceAccountName = local.ebs_csi_service_account_name
    roleArn= local.role_arn_mappings["${var.prefix}_ebscsi_sa_role"]
    
  }
  )

}

resource "helm_release" "ebs_csi_driver" {
  depends_on = [module.service_account_roles]
  name       = "aws-ebs-csi-driver"
  repository = "https://kubernetes-sigs.github.io/aws-ebs-csi-driver"
  chart      = "aws-ebs-csi-driver"
  namespace = "kube-system"     

  set {
    name = "image.repository"
    value = "602401143452.dkr.ecr.us-east-1.amazonaws.com/eks/aws-ebs-csi-driver" # Changes based on Region - This is for us-east-1 Additional Reference: https://docs.aws.amazon.com/eks/latest/userguide/add-ons-images.html
  }       

  set {
    name  = "controller.serviceAccount.create"
    value = "true"
  }

  set {
    name  = "controller.serviceAccount.name"
    value = local.ebs_csi_service_account_name
  }

  set {
    name  = "controller.serviceAccount.annotations.eks\\.amazonaws\\.com/role-arn"
    value = local.role_arn_mappings["${var.prefix}_ebscsi_sa_role"]
  }
    
}

output "ebs_csi_helm_metadata" {
  description = "Metadata Block outlining status of the deployed release."
  value = helm_release.ebs_csi_driver.metadata
}

resource "kubernetes_storage_class_v1" "ebs_sc" {  
  metadata {
    name = "ebs-sc"
  }
  storage_provisioner = "ebs.csi.aws.com"
  volume_binding_mode = "WaitForFirstConsumer"
  depends_on = [
    helm_release.ebs_csi_driver
  ]
}


resource "kubernetes_persistent_volume_claim_v1" "pvc" {
  wait_until_bound = false
  metadata {
    name = "ebs-mysql-pv-claim"
  }
  spec {
    access_modes = ["ReadWriteOnce"]
    storage_class_name = kubernetes_storage_class_v1.ebs_sc.metadata.0.name 
    resources {
      requests = {
        storage = "4Gi"
      }
    }
  }
}