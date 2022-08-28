package com.awsutitility.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListObjectsRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsResponse;
import software.amazon.awssdk.services.s3.model.S3Object;

@RestController
@RequestMapping("/documents")
public class DocumentManagementController {
	
	private S3Client s3Client;
	
	private String bucketName;
	private static final Logger logger = LoggerFactory.getLogger(DocumentManagementController.class);
	public DocumentManagementController(S3Client s3Client, @Value("${student.bucket.name}") String bucketName) {
		this.s3Client=s3Client;
		this.bucketName=bucketName;
		
	}
	@GetMapping("/")
	public List<String> getDocuments(){
		logger.info("bucket name=={}",bucketName);
		ListObjectsRequest listObjects = ListObjectsRequest
                .builder()
                .bucket(bucketName)
                .build();

        ListObjectsResponse res = s3Client.listObjects(listObjects);
        List<S3Object> objects = res.contents();
        List<String> objectsNames = new ArrayList<>();

        for (ListIterator<S3Object> iterVals = objects.listIterator(); iterVals.hasNext(); ) {
            S3Object myValue = (S3Object) iterVals.next();
            System.out.print("\n The name of the key is " + myValue.key());
            System.out.print("\n The object is " + calKb(myValue.size()) + " KBs");
            System.out.print("\n The owner is " + myValue.owner());
            objectsNames.add(myValue.key());

         }
       return objectsNames;
		
		
	}
	 //convert bytes to kbs.
    private static long calKb(Long val) {
        return val/1024;
    }

}
