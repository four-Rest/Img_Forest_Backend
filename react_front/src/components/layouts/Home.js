import React, { useState, useEffect, useRef } from 'react';
import './styles.css';

function Home() {
  const [articleData, setArticleData] = useState([]);
  const [loading, setLoading] = useState(false);
  const [page, setPage] = useState(1);
  const apiBaseUrl = process.env.REACT_APP_CORE_API_BASE_URL;

  const target = useRef(null);



  useEffect(() => {
    const fetchData = async () => {
      setLoading(true);
      try {
        const res = await fetch(`/api/article?page=${page}`);
        const data = await res.json();
        setArticleData(prevData => shuffleArray([...prevData, ...data.data]));
        setPage(prevPage => prevPage + 1);
      } catch (error) {
        console.error('Error fetching article data:', error);
      }
      setLoading(false);
    };

    const observer = new IntersectionObserver((entries) => {
      if (entries[0].isIntersecting) {
        fetchData();
      }
    }, { threshold: 1 });

    if (target.current) {
      observer.observe(target.current);
    }

    return () => {
      if (target.current) {
        observer.unobserve(target.current);
      }
    };
  }, [apiBaseUrl, page]);

   // 배열을 섞어주는 함수
   function shuffleArray(array) {
    for (let i = array.length - 1; i > 0; i--) {
      const j = Math.floor(Math.random() * (i + 1));
      [array[i], array[j]] = [array[j], array[i]];
    }
    return array;
  }


  return (
    <div className="container">
      {articleData.map((article, index) => (
        <div className="box">
          <img src={`imgFiles/${article.imgFilePath}/${article.imgFileName}`} alt="" />
        </div>
      ))}
      {loading && <div>Loading...</div>}
      <div ref={target}></div>
    </div>
  );
}

export default Home;