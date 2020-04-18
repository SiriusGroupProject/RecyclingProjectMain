package com.sirius.web.controller;

import com.amazonaws.services.s3.model.S3Object;
import com.sirius.web.service.AmazonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/v1/storage/")
public class BucketController {

    private AmazonClient amazonClient;

    @Autowired
    BucketController(AmazonClient amazonClient) {
        this.amazonClient = amazonClient;
    }

    @PostMapping("/uploadFile")
    public String uploadFile(@RequestPart(value = "file") MultipartFile file) throws IOException {
        return this.amazonClient.uploadFile(file);
    }

    @GetMapping("/getFile")
    public S3Object getFile(@RequestPart(value = "fileName") String fileName) {
        return this.amazonClient.getFileFromS3Bucket(fileName);
    }

    @DeleteMapping("/deleteFile")
    public boolean deleteFile(@RequestPart(value = "fileName") String fileName) {
        return this.amazonClient.deleteFileFromS3Bucket(fileName);
    }

}
