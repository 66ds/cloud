package com.qianbing.file.component;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@ConfigurationProperties(prefix = "spring.oos")
@Component
@Data
public class OosComponent {

    private  String endPoint;

    private  String accessKey;

    private  String secretKey;

    private  String bucket;


    public String upload(MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID() + "-" + file.getOriginalFilename();
        OSS ossClient = new OSSClientBuilder().build(getEndPoint(), getAccessKey(), getSecretKey());
        ossClient.putObject(getBucket(), fileName, file.getInputStream());
        ossClient.shutdown();
        return "https://" + getBucket() + "." + getEndPoint() + "/" + fileName;
    }
}
