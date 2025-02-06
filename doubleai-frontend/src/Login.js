import React, { useState } from "react";

const Login = () => {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [loginError, setLoginError] = useState(""); // 로그인 실패 메시지 상태 추가
    const [loading, setLoading] = useState(false); // 로딩 상태 추가

    const handleLogin = async (e) => {
        e.preventDefault();
        const user = { username, password };
        setLoading(true); // 로그인 요청 시 로딩 시작

        try {
            const res = await fetch('http://localhost:8080/api/user/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(user),
            });

            const data = await res.text();
            if (res.ok) {
                // 로그인 성공 시 JWT 저장
                localStorage.setItem('token', data);
                alert('로그인 성공');
                setLoginError(""); // 로그인 실패 메시지 초기화
                setUsername(""); // 로그인 후 사용자명 초기화
                setPassword(""); // 로그인 후 비밀번호 초기화
            } else {
                setLoginError(data); // 로그인 실패 메시지 저장
            }
        } catch (error) {
            console.error('Error:', error);
            setLoginError("로그인 중 문제가 발생했습니다."); // 네트워크 에러 처리
        }
        setLoading(false); // 로그인 요청 후 로딩 종료
    };

    return (
        <div style={styles.container}>
            <h2>로그인</h2>
            <form onSubmit={handleLogin} style={styles.form}>
                <input
                    type="text"
                    placeholder="아이디"
                    value={username}
                    onChange={(e) => setUsername(e.target.value)}
                    style={styles.input}
                />
                <input
                    type="password"
                    placeholder="비밀번호"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    style={styles.input}
                />
                <button type="submit" style={styles.button} disabled={loading}>
                    {loading ? "로딩 중..." : "로그인"} {/* 로딩 상태에 따라 버튼 텍스트 변경 */}
                </button>
            </form>

            {/* 로그인 실패 시 메시지 표시 */}
            {loginError && <div style={styles.errorMessage}>{loginError}</div>}
        </div>
    );
};

const styles = {
    container: { maxWidth: "300px", margin: "50px auto", textAlign: "center" },
    form: { display: "flex", flexDirection: "column" },
    input: { margin: "10px 0", padding: "10px", fontSize: "16px" },
    button: { padding: "10px", background: "#007BFF", color: "#fff", border: "none", cursor: "pointer" },
    errorMessage: { marginTop: "10px", color: "red", fontSize: "14px" }, // 오류 메시지 스타일 추가
};

export default Login;
