import React, { useState, useEffect } from 'react';
import Masonry from 'react-masonry-css'


function Home() {
  const [articleData, setArticleData] = useState([]);
  const apiBaseUrl = import.meta.env.VITE_CORE_API_BASE_URL;

  useEffect(() => {
    const fetchArticleData = async () => {
      try {
        const res = await fetch(`${apiBaseUrl}/article`);
        const data = await res.json();
        setArticleData(data.data);
      } catch (error) {
        console.error('Error fetching article data:', error);
      }
    };

    fetchArticleData();

    const handleResize = () => {
      if (masonry) {
        masonry.layout();
      }
    };

    const grid = document.querySelector('.grid-1');
    masonry = new Masonry(grid, {
      itemSelector: '.grid-item',
    });

    window.addEventListener('resize', handleResize);

    return () => {
      window.removeEventListener('resize', handleResize);
      if (masonry) {
        masonry.destroy();
        masonry = null;
      }
    };
  }, [apiBaseUrl]);

  return (
    <section className="main-wrapper flex flex-row w-full">
      <div className="main-container grid">
        <ul className="grid-1">
          {articleData.map((article, index) => (
            <div
              key={article.id}
              className="grid-item float-left w-[100%] sm:w-[calc(100%/2)] md:w-[calc(100%/3)] lg:w-[calc(100%/4)] p-5"
            >
              <li>
                <p>{article.id}</p>
                <img src={`/public/${article.imgFileName}`} alt="" />
              </li>
            </div>
          ))}
        </ul>
      </div>
    </section>
  );
}

