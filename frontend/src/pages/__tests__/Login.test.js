import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import { BrowserRouter } from 'react-router-dom';
import Login from '../Login';
import { login } from '../../utils/api';

// Mock the api module
jest.mock('../../utils/api', () => ({
    login: jest.fn()
}));

// Mock useNavigate
const mockNavigate = jest.fn();
jest.mock('react-router-dom', () => ({
    ...jest.requireActual('react-router-dom'),
    useNavigate: () => mockNavigate
}));

describe('Login Component', () => {
    beforeEach(() => {
        // Clear all mocks before each test
        jest.clearAllMocks();
    });

    const renderLogin = () => {
        return render(
            <BrowserRouter>
                <Login />
            </BrowserRouter>
        );
    };

    test('renders login form', () => {
        renderLogin();
        
        expect(screen.getByLabelText(/username/i)).toBeInTheDocument();
        expect(screen.getByLabelText(/password/i)).toBeInTheDocument();
        expect(screen.getByRole('button', { name: /login/i })).toBeInTheDocument();
    });

    test('handles successful login', async () => {
        const mockResponse = {
            token: 'test-token',
            username: 'testuser',
            role: 'USER'
        };
        
        login.mockResolvedValueOnce(mockResponse);
        
        renderLogin();
        
        // Fill in the form
        fireEvent.change(screen.getByLabelText(/username/i), {
            target: { value: 'testuser' }
        });
        fireEvent.change(screen.getByLabelText(/password/i), {
            target: { value: 'password123' }
        });
        
        // Submit the form
        fireEvent.click(screen.getByRole('button', { name: /login/i }));
        
        // Wait for the login API call to complete
        await waitFor(() => {
            expect(login).toHaveBeenCalledWith('testuser', 'password123');
            expect(mockNavigate).toHaveBeenCalledWith('/threads');
        });
    });

    test('handles login error', async () => {
        login.mockRejectedValueOnce(new Error('Invalid credentials'));
        
        renderLogin();
        
        // Fill in the form
        fireEvent.change(screen.getByLabelText(/username/i), {
            target: { value: 'testuser' }
        });
        fireEvent.change(screen.getByLabelText(/password/i), {
            target: { value: 'wrongpassword' }
        });
        
        // Submit the form
        fireEvent.click(screen.getByRole('button', { name: /login/i }));
        
        // Wait for error message
        await waitFor(() => {
            expect(screen.getByText(/invalid credentials/i)).toBeInTheDocument();
        });
    });

    test('validates required fields', async () => {
        renderLogin();
        
        // Try to submit without filling the form
        fireEvent.click(screen.getByRole('button', { name: /login/i }));
        
        // Check for validation messages
        expect(screen.getByText(/username is required/i)).toBeInTheDocument();
        expect(screen.getByText(/password is required/i)).toBeInTheDocument();
        
        // Verify login was not called
        expect(login).not.toHaveBeenCalled();
    });
}); 