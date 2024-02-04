import logo from './logo.svg';
import '../App.css';
import React from 'react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

const apiUrl = process.env.REACT_APP_CORE_API_BASE_URL;
const frontUrl = process.env.REACT_APP_CORE_FRONT_BASE_URL;
const Navbar = () => {
    const [isLogin, setIsLogin] = useState(false);
  
    useEffect(() => {
      setIsLogin(!!localStorage.getItem('nickname'));
    }, []);
  
    const logoutProcess = async () => {
      await logout();
      toastNotice('로그아웃 되었습니다.'); 

return (
    <div className="navbar bg-base-100">
      <div className="navbar-start">
        <div className="dropdown">
          <div tabIndex="-1" role="button" className="btn btn-ghost btn-circle">
            <FontAwesomeIcon icon={faBars} />
          </div>
          <ul tabIndex="0" className="menu menu-sm dropdown-content mt-3 z-[1] p-2 shadow bg-base-100 rounded-box w-52">
            <li><Link to="${frontUrl}/article"><FontAwesomeIcon icon={faRectangleList} /> 글 목록</Link></li>
            {isLogin ? (
              <>
                <li><Link className="nav-link" to="${frontUrl}/member/mypage"><FontAwesomeIcon icon={faAddressCard} /> 마이페이지</Link></li>
                <li><button className="nav-link" onClick={logoutProcess}><FontAwesomeIcon icon={faDoorClosed} />로그아웃</button></li>
              </>
            ) : (
              <>
                <li><Link className="nav-link" to="${frontUrl}/member/signup"><FontAwesomeIcon icon={faUserPlus} /> 회원가입</Link></li>
                <li><Link className="nav-link" to="${frontUrl}/member/login"><FontAwesomeIcon icon={faDoorOpen} /> 로그인</Link></li>
              </>
            )}
          </ul>
        </div>
      </div>
      <div className="navbar-center">
        <Link className="btn btn-ghost text-xl" to="/">Img_Forest</Link>
      </div>
      <div className="navbar-end flex items-center">
        <div tabIndex="-1" role="button" className="btn btn-ghost btn-circle">
          <FontAwesomeIcon icon={faMagnifyingGlass} />
        </div>
      </div>
    </div>
  );
};


export default header;