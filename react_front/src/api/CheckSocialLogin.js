import React, { useEffect,useState } from 'react';
import { useAuth } from "./AuthContext";
import { useNavigate, Navigate } from 'react-router-dom';

const CheckSocialLogin = () => {
    const navigate = useNavigate();
    const apiUrl = process.env.REACT_APP_CORE_API_BASE_URL;
    const frontUrl = process.env.REACT_APP_CORE_FRONT_BASE_URL;
    const { login, logout } = useAuth();

    useEffect(() => {
            // 서버에 accessToken 검증 요청
            fetch(`${apiUrl}/api/member/checkAccessToken`, {
                method: 'POST',
                credentials: 'include',
                headers: {
                    'Content-Type': 'application/json'
                }
            })
            .then(response => response.json())
            .then(data => {
                if (data === true) {
                    login();
                    localStorage.setItem('isLogin', 'true');
                    navigate("/", { replace: true });
                } else {
                    console.log('유효하지 않은 토큰입니다.');
                    //TODO: refreshToken 검사
                    logout();
                }
            })
            .catch(error => {
                console.error('에러 발생 :', error);
                logout();                
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
