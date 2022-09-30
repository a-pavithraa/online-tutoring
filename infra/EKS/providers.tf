# Configure kubeconfig for kubectl
# aws eks --region <region-code> update-kubeconfig --name <cluster_name>
# aws eks --region us-east-1 update-kubeconfig --name shiksha_cluster
terraform {
  required_providers {
    aws = {
      source = "hashicorp/aws"
      version = "4.28.0"
    }
    
  }
}

provider "aws" {
  
}