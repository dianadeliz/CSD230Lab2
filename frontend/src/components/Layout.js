// src/components/Layout.js
import React from 'react';
import { Link } from 'react-router-dom';
import './Layout.css';
import Navbar from './Navbar'; // ✅ Import the Navbar

function Layout({ children, setIsLoggedIn }) {
  return (
    <div className="bg-light min-vh-100 d-flex flex-column">
      <Navbar setIsLoggedIn={setIsLoggedIn} /> {/* ✅ Add the Navbar here */}

      <div className="container my-4 flex-grow-1">
        {children}
      </div>
    </div>
  );
}

export default Layout;
