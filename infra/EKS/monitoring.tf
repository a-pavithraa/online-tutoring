locals {
  monitoring_values = file(
  "${path.module}/helm_chart_config/monitoring_custom_values.yml")
 

}
resource "helm_release" "kube_prometheus_stack" {
  name       = "kube-prometheus-stack"
  repository = "https://prometheus-community.github.io/helm-charts"
  chart      = "kube-prometheus-stack"
  values     = [local.monitoring_values]
}



