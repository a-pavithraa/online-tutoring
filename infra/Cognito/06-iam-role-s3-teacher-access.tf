resource "aws_iam_role" "teacher_role" {
  name = "teacher_role"

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
                    "cognito-identity.amazonaws.com:aud": "${aws_cognito_identity_pool.identity_pool.id}"
                }
            }
        }
    ]
}
EOF
}

resource "aws_iam_policy" "teacher_policy" {
  name        = "test-policy"
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

resource "aws_iam_role_policy_attachment" "teacher_policy-attach" {
  role       = aws_iam_role.teacher_role.name
  policy_arn = aws_iam_policy.teacher_policy.arn
}

resource "aws_cognito_user_group" "teacher" {
 
  name         = "Teacher"
  description  = "Group for Teachers"
 
  role_arn     = aws_iam_policy.teacher_policy.arn
  user_pool_id = module.cognito_user_pool.id
}