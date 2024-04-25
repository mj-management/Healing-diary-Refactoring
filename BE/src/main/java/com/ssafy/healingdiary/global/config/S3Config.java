package com.ssafy.healingdiary.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class S3Config {
//    @Value("${cloud.aws.credentials.accessKey}")
//    public static String accessKey;
//
//    @Value("${cloud.aws.credentials.secretKey}")
//    public static String secretKey;

    @Value("${cloud.aws.s3.bucket}")
    public String bucket;

    @Value("${cloud.aws.region.static}")
    public String region;
}
