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
                    "cognito-identity.amazonaws.com:aud": "${module.cognito_user_pool.id}"
                }
            }
        }
    ]
}
EOF
}

resource "aws_iam_policy" "student_policy" {
  name        = "student-policy"
  description = "A test policy"

  policy = <<EOF
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Action": [
                "s3:ListBucket",
                "s3:GetObject",
                "s3:PutObject"
            ],
            "Effect": "Allow",
            "Resource": [
                "arn:aws:s3:::${var.assessment_bucket_name}",
                "arn:aws:s3:::${var.assessment_bucket_name}/*"
            ]
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