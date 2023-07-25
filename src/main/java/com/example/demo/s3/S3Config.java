package com.example.demo.s3;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Slf4j
@Configuration
public class S3Config {

    private final S3Credential s3Credential;

    public S3Config(S3Credential s3Credential) {
        this.s3Credential = s3Credential;
    }

    @Bean
    public S3Client s3Client() {
        log.info("AccessKey: {}, SecretKey: {}", s3Credential.getAccessKey(), s3Credential.getSecretAccessKey());
        AwsCredentialsProvider credentialsProvider = StaticCredentialsProvider
                .create(AwsBasicCredentials.create(s3Credential.getAccessKey(), s3Credential.getSecretAccessKey()));

        return S3Client.builder()
                .credentialsProvider(credentialsProvider)
                .region(Region.AP_SOUTHEAST_1)
                .build();
    }

}
