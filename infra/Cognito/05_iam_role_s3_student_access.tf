resource "aws_iam_role" "student_role" {
  name = "student_new_role"

  assume_role_policy = <<EOF
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Effect": "Allow",
            "Principal": {
                "Federated": "cognito-identity.amazonaws.com"
            },
            "Action": "sts:AssumeRoleWithWebIdentity",
            "Condition": {
                "StringEquals": {
                    "cognito-identity.amazonaws.com:aud": "${resource.aws_cognito_identity_pool.identity_pool.id}"
                }
            }
        }
    ]
}
EOF
}

resource "aws_iam_policy" "student_policy" {
  name        = "student-policy"
  description = "Giving S3 folder and DynamoDB Access"

  policy = <<EOF
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Action": [
                "s3:ListBucket"
            ],
            "Effect": "Allow",
            "Resource": [
                "arn:aws:s3:::${var.assessment_bucket_name}"
            ],
            "Condition": {
                "StringLike": {
                    "s3:prefix": [
                        "$${cognito-identity.amazonaws.com:sub}/*"
                    ]
                }
            }
        },
        {
            "Action": [
                "s3:GetObject",
                "s3:PutObject"
            ],
            "Effect": "Allow",
            "Resource": [
                "arn:aws:s3:::${var.assessment_bucket_name}/$${cognito-identity.amazonaws.com:sub}/*"
            ]
        },
        {
            "Sid": "DescribeQueryScanStudentsTable",
            "Effect": "Allow",
            "Action": [
                "dynamodb:ListTables",
                "dynamodb:GetItem",
                "dynamodb:BatchGetItem",
                "dynamodb:Query"

            ],
            "Resource": "arn:aws:dynamodb:${var.region}:${data.aws_caller_identity.current.account_id}:table/${var.dynamodb_table_name}"
        }
    ]
}
EOF
}

resource "aws_iam_role_policy_attachment" "student_policy-attach" {
  role       = aws_iam_role.student_role.name
  policy_arn = aws_iam_policy.student_policy.arn
}

resource "aws_cognito_user_group" "student" {
 
  name         = "Student"
  description  = "Group for students"
 
  role_arn     = aws_iam_policy.student_policy.arn
  user_pool_id = module.cognito_user_pool.id
}

// Attaching only Student Role. Teacher Role is not necessary. We are not using cognito IDP as of now for teacher login
resource "aws_cognito_identity_pool_roles_attachment" "myaws" {
  identity_pool_id = aws_cognito_identity_pool.identity_pool.id

  roles = {
    "authenticated" = aws_iam_role.student_role.arn
  }
  
}