import React, { useEffect, useState } from 'react';
import { toastNotice, toastWarning } from "../ToastrConfig";
import { useNavigate } from 'react-router-dom';
import { useAuth } from "../../api/AuthContext";
import { useParams } from "react-router-dom";

function Modify() {
    const [newContent, setNewContent] = useState('');
    const [newTagString, setNewTagString] = useState('');
    const [newImageFile, setNewImageFile] = useState(null);
    const navigate = useNavigate();
    const apiUrl = process.env.REACT_APP_CORE_API_BASE_URL;
    const { isLogin } = useAuth();
    const { id } = useParams();

    async function modifyArticle() {

        try {
            if (!isLogin) {
                toastWarning('로그인이 필요한 서비스입니다.');
                return;
            }

            if (!newContent.trim()) {
                toastWarning('내용을 입력해주세요.');
                return;
            }

            let response = null;

            if (newImageFile) {
                const formData = new FormData();
                formData.append('multipartFile', newImageFile);
                formData.append('content', newContent);
                formData.append('tagString', newTagString);
                console.log(formData);
                response = await fetch(`${apiUrl}/api/article/${id}`, {
                    method: "PUT",
                    credentials: "include",
                    body: formData,
                });
            } else {
                const data = {
                    content: newContent,
                    tagString: newTagString
                  };
                response = await fetch(`${apiUrl}/api/article/mode2/${id}`, {
                    method: "PUT",
                    credentials: "include",
                    headers: {
                      "Content-Type": "application/json"
                    },
                    body: JSON.stringify(data)
                  });
            }

            if (response.resultCode === "200") {
                toastNotice('수정되었습니다.');
                navigate("/", { replace: true });
            } else {
                toastWarning('수정실패');
                const errorData = await response.json();
                console.log(errorData);
            }
        } catch (error) {
            console.error('error:', error);
        }
    }

    useEffect(() => {
        const fetchData = async () => {
          try {
            const response = await fetch(`${apiUrl}/api/article/detail/${id}`);
            if (!response.ok) {
              throw new Error("Network response was not ok");
            }
            const res = await response.json();
            const articleData = res.data;
            setNewContent(articleData.content);
            setNewTagString(articleData.tags.join(' ').trim());
          } catch (error) {
            console.error("error", error);
          }
        };
        fetchData();
        
      }, [id]);


    
    function handleFileChange(event) {
        const selectedFile = event.target.files[0];

        if (selectedFile) {
            setNewImageFile(selectedFile);
            console.log('선택된 파일:', selectedFile);
        } 
    }


    return (
        <section className="form-container">
            <div className="card shadow-xl">
                <div className="card-body p-1">
                    <h1 className="card-title justify-center">이미지 등록</h1>

                    <div className="p-5">
                        

                        <div className="card-body p-1">
                            <label htmlFor="content" className="card-title">이미지 설명</label>
                            <textarea value={newContent} onChange={(e) => setNewContent(e.target.value)} rows="2" id="content" placeholder="내용을 입력하세요." className="input-field textarea textarea-bordered"></textarea>
                        </div>

                        <div className="card-body p-1">
                            <label htmlFor="tag" className="card-title">태그</label>
                            <input type="text" value={newTagString} onChange={(e) => setNewTagString(e.target.value)} id="tag" placeholder="태그를 입력하세요. 띄어쓰기로 구분됩니다." className="input-field textarea textarea-bordered" />
                        </div>
                        <div className="card-body p-1">
                        <div class="collapse bg-base-200">
  <input type="checkbox" /> 
  <div className="collapse-title text-xl font-medium tex-bold">
    이미지 파일 바꾸기
  </div>
  <div className="collapse-content"> 
                            <label htmlFor="image" className="card-title">이미지 업로드</label>
                            <input type="file" accept="image/*" id="image" onChange={handleFileChange} className="file-input file-input-bordered file-input-sm w-full max-w-xs" />
                        </div>
                        </div>
</div>

                        <button type="button" onClick={modifyArticle} className="btn">수정</button>
                    </div>
                </div>
            </div>
        </section>
    );
};

export default Modify;