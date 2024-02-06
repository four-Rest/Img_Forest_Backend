/*esLint-disable */

import "../../App.css";
import React from "react";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { useState, useEffect } from "react";
import { toastNotice } from "../ToastrConfig";
import { Link } from "react-router-dom";
import {
  faBars,
  faRectangleList,
  faAddressCard,
  faDoorClosed,
  faUserPlus,
  faDoorOpen,
  faMagnifyingGlass,
} from "@fortawesome/free-solid-svg-icons";
import { useAuth } from "../../api/AuthContext";
import LoginModal from "../elements/LoginModal";
import SignupModal from "../elements/SignupModal";

const apiUrl = process.env.REACT_APP_CORE_API_BASE_URL;
const frontUrl = process.env.REACT_APP_CORE_FRONT_BASE_URL;

const Header = () => {
  const [showModal, setShowModal] = useState(false);

  const [showLoginModal, setShowLoginModal] = useState(false);
  const [showSignupModal, setShowSignupModal] = useState(false);

  const [isLogin, setIsLogin] = useState(false);
  const { logout } = useAuth();
  useEffect(() => {
    setIsLogin(!!localStorage.getItem("nickname"));
  }, []);

  const logoutProcess = async () => {
    await logout();
    toastNotice("로그아웃 되었습니다.");
  };

  const handleShowLoginModal = () => {
    setShowLoginModal(true);
    setShowSignupModal(false); // 회원가입 모달이 열려있을 수 있으므로 닫는다
  };

  const handleShowSignupModal = () => {
    setShowSignupModal(true);
    setShowLoginModal(false); // 로그인 모달이 열려있을 수 있으므로 닫는다
  };

  return (
    <>
      <div className="navbar bg-base-100">
        <div className="navbar-start">
          <div className="dropdown">
            <div
              tabIndex={0}
              role="button"
              className="btn btn-ghost btn-circle"
            >
              <svg
                xmlns="http://www.w3.org/2000/svg"
                className="h-5 w-5"
                fill="none"
                viewBox="0 0 24 24"
                stroke="currentColor"
              >
                <path
                  strokeLinecap="round"
                  strokeLinejoin="round"
                  strokeWidth="2"
                  d="M4 6h16M4 12h16M4 18h7"
                />
              </svg>
            </div>
            <ul
              tabIndex={0}
              className="menu menu-sm dropdown-content mt-3 z-[1] p-2 shadow bg-base-100 rounded-box w-52"
            >
              <li>
                <Link to={`/article`}>
                  <FontAwesomeIcon icon={faRectangleList} /> 글 목록
                </Link>
              </li>
              {isLogin ? (
                <>
                  <li>
                    <Link className="nav-link" to={`/member/mypage`}>
                      <FontAwesomeIcon icon={faAddressCard} /> 마이페이지
                    </Link>
                  </li>
                  <li>
                    <button className="nav-link" onClick={logoutProcess}>
                      <FontAwesomeIcon icon={faDoorClosed} />
                      로그아웃
                    </button>
                  </li>
                </>
              ) : (
                <>
                  <li>
                    <Link
                      className="nav-link"
                      onClick={() => setShowLoginModal(true)}
                    >
                      <FontAwesomeIcon icon={faDoorOpen} />
                      로그인
                    </Link>
                  </li>
                  <li>
                    <Link
                      className="nav-link"
                      onClick={() => setShowSignupModal(true)}
                    >
                      <FontAwesomeIcon icon={faDoorOpen} />
                      회원가입
                    </Link>
                  </li>
                </>
              )}
            </ul>
          </div>
        </div>
        <div className="navbar-center">
          <Link className="btn btn-ghost text-xl" to={`/home`}>
            Img_Forest
          </Link>
        </div>
        <div className="navbar-end">
          <button className="btn btn-ghost btn-circle">
            <svg
              xmlns="http://www.w3.org/2000/svg"
              className="h-5 w-5"
              fill="none"
              viewBox="0 0 24 24"
              stroke="currentColor"
            >
              <path
                strokeLinecap="round"
                strokeLinejoin="round"
                strokeWidth="2"
                d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"
              />
            </svg>
          </button>
          <button className="btn btn-ghost btn-circle">
            <div className="indicator">
              <svg
                xmlns="http://www.w3.org/2000/svg"
                className="h-5 w-5"
                fill="none"
                viewBox="0 0 24 24"
                stroke="currentColor"
              >
                <path
                  strokeLinecap="round"
                  strokeLinejoin="round"
                  strokeWidth="2"
                  d="M15 17h5l-1.405-1.405A2.032 2.032 0 0118 14.158V11a6.002 6.002 0 00-4-5.659V5a2 2 0 10-4 0v.341C7.67 6.165 6 8.388 6 11v3.159c0 .538-.214 1.055-.595 1.436L4 17h5m6 0v1a3 3 0 11-6 0v-1m6 0H9"
                />
              </svg>
              <span className="badge badge-xs badge-primary indicator-item"></span>
            </div>
          </button>
        </div>
      </div>
      <LoginModal showModal={showLoginModal} setShowModal={setShowLoginModal} />
      <SignupModal
        showModal={showSignupModal}
        setShowModal={setShowSignupModal}
      />
    </>
  );
};

export default Header;
