package com.ll.demo.payment.service;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.ll.demo.payment.enums.IamportApiURL;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;


@Transactional
@Slf4j
@Service
@RequiredArgsConstructor
public class RefundService {

    public String getToken(String restApiKey, String restApiSecret) throws IOException {
        // 결제 취소 URL 설정
        URL url = new URL(IamportApiURL.CANCEL_URL.getURL());
        // HTTP 연결 생성
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");


        // 요청의 Content-Type과 Accept 헤더 설정
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Accept", "application/json");

        // 해당 연결을 출력 스트림(요청)으로 사용
        conn.setDoOutput(true);

        // JSON 객체에 엑세스 토큰 발급에 필요한 데이터 추가
        JsonObject json = new JsonObject();
        json.addProperty("imp_key", restApiKey);
        json.addProperty("imp_secret", restApiSecret);

        // 출력 스트림으로 해당 연결에 요청 전송
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
        bw.write(json.toString());
        bw.flush();
        bw.close();

        // 입력 스트림으로 엑세스 토큰 발급 요청에 대한 응답 처리
        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        Gson gson = new Gson();
        String response = gson.fromJson(br.readLine(), Map.class).get("response").toString();
        String accessToken = gson.fromJson(response, Map.class).get("access_token").toString();
        br.close();

        // HTTPS 연결 종료
        conn.disconnect();

        log.info("Iamport 엑세스 토큰 발급 성공 : {}", accessToken);
        return accessToken;
    }

    public void refundWithToken(String token, String orderNumber, String message) throws IOException {

        URL url = new URL(IamportApiURL.CANCEL_URL.getURL());
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        // 요청 방식을 POST로 설정
        conn.setRequestMethod("POST");

        // 요청의 Content-Type, Accept, Authorization 헤더 설정
        conn.setRequestProperty("Content-type", "application/json");
        conn.setRequestProperty("Accept", "application/json");
        conn.setRequestProperty("Authorization", token);

        // 해당 연결을 출력 스트림(요청)으로 사용
        conn.setDoOutput(true);

        // JSON 객체에 해당 API가 필요로하는 데이터 추가.
        JsonObject json = new JsonObject();
        json.addProperty("orderNumber", orderNumber);
        json.addProperty("message", message);


        // 출력 스트림으로 해당 conn에 요청
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
        bw.write(json.toString());
        bw.flush();
        bw.close();

        // 입력 스트림으로 conn 요청에 대한 응답 반환
        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        br.close();
        conn.disconnect();

        log.info("결제 취소 완료 : 주문 번호 {}", orderNumber);
    }
}
