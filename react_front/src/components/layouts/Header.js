/*esLint-disable */

import "../../App.css";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import React,{ useContext, useState, useEffect, useRef } from "react";
import { toastNotice } from "../ToastrConfig";
import { Link, useNavigate } from "react-router-dom";
import { SearchTagContext}  from "../../api/SearchTagContext";
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

const Header = () => {
  const [searchTag,setSearchTag] = useState('');
  const {updateSearchTag}  = useContext(SearchTagContext);
  const [iconVisible, setIconVisible] = useState(true); // 돋보기 svg를 위한 변수
  const [searchVisible, setSearchVisible] = useState(false); // 검색창
  const [showLoginModal, setShowLoginModal] = useState(false);//로그인을 위한 변수
  const [showSignupModal, setShowSignupModal] = useState(false);//회원가입을 위한 변수
  const { isLogin, logout , login} = useAuth(); // AuthContext
  const searchRef = useRef(null);// 입력 필드에 대한 참조
  const navigate = useNavigate();

  const apiUrl = process.env.REACT_APP_CORE_API_BASE_URL;
  const frontUrl = process.env.REACT_APP_CORE_FRONT_BASE_URL;

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

  const handleButtonClick = () => {
    setIconVisible(false);
    setSearchVisible(true);
  };

  const handleInputChange = (e) => {
    setSearchTag(e.target.value);
  };

  const handleKeyDown = (e) => {
    if(e.key === 'Enter') {
      updateSearchTag({tag:searchTag});
      navigate(`/article/${searchTag}`);
    }
  };

  useEffect(() => {
    function handleClickOutside(event) {
      if (searchRef.current && !searchRef.current.contains(event.target)) {
        setSearchVisible(false);
      }
    }
    // 입력 필드가 표시될 때만 이벤트 리스너를 추가
    if (searchVisible) {
      document.addEventListener("mousedown", handleClickOutside);
    }
    return () => {
      // 컴포넌트 정리 시 이벤트 리스너 제거
      document.removeEventListener("mousedown", handleClickOutside);
    };
  }, [searchVisible]);

  useEffect(() => {
    if(localStorage.getItem('isLogin')){
      fetch(`${apiUrl}/api/member/checkAccessToken`, {
        method: 'POST',
        credentials: 'include',
        headers: {
            'Content-Type': 'application/json',
        }
    })
    .then(response => response.json())
    .then(data => {
        if (data === true) {
          console.log("유효!!!!");
          login();
        } else {
            console.log('유효하지 않은 토큰입니다.');
            logout();
        }
    })
    .catch(error => {
        console.error('에러 발생 :', error);
    });
    }  
    
  }, []); 

  return (
    <>
      <div className="navbar bg-base-100" style={{ display: 'flex', justifyContent: 'flex-end' }}>
        <div className="navbar-start">
          <div className="dropdown">
            <div
              tabIndex={0}
              role="button"
              className="btn btn-ghost btn-circle"
            >
              <svg
                xmlns="http://www.w3.org/2000/svg" 
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
          <Link className="btn btn-ghost text-xl" to={`/`}>
            Img_Forest
          </Link>
        </div>
        <div className="navbar-end">
          {/* 검색버튼 있는곳  */}
          
            {!searchVisible && (
              <button className="btn btn-ghost btn-circle" onClick={handleButtonClick}><svg
              xmlns="http://www.w3.org/2000/svg"
              className={`h-5 w-5`}
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
            </svg></button>
              
            )}
            {searchVisible && (
                <div className = "search-wrapper" ref={searchRef}> 
                  <input 
                  type="text" 
                  placeholder ="Type here" 
                  className="input input-bordered w-170 max-w-xs"
                  value = {searchTag}
                  onChange={handleInputChange}
                  onKeyDown={handleKeyDown}
                  />
                </div>
            )}
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