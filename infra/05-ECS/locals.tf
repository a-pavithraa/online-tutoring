locals {
  question_paper_bucket_arn="arn:aws:s3:::${var.question_paper_bucket_name}"
  answer_sheet_bucket_arn= "arn:aws:s3:::${var.answer_sheet_bucket_name}"
  dynamodb_arn="arn:aws:dynamodb:${var.region}:${data.aws_caller_identity.current.account_id}:table/${var.dynamodb_table_name}"

  common_tags = {
    app     = "${var.prefix}_ecs"
    version = "V1"
  }
  service_definitions = [{
    name = "apigateway",
    task = {
      container_name = "apigateway"
      image_name     = "pavithravasudevan/spring-cloud-api-gateway-unsecured:0.0.1"
      container_port = 9090
      role_policy=""
      host_port      = 9090
      cpu            = 512
      memory         = 2048
      environment_variables = [{
        name   = "assessment_url"
        value = "http://assessment.${var.prefix}.ecs:8088/"
        }, {
        name   = "mdm_url"
        value = "http://mdm.${var.prefix}.ecs:8090/"
      }]


    }
    load_balancer = true

    }, {
    name = "mysql",
    task = {
      container_name = "mysql"
      image_name     = "mysql:8.0.30"
      role_policy=""
      container_port = 3307
      host_port      = 3307
      cpu            = 512
      memory         = 2048
      environment_variables = [{
        name   = "MYSQL_DATABASE"
        value = "shikshadb"
        }, {
        name   = "MYSQL_PASSWORD"
        value = var.db_password
        }, {
        name   = "MYSQL_ROOT_PASSWORD"
        value = var.db_password
        }, {
        name   = "MYSQL_TCP_PORT"
        value = "3307"
        }, {
        name   = "MYSQL_USER"
        value = "testpass"
      }]


    }
    load_balancer = false

    }, {
    name = "mdm",
    task = {
      container_name   = "mdm"
      image_name       = "pavithravasudevan/administration-service:0.0.1"
      container_port   = 8090
      host_port        = 8090
      cpu              = 512
      memory           = 2048
      role_policy = file("${path.module}/role_policies/cognito.json")
      environment_variables = [{
        name   = "spring.datasource.url"
        value = "jdbc:mysql://mysql.${var.prefix}.ecs:3307/shikshadb?user=testpass&password=testpass&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&allowPublicKeyRetrieval=true&useSSL=false"
        }, {
        name   = "mdm_url"
        value = "http://mdm.${var.prefix}.ecs:8090/"
      }]

    }
    load_balancer = false

  }, {
    name = "assessment",
    task = {
      container_name   = "assessment"
      image_name       = "pavithravasudevan/student-assessment-service:0.0.1"
      container_port   = 8088
      host_port        = 8088
      cpu              = 512
      memory           = 2048
      role_policy = templatefile("${path.module}/role_policies/sqs_s3_dynamodb.json",{question_paper_bucket=local.question_paper_bucket_arn,answer_sheet_bucket=local.answer_sheet_bucket_arn,dynamodb_table_arn=local.dynamodb_arn})
      environment_variables = [{
        name   = "spring.datasource.url"
        value = "jdbc:mysql://mysql.${var.prefix}.ecs:3307/shikshadb?user=testpass&password=testpass&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&allowPublicKeyRetrieval=true&useSSL=false"
        }, {
        name   = "restapi.mdm.url"
        value = "http://mdm.${var.prefix}.ecs:8090/"
      }]

    }
    load_balancer = false

  }]
}
