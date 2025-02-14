import { BrowserRouter as Router, Routes, Route, Link } from "react-router-dom";
import Login from "./Login";
import Chat from "./Chat";

function App() {
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
              <Route path="/" element={<Chat />} />
            </Routes>
          </header>
        </div>
      </Router>
  );
}

export default App;
