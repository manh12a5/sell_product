package com.example.demo.s3;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "aws.s3.key")
@Getter
@Setter
public class S3Credential {

    private String accessKey;
    private String secretKey;

}
