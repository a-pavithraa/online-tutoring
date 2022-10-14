resource "kubernetes_storage_class_v1" "ebs_sc" {  
  metadata {
    name = "ebs-sc"
  }
  storage_provisioner = "ebs.csi.aws.com"
  volume_binding_mode = "Immediate"
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

output "pvc_name" {
  value = kubernetes_persistent_volume_claim_v1.pvc.metadata.0.name

}