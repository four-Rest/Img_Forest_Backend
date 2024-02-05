import React, { useState, useEffect } from 'react';
import Masonry from 'react-masonry-css';


function Home() {
  const [articleData, setArticleData] = useState([]);
  const apiBaseUrl = process.env.REACT_APP_CORE_API_BASE_URL;

  useEffect(() => {
    const fetchArticleData = async () => {
      try {
        const res = await fetch(`/article`);
        const data = await res.json();
        setArticleData(data.data);
      } catch (error) {
        console.error('Error fetching article data:', error);
      }
    };

    fetchArticleData();

    
  }, [apiBaseUrl]);

  return (
    <section className="main-wrapper flex flex-row w-full">
      <div className="main-container grid">
        <ul className="grid-1">
          {articleData.map((article) => (
              <li>
                <p>{article.id}</p>
                <img src={`/src/imgFiles/2024-02-03/${article.imgFileName}`} alt="" />
              </li>
          ))}
        </ul>
      </div>
    </section>
  );
};

export default Home;