import React from 'react';
import { render, screen, fireEvent } from '@testing-library/react';
import { BrowserRouter } from 'react-router-dom';
import Login from '../../pages/Login';
import api from '../../utils/api';

// Mock the api module
jest.mock('../../utils/api', () => ({
  post: jest.fn()
}));

// Mock localStorage
const mockLocalStorage = {
  getItem: jest.fn(),
  setItem: jest.fn(),
  removeItem: jest.fn()
};
Object.defineProperty(window, 'localStorage', { value: mockLocalStorage });

describe('Login Component', () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  it('renders login form correctly', () => {
    render(
      <BrowserRouter>
        <Login />
      </BrowserRouter>
    );

    expect(screen.getByPlaceholderText('Username')).toBeInTheDocument();
    expect(screen.getByPlaceholderText('Password')).toBeInTheDocument();
    expect(screen.getByText('Login')).toBeInTheDocument();
  });

  it('handles successful login', async () => {
    const mockResponse = {
      data: {
        token: 'test-token',
        username: 'testuser',
        role: 'USER'
      }
    };
    api.post.mockResolvedValue(mockResponse);

    render(
      <BrowserRouter>
        <Login />
      </BrowserRouter>
    );

    // Fill in the form
    fireEvent.change(screen.getByPlaceholderText('Username'), {
      target: { value: 'testuser' }
    });
    fireEvent.change(screen.getByPlaceholderText('Password'), {
      target: { value: 'password' }
    });

    // Submit the form
    fireEvent.click(screen.getByText('Login'));

    // Wait for the API call to complete
    await screen.findByText('Login');

    // Verify API call
    expect(api.post).toHaveBeenCalledWith('/login', {
      username: 'testuser',
      password: 'password'
    });

    // Verify localStorage was updated
    expect(mockLocalStorage.setItem).toHaveBeenCalledWith('token', 'test-token');
    expect(mockLocalStorage.setItem).toHaveBeenCalledWith('username', 'testuser');
    expect(mockLocalStorage.setItem).toHaveBeenCalledWith('role', 'USER');
  });

  it('handles login error', async () => {
    api.post.mockRejectedValue(new Error('Invalid credentials'));

    render(
      <BrowserRouter>
        <Login />
      </BrowserRouter>
    );

    // Fill in the form
    fireEvent.change(screen.getByPlaceholderText('Username'), {
      target: { value: 'testuser' }
    });
    fireEvent.change(screen.getByPlaceholderText('Password'), {
      target: { value: 'wrongpassword' }
    });

    // Submit the form
    fireEvent.click(screen.getByText('Login'));

    // Wait for error message
    const errorMessage = await screen.findByText('Invalid credentials');
    expect(errorMessage).toBeInTheDocument();
  });
}); 