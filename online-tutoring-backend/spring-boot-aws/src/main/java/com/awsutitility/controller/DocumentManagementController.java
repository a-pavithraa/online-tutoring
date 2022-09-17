package com.awsutitility.controller;

import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;


import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

@RestController
@RequestMapping("/documents")
public class DocumentManagementController {
	
	private S3Client s3Client;
	
	private String bucketName;
	private static final Logger logger = LoggerFactory.getLogger(DocumentManagementController.class);
	public DocumentManagementController(S3Client s3Client, @Value("${questionpaper.bucket.name}") String bucketName) {
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

    @PostMapping(path="/uploadQuestionPaper",consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public void uploadQuestionPaper(@RequestParam("assignmentId") long assignmentId, @RequestParam("file") MultipartFile file) throws IOException {
    logger.info("assignment id={}",assignmentId);
        logger.info("getOriginalFilename name={}",file.getOriginalFilename());
        logger.info(" name={}",file.getName());
        Map<String, String> metadata = new HashMap<>();
        metadata.put("x-amz-meta-assignmentid", String.valueOf(assignmentId));
        String fileName = file.getOriginalFilename();
        String extension =fileName.substring(fileName.lastIndexOf("."));
        String newFileName = "QnPaper_"+assignmentId+"_"+UUID.randomUUID()+extension;
        PutObjectRequest putOb = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(newFileName)
                .metadata(metadata)
                .build();
        byte[]   bytesArray = file.getBytes();
        PutObjectResponse response = s3Client.putObject(putOb, RequestBody.fromBytes(bytesArray));

    }

}
