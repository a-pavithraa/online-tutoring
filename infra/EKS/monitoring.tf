locals {
  monitoring_values = file(
  "${path.module}/helm_chart_config/monitoring_custom_values.yml")
  promtail_values=file(  "${path.module}/helm_chart_config/promtail_custom_values.yml")
 

}
resource "helm_release" "kube_prometheus_stack" {
  name       = "kube-prometheus-stack"
  repository = "https://prometheus-community.github.io/helm-charts"
  chart      = "kube-prometheus-stack"
  values     = [local.monitoring_values]
}
resource "helm_release" "promtail" {

   name       = "promtail"
  repository = "https://grafana.github.io/helm-charts"
  chart      = "promtail"
  values     = [local.promtail_values]
  
}

resource "helm_release" "loki" {

   name       = "loki"
  repository = "https://grafana.github.io/loki/charts"
  chart      = "loki-stack"
  set {
     name  = "loki.serviceName"
    value = "loki"
    
  }
 
  
}



