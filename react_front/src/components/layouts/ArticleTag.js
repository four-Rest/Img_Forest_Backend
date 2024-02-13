import React, { useState, useEffect, useContext} from 'react';
import './styles.css';
import {SearchTagContext} from '../../api/SearchTagContext';
import {useParams} from 'react-router-dom';
function ArticleTag () {

    const {tagString}  = useParams();

    console.log(tagString);


    const {searchTag}  = useContext(SearchTagContext);
    const [articleData, setArticleData] = useState([]);
    useEffect(() => {
        const fetchData = async() => {

            try{
                const res = await fetch(`/api/article/${tagString}`);
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
        <div className="container">
        {articleData.map((article,index) => {
          console.log(`imgFiles/${article.imgFilePath}/${article.imgFileName}`);
          return (
            <div key={index} className= "box">
              <img src={`imgFiles/${article.imgFilePath}/${article.imgFileName}`} alt="" />
            </div>
          );
        })}
      </div>
       
    );
}

export default ArticleTag;
