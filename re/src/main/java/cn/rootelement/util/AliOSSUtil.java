package cn.rootelement.util;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.*;
import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.List;

/**
 * 阿里云 OSS 对象存储工具类
 *
 * @author ljt
 * @version 1.0.1
 * @date 2020/4/16
 */
@Component
@Data
@ToString
@ConfigurationProperties(prefix = "aliyun.oss")
@Slf4j
public class AliOSSUtil {

    /** 阿里云 oss endpoint **/
    private String endpoint;

    /** 阿里云oss 访问id **/
    private String accessKeyId;

    /** 阿里云oss 访问秘钥 **/
    private String accessKeySecret;

    //################################### 存储空间 ###################################//
    /**
     * 在oss中创建存储空间
     *
     * @param bucketName 存储空间的名字
     */
    public void createBucket(String bucketName) {
        OSS ossClient = new OSSClientBuilder()
                .build(endpoint, accessKeyId, accessKeySecret);
        // 创建存储空间。
        try {
            CreateBucketRequest createBucketRequest = new CreateBucketRequest(bucketName);
            Bucket bucket = ossClient.createBucket(createBucketRequest);
            if (null != bucket) {
                log.info("创建[{}]成功", bucketName);
            } else {
                log.error("创建[{}]失败", bucketName);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            // 关闭OSSClient。
            ossClient.shutdown();
        }
    }

    //################################### 文件上传 ###################################//

    public void upload() {

    }


    //################################### 文件下载 ###################################//
    /**
     * 下载文件到本地
     * @param bucketName 操作的bucket
     * @param objectName 文件全路径名  例如  a/b/c.jpg
     * @param localPath 下载到本地的路径
     */
    public void download(String bucketName, String objectName, String localPath) {
        OSS ossClient = new OSSClientBuilder()
                .build(endpoint, accessKeyId, accessKeySecret);
        try {
            // 下载OSS文件到本地文件。如果指定的本地文件存在会覆盖，不存在则新建。
            ossClient.getObject(new GetObjectRequest(bucketName, objectName), new File(localPath));
            log.info("文件[{}]下载成功", localPath);
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            // 关闭OSSClient。
            ossClient.shutdown();
        }
    }

    //################################### 管理文件 ###################################//

    /**
     * 判断文件是否存在
     * @param bucketName bucket名字
     * @param objectName 文件的全路径名 例如 a/b/c.png
     * @return 存在返回true，不存在返回false
     */
    public boolean isFileExist(String bucketName, String objectName) {
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder()
                .build(endpoint, accessKeyId, accessKeySecret);

        // 判断文件是否存在。doesObjectExist还有一个参数isOnlyInOSS，如果为true则忽略302重定向或镜像；如果为false，则考虑302重定向或镜像。
        boolean found = ossClient.doesObjectExist(bucketName, objectName, true);
        // 关闭OSSClient。
        ossClient.shutdown();
        return found;
    }

    /**
     * 根据前缀列出某个bucket中所有对象
     * @param bucketName bucket的名字
     * @param prefix 前缀
     * @return 返回 {@link OSSObjectSummary} 集合
     */
    public List<OSSObjectSummary> listObjects(String bucketName, String prefix) {
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        // 列举文件。 如果不设置KeyPrefix，则列举存储空间下所有的文件。KeyPrefix，则列举包含指定前缀的文件。
        ObjectListing objectListing = ossClient.listObjects(bucketName, prefix);
        List<OSSObjectSummary> sums = objectListing.getObjectSummaries();
        // 关闭OSSClient。
        ossClient.shutdown();
        return sums;
    }

    /**
     * 列出某个bucket中所有对象
     * @param bucketName bucket的名字
     * @return 返回 {@link OSSObjectSummary} 集合
     */
    public List<OSSObjectSummary> listObjects(String bucketName) {
        return listObjects(bucketName, null);
    }


    /**
     * 删除一个文件
     * @param bucketName bucket的名字
     * @param objectName 要删除文件的全路径名
     * @return 删除成功返回true，失败返回false
     */
    public boolean deleteObject(String bucketName, String objectName) {
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder()
                .build(endpoint, accessKeyId, accessKeySecret);
        try {
            // 删除文件。如需删除文件夹，请将ObjectName设置为对应的文件夹名称。如果文件夹非空，则需要将文件夹下的所有object删除后才能删除该文件夹。
            ossClient.deleteObject(bucketName, objectName);
            return true;
        } catch (Exception e) {
            log.error("删除{}失败, 原因：{}", objectName, e.getMessage());
            return false;
        } finally {
            // 关闭OSSClient。
            ossClient.shutdown();
        }
    }
}
