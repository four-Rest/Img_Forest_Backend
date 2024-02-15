import React, { useState, useEffect, useContext} from 'react';
import './styles.css';
import {SearchTagContext} from '../../api/SearchTagContext';
import {useParams} from 'react-router-dom';
function ArticleTag () {

    const {tagString}  = useParams();



    const {searchTag}  = useContext(SearchTagContext);
    const [articleData, setArticleData] = useState([]);
    const apiUrl = process.env.REACT_APP_CORE_API_BASE_URL;

    useEffect(() => {
        const fetchData = async() => {

            try{
                const res = await fetch(`${apiUrl}/api/article/${tagString}`);
                const data = await res.json();
                const dataArray = Array.from (data.data);
                console.log(dataArray);
                setArticleData(dataArray);
            }
            catch(error) {
                console.error('Error fetching data',error);
            }
        };
        fetchData();
    },[tagString]);

    
    return (
      articleData.length !== 0
      ? <div className="container pt-24">
         {articleData.map((article) => (
          <div key={article.id} className="box">
            <img src={`${apiUrl}/gen/${article.imgFilePath}/${article.imgFileName}`} alt="a" />
          </div>
        ))}
      </div>
      : <></>
    );
}

export default ArticleTag;
