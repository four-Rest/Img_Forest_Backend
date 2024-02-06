import React, { useState, useEffect, Component } from 'react';
import './styles.css';


function Home() {
  const [articleData, setArticleData] = useState([]);
  const apiBaseUrl = process.env.REACT_APP_CORE_API_BASE_URL;

  useEffect(() => {
    const fetchArticleData = async () => {
      try {
        const res = await fetch(`/api/article`);
        const data = await res.json();
        setArticleData(data.data);
      } catch (error) {
        console.error('Error fetching article data:', error);
      }
    };

    fetchArticleData();

    
  }, [apiBaseUrl]);

  return (
      <div className="container">
          {articleData.map((article) => (
              <div className= "box">
                <img src={`imgFiles/${article.imgFilePath}/${article.imgFileName}`} alt="" />
              </div>
          ))}
      </div>
  );
};

export default Home;