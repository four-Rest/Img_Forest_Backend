import React, { useState, useEffect, useRef } from 'react';
import './styles.css';

function Home() {
  const [articleData, setArticleData] = useState([]);
  const [loading, setLoading] = useState(false);
  const [startIndex, setStartIndex] = useState(0);
  const [endIndex, setEndIndex] = useState(10);
  const apiBaseUrl = process.env.REACT_APP_CORE_API_BASE_URL;

  const target = useRef(null);

  useEffect(() => {
    const fetchData = async () => {
      setLoading(true);
      try {
        const res = await fetch(`/api/article`);
        const data = await res.json();
        setArticleData(data.data);
      } catch (error) {
        console.error('Error fetching article data:', error);
      }
      setLoading(false);
    };
    fetchData();
  }, [apiBaseUrl]);

  useEffect(() => {
    const observer = new IntersectionObserver((entries) => {
      if (entries[0].isIntersecting && !loading) {
        setLoading(true);
        setEndIndex(prevEndIndex => prevEndIndex + 10); // 스크롤이 끝에 도달하면 endIndex를 증가시켜 새로운 데이터를 불러옴
      }
    }, { threshold: 1 });

    if (!loading && target.current) {
      observer.observe(target.current);
    }

    return () => {
      if (target.current) {
        observer.unobserve(target.current);
      }
    };
  }, [loading]); // loading이 변경될 때만 useEffect 실행

  return (
    <div className="container">
      {articleData.slice(startIndex, endIndex).map((article, index) => (
        <div key={index} className="box">
          <img src={`imgFiles/${article.imgFilePath}/${article.imgFileName}`} alt="" />
        </div>
      ))}
      <div ref={target}></div>
    </div>
  );
}

export default Home;
