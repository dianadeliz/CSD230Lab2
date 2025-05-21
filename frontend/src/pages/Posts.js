import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import api from '../utils/api';

function Posts() {
  const { threadId } = useParams();
  const [posts, setPosts] = useState([]);
  const [message, setMessage] = useState('');
  const [postToDelete, setPostToDelete] = useState(null);
  const isAdmin = localStorage.getItem('role') === 'admin';

  useEffect(() => {
    fetchPosts();
  }, []);

  const fetchPosts = async () => {
    const res = await api.get(`/posts/thread/${threadId}`);
    setPosts(res.data);
  };

  const createPost = async (e) => {
    e.preventDefault();
    await api.post(`/posts`, {
      content: message,
      threadId,
      username: localStorage.getItem('username'),
      createdAt: Date.now()
    });
    setMessage('');
    fetchPosts();
    window.bootstrap.Modal.getInstance(document.getElementById('replyModal')).hide();
  };

  const confirmDelete = async () => {
    if (postToDelete) {
      await api.delete(`/posts/${postToDelete}`);
      setPostToDelete(null);
      fetchPosts();
    }
    window.bootstrap.Modal.getInstance(document.getElementById('deletePostModal')).hide();
  };

  return (
    <div className="container py-4 px-3 px-md-5">
      {/* Header Card */}
      <div className="card shadow mb-4 bg-primary text-white border-0 rounded-4">
        <div className="card-body d-flex flex-column flex-md-row justify-content-between align-items-center p-4">
          <h3 className="mb-3 mb-md-0 fw-bold" style={{ letterSpacing: '1px' }}>Posts</h3>
          <button className="btn btn-light text-primary fw-bold px-4 py-2 rounded-pill shadow-sm" data-bs-toggle="modal" data-bs-target="#replyModal">
            + Reply
          </button>
        </div>
      </div>

      <ul className="list-group w-100">
        {posts.map((post, index) => (
          <li key={index} className="list-group-item d-flex justify-content-between flex-column">
            <div>
              <strong>{post.username}</strong>
            </div>
            <div>{post.content}</div>
            <div className="d-flex justify-content-between align-items-center mt-2">
              <small className="text-muted">{new Date(post.createdAt).toLocaleString()}</small>
              {isAdmin && (
                <button
                  className="btn btn-sm btn-danger"
                  data-bs-toggle="modal"
                  data-bs-target="#deletePostModal"
                  onClick={() => setPostToDelete(post.id)}
                >
                  Delete
                </button>
              )}
            </div>
          </li>
        ))}
      </ul>

      {/* Reply Modal */}
      <div className="modal fade" id="replyModal" tabIndex="-1">
        <div className="modal-dialog">
          <div className="modal-content bg-dark text-white">
            <div className="modal-header">
              <h5 className="modal-title">Write a Reply</h5>
              <button className="btn-close btn-close-white" data-bs-dismiss="modal"></button>
            </div>
            <form onSubmit={createPost}>
              <div className="modal-body">
                <input
                  type="text"
                  className="form-control"
                  value={message}
                  onChange={(e) => setMessage(e.target.value)}
                  placeholder="Write your reply"
                  required
                />
              </div>
              <div className="modal-footer">
                <button className="btn btn-success" type="submit">Post</button>
              </div>
            </form>
          </div>
        </div>
      </div>

      {/* Confirm Delete Modal for Posts */}
      <div className="modal fade" id="deletePostModal" tabIndex="-1">
        <div className="modal-dialog">
          <div className="modal-content">
            <div className="modal-header">
              <h5 className="modal-title">Delete Post</h5>
              <button className="btn-close" data-bs-dismiss="modal"></button>
            </div>
            <div className="modal-body">
              Are you sure you want to delete this post?
            </div>
            <div className="modal-footer">
              <button className="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
              <button className="btn btn-danger" onClick={confirmDelete}>Yes, Delete</button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default Posts;
