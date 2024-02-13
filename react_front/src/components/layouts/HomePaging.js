import React, { useState, useEffect, useRef } from 'react';
import './styles.css'; 

function HomePaging() { 
  
  const [articleData, setArticleData] = useState([]); 
  const [loading, setLoading] = useState(false); 
  const [pageNo, setPageNo] = useState(0);
  const [pageSize,setPageSize] = useState(10);
  const [totalPages, setTotalPages] = useState(0);
  const apiBaseUrl = process.env.REACT_APP_CORE_API_BASE_URL;

  const target = useRef(null); // IntersectionObserver를 위한 ref 생성

  useEffect(() => {
  
    const fetchData = async () => {
      setLoading(true); 
      try {
        const res = await fetch(`/api/article?pageNo=${pageNo}&pageSize=${pageSize}`); 
        const data = await res.json(); 
        console.log(data.data);
        setArticleData(prevData => prevData.concat(data.data)); // 기존 이미지 데이터에 새로운 데이터를 추가
        setTotalPages(data.totalPages);

      } catch (error) {
        console.error('Error fetching article data:', error); 
      }
      setLoading(false); 
    };
    fetchData(); 
  }, [apiBaseUrl, pageNo, pageSize]); 

  useEffect(() => {
    // useEffect를 사용하여 target 요소의 가시성을 감시하고 스크롤 시 추가 데이터 로드
    const observer = new IntersectionObserver(
      async(entries) => {
        if (entries[0].isIntersecting && !loading && pageNo < totalPages -1) { // target 요소가 화면에 나타났고 로딩 중이 아닌 경우
         setPageNo(prevPageNo => prevPageNo + 1);
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
  }, [loading, pageNo, totalPages]); 
  return (
    <div className="container">
       {articleData.map((article, index) => (
        <div key={index} className="box">
          <img src={`imgFiles/${article.imgFilePath}/${article.imgFileName}`} alt="" />
        </div>
      ))}
      <div ref={target}></div> 
    </div>
  );
}

export default HomePaging; 
