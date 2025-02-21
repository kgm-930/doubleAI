import React, { useState } from "react";

const Chat = () => {
    const [inputValue, setInputValue] = useState(""); // 입력값 상태
    const [messages, setMessages] = useState([]); // 대화 기록 상태

    const handleSubmit = async (event) => {
        event.preventDefault();
        if (!inputValue.trim()) return;

        setMessages(prevMessages => [...prevMessages, { user: "Q", text: inputValue }]);

        try {
            const response = await fetch("http://localhost:8080/api/chat/ask", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": `Bearer ${localStorage.getItem("token")}` // JWT 토큰 필요 시 추가
                },
                body: JSON.stringify({ input: inputValue }),
            });

            if (!response.ok) {
                throw new Error(`Server Error: ${response.status}`);
            }

            const data = await response.json();
            console.log("서버 응답:", data);

            if (data.response) {
                setMessages(prevMessages => [...prevMessages, { user: "A", text: data.response }]);
            } else {
                setMessages(prevMessages => [...prevMessages, { user: "A", text: "응답을 가져올 수 없습니다." }]);
            }
        } catch (error) {
            console.error("API 요청 실패:", error);
            setMessages(prevMessages => [...prevMessages, { user: "A", text: "응답을 가져올 수 없습니다." }]);
        }

        setInputValue(""); // 입력창 초기화
    };

    return (
        <div>
            <form onSubmit={handleSubmit}>
                <input
                    type="text"
                    value={inputValue}
                    onChange={(e) => setInputValue(e.target.value)}
                    placeholder="Ask me anything..."
                />
                <button type="submit">Submit</button>
            </form>
            <div>
                {messages.map((msg, index) => (
                    <p key={index}>
                        <strong>{msg.user}:</strong> {msg.text}
                    </p>
                ))}
            </div>
        </div>
    );
};

export default Chat;
