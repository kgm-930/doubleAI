import React, {useState} from "react";
import {useNavigate} from "react-router-dom";

const Login = () => {
    const [formData, setFormData] = useState({username: "", password: ""});
    const [message, setMessage] = useState("");
    const navigate = useNavigate(); // 리다이렉트용

    const handleChange = (e) => {
        setFormData({...formData, [e.target.name]: e.target.value});
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setMessage("");

        try {
            const response = await fetch("http://localhost:8080/api/users/login", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(formData),
            });

            const token = await response.text(); // 응답은 JWT 토큰 문자열

            if (response.ok) {
                localStorage.setItem("token", token);
                navigate("/chat"); // ✅ 채팅 페이지로 이동
            } else {
                setMessage("로그인 실패: " + token);
            }
        } catch (error) {
            console.error("로그인 요청 실패:", error);
            setMessage("서버 오류 발생");
        }
    };

    return (
        <div>
            <h2>로그인</h2>

            <form onSubmit={handleSubmit}>
                <input
                    type="text"
                    name="username"
                    placeholder="아이디"
                    value={formData.username}
                    onChange={handleChange}
                    required
                />
                <br/>
                <input
                    type="password"
                    name="password"
                    placeholder="비밀번호"
                    value={formData.password}
                    onChange={handleChange}
                    required
                />
                <br/>
                <div>
                    <button type="submit">로그인</button>
                    <p>
                        아직 계정이 없으신가요? <a href="/register">회원가입</a>
                    </p>
                </div>

            </form>
            {message && <p>{message}</p>}
        </div>
    );
};

export default Login;
