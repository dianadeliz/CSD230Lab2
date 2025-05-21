import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../utils/api';

function Login({ setIsLoggedIn }) {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const navigate = useNavigate();

  const handleLogin = async (e) => {
    e.preventDefault();
    try {
      const res = await api.post('/login', { username, password });
      localStorage.setItem('token', res.data.token);
      localStorage.setItem('username', username);
      localStorage.setItem('role', res.data.role || 'user');
      setIsLoggedIn(true);
      navigate('/threads');
    } catch (err) {
      alert('Login failed. Please try again.');
    }
  };

  return (
    <div className="d-flex justify-content-center align-items-center vh-100 bg-light">
      <div className="card shadow p-4" style={{ width: '100%', maxWidth: '400px' }}>
        <h4 className="mb-3 text-center">Login</h4>
        <form onSubmit={handleLogin}>
          <div className="mb-3">
            <input
              className="form-control"
              type="text"
              placeholder="Username"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              required
            />
          </div>
          <div className="mb-3">
            <input
              className="form-control"
              type="password"
              placeholder="Password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
            />
          </div>
          <button className="btn btn-primary w-100" type="submit">Login</button>
        </form>
      </div>
    </div>
  );
}

export default Login;
