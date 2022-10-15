locals {
 
  external_dns_values = templatefile(
  "${path.module}/helm_chart_config/external_dns_custom_values.yml",{
    repositoryName="k8s.gcr.io/external-dns/external-dns"
    serviceAccountName = local.external_dns_service_account_name
    roleArn= local.role_arn_mappings["${var.prefix}_externaldns_sa_role"]
  
  }
  )

}
resource "helm_release" "external_dns" {
  depends_on = [module.service_account_roles]
  name       = "external-dns"
  repository = "https://kubernetes-sigs.github.io/external-dns/"
  chart      = "external-dns"
  namespace = "default"
   values     = [local.external_dns_values]

 

}
# Helm Release Outputs
output "externaldns_helm_metadata" {
  description = "Metadata Block outlining status of the deployed release."
  value       = helm_release.external_dns.metadata
}