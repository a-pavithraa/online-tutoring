module "vpc" {
  source               = "terraform-aws-modules/vpc/aws"
  version              = "3.16.1"
  name                 = "${var.prefix}_vpc"
  cidr                 = var.vpc_cidr_block
  azs                  = data.aws_availability_zones.available.names
  private_subnets      = [cidrsubnet(var.vpc_cidr_block, 8, 0), cidrsubnet(var.vpc_cidr_block, 8, 2), cidrsubnet(var.vpc_cidr_block, 8, 3)]
  public_subnets       = [cidrsubnet(var.vpc_cidr_block, 8, 4), cidrsubnet(var.vpc_cidr_block, 8, 5), cidrsubnet(var.vpc_cidr_block, 8, 6)]
  enable_nat_gateway   = true
  single_nat_gateway   = true
  enable_dns_hostnames = true
  tags                 = local.common_tags


}
