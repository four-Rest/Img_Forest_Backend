import React, { useEffect, useState } from "react";
import "./DetailModal.css"; // DetailModal에 대한 스타일시트
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  faCircleDown,
  faHeart,
  faHeartCrack,
  faComment,
  faTag,
  faPaperPlane,
  comments,
} from "@fortawesome/free-solid-svg-icons";

function DetailModal({ showModal, setShowModal, articleId }) {
  const [detail, setDetail] = useState(null);
  const [comment, setComment] = useState("");
  const [currentComments, setCurrentComments] = useState([]);
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
        console.log(res.data);
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
    try {
      const name = localStorage.getItem("username");
      const response = await fetch(`${apiUrl}/api/comment/`, {
        method: "POST",
        credentials: "include",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          articleId: articleId,
          username: name,
          content: comment,
        }),
      });

      if (response.ok) {
        const newCommentData = await response.json(); // 새 댓글 데이터
        const newComment = {
          ...newCommentData.data,
          username: name, // 새 댓글 객체에 username 추가
        };

        // 새 댓글을 목록에 추가
        setDetail((prevDetail) => ({
          ...prevDetail,
          listCommentResponses: [
            ...prevDetail.listCommentResponses,
            newComment,
          ],
        }));
        setComment(""); // 입력 필드 초기화
      } else {
        console.error("댓글 생성 실패");
        // 실패 시 사용자에게 알림
      }
    } catch (error) {
      console.error("에러 발생:", error);
      // 네트워크 오류 처리
    }
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
    const imagePath = `${apiUrl}/gen/${imgFilePath}/${imgFileName}`;
    downloadImage(imagePath, imgFileName);
  };

  const calculateTimeAgo = (createdDate) => {
    const date1 = new Date(createdDate);
    const date2 = new Date();
    const difference = date2 - date1;

    const days = Math.floor(difference / (1000 * 60 * 60 * 24));
    const hours = Math.floor(difference / (1000 * 60 * 60));
    const minutes = Math.floor(difference / (1000 * 60));
    const seconds = Math.floor(difference / 1000);

    if (days >= 1) {
      return `${days}일`;
    } else if (hours >= 1) {
      return `${hours}시간`;
    } else if (minutes >= 1) {
      return `${minutes}분`;
    } else {
      return `${seconds}초`;
    }
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
          <img src={`${apiUrl}/gen/${imgFilePath}/${imgFileName}`} alt="Article" />
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
          <div>
            <ul>
              {listCommentResponses.map((comment, index) => (
                <li key={index}>
                  <strong>{comment.username}</strong>: {comment.content}
                  <br />
                  <span className="commentTime">
                    {calculateTimeAgo(comment.createdDate)}
                  </span>
                </li>
              ))}
            </ul>
          </div>
          <div
            className="comment-input-container"
            style={{
              marginTop: "20px",
              background: "#f0f0f0",
              borderRadius: "5px",
              padding: "10px",
              display: "flex",
              alignItems: "center",
            }}
          >
            <input
              type="text"
              value={comment}
              onChange={(e) => setComment(e.target.value)}
              placeholder="댓글을 입력하세요..."
              style={{
                flex: 1,
                padding: "10px",
                border: "none",
                borderRadius: "5px",
                background: "#e9ecef",
                marginRight: "10px", // 버튼과의 간격 조정
              }}
              onKeyDown={(e) => {
                if (e.key === "Enter") {
                  handleCommentSubmit();
                  e.preventDefault(); // 엔터 키로 인한 폼 제출 동작을 방지
                }
              }}
            />
            {comment && (
              <button
                onClick={handleCommentSubmit}
                style={{
                  display: "flex",
                  justifyContent: "center",
                  alignItems: "center",
                  padding: "10px",
                  border: "none",
                  borderRadius: "5px",
                  background: "#007bff",
                  color: "white",
                  cursor: "pointer",
                }}
              >
                <FontAwesomeIcon icon={faPaperPlane} />
              </button>
            )}
          </div>
        </div>
      </div>
    </div>
  );
}

export default DetailModal;
