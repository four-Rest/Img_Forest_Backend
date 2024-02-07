import React, { useState, useEffect, useRef } from 'react';
import './styles.css';

function Home() {
  const [articleData, setArticleData] = useState([]);
  const [loading, setLoading] = useState(false);
  const [startIndex, setStartIndex] = useState(0);
  const [endIndex, setEndIndex] = useState(30);
  const apiBaseUrl = process.env.REACT_APP_CORE_API_BASE_URL;

  const target = useRef(null);

  useEffect(() => {
    const fetchData = async () => {
      setLoading(true);
      try {
        const res = await fetch(`/api/article`);
        const data = await res.json();
        setArticleData(prevData => prevData.concat(data.data));
      } catch (error) {
        console.error('Error fetching article data:', error);
      }
      setLoading(false);
    };
    fetchData();
  }, [apiBaseUrl]);

  useEffect(() => {
    const observer = new IntersectionObserver(
      async(entries) => {
        if (entries[0].isIntersecting && !loading) {
          setLoading(true);
          //setStartIndex(prevStartIndex => prevStartIndex + 10);
          setEndIndex(prevEndIndex => prevEndIndex + Math.min(10, articleData.length - prevEndIndex));

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
  }, [loading, articleData]); // loading이 변경될 때만 useEffect 실행


  // useEffect(() => {
  //   if (!loading) {
  //     const remainingImages = articleData.length - endIndex;
  //     if (remainingImages < 10 && remainingImages > 0) {
  //       setEndIndex(articleData.length); // articleData의 길이 - endIndex가 10 미만인 경우, 나머지 이미지 전부 렌더링
  //     }
  //   }
  // }, [endIndex, loading, articleData]);


  return (
    <div className="container">
      {articleData.slice(startIndex, endIndex).map((article) => (
        <div key={article.id} className="box">
          <img src={`imgFiles/${article.imgFilePath}/${article.imgFileName}`} alt="" />
        </div>
      ))}
      <div ref={target}></div>
    </div>
  );
}

export default Home;