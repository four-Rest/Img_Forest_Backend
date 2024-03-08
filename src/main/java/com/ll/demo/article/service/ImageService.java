package com.ll.demo.article.service;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.ll.demo.article.entity.Article;
import com.ll.demo.article.entity.Image;
import com.ll.demo.article.repository.ArticleRepository;
import com.ll.demo.article.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ImageService {

    private final ImageRepository imageRepository;
    private final AmazonS3 s3;
    private final String bucketName = "img-forest-image";

    //S3 파일 목록 조회 테스트
    public List<String> getList() {


        List<String> fileList = new ArrayList<>();
        try {
            ListObjectsRequest listObjectsRequest = new ListObjectsRequest()
                    .withBucketName(bucketName)
                    .withDelimiter("/")
                    .withMaxKeys(300);

            ObjectListing objectListing = s3.listObjects(listObjectsRequest);

            for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
                fileList.add(objectSummary.getKey());
            }
            for (String commonPrefixes : objectListing.getCommonPrefixes()) {
                fileList.add(commonPrefixes);
            }
        } catch (AmazonS3Exception e) {
            e.printStackTrace();
        } catch(SdkClientException e) {
            e.printStackTrace();
        }

        return fileList;
    }

    @Transactional
    public String setImagePath() {

        //현재 날짜를 폴더 이름으로 지정
        LocalDateTime createdTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        String formattedDateTime = createdTime.format(formatter);

        //저장 디렉토리 결정
        String projectPath = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\imgFiles\\%s".formatted(formattedDateTime);
        String os = System.getProperty("os.name").toLowerCase();
        if (!os.contains("win")) {
            projectPath = System.getProperty("user.dir") + "/src/main/resources/static/imgFiles/%s".formatted(formattedDateTime);
        }

        //해당 날짜의 디렉토리가 존재하지 않으면 생성
        Path path = Paths.get(projectPath);
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
            } catch (Exception e) {
                //폴더 생성 실패
            }
        }
        return projectPath;
    }

    @Transactional
    public Image create(Article article, MultipartFile file) throws IOException {

        String projectPath = setImagePath();

        UUID uuid = UUID.randomUUID();

        String fileName = uuid + "_" + file.getOriginalFilename();

        File saveFile = new File(projectPath, fileName);

        file.transferTo(saveFile);

        Image image = Image.builder()
                .article(article)
                .fileName(fileName)
                .path(projectPath)
                .build();

        imageRepository.save(image);
        return image;
    }

    @Transactional
    public Image create2(Article article, MultipartFile multipartFile) throws IOException {

        //저장 경로, 파일 이름 설정
        String imgPath = setImagePath2();

        UUID uuid = UUID.randomUUID();

        String fileName = uuid + "_" + multipartFile.getOriginalFilename();

        //멀티파트 파일을 일반 파일로 전환
        File file = convertMultipartFileToFile(multipartFile);


        //Object storage에 업로드
        try {
            s3.putObject(new PutObjectRequest(bucketName,imgPath + "/" + fileName, file));
        } catch (AmazonS3Exception e) {
            e.printStackTrace();
        } catch(SdkClientException e) {
            e.printStackTrace();
        }

        //이미지 객체 생성
        Image image = Image.builder()
                .article(article)
                .fileName(fileName)
                .path(imgPath)
                .build();

        //저장
        imageRepository.save(image);

        //로컬에 생성된 파일 삭제
        file.delete();

        return image;

    }

    @Transactional
    public File convertMultipartFileToFile(MultipartFile file) throws IOException {
        File convertedFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
        FileOutputStream fos = new FileOutputStream(convertedFile);
        fos.write(file.getBytes());
        fos.close();
        return convertedFile;
    }


    @Transactional
    public String setImagePath2() {

        //현재 날짜를 폴더 이름으로 지정
        LocalDateTime createdTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        String path = createdTime.format(formatter);

        //path폴더가 있는지 확인
        String folderPath = path + "/";

        ListObjectsRequest listObjectsRequest = new ListObjectsRequest()
                .withBucketName(bucketName)
                .withPrefix(folderPath)
                .withDelimiter("/");

        ObjectListing objects = s3.listObjects(listObjectsRequest);

        List<String> commonPrefixes = objects.getCommonPrefixes();

        if (commonPrefixes.isEmpty()) {
            //path폴더가 없으면 생성
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(0L);
            objectMetadata.setContentType("application/x-directory");
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, path, new ByteArrayInputStream(new byte[0]), objectMetadata);

            try {
                s3.putObject(putObjectRequest);
            } catch (AmazonS3Exception e) {
                e.printStackTrace();
            } catch (SdkClientException e) {
                e.printStackTrace();
            }
        }
        return path;
    }

    @Transactional
    public void delete(Image image) throws IOException {

        String projectPath = image.getPath();

        String fileName = image.getFileName();
        Path filePath = Paths.get(projectPath, fileName);

        deleteFile(filePath);

        imageRepository.delete(image);
    }

    @Transactional
    public void delete2(Image image) {
        String path = image.getPath();
        String fileName = image.getFileName();

        try {
            s3.deleteObject(bucketName, path + "/" + fileName);
        } catch (AmazonS3Exception e) {
            e.printStackTrace();
        } catch(SdkClientException e) {
            e.printStackTrace();
        }

        imageRepository.delete(image);
    }

    @Transactional
    public void modify(Image image, MultipartFile file) throws IOException {

        String projectPath = setImagePath();

        //기존 이미지 파일 삭제
        String oldFileName = image.getFileName();

        Path oldFilePath = Paths.get(image.getPath(), oldFileName);

        deleteFile(oldFilePath);

        //새 이미지 파일 저장
        UUID uuid = UUID.randomUUID();

        String newFileName = uuid + "_" + file.getOriginalFilename();

        File saveFile = new File(projectPath, newFileName);

        file.transferTo(saveFile);

        //Image객체의 fileName을 새 이미지파일로 변경
        image.modifyFileName(newFileName);

    }

    @Transactional
    public void deleteFile(Path filePath) throws IOException {

        if (Files.exists(filePath)) {
            try {
                Files.delete(filePath);
            } catch (IOException e) {

            }
        }

    }
}