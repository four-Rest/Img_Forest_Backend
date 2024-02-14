import React, { useEffect, useState, useRef } from "react";
import { useParams } from "react-router-dom";

const Detail = () => {
  const { id } = useParams();
  const [detail, setDetail] = useState(null);
  const apiUrl = process.env.REACT_APP_CORE_API_BASE_URL;

  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await fetch(`${apiUrl}/api/article/detail/${id}`);
        if (!response.ok) {
          throw new Error("Network response was not ok");
        }
        const res = await response.json();
        setDetail(res.data);
      } catch (error) {
        console.error("error", error);
      }
    };
    fetchData();
  }, [id]);

  if (!detail) {
    return <div>Loading...</div>;
  }

  const {
    content,
    username,
    paid,
    price,
    tags,
    imgFilePath,
    imgFileName,
    likes,
    likeValue,
    listCommentResponses,
  } = detail;
  return (
    <div>
      <div>
        <div className="box">
          <img src={`/imgFiles/${imgFilePath}/${imgFileName}`} alt="dd" />
        </div>
        <h2>content == {content}</h2>
        <h2>username == {username}</h2>
        <h2>paid == {paid ? "Yes" : "No"}</h2>
        <h2>price == {price}</h2>
        <h2>Tags:</h2>
        <ul>
          {tags &&
            tags.map((tag, index) => (
              <li key={index}>
                {index + 1}. {tag}
              </li>
            ))}
        </ul>
        <h2>likes == {likes}</h2>
        <h2>imgFilePath == {imgFilePath}</h2>
        <h2>imgFileName == {imgFileName}</h2>
        <h2>likeValue == {likeValue ? "Yes" : "No"}</h2>
        <h2>listCommentResponses == {listCommentResponses}</h2>
      </div>
    </div>
  );
};

export default Detail;
