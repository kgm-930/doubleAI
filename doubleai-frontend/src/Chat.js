import React, { useState } from "react";
import { useNavigate } from "react-router-dom";

const Chat = () => {
    const [input, setInput] = useState("");
    const [response, setResponse] = useState([]);
    const navigate = useNavigate(); // 이제 Router 내부에서 사용됨

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const token = localStorage.getItem("token");

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
                body: JSON.stringify({
                    messages: [
                        { role: "system", content: "당신은 유용한 정보를 제공하는 친절한 AI 챗봇입니다. 질문에 대해 항상 자세하고 정확하며 창의적인 한국어 답변을 제공합니다." },
                        { role: "user", content: input }
                    ]
                }),
            });

            const data = await res.json();
            const botAnswer = data.choices[0].message.content; // 한국어 응답

            setResponse((prevResponse) => [...prevResponse, { question: input, answer: botAnswer }]);
        } catch (error) {
            console.error("Error:", error);
        }

        setInput("");
    };



    return (
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
                        <p>
                            <strong>Q: </strong>
                            {item.question}
                        </p>
                        <p>
                            <strong>A: </strong>
                            {item.answer}
                        </p>
                    </div>
                ))}
            </div>
        </div>
    );
};

export default Chat;
