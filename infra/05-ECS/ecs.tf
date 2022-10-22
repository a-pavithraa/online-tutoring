resource "aws_ecs_cluster" "main" {
  name = "${var.prefix}_cluster"
  tags = local.common_tags
}
module "ecs" {
  source                = "./ecs_service"
  for_each              = { for i in local.service_definitions : i.name => i }
  service_name          = each.value.name
  container_name        = each.value.task.container_name
  container_port        = each.value.task.container_port
  role_policy           = each.value.task.role_policy
  image_name            = each.value.task.image_name
  region                = var.region
  vpc_id                = module.vpc.vpc_id
  vpc_cidr_block        = module.vpc.vpc_cidr_block
  vpc_private_subnets   = module.vpc.private_subnets
  load_balancer_flag    = each.value.load_balancer
  target_group_arn      = module.alb.target_group_arns[0]
  cpu_allocated         = each.value.task.cpu
  memory_allocated      = each.value.task.memory
  cluster_id            = aws_ecs_cluster.main.id
  app_count             = 1
  environment_variables = each.value.task.environment_variables
  execution_role_arn    = aws_iam_role.main_ecs_tasks.arn
  namespace_id = aws_service_discovery_private_dns_namespace.ecs_discovery_service_ns.id

}