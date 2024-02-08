import React, { useEffect } from 'react';

const CheckSocialLogin = () => {
    const apiUrl = process.env.REACT_APP_CORE_API_BASE_URL;
    useEffect(() => {
        // URL에서 쿼리 파라미터 추출
        const queryParams = new URLSearchParams(window.location.search);
        const accessToken = queryParams.get('accessToken');
        const refreshToken = queryParams.get('refreshToken');

        if (accessToken && refreshToken) {
            // 서버에 accessToken 검증 요청
            fetch(`${apiUrl}/api/member/checkAccessToken`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    // 서버가 Bearer 토큰을 요구한다면, Authorization 헤더에 추가
                    'Authorization': `Bearer ${accessToken}`
                },
                body: JSON.stringify({ accessToken })
            })
            .then(response => response.json())
            .then(data => {
                if (data === true) {
                    // localStorage에 토큰 저장
                    localStorage.setItem('accessToken', accessToken);
                    localStorage.setItem('refreshToken', refreshToken);
                    console.log('유효한 토큰입니다.');
                } else {
                    console.log('유효하지 않은 토큰입니다.');
                }
            })
            .catch(error => {
                console.error('에러 발생 :', error);
            });
        } else {
            console.log("Tokens are not available in the URL.");
        }
    }, []);

    return (
        <div>
            <h1>Checking Social Login...</h1>
            {/* 추가적인 UI 요소나 메시지 */}
        </div>
    );
};

export default CheckSocialLogin;
