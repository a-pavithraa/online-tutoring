locals {
  common_tags = {
    app     = "Shiksha"
    version = "V1"
  }
  assignment_buckets = [{
    bucket_name = "${var.prefix}-questionpapers",
    s3_notification_prefix=""
    lifecycle_rules = {
      prefix_policies = [{
        prefix          = "",
        expiration_days = 7,
        name            = "${var.prefix}-questionpapers_all"
    }] }

    }, {
    bucket_name = "${var.prefix}-answersheets",
     s3_notification_prefix="Uploads/"
    lifecycle_rules = {
      prefix_policies = [{
        prefix          = "",
        expiration_days = 15,
        name            = "${var.prefix}-answersheets"
        }, {
        prefix          = "/Notification",
        expiration_days = 1,
        name            = "${var.prefix}-answersheets_notification"
      }],
      tag_policies = [{
        tag_name = "student_notification"
        value    = "Y",
        name     = "${var.prefix}-questionpapers_tag"
      }]

    }
    }
  ]
}

