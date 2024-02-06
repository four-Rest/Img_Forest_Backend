import React, { useState } from "react";

const SignupModal = ({ showModal, setShowModal }) => {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const apiUrl = process.env.REACT_APP_CORE_API_BASE_URL;
  const handleLogin = (e) => {
    e.preventDefault();
    // 여기에 로그인 로직 구현
    console.log(username, password);
    setShowModal(false); // 로그인 시도 후 모달 닫기
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
        회원가입
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
              <h3 className="font-bold text-3xl text-center mb-4">회원가입</h3>
              <div className="form-control w-full mb-4 flex flex-col items-center">
                <label className="label w-full max-w-md">
                  <span className="label-text">ID</span>
                </label>
                <input
                  type="text"
                  placeholder="ID를 입력해주세요."
                  className="input input-bordered w-full max-w-md"
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
                />
              </div>
              <div className="form-control w-full mb-4 flex flex-col items-center">
                <label className="label w-full max-w-md">
                  <span className="label-text">비밀번호 확인</span>
                </label>
                <input
                  type="password"
                  placeholder="비밀번호를 확인해주세요."
                  className="input input-bordered w-full max-w-md"
                />
              </div>
              <div className="form-control w-full mb-4 flex flex-col items-center">
                <label className="label w-full max-w-md">
                  <span className="label-text">이메일</span>
                </label>
                <input
                  type="email"
                  placeholder="이메일를 입력해주세요."
                  className="input input-bordered w-full max-w-md"
                />
              </div>
              <div className="form-control w-full mb-4 flex flex-col items-center">
                <label className="label w-full max-w-md">
                  <span className="label-text">닉네임</span>
                </label>
                <input
                  type="text"
                  placeholder="닉네임을 입력해주세요."
                  className="input input-bordered w-full max-w-md"
                />
              </div>

              <div className="modal-action flex justify-center w-full">
                <button
                  type="submit"
                  className="btn btn-primary w-full max-w-xs"
                >
                  회원가입
                </button>
              </div>
              <div className="modal-action flex justify-center w-full">
                <button
                  type="button"
                  className="btn btn-primary w-full max-w-xs"
                  onClick={handleKakaoLogin}
                >
                  카카오 로그인
                </button>
              </div>
            </form>
          </div>
          <div
            className="modal-overlay"
            onClick={() => setShowModal(false)}
          ></div>
        </div>
      )}
    </>
  );
};

export default SignupModal;
