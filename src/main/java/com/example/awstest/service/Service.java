package com.example.awstest.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import jakarta.annotation.PostConstruct;
import org.apache.commons.io.FileUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * @author Neevels
 * @version 1.0
 * @date 03.11.2023 14:11
 */
@org.springframework.stereotype.Service
public class Service {

    private final AmazonS3 amazonS3;

    public Service(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    public void uploadToAWS() {
        String bucket = "tripmanagementbucket";
        List<Bucket> buckets = amazonS3.listBuckets();
        String html;
        try {
            html = getHtml();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        byte[] bytes = html.getBytes();
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(bytes.length);
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, "htmlStr", new ByteArrayInputStream(bytes), objectMetadata);
        amazonS3.putObject(putObjectRequest);
        //        try {
//            File file = new File("zadanie_na_komandirovku_novoe.docx");
//            amazonS3.putObject(bucket, "test", file);
//        } catch (AmazonServiceException e) {
//            System.out.println(e.getMessage());
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }
    }

    public String getFromAws() {
        S3Object s3Object = amazonS3.getObject(new GetObjectRequest("tripmanagementbucket", "htmlStr"));
        S3ObjectInputStream objectContent = s3Object.getObjectContent();

        // Прочитайте строку из объекта
        String retrievedContent = null;
        try {
            retrievedContent = new String(objectContent.readAllBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Выведите извлеченную строку
        System.out.println("Извлеченная строка: " + retrievedContent);

        // Закройте потоки и освободите ресурсы
        try {
            objectContent.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            s3Object.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return retrievedContent;
    }

    String getHtml() throws Exception {
        String emailMessage = new String(Files.readAllBytes(Paths.get("D:\\AWSTest\\src\\main\\resources\\templates\\doc.html")), StandardCharsets.UTF_8);
        emailMessage = emailMessage.replace("{{FIO}}", "Игнатович Илья Викторович");
        emailMessage = emailMessage.replace("{{POSITION}}", "инженер-программист");
        emailMessage = emailMessage.replace("{{COUNTRY}}", "Беларусь");
        emailMessage = emailMessage.replace("{{CITY}}", "Минск");
        emailMessage = emailMessage.replace("{{COMPANY_NAME}}", "MODSEN");
        emailMessage = emailMessage.replace("{{COUNT_OF_DAY}}", "5");
        emailMessage = emailMessage.replace("{{GOAL}}", "Получение новых навыков");
        emailMessage = emailMessage.replace("{{TASK}}", "Решение различных вопросов.");
        return emailMessage;
    }


}
