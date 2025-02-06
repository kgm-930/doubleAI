import React, { useState } from "react";
import { BrowserRouter as Router, Routes, Route, Link, useNavigate } from "react-router-dom";
import Login from "./Login";
import "./App.css";

function App() {
  const [input, setInput] = useState("");
  const [response, setResponse] = useState([]);
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const token = localStorage.getItem("token"); // JWT 가져오기

      if (!token) {
        alert("로그인이 필요합니다.");
        navigate("/login");
        return;
      }

      const res = await fetch("http://localhost:8080/api/chat/ask", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify({ question: input }), // 객체 형식으로 변경
      });

      const data = await res.text();
      setResponse((prevResponse) => [...prevResponse, { question: input, answer: data }]);
    } catch (error) {
      console.error("Error:", error);
    }

    setInput(""); // 입력값 초기화
  };

  return (
      <Router>
        <div className="App">
          <header className="App-header">
            <h1>Double AI Chat</h1>
            <nav>
              <Link to="/login">로그인</Link> | <Link to="/">채팅</Link>
            </nav>
            <Routes>
              <Route path="/login" element={<Login />} />
              <Route
                  path="/"
                  element={
                    <div>
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
                        {response.map((item, index) => (
                            <div key={index}>
                              <p><strong>Q: </strong>{item.question}</p>
                              <p><strong>A: </strong>{item.answer}</p>
                            </div>
                        ))}
                      </div>
                    </div>
                  }
              />
            </Routes>
          </header>
        </div>
      </Router>
  );
}

export default App;
