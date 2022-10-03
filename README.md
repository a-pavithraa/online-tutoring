**Shiksha**

Pandemic has brought a huge change in education system. Platforms such as Zoom, Google Meet, Microsoft Teams have enabled to connect the teachers and students across various parts of the world.  

It has also enabled many women to restart their career in teaching. The one thing that I felt is the channels in which the child's progress or any impending assignment/test is either through WhatsApp or Email.  The teacher is also not able to maintain the track record of the student .  

This POC idea was born when one of my friends who is a private tutor also felt the same and she asked whether a website could be developed. She would not be able to afford the compute services in cloud. So I used only the services that could come in free tier or where the charges would be very minimal. She has a decent configuration laptop and can be used for compute. During the initial phase, only the tutor would have access to the all the functionalities and the students could only upload their assessments.

**Approach:**

Since this is a small application, I went for monolithic architecture with 2 containers one for the application and one for mysql db with regular backups of the volumes taken.  

For brushing up my skills in kubernetes, I split the monolith to 2 microservices. This is just for my learning. 

 I went for monolith approach with 2 containers - one for the application and one for mysql db with regular backups of the volumes taken.  

When there is any assessment, she could schedule the same and upload the question paper to S3 via UI. An Email alert would be triggered to the students (SES). I also made use of S3 Event notification and SQS. Same process would be followed for Answer sheets upload also. 

It has been some months since I worked with Kubernetes.  So I split the monolith to two microservices. I also made created another microservice making use of Spring Cloud Gateway to handle the authorization part to eliminate the duplication across the services.



TO BE DONE:

Validation for mapping subjects and grades- Check whether any previous teacher has been already mapped

Re upload Documents

Upgrade table component to grid with pagination

Validation for uploaded files(type and size)

Implement separate db for each 

Revert S3 Upload and Cognito User Creation on exception
