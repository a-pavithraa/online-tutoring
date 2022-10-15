**Shiksha**

Pandemic has brought a huge change in education system. Platforms such as Zoom, Google Meet, Microsoft Teams have enabled to connect the teachers and students across various parts of the world.  

It has also empowered many women to restart their career in teaching.  Their working capital is very minimal and they are not able to afford the licensing costs these platform requires.  The channel available to them  in which they can share  child's progress or any impending assignment/test is mainly whatsapp.  The teacher is also not able to maintain the track record of the student .  

This POC idea was born when one of my friends who is a private tutor felt that having a website would reduce the overhead in managing her classes . The main consideration I had is affordability. I didn't want teachers to incur huge costs in cloud bill.   So I used only the services that could come in free tier or the charges are minimal. She has a decent configuration laptop which can be used for compute. During the initial phase, only the tutor would have access to the all the functionalities .Students could only upload their assessments. When there is any assessment, teacher could schedule the same and upload the question paper to S3 via UI. An Email alert would be triggered to the students (SES). I also made use of S3 Event notification and SQS. Same process would be followed for Answer sheets upload also. Using S3 Event Notification and SQS , the  upload done by student can be tracked and status can be updated whenever the server is up.



![image-20221015140146757](C:\Users\apavithra\AppData\Roaming\Typora\typora-user-images\image-20221015140146757.png)



![image-20221015140719891](C:\Users\apavithra\AppData\Roaming\Typora\typora-user-images\image-20221015140719891.png)



![image-20221015141030648](C:\Users\apavithra\AppData\Roaming\Typora\typora-user-images\image-20221015141030648.png)



![image-20221015141106489](C:\Users\apavithra\AppData\Roaming\Typora\typora-user-images\image-20221015141106489.png)

![image-20221015141128666](C:\Users\apavithra\AppData\Roaming\Typora\typora-user-images\image-20221015141128666.png)

![image-20221015141151409](C:\Users\apavithra\AppData\Roaming\Typora\typora-user-images\image-20221015141151409.png)

![image-20221015141220467](C:\Users\apavithra\AppData\Roaming\Typora\typora-user-images\image-20221015141220467.png)

![image-20221015141314583](C:\Users\apavithra\AppData\Roaming\Typora\typora-user-images\image-20221015141314583.png)



![image-20221015141357473](C:\Users\apavithra\AppData\Roaming\Typora\typora-user-images\image-20221015141357473.png)

![image-20221015141433801](C:\Users\apavithra\AppData\Roaming\Typora\typora-user-images\image-20221015141433801.png)

**Features:**

1. Teachers Registration
2. Students Registration
3. Assign teacher to grade and subject
4. Schedule assessment
5. Upload question Paper, answer Sheet by teacher and student respectively
6. Update marks and upload corrected sheets by teacher
7. View Progress of the student.

**Approach:**

Initially I had three containers for application backend, application frontend ,MySQL database. Regular backups of the volumes can be taken and stored in S3.  I have also used S3 for teachers to upload question papers and students to upload answer sheets.  Following AWS Services are used:

1. S3 for teachers to upload question papers and students to upload answer sheets
2. DynamoDB for storing pending assessment details (to be displayed to students)
3. SQS for asynchronous messaging
4. SES for email
5. Cognito User Pool and Identity Pool for authentication and authorization

I have used EKS for my learning purpose. Docker Compose and minikube can also be used.

Initially I went for a single service for backend. Now I have split the service to two but used the same database. Even though updates are done on different tables on each of the services, I am planning to have a separate database for each of the services. 

I have used React for frontend development, Spring Boot for backend development, Terraform for infrastructure provisioning.

**Application Configuration:**

For backend, there are three spring boot projects

1. Administration Service
2. Student Assessment Service 
3. Spring Cloud API

Spring Cloud API handles the authorization part and routes to either Administration service or Assessment Service depending on the URL. All the routes are protected. Since the users would not be there during the initial launch, login would not be possible for the first time . For testing purpose, endpoint for registering teacher is opened. During the first time, **pavithravasudevan/spring-cloud-api-gateway-unsecured:0.0.1** should be used for registering the first user. Execute the below code. Once first user is registered, then this image can be replaced with  **pavithravasudevan/spring-cloud-api-gateway:0.0.1**

```
curl -X POST -H "Content-Type: application/json" \
    -d '{"fullName":"Teacher2022", "userName":"Teacher2022", "emailId": "teacher@gmail.com", "password":"Welcome@01"}' \
    https://shiksha.saaralkaatru.com/mdm/admin/teacher
```



For building image execute the command in the respective project root directory. Ensure that you change the repository name in pom.xml

```
mvn spring-boot:build-image -DskipTests
```

For frontend, there are 2 react projects

1. student-app
2. teacher-app

Create a file .env  and add the following values. We would be using terraform to create cognito user pool id

teacher-app  .env

```
REACT_APP_USER_POOL_ID=us-east-xx
REACT_APP_CLIENT_ID=xxxxx
REACT_APP_SERVER_URL=https://shiksha.custom_domain_name/
```

student-app .env

```
REACT_APP_USER_POOL_ID=us-east-xx
REACT_APP_CLIENT_ID=xxxxx
REACT_APP_IDENTITY_POOL_ID=us-east-1:x-x-x-x-xx
REACT_APP_COGNITO_ENDPOINT=cognito-idp.us-east-1.amazonaws.com/us-east-x
```

To build docker image, execute the following command in the 

```
docker build -t repo-name/shikshafronend .
docker push repo-name/shikshafronend
```

Pre-Requisites:

1. AWS Account
2. E-Mail IDs that are to be used should be verified in SES
3. Domain hosted in R53

The folder "infra" has four sub-folders. They are numbered in the ordered in which they have to be applied. 

​	**01-FileUploadsResources**

​	This terraform project provisions the following resources:

1.  S3 buckets for uploading question papers and answer sheets. 
2. S3 Event notifications with SQS as target
3. DynamoDB for storing pending assessments. This is then used in UI targeted for students.

Please make a note of the outputs . The bucket name and queue name would be used in the other projects

```
s3_bucket_name = {
  "shiksha-answersheets" = {
    "bucket_name" = "shiksha-answersheets.612efef2"
    "queue_arn" = "arn:aws:sqs:us-east-1::shiksha-answersheets_sqs"  
  }
  "shiksha-questionpapers" = {
    "bucket_name" = "shiksha-questionpapers.bf56ce00"
    "queue_arn" = "arn:aws:sqs:us-east-1::shiksha-questionpapers_sqs"
  }
}
```

**02-Cognito**

This terraform project creates the following resources

1. Cognito User Pools

2. Cognito Identity Pools

3. Groups for Teacher and Student with the corresponding roles mapped

   Buckets created in previous step need to be added in tf.vars 

```
client_endpoint = "cognito-idp.us-east-1.amazonaws.com/us-east-xx"
client_id = [
  "4d0rspobol474rft7nq57djjq",
]
client_pool_id = "us-east-xx"
identity_pool_id = "us-east-1:x-x-x-x-x"   
```

Please make not of the User Pool Id , Identity Pool ID, Client ID. These values would be used in both frontend and backend projects.

**03-EKS**

This step is optional. This terraform project creates the following resources:

1. EKS Cluster
2. IAM roles for service accounts to access various AWS Services
3. Service Accounts
4. Helm Charts for deploying Ingress, External DNS, MySQL, EBS-CSI driver and Application
5. Helm charts for monitoring (kube-prometheus) and logging (loki and promtail)
6. Storage Class and PVC

Application can be deployed either through Helm Chart or directly using Kubernetes Manifests

If helm installation is required set use_helm to true in terraform.tfvars

If the flag is set to false, then mysql helm chart would be installed and the other resources can be deployed using

```
kubectl apply -f kubernetes_manifests/
```

Values that can be customized for application helm chart . 

```
image:
  repositoryName: pavithravasudevan
aws:
  cognito:
    userPoolId: us-east-1_xx
    appClientId: xx
  region: us-east-1
ports:
    apigateway: 9090
    mdm: 8090
    assessment: 8088
    mysql: 3306
    frontend: 80
ingress:
  domainName: saaralkaatru.com
serviceAccountName:
  mdm: mdm
  assessment: assessment

mysql:
  auth:
    database: shikshadb
    username: testpass
    password: testpass
    rootPassword: testpass
  volumePermissions:
    enabled: true
  primary:
    persistence:
      size: 4Gi
      existingClaim: ebs-mysql-pv-claim  
```

For existingClaim value, name needs to match the pvc created using terraform. If this value is not given, then MYSQL Helm chart takes care of creating EBS volume. 

Since I am using flyway db migration, the tables are created automatically once the application is up.

**04-S3CloudFront**

This terraform project provisions S3 bucket along with cloud front distribution for static website. This step is optional



**Local Set-up**

I have created a docker-compose file in online-tutoring-backend folder. Create a folder called env and two files mdm.env and student-assessment.env

Sample contents of mdm.env and student-assessment.env

```
spring.datasource.url=jdbc:mysql://mysql:3307/shikshadb?user=testpass&password=testpass&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&allowPublicKeyRetrieval=true&useSSL=false
restapi.mdm.url=http://mdm:8090/ -- This is only for assessment service
AWS_REGION=us-east-1
AWS_ACCESS_KEY_ID=xxxxx
AWS_SECRET_ACCESS_KEY=xxxxx
```



**TO BE DONE IN NEXT PHASE:**

Validation for mapping subjects and grades- Check whether any previous teacher has been already mapped

Re upload Documents

Upgrade table component to grid with pagination

Validation for uploaded files(type and size)

Implement separate db for each  of the services

Revert S3 Upload and Cognito User Creation on exception

CI/CD
