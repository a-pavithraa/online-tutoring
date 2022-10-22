resource "aws_iam_policy" "awsservicepolicy" {
  count = var.role_policy!=""?1:0
  name        = "${var.service_name}_policy"
  path        = "/"
  description = "ECS Service Policy"
  policy = var.role_policy
}
resource "aws_iam_role" "task_role" {
  name = "ecs_tasks-${var.container_name}-role"
   count = var.role_policy!=""?1:0
  assume_role_policy = <<EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Action": "sts:AssumeRole",
      "Principal": {
        "Service": "ecs-tasks.amazonaws.com"
      },
      "Effect": "Allow",
      "Sid": ""
    }
  ]
}
EOF
}
resource "aws_iam_role_policy_attachment" "attach_policy_role" {
   count = var.role_policy!=""?1:0
  role       = aws_iam_role.task_role[0].name
  policy_arn = aws_iam_policy.awsservicepolicy[0].arn
}
resource "aws_cloudwatch_log_group" "cloudwatch" {
  name = "${var.container_name}cloudwatchgroup"

  tags = {
    Environment = "production"
    Application = "${var.service_name}"
  }
}

resource "aws_cloudwatch_log_stream" "cloudwatch" {
  name           = "${var.container_name}cloudwatch"
  log_group_name = aws_cloudwatch_log_group.cloudwatch.name
}


resource "aws_ecs_task_definition" "main" {
  family             =  var.container_name
 
  task_role_arn = var.role_policy!=""?aws_iam_role.task_role[0].arn:""
  execution_role_arn = var.execution_role_arn
  network_mode       = "awsvpc"
  requires_compatibilities = ["FARGATE"]
  cpu    = var.cpu_allocated
  memory = var.memory_allocated
  container_definitions = <<DEFINITION
[
    {
      "name": "${var.container_name}",
      "image": "${var.image_name}",
      "essential": true,
      "environment": ${jsonencode(var.environment_variables)},
      "portMappings": [
        {
          "containerPort": ${var.container_port},
          "hostPort": ${var.container_port}
        }
      ],
      "logConfiguration": {
        "logDriver": "awslogs",
        "options": {
          "awslogs-group": "${aws_cloudwatch_log_group.cloudwatch.name}",
          "awslogs-region": "${var.region}",
          "awslogs-stream-prefix": "shikshacloudwatch"
        }
      }
    }
    
  ]
      
    
    DEFINITION
  
}
resource "aws_ecs_service" "apiecsservice" {
  name            = var.service_name
depends_on = [
    aws_ecs_task_definition.main
  ]
  cluster         = var.cluster_id
  task_definition = aws_ecs_task_definition.main.family
  desired_count   = var.app_count
  launch_type     = "FARGATE"

  network_configuration {
    security_groups = [module.ecstask_sg.security_group_id]
    subnets         = var.vpc_private_subnets
  }
  service_registries{
    registry_arn = aws_service_discovery_service.ecs_discovery_service.arn
    

  }
  dynamic "load_balancer"{
     for_each = var.load_balancer_flag == true ? [true] : []
     content{
         target_group_arn =var.target_group_arn
         container_name   = var.container_name
          container_port   = var.container_port

     }
  }

  


}