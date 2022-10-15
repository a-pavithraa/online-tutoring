/**
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

  name       = "aws-ebs-csi-driver"
  repository = "https://kubernetes-sigs.github.io/aws-ebs-csi-driver"
  chart      = "aws-ebs-csi-driver"
  namespace = "kube-system"    
  values =[local.ebs_csi_values]

    
}

output "ebs_csi_helm_metadata" {
  description = "Metadata Block outlining status of the deployed release."
  value = helm_release.ebs_csi_driver.metadata
}


*/