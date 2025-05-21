import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
// import './Navbar.css'; // Removed because the file does not exist

function Navbar({ setIsLoggedIn }) {
  const navigate = useNavigate();

  const handleLogout = () => {
    localStorage.removeItem('token');
    localStorage.removeItem('username');
    localStorage.removeItem('role');
    setIsLoggedIn(false);
    navigate('/');
  };

  return (
    <nav className="navbar navbar-expand-lg navbar-light bg-white shadow-sm px-3">
      <Link className="navbar-brand fw-bold" to="/threads">Discussion Board</Link>
      <div className="collapse navbar-collapse">
        <ul className="navbar-nav ms-auto mb-2 mb-lg-0">
          <li className="nav-item">
            <button className="btn btn-outline-danger ms-2" onClick={handleLogout}>Logout</button>
          </li>
        </ul>
      </div>
    </nav>
  );
}

export default Navbar;
