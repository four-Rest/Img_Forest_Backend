import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import {toastNotice, toastWarning} from "../ToastrConfig";
import { useNavigate, Navigate } from 'react-router-dom';
function Article() {
    const [content, setContent] = useState('');
    const [tagString, setTagString] = useState('');
    const [imageFile, setImageFile] = useState(null);
    const navigate = useNavigate();
    const apiBaseUrl = process.env.REACT_APP_CORE_API_BASE_URL;

    async function createArticle() {
        try {
            if (!imageFile) {
                console.error('이미지를 선택해주세요.');
                return;
            }

            const formData = new FormData();
            formData.append('image', imageFile);
            formData.append('content', content);
            formData.append('tagString', tagString);
            console.log(formData);
            const response = await fetch(`${apiBaseUrl}/api/article`, {
                headers: {
                    "Content-Type": "application/json",
                  },
                  method: "POST",
                  credentials: "include",
                  body: JSON.stringify(formData),
            });

            if (response.ok) {
                toastNotice('게시글이 작성되었습니다.');
                navigate("/article", { replace: true });
            } else {
                toastWarning('게시글 작성에 실패했습니다.');
                const errorData = await response.json();
                console.log(errorData);
                navigate("/article", { replace: true });

            }
        } catch (error) {
            console.error('게시글 작성 중 에러 발생:', error);
            navigate("/article", { replace: true });
        }
    }

    function handleFileChange(event) {
        const selectedFile = event.target.files[0];

        if (selectedFile) {
            setImageFile(selectedFile);
            console.log('선택된 파일:', selectedFile);
        } else {
            console.error('파일을 선택해주세요.');
        }
    }

    return (
        <section className="form-container">
            <div className="card shadow-xl">
                <div className="card-body p-1">
                    <h1 className="card-title justify-center">게시글 작성</h1>

                    <form className="p-5">
                        <div className="card-body p-1">
                            <label htmlFor="image" className="card-title">이미지 업로드</label>
                            <input type="file" accept="image/*" id="image" onChange={handleFileChange} className="input-field" />
                        </div>

                        <div className="card-body p-1">
                            <label htmlFor="content" className="card-title">게시글 제목</label>
                            <textarea value={content} onChange={(e) => setContent(e.target.value)} rows="2" id="content" placeholder="게시글 제목을 입력하세요" className="input-field"></textarea>
                        </div>

                        <div className="card-body p-1">
                            <label htmlFor="tag" className="card-title">태그</label>
                            <input type="text" value={tagString} onChange={(e) => setTagString(e.target.value)} id="tag" placeholder="태그를 입력하세요" className="input-field" />
                        </div>

                        <button onClick={createArticle} className="btn">작성</button>
                    </form>
                </div>
            </div>
        </section>
    );
};

export default Article;