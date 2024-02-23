import React, { useEffect, useState } from 'react';
import { toastNotice, toastWarning } from "../ToastrConfig";
import { useNavigate } from 'react-router-dom';
import { useAuth } from "../../api/AuthContext";

function Article() {
    const [content, setContent] = useState('');
    const [tagString, setTagString] = useState('');
    const [imageFile, setImageFile] = useState(null);
    const navigate = useNavigate();
    const apiUrl = process.env.REACT_APP_CORE_API_BASE_URL;
    const { isLogin } = useAuth();

    async function createArticle() {

        try {
            if (!isLogin) {
                toastWarning('로그인이 필요한 서비스입니다.');
                return;
            }

            if (!imageFile) {
                toastWarning('이미지를 선택해주세요.');
                return;
            }

            if (!content.trim()) {
                toastWarning('내용을 입력주세요.');
                return;
            }

            const formData = new FormData();
            formData.append('multipartFile', imageFile);
            formData.append('content', content);
            formData.append('tagString', tagString);
            console.log(formData);
            const response = await fetch(`${apiUrl}/api/article`, {
                method: "POST",
                credentials: "include",
                body: formData,
            });

            if (response.ok) {
                navigate("/", { replace: true });
                toastNotice('이미지가 등록되었습니다.');
            } else {
                toastWarning('이미지 등록에 실패했습니다.');
                const errorData = await response.json();
                console.log(errorData);
            }
        } catch (error) {
            console.error('error:', error);
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
                    <h1 className="card-title justify-center">이미지 등록</h1>

                    <div className="p-5">
                        <div className="card-body p-1">
                            <label htmlFor="image" className="card-title">이미지 업로드</label>
                            <input type="file" accept="image/*" id="image" onChange={handleFileChange} className="file-input file-input-bordered file-input-sm w-full max-w-xs" />
                        </div>

                        <div className="card-body p-1">
                            <label htmlFor="content" className="card-title">이미지 설명</label>
                            <textarea value={content} onChange={(e) => setContent(e.target.value)} rows="2" id="content" placeholder="내용을 입력하세요." className="input-field textarea textarea-bordered"></textarea>
                        </div>

                        <div className="card-body p-1">
                            <label htmlFor="tag" className="card-title">태그</label>
                            <input type="text" value={tagString} onChange={(e) => setTagString(e.target.value)} id="tag" placeholder="태그를 입력하세요. 띄어쓰기로 구분됩니다." className="input-field textarea textarea-bordered" />
                        </div>

                        <button type="button" onClick={createArticle} className="btn">작성</button>
                    </div>
                </div>
            </div>
        </section>
    );
};

export default Article;