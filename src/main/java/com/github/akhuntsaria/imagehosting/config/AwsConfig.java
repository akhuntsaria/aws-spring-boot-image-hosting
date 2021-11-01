package com.github.akhuntsaria.imagehosting.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AwsConfig {

    @Value("${aws.access-key}")
    private String awsAccessKey;

    @Value("${aws.secret-key}")
    private String awsSecretKey;

    @Bean
    public AmazonS3 s3() {
        AWSCredentials awsCredentials = new BasicAWSCredentials(awsAccessKey, awsSecretKey);

        return AmazonS3ClientBuilder
                .standard()
                .withRegion("us-east-2")
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();
    }
}
