/*esLint-disable */
import './App.css'; // 스타일 시트
import logo from './logo.svg';
import './App.css';
import React from 'react';
import { toastNotice } from './components/ToastrConfig';
import { AuthProvider } from './api/AuthContext';
import { Header,Footer } from '@/components/layout';
import { BrowserRouter as Router } from 'react-router-dom';


function App() {

  const [articles, setArticles] = useState([]); // 기사 데이터를 저장할 상태
  const apiUrl = process.env.REACT_APP_CORE_API_BASE_URL;


  // Masonry 레이아웃을 초기화합니다.
  useEffect(() => {
    const grid = document.querySelector('.grid-1');
    if (grid) {
      const msnry = new Masonry(grid, {
        itemSelector: '.grid-item',
      });

      // 윈도우 크기가 변경될 때 레이아웃을 재배치합니다.
      const handleResize = () => msnry.layout();
      window.addEventListener('resize', handleResize);

      // 컴포넌트가 언마운트 될 때 이벤트 리스너를 제거합니다.
      return () => window.removeEventListener('resize', handleResize);
    }
  }, []);

  useEffect(() => {
    async function loadArticles() {
      try {
        // Template literal 사용 시 중괄호 밖에 $를 사용합니다.
        const response = await fetch(`/article`);
        const data = await response.json();
        setArticles(data.data);
      } catch (error) {
        console.error("Failed to fetch articles:", error);
      }
    }
  
    loadArticles();
  }, []);
  // async function handleLogout() {
  //   // 로그아웃 로직 구현
  //   toastNotice('로그아웃 성공'); // Toast 알림을 표시
  //   setLoginNickname(''); // 닉네임 상태를 비웁니다.
  // }

  return (
    <AuthProvider>
      <Router>
        <Header/>
        <section className="main-wrapper flex flex-row w-full">
      <div className="main-container grid">
        {articles.length === 0 ? (
          <p>이미지를 불러오는 중입니다.</p>
        ) : (
          <ul className="grid-1">
            {articles.map((article, index) => (
              <div
                key={index}
                className="grid-item float-left w-[100%] sm:w-[calc(100%/2)] md:w-[calc(100%/3)] lg:w-[calc(100%/4)] p-5"
              >
                <li>
                  <p>{article.id}</p>
                  <img src={`${article.path}/${article.imgFileName}`} alt="" />
                </li>
              </div>
            ))}
          </ul>
        )}
      </div>
    </section>
        <Footer/>
      </Router>
    </AuthProvider>
  );
}

export default App;