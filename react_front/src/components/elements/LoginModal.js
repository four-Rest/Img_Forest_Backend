import React, { useState } from "react";
import { toastNotice, toastWarning } from "../ToastrConfig";
import { useAuth } from "../../api/AuthContext";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  faComment
} from "@fortawesome/free-solid-svg-icons";

const LoginModal = ({ showModal, setShowModal }) => {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const apiUrl = process.env.REACT_APP_CORE_API_BASE_URL;

  const { login , logout } = useAuth();
  const signupData = {
    username,
    password,
  };

  const handleLogin = async (e) => {
    e.preventDefault();
    try {
      const response = await fetch(`${apiUrl}/api/member/login`, {
        headers: {
          "Content-Type": "application/json",
        },
        method: "POST",
        credentials: "include",
        body: JSON.stringify(signupData),
      });
      if (response.ok) {
        const res = await response.json(); // 응답이 성공적인 경우에만 JSON 파싱
        localStorage.setItem("username", res.data.username);
        localStorage.setItem("nickname", res.data.nickname);
        localStorage.setItem('isLogin', 'true');
      
        login();
        setShowModal(false); // 로그인 성공 후 모달 닫기
        console.log("로그인")
        toastNotice("로그인 완료.");
      } else {
        // 서버 에러 처리
        const errorData = await response.json();
        toastWarning("존재하지 않는 회원입니다.");
        logout();
      }
    } catch (error) {
      console.error("login Error:", error);
      logout();
    }
  };

  if (!showModal) return null;
  const handleKakaoLogin = () => {
    const kakaoLoginUrl = `${apiUrl}/oauth2/authorization/kakao`;

    // 사용자를 카카오 로그인 페이지로 리다이렉트
    window.location.href = kakaoLoginUrl;
  };
  return (
    <>
      <button className="btn" onClick={() => setShowModal(true)}>
        로그인
      </button>

      {showModal && (
        <div className="modal modal-open">
          <div className="modal-box">
            <form>
              <label
                htmlFor="login-modal"
                className="btn btn-sm btn-circle absolute right-2 top-2"
                onClick={() => setShowModal(false)}
              >
                ✕
              </label>
              <h3 className="font-bold text-3xl text-center mb-4">로그인</h3>
              <div className="form-control w-full mb-4 flex flex-col items-center">
                <label className="label w-full max-w-md">
                  <span className="label-text">ID</span>
                </label>
                <input
                  type="text"
                  placeholder="ID를 입력해주세요."
                  className="input input-bordered w-full max-w-md"
                  value={username}
                  onChange={(e) => setUsername(e.target.value)}
                />
              </div>
              <div className="form-control w-full mb-4 flex flex-col items-center">
                <label className="label w-full max-w-md">
                  <span className="label-text">비밀번호</span>
                </label>
                <input
                  type="password"
                  placeholder="비밀번호를 입력해주세요."
                  className="input input-bordered w-full max-w-md"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                />
              </div>

              <div className="modal-action flex justify-center w-full">
                <button
                  type="submit"
                  className="btn btn-outline w-full max-w-xs"
                  onClick={handleLogin}
                >
                  로그인
                </button>
              </div>
              <div className="modal-action flex justify-center w-full">
                <button
                  type="button"
                  className="btn btn-warning w-full max-w-xs"
                  onClick={handleKakaoLogin}
                >
                  <FontAwesomeIcon icon={faComment} />
                  카카오 로그인
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </>
  );
};

export default LoginModal;
