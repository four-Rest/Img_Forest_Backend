import React, { useState, useEffect, useRef} from 'react';
import './styles.css';


function Home() {
  const [articleData, setArticleData] = useState([]);

  const [loading, setLoading] = useState(false);
  const [page,setPage] = useState(1);
  const apiBaseUrl = process.env.REACT_APP_CORE_API_BASE_URL;

  const target = useRef(null);

  useEffect(() => {
    const fetchData = async () => {
      setLoading(true);
      try {
        const res = await fetch(`/api/article?page=${page}`);
        const data = await res.json();
        setArticleData(prevData => [...prevData, ...data.data]);
        setPage(prevPage => prevPage +1);
      }catch(error) {
        console.error('Error fetching article data', error);
      }

      setLoading(false);
    };

    const observer = new IntersectionObserver((entries) => {

      if(entries[0].isIntersecting) {
        fetchData();
      }
    }, {threshold :1 });

    if(target.current) {
      observer.observe(target.current);
    }
    return () => {
      if(target.current) {
        observer.unobserve(target.current);
      }
    };
  }, [apiBaseUrl,page]);
  // useEffect(() => {
  //   const fetchArticleData = async () => {
  //     try {
  //       const res = await fetch(`/api/article`);
  //       const data = await res.json();
  //       setArticleData(data.data);
  //     } catch (error) {
  //       console.error('Error fetching article data:', error);
  //     }
  //   };

  //   fetchArticleData();

    
  // }, [apiBaseUrl]);

  return (
      <div className="container">
          {articleData.map((article) => (
              <div className= "box">
                <img src={`imgFiles/${article.imgFilePath}/${article.imgFileName}`} alt="" />
              </div>
          ))}
          {loading && <div>Loading...</div>}
          <div ref = {target}></div>
      </div>
  );
};

export default Home;