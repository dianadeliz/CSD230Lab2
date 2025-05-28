import React, { useState } from 'react';
import './Chatbot.css';
import api from '../utils/api';
import chatbotAvatar from '../chatbotAvatar.png';

function Chatbot() {
  const [open, setOpen] = useState(false);
  const [messages, setMessages] = useState([
    { from: 'bot', text: 'Hi! Ask me to find discussions or posts by keyword.' }
  ]);
  const [input, setInput] = useState('');

  const handleSend = async (e) => {
    e.preventDefault();
    if (!input.trim()) return;
    setMessages([...messages, { from: 'user', text: input }]);
    let reply = '';
    const lowerInput = input.toLowerCase();
    if (lowerInput.includes('hello')) {
      reply = 'Hello! How can I help you find discussions or posts today?';
    } else if (lowerInput.includes('goodbye') || lowerInput.includes('bye')) {
      reply = 'Goodbye! If you need help again, just open the chatbot.';
    } else if (lowerInput.includes('discussion') || lowerInput.includes('thread')) {
      // Search threads
      const keyword = input.replace(/.*about|find|discussion|thread/gi, '').trim();
      const res = await api.get(`/chatbot/search/threads?keyword=${encodeURIComponent(keyword)}`);
      if (res.data.length > 0) {
        reply = 'Found discussions:\n' + res.data.map(t => `• ${t.title}`).join('\n');
      } else {
        reply = 'No discussions found for that keyword.';
      }
    } else if (lowerInput.includes('post')) {
      // Search posts
      const keyword = input.replace(/.*about|find|post/gi, '').trim();
      const res = await api.get(`/chatbot/search/posts?keyword=${encodeURIComponent(keyword)}`);
      if (res.data.length > 0) {
        reply = 'Found posts:\n' + res.data.map(p => `• ${p.content.substring(0, 50)}...`).join('\n');
      } else {
        reply = 'No posts found for that keyword.';
      }
    } else {
      reply = 'Please ask me to find discussions or posts by keyword!';
    }
    setMessages(msgs => [...msgs, { from: 'bot', text: reply }]);
    setInput('');
  };

  return (
    <div>
      <div
        className="chatbot-toggle"
        onClick={() => setOpen(o => !o)}
        style={{
          position: 'fixed',
          bottom: 24,
          right: 24,
          zIndex: 9999,
          cursor: 'pointer',
          background: 'white',
          borderRadius: '50%',
          width: 56,
          height: 56,
          boxShadow: '0 2px 8px rgba(0,0,0,0.2)',
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'center',
          padding: 0,
          border: '2px solid #0d6efd'
        }}
        title="Chatbot"
      >
        <img
          src={chatbotAvatar}
          alt="Chatbot"
          style={{
            width: 44,
            height: 44,
            borderRadius: '50%',
            objectFit: 'cover'
          }}
        />
      </div>
      {open && (
        <div
          className="chatbot-window"
          style={{
            position: 'fixed',
            bottom: 90,
            right: 24,
            width: 320,
            maxHeight: 400,
            background: 'white',
            borderRadius: 12,
            boxShadow: '0 2px 16px rgba(0,0,0,0.2)',
            zIndex: 9999,
            display: 'flex',
            flexDirection: 'column'
          }}
        >
          <div style={{ padding: 12, borderBottom: '1px solid #eee', background: '#0d6efd', color: 'white', borderTopLeftRadius: 12, borderTopRightRadius: 12 }}>
            Chatbot Helper
          </div>
          <div style={{ flex: 1, overflowY: 'auto', padding: 12 }}>
            {messages.map((msg, i) => (
              <div key={i} style={{ textAlign: msg.from === 'bot' ? 'left' : 'right', margin: '8px 0' }}>
                <span style={{
                  display: 'inline-block',
                  background: msg.from === 'bot' ? '#f1f1f1' : '#0d6efd',
                  color: msg.from === 'bot' ? '#333' : 'white',
                  borderRadius: 16,
                  padding: '6px 12px',
                  maxWidth: '80%',
                  wordBreak: 'break-word'
                }}>
                  {msg.text}
                </span>
              </div>
            ))}
          </div>
          <form onSubmit={handleSend} style={{ display: 'flex', borderTop: '1px solid #eee', padding: 8 }}>
            <input
              className="form-control"
              style={{ borderRadius: 16, marginRight: 8 }}
              value={input}
              onChange={e => setInput(e.target.value)}
              placeholder="Type your question..."
            />
            <button className="btn btn-primary" style={{ borderRadius: 16 }} type="submit">Send</button>
          </form>
        </div>
      )}
    </div>
  );
}

export default Chatbot;