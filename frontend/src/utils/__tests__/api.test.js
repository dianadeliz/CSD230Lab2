import axios from 'axios';
import { login, getThreads, createThread, deleteThread } from '../api';

// Mock axios
jest.mock('axios');

describe('API Utility', () => {
    beforeEach(() => {
        // Clear all mocks and localStorage before each test
        jest.clearAllMocks();
        localStorage.clear();
    });

    describe('Token Handling', () => {
        test('sets token in localStorage after successful login', async () => {
            const mockResponse = {
                data: {
                    token: 'test-token',
                    username: 'testuser',
                    role: 'USER'
                }
            };
            axios.post.mockResolvedValueOnce(mockResponse);

            await login('testuser', 'password123');

            expect(localStorage.getItem('token')).toBe('test-token');
            expect(localStorage.getItem('username')).toBe('testuser');
            expect(localStorage.getItem('role')).toBe('USER');
        });

        test('includes Bearer token in authenticated requests', async () => {
            // Set up token in localStorage
            localStorage.setItem('token', 'test-token');

            const mockResponse = {
                data: [{ id: '1', title: 'Thread 1' }]
            };
            axios.get.mockResolvedValueOnce(mockResponse);

            await getThreads();

            expect(axios.get).toHaveBeenCalledWith(
                expect.any(String),
                expect.objectContaining({
                    headers: expect.objectContaining({
                        Authorization: 'Bearer test-token'
                    })
                })
            );
        });

        test('handles missing token gracefully', async () => {
            // Ensure no token is set
            localStorage.removeItem('token');

            const mockResponse = {
                data: [{ id: '1', title: 'Thread 1' }]
            };
            axios.get.mockResolvedValueOnce(mockResponse);

            await getThreads();

            expect(axios.get).toHaveBeenCalledWith(
                expect.any(String),
                expect.objectContaining({
                    headers: expect.not.objectContaining({
                        Authorization: expect.any(String)
                    })
                })
            );
        });
    });

    describe('API Calls', () => {
        test('login makes POST request to correct endpoint', async () => {
            const mockResponse = {
                data: {
                    token: 'test-token',
                    username: 'testuser',
                    role: 'USER'
                }
            };
            axios.post.mockResolvedValueOnce(mockResponse);

            await login('testuser', 'password123');

            expect(axios.post).toHaveBeenCalledWith(
                expect.stringContaining('/api/login'),
                expect.objectContaining({
                    username: 'testuser',
                    password: 'password123'
                })
            );
        });

        test('getThreads makes GET request with auth header', async () => {
            localStorage.setItem('token', 'test-token');
            const mockResponse = {
                data: [{ id: '1', title: 'Thread 1' }]
            };
            axios.get.mockResolvedValueOnce(mockResponse);

            await getThreads();

            expect(axios.get).toHaveBeenCalledWith(
                expect.stringContaining('/api/threads'),
                expect.objectContaining({
                    headers: expect.objectContaining({
                        Authorization: 'Bearer test-token'
                    })
                })
            );
        });

        test('createThread makes POST request with auth header', async () => {
            localStorage.setItem('token', 'test-token');
            const mockResponse = {
                data: { id: '1', title: 'New Thread' }
            };
            axios.post.mockResolvedValueOnce(mockResponse);

            await createThread('New Thread');

            expect(axios.post).toHaveBeenCalledWith(
                expect.stringContaining('/api/threads'),
                expect.objectContaining({
                    title: 'New Thread'
                }),
                expect.objectContaining({
                    headers: expect.objectContaining({
                        Authorization: 'Bearer test-token'
                    })
                })
            );
        });

        test('deleteThread makes DELETE request with auth header', async () => {
            localStorage.setItem('token', 'test-token');
            const mockResponse = {
                data: { message: 'Thread deleted' }
            };
            axios.delete.mockResolvedValueOnce(mockResponse);

            await deleteThread('1');

            expect(axios.delete).toHaveBeenCalledWith(
                expect.stringContaining('/api/threads/1'),
                expect.objectContaining({
                    headers: expect.objectContaining({
                        Authorization: 'Bearer test-token'
                    })
                })
            );
        });
    });

    describe('Error Handling', () => {
        test('handles API errors appropriately', async () => {
            const errorMessage = 'Invalid credentials';
            axios.post.mockRejectedValueOnce({
                response: {
                    data: { message: errorMessage }
                }
            });

            await expect(login('testuser', 'wrongpassword')).rejects.toThrow(errorMessage);
        });

        test('handles network errors appropriately', async () => {
            axios.get.mockRejectedValueOnce(new Error('Network Error'));

            await expect(getThreads()).rejects.toThrow('Network Error');
        });
    });
}); 