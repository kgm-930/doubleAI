import React, { useState, useEffect } from "react";

const Chat = () => {
    const [inputValue, setInputValue] = useState("");
    const [messages, setMessages] = useState([]);

    useEffect(() => {
        const token = localStorage.getItem("token");

        if (!token) {
            alert("로그인이 필요합니다.");
            window.location.href = "/login";
            return;
        }

        fetch("http://localhost:8080/api/chat/history", {
            headers: {
                "Authorization": `Bearer ${token}`,
            },
        })
            .then(res => {
                if (!res.ok) throw new Error("불러오기 실패");
                return res.json();
            })
            .then(data => {
                // 기록을 Q → A 순서로 변환
                const formatted = data.flatMap(item => [
                    { user: "Q", text: item.question },
                    { user: "A", text: item.answer },
                ]);
                setMessages(formatted);
            })
            .catch(err => {
                console.error("대화 기록 불러오기 실패:", err);
            });
    }, []);

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (!inputValue.trim()) return;

        const token = localStorage.getItem("token");

        // 먼저 사용자 메시지를 보여줌
        setMessages(prev => [...prev, { user: "Q", text: inputValue }]);

        try {
            const res = await fetch("http://localhost:8080/api/chat/ask", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": `Bearer ${token}`,
                },
                body: JSON.stringify({ input: inputValue }),
            });

            const data = await res.json();

            setMessages(prev => [...prev, { user: "A", text: data.response }]);
        } catch (err) {
            console.error("API 요청 실패:", err);
            setMessages(prev => [...prev, { user: "A", text: "응답 오류" }]);
        }

        setInputValue("");
    };

    const handleLogout = () => {
        localStorage.removeItem("token");
        window.location.href = "/login";
    };

    return (
        <div>
            <h2>Double AI Chat</h2>
            <button onClick={handleLogout}>로그아웃</button>

            <div>
                {messages.map((msg, index) => (
                    <p key={index}>
                        <strong>{msg.user}:</strong> {msg.text}
                    </p>
                ))}
            </div>

            <form onSubmit={handleSubmit}>
                <input
                    type="text"
                    value={inputValue}
                    onChange={(e) => setInputValue(e.target.value)}
                    placeholder="메시지를 입력하세요"
                />
                <button type="submit">전송</button>
            </form>
        </div>
    );
};

export default Chat;
