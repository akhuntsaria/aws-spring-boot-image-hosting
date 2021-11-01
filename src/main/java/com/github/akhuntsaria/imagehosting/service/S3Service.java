package com.github.akhuntsaria.imagehosting.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class S3Service {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final AmazonS3 amazonS3;

    private final String bucketName;

    public S3Service(AmazonS3 amazonS3,
                     @Value("${aws.s3.bucket-name}") String bucketName) {
        this.amazonS3 = amazonS3;
        this.bucketName = bucketName;
    }

    /**
     * @param file file
     * @return uploaded file's id
     */
    public String upload(MultipartFile file) {
        String id = UUID.randomUUID().toString();
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.addUserMetadata("original-filename", file.getOriginalFilename());

        try {
            amazonS3.putObject(bucketName, id, file.getInputStream(), metadata);

            log.info("File {} was uploaded with id {}", file.getOriginalFilename(), id);

            return id;
        } catch (IOException e) {
            log.error("Could not upload file {}", file.getOriginalFilename(), e);
        }

        return null;
    }
}
