// src/App.js
import React, { useState, useEffect } from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import Threads from './pages/Threads';
import Posts from './pages/Posts';
import Login from './pages/Login';
import Register from './pages/Register';
import Layout from './components/Layout';

function App() {
  const [isLoggedIn, setIsLoggedIn] = useState(!!localStorage.getItem('token'));

  // Listen for changes to localStorage (e.g., login/logout in other tabs)
  useEffect(() => {
    const handleStorage = () => setIsLoggedIn(!!localStorage.getItem('token'));
    window.addEventListener('storage', handleStorage);
    return () => window.removeEventListener('storage', handleStorage);
  }, []);

  // Pass setIsLoggedIn to Login so it can update after successful login
  return (
    <Router>
      <Routes>
        <Route path="/register" element={<Register />} />
        <Route path="/" element={isLoggedIn ? <Navigate to="/threads" /> : <Login setIsLoggedIn={setIsLoggedIn} />} />
        <Route
          path="/threads"
          element={
            isLoggedIn ? (
              <Layout setIsLoggedIn={setIsLoggedIn}><Threads /></Layout>
            ) : (
              <Navigate to="/" />
            )
          }
        />
        <Route
          path="/posts/:threadId"
          element={
            isLoggedIn ? (
              <Layout setIsLoggedIn={setIsLoggedIn}><Posts /></Layout>
            ) : (
              <Navigate to="/" />
            )
          }
        />
        {/* Catch-all: redirect unknown routes */}
        <Route path="*" element={<Navigate to={isLoggedIn ? "/threads" : "/"} />} />
      </Routes>
    </Router>
  );
}

export default App;

