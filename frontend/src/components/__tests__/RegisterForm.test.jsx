import React from 'react';
import { render, screen, fireEvent } from '@testing-library/react';
import { BrowserRouter } from 'react-router-dom';
import Register from '../../pages/Register';
import api from '../../utils/api';

// Mock the api module
jest.mock('../../utils/api', () => ({
  post: jest.fn()
}));

describe('Register Component', () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  it('renders register form correctly', () => {
    render(
      <BrowserRouter>
        <Register />
      </BrowserRouter>
    );

    expect(screen.getByPlaceholderText('Username')).toBeInTheDocument();
    expect(screen.getByPlaceholderText('Password')).toBeInTheDocument();
    expect(screen.getByText('Register')).toBeInTheDocument();
  });

  it('handles successful registration', async () => {
    api.post.mockResolvedValue({ data: 'User registered' });

    render(
      <BrowserRouter>
        <Register />
      </BrowserRouter>
    );

    // Fill in the form
    fireEvent.change(screen.getByPlaceholderText('Username'), {
      target: { value: 'newuser' }
    });
    fireEvent.change(screen.getByPlaceholderText('Password'), {
      target: { value: 'password123' }
    });

    // Submit the form
    fireEvent.click(screen.getByText('Register'));

    // Wait for the API call to complete
    await screen.findByText('Register');

    // Verify API call
    expect(api.post).toHaveBeenCalledWith('/register', {
      username: 'newuser',
      password: 'password123',
      role: 'USER'
    });
  });

  it('handles registration error', async () => {
    api.post.mockRejectedValue(new Error('Username already exists'));

    render(
      <BrowserRouter>
        <Register />
      </BrowserRouter>
    );

    // Fill in the form
    fireEvent.change(screen.getByPlaceholderText('Username'), {
      target: { value: 'existinguser' }
    });
    fireEvent.change(screen.getByPlaceholderText('Password'), {
      target: { value: 'password123' }
    });

    // Submit the form
    fireEvent.click(screen.getByText('Register'));

    // Wait for error message
    const errorMessage = await screen.findByText('Username already exists');
    expect(errorMessage).toBeInTheDocument();
  });
}); 