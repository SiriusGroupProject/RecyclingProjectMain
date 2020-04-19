package com.sirius.web.service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;

import com.amazonaws.services.s3.model.S3Object;
import com.sirius.web.utils.Utility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
public class AmazonClient {

    private Logger logger = LoggerFactory.getLogger(AmazonClient.class);

    private AmazonS3 s3Client;

    @Value("${amazonProperties.endPointUrl}")
    private String endPointUrl;

    @Value("${amazonProperties.bucketName}")
    private String bucketName;

    @Value("${amazonProperties.accessKey}")
    private String accessKey;

    @Value("${amazonProperties.secretKey}")
    private String secretKey;

    @PostConstruct
    private void initializeAmazon(){
        AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
        this.s3Client = new AmazonS3Client(credentials);
    }

    public String uploadFile(MultipartFile multipartFile) throws IOException {
        String fileUrl = "";
        try {
            File file = convertMultiPartToFile(multipartFile);
            String fileName = multipartFile.getOriginalFilename();
            fileUrl = endPointUrl + "/" + bucketName + "/" + fileName;
            uploadFileTos3bucket(fileName, file);
            file.delete();
            logger.info("&" + Utility.trace(Thread.currentThread().getStackTrace()) + " ***Uploaded file name: " + fileName);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return fileUrl;
    }

    public S3Object getFileFromS3Bucket(String fileName) {
        S3Object s3object = null;

        try {
            s3object = s3Client.getObject(new GetObjectRequest(bucketName, fileName));
            logger.info("&" + Utility.trace(Thread.currentThread().getStackTrace()) + " ***Getted file name: " + fileName);

        } catch (AmazonServiceException ase) {
            logger.error("&" + Utility.trace(Thread.currentThread().getStackTrace()) + " ***Caught an AmazonServiceException from GET requests, rejected reasons: " + ase);
        } catch (AmazonClientException ace) {
            logger.error("&" + Utility.trace(Thread.currentThread().getStackTrace()) + " ***Caught an AmazonClientException: Error Message: " + ace.getMessage());
        }
        return s3object;
    }

    public boolean deleteFileFromS3Bucket(String fileName) {
        s3Client.deleteObject(new DeleteObjectRequest(bucketName, fileName));
        logger.info("&" + Utility.trace(Thread.currentThread().getStackTrace()) + " ***Deleted file name: " + fileName);
        return true;
    }

    public void uploadFileTos3bucket(String fileName, File file) {
        s3Client.putObject(new PutObjectRequest(bucketName, fileName, file));
    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }

}
