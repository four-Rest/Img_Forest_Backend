import React, { useEffect, useState } from "react";
import "./DetailModal.css"; // DetailModal에 대한 스타일시트
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  faCircleDown,
  faHeart,
  faComment,
  comments,
} from "@fortawesome/free-solid-svg-icons";

function DetailModal({ showModal, setShowModal, articleId }) {
  const [detail, setDetail] = useState(null);
  const apiUrl = process.env.REACT_APP_CORE_API_BASE_URL;

  useEffect(() => {
    if (!articleId) return;

    const fetchData = async () => {
      try {
        const response = await fetch(
          `${apiUrl}/api/article/detail/${articleId}`
        );
        if (!response.ok) {
          throw new Error("Network response was not ok");
        }
        const res = await response.json();
        setDetail(res.data);
      } catch (error) {
        console.error("Error fetching article detail:", error);
      }
    };

    fetchData();
  }, [articleId, apiUrl]);

  if (!showModal) return null;

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
    <div
      className="detailModalBackdrop"
      style={{ display: showModal ? "flex" : "none" }}
    >
      <div className="detailModalContent">
        <label
          htmlFor="login-modal"
          className="btn btn-sm btn-circle absolute right-2 top-2"
          onClick={() => setShowModal(false)}
        >
          ✕
        </label>
        <div className="detailModalLeft">
          <img src={`/imgFiles/${imgFilePath}/${imgFileName}`} alt="Article" />
        </div>
        <div className="detailModalRight">
          <div className="tags-container">
            {tags.map((tag, index) => (
              <span key={index} className="tag">
                {tag}
              </span> // 스타일 클래스 적용
            ))}
          </div>
          <h2 className="title mt-3">작성자 : {username}</h2>
          <h2 className="mb-3">{imgFilePath}</h2>
          <textarea className="disabledTextarea" disabled value={content} />

          <div style={{ display: "flex", alignItems: "center" }}>
            <p className="info" style={{ margin: 0 }}>
              {paid ? "유료" : "무료"}
            </p>
            {paid && (
              <p className="price" style={{ margin: "0 10px" }}>
                {price}원
              </p>
            )}
            <button onClick={handleDownload} className="downloadBtn">
              저장
            </button>
          </div>
          <p className="likes">
            <FontAwesomeIcon icon={faHeart} />
            {likes}
          </p>

          <div className="comments">
            <FontAwesomeIcon icon={faComment} />
            댓글 {listCommentResponses.length}개
          </div>
        </div>
      </div>
    </div>
  );
}

export default DetailModal;
