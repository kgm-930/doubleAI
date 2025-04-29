import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import "./Login.css";

const Login = () => {
    const [formData, setFormData] = useState({ username: "", password: "" });
    const [message, setMessage] = useState("");
    const navigate = useNavigate();

    const handleChange = (e) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setMessage("");

        try {
            const response = await fetch("http://localhost:8080/api/users/login", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(formData),
            });

            const token = await response.text();

            if (response.ok) {
                localStorage.setItem("token", token);
                navigate("/chat");
            } else {
                setMessage("로그인 실패: " + token);
            }
        } catch (error) {
            console.error("로그인 요청 실패:", error);
            setMessage("서버 오류 발생");
        }
    };

    return (
        <div className="login-container">
            <h2>Double AI Chat</h2>
            <div className="login-box">
                <form onSubmit={handleSubmit}>
                    <input
                        type="text"
                        name="username"
                        placeholder="아이디"
                        value={formData.username}
                        onChange={handleChange}
                        required
                    />
                    <input
                        type="password"
                        name="password"
                        placeholder="비밀번호"
                        value={formData.password}
                        onChange={handleChange}
                        required
                    />
                    <button type="submit">로그인</button>
                </form>
                {message && <p className="error-message">{message}</p>}
                <p className="register-link">
                    계정이 없으신가요? <a href="/register">회원가입</a>
                </p>
            </div>
        </div>
    );
};

export default Login;
