import React, { useEffect, useState } from "react";
import { useAuth } from "./AuthContext";
import { useNavigate, Navigate } from "react-router-dom";

const CheckSocialLogin = () => {
  const navigate = useNavigate();
  const apiUrl = process.env.REACT_APP_CORE_API_BASE_URL;
  const frontUrl = process.env.REACT_APP_CORE_FRONT_BASE_URL;
  const { login, logout } = useAuth();

  useEffect(() => {
    // 서버에 accessToken 검증 요청
    fetch(`${apiUrl}/api/member/checkAccessToken`, {
      method: "POST",
      credentials: "include",
      headers: {
        "Content-Type": "application/json",
      },
    })
      .then((response) => response.json())
      .then((data) => {
        // 서버 응답에서 resultCode와 LoginResponseDto 정보를 확인
        if (data.resultCode === "200") {
          // 로그인 성공 처리
          login();
          console.log("로그인 성공");
          localStorage.setItem("username", data.data.username); // 응답에서 username 추출
          localStorage.setItem("isLogin", "true");
          navigate("/", { replace: true }); // 홈으로 리디렉션
        } else {
          // 로그인 실패 처리 (유효하지 않은 토큰, 토큰 만료 등)
          console.log(data.message); // 서버에서 보낸 오류 메시지 출력
          logout(); // 로그아웃 처리
        }
      })
      .catch((error) => {
        console.error("에러 발생:", error);
        logout(); // 네트워크 오류 등의 이유로 로그아웃 처리
      });
  }, []);

  return (
    <div>
      <h1>Checking Social Login...</h1>
      {/* 추가적인 UI 요소나 메시지 */}
    </div>
  );
};

export default CheckSocialLogin;
