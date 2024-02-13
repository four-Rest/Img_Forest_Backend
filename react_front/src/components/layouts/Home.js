import React, { useState, useEffect, useRef } from 'react';
import './styles.css';

function Home() {

  const [articleData, setArticleData] = useState([]); // 이미지 데이터 배열
  const [loading, setLoading] = useState(false); // 로딩 상태
  const [startIndex, setStartIndex] = useState(0); // 표시할 이미지의 시작 인덱스
  const [endIndex, setEndIndex] = useState(30); // 표시할 이미지의 마지막 인덱스
  const apiBaseUrl = process.env.REACT_APP_CORE_API_BASE_URL; // 환경 변수에서 API 기본 URL을 가져옴

  const target = useRef(null); // IntersectionObserver를 위한 ref 생성

  useEffect(() => {
    // useEffect를 사용하여 컴포넌트가 마운트되었을 때, 데이터를 가져오는 비동기 함수 호출
    const fetchData = async () => {
      setLoading(true); // 로딩 상태를 true로 설정
      try {
        const res = await fetch(`${apiBaseUrl}/api/article`); // API에서 이미지 데이터를 가져옴
        const data = await res.json(); // 응답 데이터를 JSON 형식으로 변환
        setArticleData(prevData => prevData.concat(data.data)); // 기존 이미지 데이터에 새로운 데이터를 추가
        console.log('데이터 개수:', data.data.length); // 데이터 개수를 콘솔에 출력

      } catch (error) {
        console.error('Error fetching article data:', error); // 오류가 발생하면 콘솔에 오류 메시지 출력
      }
      setLoading(false); // 로딩 상태를 false로 설정
    };
    fetchData(); // fetchData 함수 호출
  }, [apiBaseUrl]); // apiBaseUrl이 변경될 때만 useEffect 실행

  useEffect(() => {
    // useEffect를 사용하여 target 요소의 가시성을 감시하고 스크롤 시 추가 데이터 로드
    const observer = new IntersectionObserver(
        async(entries) => {
          if (entries[0].isIntersecting && !loading) { // target 요소가 화면에 나타났고 로딩 중이 아닌 경우
            setLoading(true); // 로딩 상태를 true로 설정
            setEndIndex(prevEndIndex => Math.max(prevEndIndex + 10, Math.min(prevEndIndex + 10, articleData.length))); // endIndex를 업데이트하여 추가 이미지를 표시
            //setEndIndex(prevEndIndex => Math.min(prevEndIndex + 10, articleData.length));
            // 현재 ArticleData.length=45, 어떨때는 40개 렌더링 어떨때는 50개 렌더링
          }
        }, { threshold: 0 }); // IntersectionObserver 옵션 설정

    if (!loading && target.current) { // 로딩 중이 아니고 target이 현재 존재하는 경우
      observer.observe(target.current); // IntersectionObserver를 target에 등록하여 가시성 감시
    }

    return () => {
      if (target.current) {
        observer.unobserve(target.current); // 컴포넌트 언마운트 시 IntersectionObserver에서 target 제거
      }
    };
  }, [loading, articleData]); // loading 또는 articleData가 변경될 때만 useEffect 실행

  return (
      <div className="container">
        {articleData.slice(startIndex, endIndex).map((article) => (
            <div key={article.id} className="box">
              <img src={`${apiBaseUrl}}/api/article/image/${article.imgFileName}`} alt="" />
            </div>
        ))}
        <div ref={target}></div>
      </div>
  );
}

export default Home; // Home 컴포넌트를 내보냄