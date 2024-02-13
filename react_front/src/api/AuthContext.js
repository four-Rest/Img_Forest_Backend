import React, { createContext, useContext, useState } from "react";
import cookie from 'react-cookies';
const AuthContext = createContext();

export const useAuth = () => useContext(AuthContext);

export const AuthProvider = ({ children }) => {
  const [isLogin, setIsLogin] = useState(false);
  const apiUrl = process.env.REACT_APP_CORE_API_BASE_URL;
  const login = () => setIsLogin(true);

  function deleteCookie(name) {
    cookie.remove(name, { path: '/' },1000);
}

  const logout = () => {
    localStorage.removeItem("username");
    localStorage.removeItem("nickname");
    localStorage.removeItem("isLogin");
    fetch(`${apiUrl}/api/member/logout`, {
      method: 'POST',
      credentials: 'include',
      headers: {
          'Content-Type': 'application/json',
      }
  });
    setIsLogin(false)
    window.location.href = `/`;
    ;
  };

  function getTokenExpiry(token) {
    const payload = token.split('.')[1]; // 토큰의 두 번째 부분(payload)를 추출
    const decodedPayload = atob(payload); // Base64Url 디코드
    const payloadObj = JSON.parse(decodedPayload); // JSON 문자열을 객체로 변환
    const expiry = payloadObj.exp; // exp 필드 값 추출
  
    return new Date(expiry * 1000); // JavaScript Date 객체로 변환 (밀리초 단위로 변환 필요)
  }

  
  return (
    <AuthContext.Provider value={{ isLogin, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
};
