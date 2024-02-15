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

  const downloadImage = (path, filename) => {
    const link = document.createElement("a");
    link.href = path;
    link.download = filename;
    document.body.appendChild(link); // DOM에 링크 추가
    link.click(); // 링크 클릭
    document.body.removeChild(link); // DOM에서 링크 제거
  };

  const handleDownload = () => {
    const imagePath = `/imgFiles/${imgFilePath}/${imgFileName}`;
    downloadImage(imagePath, imgFileName);
  };

  return (
    <div>
      <div>
        <div className="box">
          <img src={`/imgFiles/${imgFilePath}/${imgFileName}`} alt="dd" />
          <button onClick={handleDownload}>Download Image</button>
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
