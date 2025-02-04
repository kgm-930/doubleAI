import React, { useState } from 'react';

function Chat({ token }) {
    const [input, setInput] = useState('');
    const [response, setResponse] = useState('');

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const res = await fetch('http://localhost:8080/api/chat/ask', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`,
                },
                body: JSON.stringify({ question: input }),
            });
            const data = await res.text();
            setResponse(data);
        } catch (error) {
            console.error('Error:', error);
        }
        setInput('');
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
                <p>{response}</p>
            </div>
        </div>
    );
}

export default Chat;
