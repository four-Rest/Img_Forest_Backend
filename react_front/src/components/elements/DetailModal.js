import React, { useEffect, useState } from "react";
import "./DetailModal.css"; // DetailModal에 대한 스타일시트
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  faCircleDown,
  faHeart,
  faHeartCrack,
  faComment,
  faTag,
  comments,
} from "@fortawesome/free-solid-svg-icons";

function DetailModal({ showModal, setShowModal, articleId }) {
  const [detail, setDetail] = useState(null);
  const [comment, setComment] = useState("");
  const apiUrl = process.env.REACT_APP_CORE_API_BASE_URL;

  useEffect(() => {
    if (!articleId) return;

    const fetchData = async () => {
      try {
        const response = await fetch(
          `${apiUrl}/api/article/detail/${articleId}`,
          {
            method: "GET",
            credentials: "include",
            headers: {
              "Content-Type": "application/json",
            },
          }
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

  // 추천
  const handleLike = async () => {
    try {
      const response = await fetch(`${apiUrl}/api/article/like/${articleId}`, {
        method: "POST",
        credentials: "include",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ id: articleId }),
      });
      if (!response.ok) {
        throw new Error("Network response was not ok");
      }
      setDetail((prevDetail) => ({
        ...prevDetail,
        likes: prevDetail.likes + 1, // 추천 수 증가
        likeValue: true, // 추천 상태로 변경
      }));
    } catch (error) {
      console.error("Error updating like status:", error);
    }
  };

  //추천 취소
  const handledisLike = async () => {
    try {
      const response = await fetch(`${apiUrl}/api/article/like/${articleId}`, {
        method: "DELETE",
        credentials: "include",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ id: articleId }),
      });
      if (!response.ok) {
        throw new Error("Network response was not ok");
      }
      setDetail((prevDetail) => ({
        ...prevDetail,
        likes: prevDetail.likes - 1, // 추천 수 감소
        likeValue: false, // 추천 취소 상태로 변경
      }));
    } catch (error) {
      console.error("Error updating like status:", error);
    }
  };

  //댓글 작성
  const handleCommentSubmit = async () => {
    // 서버로 댓글 전송 로직 구현
    console.log(comment); // 입력된 댓글 확인
    // 댓글 전송 후 입력 필드 초기화
    setComment("");
  };

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

  //이미지 다운로드
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
            <FontAwesomeIcon icon={faTag} />
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
          <div style={{ textAlign: "left" }}>
            <p className="likes">
              {likeValue ? (
                <span
                  className="heart"
                  onClick={() => {
                    handledisLike();
                  }}
                >
                  <FontAwesomeIcon icon={faHeart} />
                  {likes}
                </span>
              ) : (
                <span
                  className="canHeart"
                  onClick={() => {
                    handleLike();
                  }}
                >
                  <FontAwesomeIcon icon={faHeart} />
                  {likes}
                </span>
              )}
            </p>
          </div>
          <div className="comments">
            <FontAwesomeIcon icon={faComment} />
            댓글 {listCommentResponses.length}개
          </div>
          <div
            className="comment-input-container"
            style={{
              marginTop: "20px",
              background: "#f0f0f0",
              borderRadius: "5px",
              padding: "10px",
            }}
          >
            <input
              type="text"
              value={comment}
              onChange={(e) => setComment(e.target.value)}
              placeholder="댓글을 입력하세요..."
              style={{
                width: "100%",
                padding: "10px",
                border: "none",
                borderRadius: "5px",
                background: "#e9ecef",
              }}
              onKeyDown={(e) => {
                if (e.key === "Enter") {
                  handleCommentSubmit();
                  e.preventDefault(); // 엔터 키로 인한 폼 제출 동작을 방지
                }
              }}
            />
          </div>
        </div>
      </div>
    </div>
  );
}

export default DetailModal;
