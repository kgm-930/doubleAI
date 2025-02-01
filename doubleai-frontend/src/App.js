import React, { useState } from 'react';
import './App.css';

function App() {
  const [input, setInput] = useState('');
  const [response, setResponse] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    try{
      const res = await fetch('http://localhost:8080/api/chat/ask',{
        method: 'POST',
        headers: {
          'Content-Type' : 'application/json',
        },
        body:JSON.stringify(input),
      });
      const data = await res.text();
      setResponse(data);
    } catch (error){
      console.error('Error : ', error);
    }

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