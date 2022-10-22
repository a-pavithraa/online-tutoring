resource "aws_service_discovery_private_dns_namespace" "ecs_discovery_service_ns" {
  name        = "${var.prefix}.ecs"
  description = "Namespace for ecs"
  vpc         = module.vpc.vpc_id
}

