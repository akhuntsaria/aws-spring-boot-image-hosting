package com.github.akhuntsaria.imagehosting.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AwsConfig {

    private final String awsAccessKey;

    private final String awsSecretKey;

    private final String awsRegion;

    public AwsConfig(@Value("${aws.access-key}") String awsAccessKey,
                     @Value("${aws.secret-key}") String awsSecretKey,
                     @Value("${aws.region}") String awsRegion) {
        this.awsAccessKey = awsAccessKey;
        this.awsSecretKey = awsSecretKey;
        this.awsRegion = awsRegion;
    }

    @Bean
    public DynamoDB dynamoDb() {
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder
                .standard()
                .withRegion(awsRegion)
                .withCredentials(new AWSStaticCredentialsProvider(getCredentials()))
                .build();

        return new DynamoDB(client);
    }

    @Bean
    public AmazonS3 s3() {
        return AmazonS3ClientBuilder
                .standard()
                .withRegion(awsRegion)
                .withCredentials(new AWSStaticCredentialsProvider(getCredentials()))
                .build();
    }

    private AWSCredentials getCredentials() {
        return new BasicAWSCredentials(awsAccessKey, awsSecretKey);
    }
}
