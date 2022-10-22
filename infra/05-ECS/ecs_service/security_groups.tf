module "ecstask_sg" {
  source  = "terraform-aws-modules/security-group/aws"
  version = "4.13.1"
  vpc_id      = var.vpc_id 
  name        = "ecs-sg-${var.service_name}"
   egress_rules = ["all-all"]
   ingress_with_cidr_blocks = [{
      from_port   = var.container_port
      to_port     =  var.container_port
      protocol    = 6
      description = "Allow ${var.container_port} for ${var.service_name}"
      cidr_blocks = var.vpc_cidr_block
    }]
}