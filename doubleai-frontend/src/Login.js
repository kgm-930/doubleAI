import React, { useState } from 'react';

function Login({ setAuth }) {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [message, setMessage] = useState('');

    const handleLogin = async (e) => {
        e.preventDefault();
        try {
            const response = await fetch('http://localhost:8080/api/user/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ username, password }),
            });

            const token = await response.text();

            if (response.ok){
                localStorage.setItem('token',token);
                setAuth(true);
                setMessage('로그인 성공');
            }else{
                setMessage('아이디나 패스워드가 틀렸습니다');
            }
        } catch (error) {
            console.error('Error:', error);
            setMessage('로그인 실패');
        }
    };

    return (
        <div>
            <h2>로그인</h2>
            <form onSubmit={handleLogin}>
                <input
                    type="text"
                    placeholder="아이디"
                    value={username}
                    onChange={(e) => setUsername(e.target.value)}
                    required
                />
                <input
                    type="password"
                    placeholder="비밀번호"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    required
                />
                <button type="submit">로그인</button>
            </form>
            <p>{message}</p>
        </div>
    );
}

export default Login;
