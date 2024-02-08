import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import {toastNotice} from "../ToastrConfig";

function Article() {
    const [content, setContent] = useState('');
    const [tagString, setTagString] = useState('');
    const [imageFile, setImageFile] = useState(null);

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

            const response = await fetch(`${apiBaseUrl}/ArticleController/createArticle`, {
                method: 'POST',
                body: formData
            });

            if (response.ok) {
                toastNotice('게시글이 작성되었습니다.');
            } else {
                toastNotice('게시글 작성에 실패했습니다.');
            }
        } catch (error) {
            console.error('게시글 작성 중 에러 발생:', error);
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
            <Link to="/home">홈으로 이동</Link>
        </section>
    );
};

export default Article;