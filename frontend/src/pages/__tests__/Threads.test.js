import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import { BrowserRouter } from 'react-router-dom';
import Threads from '../Threads';
import { getThreads, createThread, deleteThread } from '../../utils/api';

// Mock the api module
jest.mock('../../utils/api', () => ({
    getThreads: jest.fn(),
    createThread: jest.fn(),
    deleteThread: jest.fn()
}));

// Mock useNavigate
const mockNavigate = jest.fn();
jest.mock('react-router-dom', () => ({
    ...jest.requireActual('react-router-dom'),
    useNavigate: () => mockNavigate
}));

describe('Threads Component', () => {
    const mockThreads = [
        { id: '1', title: 'Thread 1', username: 'user1', createdAt: '2024-03-20T10:00:00Z' },
        { id: '2', title: 'Thread 2', username: 'user2', createdAt: '2024-03-20T11:00:00Z' }
    ];

    beforeEach(() => {
        // Clear all mocks before each test
        jest.clearAllMocks();
        // Mock localStorage
        Storage.prototype.getItem = jest.fn(() => 'test-token');
    });

    const renderThreads = () => {
        return render(
            <BrowserRouter>
                <Threads />
            </BrowserRouter>
        );
    };

    test('renders threads list', async () => {
        getThreads.mockResolvedValueOnce(mockThreads);
        
        renderThreads();
        
        // Wait for threads to load
        await waitFor(() => {
            expect(screen.getByText('Thread 1')).toBeInTheDocument();
            expect(screen.getByText('Thread 2')).toBeInTheDocument();
        });
    });

    test('handles thread creation', async () => {
        getThreads.mockResolvedValueOnce(mockThreads);
        createThread.mockResolvedValueOnce({ id: '3', title: 'New Thread', username: 'testuser' });
        
        renderThreads();
        
        // Wait for initial threads to load
        await waitFor(() => {
            expect(screen.getByText('Thread 1')).toBeInTheDocument();
        });
        
        // Open create thread modal
        fireEvent.click(screen.getByText(/create thread/i));
        
        // Fill in the form
        fireEvent.change(screen.getByLabelText(/title/i), {
            target: { value: 'New Thread' }
        });
        
        // Submit the form
        fireEvent.click(screen.getByRole('button', { name: /create/i }));
        
        // Verify thread was created
        await waitFor(() => {
            expect(createThread).toHaveBeenCalledWith('New Thread');
            expect(getThreads).toHaveBeenCalledTimes(2); // Initial load + refresh after create
        });
    });

    test('handles thread deletion for admin', async () => {
        // Mock admin role
        Storage.prototype.getItem = jest.fn((key) => {
            if (key === 'token') return 'test-token';
            if (key === 'role') return 'ADMIN';
            return null;
        });

        getThreads.mockResolvedValueOnce(mockThreads);
        deleteThread.mockResolvedValueOnce({ message: 'Thread deleted' });
        
        renderThreads();
        
        // Wait for threads to load
        await waitFor(() => {
            expect(screen.getByText('Thread 1')).toBeInTheDocument();
        });
        
        // Click delete button for first thread
        const deleteButtons = screen.getAllByText(/delete/i);
        fireEvent.click(deleteButtons[0]);
        
        // Confirm deletion
        fireEvent.click(screen.getByRole('button', { name: /confirm/i }));
        
        // Verify thread was deleted
        await waitFor(() => {
            expect(deleteThread).toHaveBeenCalledWith('1');
            expect(getThreads).toHaveBeenCalledTimes(2); // Initial load + refresh after delete
        });
    });

    test('does not show delete buttons for non-admin users', async () => {
        // Mock user role
        Storage.prototype.getItem = jest.fn((key) => {
            if (key === 'token') return 'test-token';
            if (key === 'role') return 'USER';
            return null;
        });

        getThreads.mockResolvedValueOnce(mockThreads);
        
        renderThreads();
        
        // Wait for threads to load
        await waitFor(() => {
            expect(screen.getByText('Thread 1')).toBeInTheDocument();
        });
        
        // Verify delete buttons are not present
        expect(screen.queryByText(/delete/i)).not.toBeInTheDocument();
    });

    test('handles error when loading threads', async () => {
        getThreads.mockRejectedValueOnce(new Error('Failed to load threads'));
        
        renderThreads();
        
        // Wait for error message
        await waitFor(() => {
            expect(screen.getByText(/failed to load threads/i)).toBeInTheDocument();
        });
    });
}); 