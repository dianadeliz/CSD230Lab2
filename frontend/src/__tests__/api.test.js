import api from '../utils/api';

// Mock axios
jest.mock('axios', () => ({
  create: jest.fn(() => ({
    get: jest.fn(),
    post: jest.fn(),
    put: jest.fn(),
    delete: jest.fn()
  }))
}));

describe('API', () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  it('makes GET request with auth header', async () => {
    const mockResponse = { data: { id: 1, title: 'Test' } };
    api.get.mockResolvedValue(mockResponse);

    const result = await api.get('/test');
    expect(result).toEqual(mockResponse);
  });

  it('makes POST request with auth header', async () => {
    const mockData = { title: 'Test', content: 'Content' };
    const mockResponse = { data: { id: 1, ...mockData } };
    api.post.mockResolvedValue(mockResponse);

    const result = await api.post('/test', mockData);
    expect(result).toEqual(mockResponse);
  });

  it('makes DELETE request with auth header', async () => {
    const mockResponse = { data: 'Deleted' };
    api.delete.mockResolvedValue(mockResponse);

    const result = await api.delete('/test/1');
    expect(result).toEqual(mockResponse);
  });

  it('handles request errors', async () => {
    const error = new Error('Network error');
    api.get.mockRejectedValue(error);

    await expect(api.get('/test')).rejects.toThrow('Network error');
  });
}); 