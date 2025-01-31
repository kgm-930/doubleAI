import React, { useState } from 'react';
import './App.css';

function App() {
  const [input, setInput] = useState('');
  const [response, setResponse] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    // 여기에 API 호출 로직을 추가할 예정입니다.
    setResponse(`You asked: ${input}`);
    setInput('');
  };

  return (
      <div className="App">
        <header className="App-header">
          <h1>Double AI Chat</h1>
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