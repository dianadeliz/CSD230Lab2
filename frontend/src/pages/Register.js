// Register.js
import React, { useState } from 'react';
import { register } from '../utils/api';
import Layout from '../components/Layout';
import { useNavigate } from 'react-router-dom';

function Register() {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const navigate = useNavigate();

  const handleRegister = async (e) => {
    e.preventDefault();
    await register(username, password);
    navigate('/');
  };

  return (
    <Layout>
      <div className="d-flex justify-content-center align-items-center min-vh-100">
        <div className="card p-4 shadow w-100" style={{ maxWidth: '400px' }}>
          <h4 className="mb-3">Create Account</h4>
          <form onSubmit={handleRegister}>
            <div className="mb-3">
              <input
                type="text"
                className="form-control"
                placeholder="Username"
                value={username}
                onChange={(e) => setUsername(e.target.value)}
                required
              />
            </div>
            <div className="mb-3">
              <input
                type="password"
                className="form-control"
                placeholder="Password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                required
              />
            </div>
            <button className="btn btn-primary w-100" type="submit">Register</button>
          </form>
        </div>
      </div>
    </Layout>
  );
}

export default Register;
