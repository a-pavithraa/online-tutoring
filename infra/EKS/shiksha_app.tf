
locals {

  mysql_values = templatefile(
  "${path.module}/helm_chart_config/mysql_custom_values.yml", { pvc_claim = kubernetes_persistent_volume_claim_v1.pvc.metadata.0.name })
  shikshaapp_custom_values = templatefile(
    "${path.module}/helm_chart_config/shikshaapp_custom_values.yml", {
      pvc_claim                  = kubernetes_persistent_volume_claim_v1.pvc.metadata.0.name
      mdm_service_account        = local.cognito_service_account_name
      assessment_service_account = local.app_service_account_name
  })

}


/**
resource "helm_release" "mysql" {
  name       = "mysql"
  repository = "https://charts.bitnami.com/bitnami"
  chart      = "mysql"
  values     = [local.mysql_values]
  depends_on = [
    kubernetes_persistent_volume_claim_v1.pvc
    
  ]

}
*/


resource "helm_release" "shikshaapp" {
  count = var.use_helm ? 1 : 0
  name              = "shikshaapp"
  chart             = "${path.module}/shiksha-helm-chart"
  values            = [local.shikshaapp_custom_values]
  dependency_update = true
 
}
