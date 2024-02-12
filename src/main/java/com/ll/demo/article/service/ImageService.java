package com.ll.demo.article.service;

import com.ll.demo.article.entity.Article;
import com.ll.demo.article.entity.Image;
import com.ll.demo.article.repository.ArticleRepository;
import com.ll.demo.article.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ImageService {

    private final ImageRepository imageRepository;

    @Transactional
    public String setImagePath() {

        //현재 날짜를 폴더 이름으로 지정
        LocalDateTime createdTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDateTime = createdTime.format(formatter);

        //저장 디렉토리 결정
        String projectPath = System.getProperty("user.dir") + "\\react_front\\public\\imgFiles\\%s".formatted(formattedDateTime);
        String os = System.getProperty("os.name").toLowerCase();
        if (!os.contains("win")) {
            projectPath = System.getProperty("user.dir") + "/react_front/public/imgFiles/%s".formatted(formattedDateTime);
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
    public void delete(Image image) throws IOException {

        String projectPath = image.getPath();

        String fileName = image.getFileName();
        Path filePath = Paths.get(projectPath, fileName);

        deleteFile(filePath);

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
        image.setFileName(newFileName);

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

    public Image getImageByFileName(String imageName) {
        return imageRepository.getImageByFileName(imageName);
    }
}