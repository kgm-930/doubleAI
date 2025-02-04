import React, { useState } from 'react';
import './App.css';

function App() {
  const [input, setInput] = useState('');
  const [response, setResponse] = useState('');
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');

  const handleLogin = async (e) => {
    e.preventDefault();
    const user = { username, password };

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
        const token = data; // JWT는 응답으로 돌아온 값으로 가정
        localStorage.setItem('token', token);
        alert('로그인 성공');
      } else {
        alert(data); // 로그인 실패 메시지 출력
      }
    } catch (error) {
      console.error('Error:', error);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      // 로컬 스토리지에서 토큰 가져오기
      const token = localStorage.getItem('token');

      const res = await fetch('http://localhost:8080/api/chat/ask', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`, // JWT를 Authorization 헤더에 포함
        },
        body: JSON.stringify(input),
      });

      const data = await res.text();
      setResponse(data);
    } catch (error) {
      console.error('Error:', error);
    }

    setInput('');
  };

  return (
      <div className="App">
        <header className="App-header">
          <h1>Double AI Chat</h1>
          <form onSubmit={handleLogin}>
            <input
                type="text"
                value={username}
                onChange={(e) => setUsername(e.target.value)}
                placeholder="Username"
            />
            <input
                type="password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                placeholder="Password"
            />
            <button type="submit">Login</button>
          </form>
          <form onSubmit={handleSubmit}>
            <input
                type="text"
                value={input}
                onChange={(e) => setInput(e.target.value)}
                placeholder="Ask me anything..."
            />
            <button type="submit">Submit</button>
          </form>
          <div className="response">
            <p>{response}</p>
          </div>
        </header>
      </div>
  );
}

export default App;
