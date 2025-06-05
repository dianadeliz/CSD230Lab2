import React from 'react';
import { render, screen, fireEvent } from '@testing-library/react';
import { BrowserRouter } from 'react-router-dom';
import Posts from '../../pages/Posts';
import api from '../../utils/api';

// Mock the api module
jest.mock('../../utils/api', () => ({
  get: jest.fn(),
  post: jest.fn(),
  delete: jest.fn()
}));

// Mock localStorage
const mockLocalStorage = {
  getItem: jest.fn(),
  setItem: jest.fn(),
  removeItem: jest.fn()
};
Object.defineProperty(window, 'localStorage', { value: mockLocalStorage });

describe('Posts Component', () => {
  const mockPosts = [
    {
      id: '1',
      content: 'Test post 1',
      username: 'user1',
      createdAt: new Date().toISOString()
    },
    {
      id: '2',
      content: 'Test post 2',
      username: 'user2',
      createdAt: new Date().toISOString()
    }
  ];

  beforeEach(() => {
    // Reset all mocks before each test
    jest.clearAllMocks();
    mockLocalStorage.getItem.mockReturnValue('user1');
    api.get.mockResolvedValue({ data: mockPosts });
  });

  it('renders posts correctly', async () => {
    render(
      <BrowserRouter>
        <Posts />
      </BrowserRouter>
    );

    // Wait for posts to be loaded
    const post1 = await screen.findByText('Test post 1');
    const post2 = await screen.findByText('Test post 2');

    expect(post1).toBeInTheDocument();
    expect(post2).toBeInTheDocument();
  });

  it('creates a new post', async () => {
    api.post.mockResolvedValue({ data: { id: '3', content: 'New post', username: 'user1' } });

    render(
      <BrowserRouter>
        <Posts />
      </BrowserRouter>
    );

    // Open reply modal
    const replyButton = screen.getByText('+ Reply');
    fireEvent.click(replyButton);

    // Fill in the form
    const input = screen.getByPlaceholderText('Write your reply');
    fireEvent.change(input, { target: { value: 'New post' } });

    // Submit the form
    const submitButton = screen.getByText('Post');
    fireEvent.click(submitButton);

    // Verify API call
    expect(api.post).toHaveBeenCalledWith('/posts', expect.any(Object));
  });
}); 