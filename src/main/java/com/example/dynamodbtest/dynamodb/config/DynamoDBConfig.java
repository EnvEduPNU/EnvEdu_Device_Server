//package com.example.dynamodbtest.dynamodb.config;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
//import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
//import software.amazon.awssdk.regions.Region;
//import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
//
//
//import java.net.URI;
//
//@Configuration
//public class DynamoDBConfig {
//
//    @Value("${cloud.aws.credentials.access-key}")
//    private String awsAccessKeyId;
//
//    @Value("${cloud.aws.credentials.secret-key}")
//    private String awsSecretKey;
//
//    @Value("${cloud.aws.region.static}")
//    private String awsRegion;
//
//    @Value("${cloud.aws.dynamodb.endpoint}")
//    private String awsDynamoDBEndpoint;
//
//    @Bean
//    public DynamoDbAsyncClient dynamoDbAsyncClient() {
//        return DynamoDbAsyncClient.builder()
//                .endpointOverride(URI.create(awsDynamoDBEndpoint))
//                .region(Region.of(awsRegion))
//                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(awsAccessKeyId, awsSecretKey)))
//                .build();
//    }
//}
//
