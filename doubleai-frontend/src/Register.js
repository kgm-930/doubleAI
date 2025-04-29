import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import "./Login.css";

const Register = () => {
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
            const response = await fetch("http://localhost:8080/api/users/register", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(formData),
            });

            if (response.ok) {
                setMessage("회원가입 성공! 로그인 페이지로 이동합니다.");
                setTimeout(() => navigate("/login"), 1500);
            } else {
                const data = await response.json();
                setMessage(data.message || "회원가입 실패");
            }
        } catch (error) {
            console.error("회원가입 실패:", error);
            setMessage("서버 오류 발생");
        }
    };

    return (
        <div className="login-container">
            <h2>회원가입</h2>
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
                    <button type="submit">회원가입</button>
                </form>
                {message && <p className="error-message">{message}</p>}
                <p className="register-link">
                    이미 계정이 있으신가요? <a href="/login">로그인</a>
                </p>
            </div>
        </div>
    );
};

export default Register;
