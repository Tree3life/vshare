package com.tree3.utils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class OSSUtils {

    private static String ACCESSKEYID = "xxxxxxxxxxxxxxx";
    private static String SECRET = "xxxxxxxxxxxx";
    private static String BUCKET_NAME = "xxxxxxxxxx";
    private static String ENDPOINT = "oss-cn-hangzhou.xxxxxxxxxxxxxx.com";
    private static String ENDPOINT_URL = "http://xxxxxxxxxxxxxx.aliyuncs.com";


    /**
     * 上传文件
     */
    public static String upload(File file, String path, String fileName) throws FileNotFoundException {
        return upload(new FileInputStream(file), path, fileName);
    }

    /**
     * 上传文件
     *
     * @param inputStream
     * @return
     */
    public static String upload(InputStream inputStream, String path, String fileName) {
        // Endpoint以杭州为例，其它Region请按实际情况填写。
        // 云账号AccessKey有所有API访问权限，建议遵循阿里云安全最佳实践，创建并使用RAM子账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建。
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(ENDPOINT_URL, ACCESSKEYID, SECRET);
        // 上传文件流。
        String key = path + "/" + fileName;
        ossClient.putObject(BUCKET_NAME, key, inputStream);
        // 关闭OSSClient。
        ossClient.shutdown();
        return "https://" + BUCKET_NAME + "." + ENDPOINT + "/" + key;
    }

}
