package com.ssafy.healingdiary.infra.storage;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.ssafy.healingdiary.global.config.S3Config;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class S3StorageClient implements StorageClient {

    private final S3Config s3Config;
    private AmazonS3 s3Client;


    @PostConstruct
    @Profile({"prod"})
    public void setS3Client() {
//        AWSCredentials credentials = new BasicAWSCredentials(S3Config.accessKey, S3Config.secretKey);

        s3Client = AmazonS3ClientBuilder.standard()
//            .withCredentials(new AWSStaticCredentialsProvider(credentials))
            .withRegion(s3Config.region)
            .build();
    }

    @Override
    public String uploadFile(MultipartFile file) throws IOException {
        if (file != null && !file.isEmpty()) {
            String fileName = String.valueOf(UUID.randomUUID());
            InputStream inputStream = file.getInputStream();
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            s3Client.putObject(new PutObjectRequest(s3Config.bucket, fileName, inputStream, metadata)
                .withCannedAcl(CannedAccessControlList.PublicRead));
            return s3Client.getUrl(s3Config.bucket, fileName).toString();
        } else {
            return null;
        }
    }

    @Override
    public void deleteFile(String fileUrl) throws IOException {
        String key = fileUrl;
        try {
            if (fileUrl == null) {
                return;
            }
            s3Client.deleteObject(s3Config.bucket, (key).substring(54));
        } catch (AmazonServiceException e) {
            log.error(e.getErrorMessage());
            System.exit(1);
        }
    }





//    @Bean
//    @Profile({"local"})
//    @PostConstruct
//    public void localSetS3() {
//        AwsClientBuilder.EndpointConfiguration endpoint = new AwsClientBuilder.EndpointConfiguration(getUri(), s3Config.region);
//
//        s3Client= AmazonS3ClientBuilder
//                .standard()
//                .withPathStyleAccessEnabled(true)
//                .withEndpointConfiguration(endpoint)
//                .withCredentials(new AWSStaticCredentialsProvider(new AnonymousAWSCredentials()))
//                .build();
//
//    }

    private String getUri() {
        return UriComponentsBuilder.newInstance()
                .scheme("http")
                .host("localhost")
                .port(8081)
                .build()
                .toUriString();
    }
}
