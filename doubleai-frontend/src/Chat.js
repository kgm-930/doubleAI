import React, {useState, useEffect, useRef} from "react";
import "./Chat.css"; // 스타일 분리

const Chat = () => {
    const [inputValue, setInputValue] = useState("");
    const [messages, setMessages] = useState([]);
    const chatEndRef = useRef(null);

    // 기록 불러오기
    useEffect(() => {
        const token = localStorage.getItem("token");
        if (!token) {
            alert("로그인이 필요합니다.");
            window.location.href = "/login";
            return;
        }

        fetch("http://localhost:8080/api/chat/history", {
            headers: {Authorization: `Bearer ${token}`},
        })
            .then(res => res.json())
            .then(data => {
                const formatted = data.flatMap(d => [
                    {user: "Q", text: d.question},
                    {user: "A", text: d.answer},
                ]);
                setMessages(formatted);
            });
    }, []);

    // 스크롤 맨 아래로 이동
    useEffect(() => {
        if (messages.length > 0) {
            const chatBox = document.querySelector(".chat-box");
            if (chatBox) {
                chatBox.scrollTop = chatBox.scrollHeight;
            }
        }
    }, [messages]);


    const handleSubmit = async (e) => {
        e.preventDefault();
        if (!inputValue.trim()) return;

        const token = localStorage.getItem("token");
        setMessages(prev => [...prev, {user: "Q", text: inputValue}]);

        try {
            const res = await fetch("http://localhost:8080/api/chat/ask", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    Authorization: `Bearer ${token}`,
                },
                body: JSON.stringify({input: inputValue}),
            });
            const data = await res.json();
            setMessages(prev => [...prev, {user: "A", text: data.response}]);
        } catch {
            setMessages(prev => [...prev, {user: "A", text: "응답 오류"}]);
        }

        setInputValue("");
    };

    const handleLogout = () => {
        localStorage.removeItem("token");
        window.location.href = "/login";
    };

    const handleDeleteChat = async () => {
        const token = localStorage.getItem("token");
        if (!token) return;

        if (!window.confirm("정말 대화 기록을 삭제하시겠습니까?")) {
            return;
        }

        try {
            const res = await fetch("http://localhost:8080/api/chat/delete", {
                method: "DELETE",
                headers: {
                    "Authorization": `Bearer ${token}`
                }
            });

            if (res.ok) {
                setMessages([]); // 삭제 후 채팅창 비우기
            } else {
                alert("대화 삭제 실패");
            }
        } catch (error) {
            console.error("대화 삭제 에러:", error);
            alert("서버 오류 발생");
        }
    };

    return (
        <div className="chat-container">
            <h2>Double AI Chat</h2>
            <button className="logout-btn" onClick={handleLogout}>로그아웃</button>
            <button className="delete-btn" onClick={handleDeleteChat}>대화 삭제</button>

            <div className="chat-box">
                {messages.map((msg, idx) => (
                    <div key={idx} className={`bubble ${msg.user === "Q" ? "question" : "answer"}`}>
                        {msg.text.split('\n').map((line, i) => (
                            <span key={i}>
                {line}
                                <br/>
            </span>
                        ))}
                    </div>
                ))}

                <div ref={chatEndRef}/>
            </div>

            <form className="chat-form" onSubmit={handleSubmit}>
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
