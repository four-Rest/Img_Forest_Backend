package com.ll.demo.global.config;

import com.ll.demo.article.dto.ArticleRequestDto;
import com.ll.demo.article.service.ArticleService;
import com.ll.demo.global.service.SystemService;
import com.ll.demo.member.dto.MemberCreateRequestDto;
import com.ll.demo.member.entity.Member;
import com.ll.demo.member.service.MemberService;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Transactional
@Configuration
public class ImgTestDataConfig {

    @Bean
    public ApplicationRunner initImgTestData(
            SystemService systemService,
            MemberService memberService,
            ArticleService articleService
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

            //테스트이미지 파일을 테스트용 multipartFile로 불러옴
            String imgSourcePath = System.getProperty("user.dir") + "\\react_front\\public\\imgFiles\\testImage.png";
            String os = System.getProperty("os.name").toLowerCase();
            if (!os.contains("win")) {
                imgSourcePath = System.getProperty("user.dir") + "/react_front/public/imgFiles/testImage.png";
            }

            Path path = Paths.get(imgSourcePath);
            String name = "testImage.png";
            String originalFilename = "testImage.png";
            String contentType = "image/png";
            byte[] content = null;
            try {
                content = Files.readAllBytes(path);
            } catch (final IOException e) {
            }

            MultipartFile multipartFile = new MockMultipartFile(name,
                    originalFilename, contentType, content);

            //테스트데이터 생성
            Member member = memberService.findByUsername("testUserForImgTest");
            for (int i = 1; i <= 10; i++) {
                ArticleRequestDto articleRequestDto = new ArticleRequestDto();
                articleRequestDto.setContent("content of test article %d".formatted(i));
                articleRequestDto.setTagString("test example image %d".formatted(i));
                articleRequestDto.setMultipartFile(multipartFile);

                articleService.create(articleRequestDto, member);
            }
        };
    }
}
