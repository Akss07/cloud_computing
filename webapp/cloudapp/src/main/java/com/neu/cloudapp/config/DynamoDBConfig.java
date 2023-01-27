package com.neu.cloudapp.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DynamoDBConfig {

    private static final String accessKey = "";
    private static final String secretKey = "";

    @Bean
    public DynamoDBMapper dynamoDBMapper() {


        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
                //.withCredentials(new AWSStaticCredentialsProvider(amazonAWSCredentials()))
                .withRegion(Regions.US_EAST_1).build();
        DynamoDBMapper dynamoDBMapper = new DynamoDBMapper(client, DynamoDBMapperConfig.DEFAULT);

        return dynamoDBMapper;
    }

    @Bean
    public AWSCredentials amazonAWSCredentials() {
        return new BasicAWSCredentials(accessKey, secretKey);
    }
}
