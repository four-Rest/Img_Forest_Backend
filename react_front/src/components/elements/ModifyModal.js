import React, { useEffect, useState } from "react";
import { toastNotice, toastWarning } from "../ToastrConfig";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  faComment
} from "@fortawesome/free-solid-svg-icons";

const ModifyModal = ({ showModal, setShowModal }) => {
    const [username, setUsername] = useState("");
    const [password1, setPassword1] = useState("");
    const [password2, setPassword2] = useState("");
    const [email, setEmail] = useState("");
    const [nickname, setNickname] = useState("");
    const apiUrl = process.env.REACT_APP_CORE_API_BASE_URL;

    useEffect(() => {
        if (showModal) {
            const fetchUserData = async () => {
                try {
                    const response = await fetch(`${apiUrl}/api/member/update`,{
                        method:"GET",
                        credentials: "include",
                    });
    
                    if(response.ok) {
                        const userData = await response.json();

                        setUsername(userData.data.loginId || "");
                        setEmail(userData.data.email || "");
                        setNickname(userData.data.nickname || "");
                    }
                    else {
                        toastWarning("회원정보를 불러오지 못했습니다.");
                    }
                }catch(error) {
                    console.error("Fetch User Data Error:", error);
                }
            };
            fetchUserData();
        }
    }, [apiUrl, showModal]);

    const modifyData = {
        username,
        password1,
        password2,
        email,
        nickname,
    };
    const handleModify = async (e) => {
        e.preventDefault();

        if (!passwordCheck()) {
        toastWarning("비밀번호가 일치하지 않습니다.");
        return;
        }
        if (!blankCheck()) {
        toastWarning("필수 정보를 모두 입력해주세요.");
        return;
        }

        try {
        const response = await fetch(`${apiUrl}/api/member/update`, {
            headers: {
            "Content-Type": "application/json",
            },
            method: "POST",
            credentials: "include",
            body: JSON.stringify(modifyData),
        });

        if (response.ok) {
            setShowModal(false); // 회원정보 수정 성공 후 모달 닫기
            toastNotice("정보 수정 완료.");
        } else {
            // 서버 에러 처리
            const errorData = await response.json();
            toastWarning("중복된 이름입니다.");
        }
        } catch (error) {
        console.error("Signup Error:", error);
        }
    };

    function passwordCheck() {
        return password1 === password2;
    }

    function blankCheck() {
        return (
        username.trim() &&
        password1.trim() &&
        password2.trim() &&
        email.trim() &&
        nickname.trim()
        );
    }

    if (!showModal) return null;

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
                <h3 className="font-bold text-3xl text-center mb-4">내 정보 수정</h3>
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
                    value={password1}
                    onChange={(e) => setPassword1(e.target.value)}
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
                    value={password2}
                    onChange={(e) => setPassword2(e.target.value)}
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
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
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
                    value={nickname}
                    onChange={(e) => setNickname(e.target.value)}
                    />
                </div>

                <div className="modal-action flex justify-center w-full">
                <button
                  type="submit"
                  className="btn btn-outline w-full max-w-xs"
                  onClick={handleModify}
                >
                  수정
                </button>
                </div>
                </form>
            </div>
            </div>
        )}
        </>
    );
};

export default ModifyModal;
