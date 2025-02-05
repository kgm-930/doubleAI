import React, { useState } from "react";
import { BrowserRouter as Router, Routes, Route, Link } from "react-router-dom";
import Login from "./Login";
import "./App.css";

function App() {
  const [input, setInput] = useState("");
  const [response, setResponse] = useState("");

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const token = localStorage.getItem("token"); // JWT 가져오기

      const res = await fetch("http://localhost:8080/api/chat/ask", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify(input),
      });

      const data = await res.text();
      setResponse(data);
    } catch (error) {
      console.error("Error:", error);
    }

    setInput("");
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
                    <form onSubmit={handleSubmit}>
                      <input
                          type="text"
                          value={input}
                          onChange={(e) => setInput(e.target.value)}
                          placeholder="Ask me anything..."
                      />
                      <button type="submit">Submit</button>
                    </form>
                  }
              />
            </Routes>
            <div className="response">
              <p>{response}</p>
            </div>
          </header>
        </div>
      </Router>
  );
}

export default App;
