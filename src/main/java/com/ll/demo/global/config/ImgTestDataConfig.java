package com.ll.demo.global.config;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.ll.demo.article.dto.ArticleRequestDto;
import com.ll.demo.article.service.ArticleService;
import com.ll.demo.global.app.AppConfig;
import com.ll.demo.global.service.SystemService;
import com.ll.demo.member.dto.MemberCreateRequestDto;
import com.ll.demo.member.entity.Member;
import com.ll.demo.member.service.MemberService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.IntStream;

@Transactional
@Configuration
public class ImgTestDataConfig {

    @Bean
    public ApplicationRunner initImgTestData(
            SystemService systemService,
            MemberService memberService,
            ArticleService articleService,
            AmazonS3 s3,
            S3Util s3Util
    ) {
        return args -> {
            if (systemService.testDataCreated()) return;

            //테스트용 사용자 생성
            MemberCreateRequestDto memberCreateRequestDto = new MemberCreateRequestDto();
            memberCreateRequestDto.setUsername("testUserForImgTest");
            memberCreateRequestDto.setPassword1("1234");
            memberCreateRequestDto.setPassword2("1234");
            memberCreateRequestDto.setEmail("test@email.email");
            memberCreateRequestDto.setNickname("testUserForImgTest");
            memberService.signup(memberCreateRequestDto);


            //테스트데이터 사용자생성
            Member member = memberService.findByUsername("testUserForImgTest");

            //이미지파일 이름 설정
            String[] fileNames = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o"};

            //OS에 따른 path 설정
            String imgSourcePath = System.getProperty("user.dir") + "\\src\\main\\resources\\";
            String os = System.getProperty("os.name").toLowerCase();
            if (!os.contains("win")) {
                imgSourcePath = System.getProperty("user.dir") + "/src/main/resources/";
            }

            //테스트데이터용 이미지 다운로드
            String finalImgSourcePath = imgSourcePath;
            Arrays.stream(fileNames)
                    .forEach(fileName -> {
                        try {
                            S3Object s3Object = s3.getObject(s3Util.getBucketName(), "testImg/" + fileName + ".jpeg");
                            S3ObjectInputStream s3ObjectInputStream = s3Object.getObjectContent();
                            String downloadFilePath = finalImgSourcePath + fileName + ".jpeg";

                            OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(downloadFilePath));
                            byte[] bytesArray = new byte[4096];
                            int bytesRead = -1;
                            while ((bytesRead = s3ObjectInputStream.read(bytesArray)) != -1) {
                                outputStream.write(bytesArray, 0, bytesRead);
                            }

                            outputStream.close();
                            s3ObjectInputStream.close();
                        } catch (AmazonS3Exception e) {
                            e.printStackTrace();
                        } catch(SdkClientException e) {
                            e.printStackTrace();
                        } catch (FileNotFoundException e) {
                            throw new RuntimeException(e);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                            }
                    );

            //객체화, article 데이터 생성
            for (int i = 0; i < 45; i++) {

                //테스트이미지 파일을 테스트용 multipartFile로 불러옴

                Path path = Paths.get(imgSourcePath + "%s.jpeg".formatted(fileNames[i % 15]));
                String name = "%s.jpeg".formatted(fileNames[i % 15]);
                String originalFilename = "%s.jpeg".formatted(fileNames[i % 15]);
                String contentType = "image/jpeg";
                byte[] content = null;
                try {
                    content = Files.readAllBytes(path);
                } catch (final IOException e) {
                }

                MultipartFile multipartFile = new MockMultipartFile(name,
                        originalFilename, contentType, content);
                ArticleRequestDto articleRequestDto = new ArticleRequestDto();
                articleRequestDto.setContent("content of test article %d".formatted(i));
                articleRequestDto.setTagString("test example image %d".formatted(i));
                articleRequestDto.setMultipartFile(multipartFile);

                articleService.create(articleRequestDto, member);
            }

            //로컬에 저장된 테스트이미지 삭제
            Arrays.stream(fileNames)
                    .forEach(fileName -> {
                        File file = new File(finalImgSourcePath + fileName + ".jpeg");
                        file.delete();
                    });
        };
    }
}