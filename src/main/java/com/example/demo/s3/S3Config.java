package com.example.demo.s3;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicSessionCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class S3Config {

    private final S3Credential s3Credential;

    public S3Config(S3Credential s3Credential) {
        this.s3Credential = s3Credential;
    }

    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
                .region(Region.AP_SOUTHEAST_1)
                .build();
    }

    @Bean
    public AmazonS3 amazonS3() {
        BasicSessionCredentials basicSessionCredentials
                = new BasicSessionCredentials(s3Credential.getAccessKey(), s3Credential.getSecretKey(), "");

        return AmazonS3ClientBuilder.standard()
                .withRegion(Regions.AP_SOUTHEAST_1)
                .withCredentials(new AWSStaticCredentialsProvider(basicSessionCredentials))
                .build();
    }

}
