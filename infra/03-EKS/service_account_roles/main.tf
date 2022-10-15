
resource "aws_iam_policy" "awsservicepolicy" {
  name        = var.name
  path        = var.path
  description = var.description 
  policy = var.policy
}

data "aws_iam_policy_document" "sa_service_role" {
  statement {
    actions = ["sts:AssumeRoleWithWebIdentity"]

    principals {
      type = "Federated"
      identifiers = [
        var.oidc_arn
      ]
    }
     
    condition {
      test     = "StringEquals"
      variable = "${var.aws_iam_oidc_connect_provider_extract_from_arn}:sub"
      values = [
        "system:serviceaccount:${var.k8s_service_account_namespace}:${var.k8s_service_account_name}"
      ]
    }
    condition {
      test     = "StringEquals"
      variable = "${var.aws_iam_oidc_connect_provider_extract_from_arn}:aud"
      values = [
       "sts.amazonaws.com"
      ]
    }
  }
}

resource "aws_iam_role" "sa_service_role" {
  name               = var.role_name
  assume_role_policy = data.aws_iam_policy_document.sa_service_role.json
}

resource "aws_iam_role_policy_attachment" "test-attach" {
  role       = aws_iam_role.sa_service_role.name
  policy_arn = aws_iam_policy.awsservicepolicy.arn
}