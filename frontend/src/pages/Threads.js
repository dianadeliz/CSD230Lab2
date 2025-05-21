import React, { useState, useEffect } from 'react';
import api from '../utils/api';
import { Link } from 'react-router-dom';

function Threads() {
  const [threads, setThreads] = useState([]);
  const [title, setTitle] = useState('');
  const [threadToDelete, setThreadToDelete] = useState(null);
  const isAdmin = localStorage.getItem('role') === 'admin';

  useEffect(() => {
    fetchThreads();
  }, []);

  const fetchThreads = async () => {
    const res = await api.get('/threads');
    setThreads(res.data);
  };

  const createThread = async (e) => {
    e.preventDefault();
    await api.post('/threads', {
      title,
      username: localStorage.getItem('username'),
      createdAt: Date.now(),
    });
    setTitle('');
    fetchThreads();
    window.bootstrap.Modal.getInstance(document.getElementById('threadModal')).hide();
  };

  const confirmDelete = async () => {
    if (threadToDelete) {
      await api.delete(`/threads/${threadToDelete}`);
      setThreadToDelete(null);
      fetchThreads();
    }
    window.bootstrap.Modal.getInstance(document.getElementById('deleteModal')).hide();
  };

  return (
    <div className="container py-4 px-3 px-md-5">
      {/* Header Card */}
      <div className="card shadow mb-4 bg-primary text-white border-0 rounded-4">
        <div className="card-body d-flex flex-column flex-md-row justify-content-between align-items-center p-4">
          <h2 className="mb-3 mb-md-0 fw-bold" style={{ letterSpacing: '1px' }}>Discussion Threads</h2>
          <button className="btn btn-light text-primary fw-bold px-4 py-2 rounded-pill shadow-sm" data-bs-toggle="modal" data-bs-target="#threadModal">
            + New Thread
          </button>
        </div>
      </div>

      <div className="row">
        {threads.map(thread => (
          <div className="col-12 col-md-6 col-lg-4 mb-3" key={thread.id}>
            <div className="card shadow-sm p-3 h-100">
              <h5>{thread.title}</h5>
              <small className="text-muted">
                Posted by {thread.username} <br />
                {new Date(thread.createdAt).toLocaleString()}
              </small>
              <Link to={`/posts/${thread.id}`} className="btn btn-outline-secondary mt-3 w-100">
                View Posts
              </Link>
              {isAdmin && (
                <button
                  className="btn btn-danger btn-sm mt-2 w-100"
                  data-bs-toggle="modal"
                  data-bs-target="#deleteModal"
                  onClick={() => setThreadToDelete(thread.id)}
                >
                  Delete Thread
                </button>
              )}
            </div>
          </div>
        ))}
      </div>

      {/* Create Thread Modal */}
      <div className="modal fade" id="threadModal" tabIndex="-1">
        <div className="modal-dialog">
          <div className="modal-content bg-dark text-white">
            <div className="modal-header">
              <h5 className="modal-title">New Thread</h5>
              <button className="btn-close btn-close-white" data-bs-dismiss="modal"></button>
            </div>
            <form onSubmit={createThread}>
              <div className="modal-body">
                <input
                  type="text"
                  className="form-control"
                  value={title}
                  onChange={(e) => setTitle(e.target.value)}
                  placeholder="Enter thread title"
                  required
                />
              </div>
              <div className="modal-footer">
                <button className="btn btn-success" type="submit">Create</button>
              </div>
            </form>
          </div>
        </div>
      </div>

      {/* Confirm Delete Modal */}
      <div className="modal fade" id="deleteModal" tabIndex="-1">
        <div className="modal-dialog">
          <div className="modal-content">
            <div className="modal-header">
              <h5 className="modal-title">Confirm Delete</h5>
              <button className="btn-close" data-bs-dismiss="modal"></button>
            </div>
            <div className="modal-body">
              Are you sure you want to delete this thread?
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

export default Threads;
