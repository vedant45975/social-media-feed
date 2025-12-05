import React, { useState, useEffect } from 'react';
import './Feed.css';
import CreatePostModal from './CreatePostModal';

const Feed = ({ user, onLogout }) => {
  const [posts, setPosts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [showCreateModal, setShowCreateModal] = useState(false);
  const [refreshing, setRefreshing] = useState(false);

  const fetchFeed = async () => {
    try {
      setError('');
      const response = await fetch('http://localhost:8080/api/posts/feed', {
        headers: {
          'X-User-Id': user.id.toString(),
        },
      });

      if (!response.ok) {
        throw new Error('Failed to fetch feed');
      }

      const data = await response.json();
      setPosts(data);
    } catch (err) {
      setError('Failed to load feed. Please try again.');
      console.error('Error fetching feed:', err);
    } finally {
      setLoading(false);
      setRefreshing(false);
    }
  };

  useEffect(() => {
    fetchFeed();
  }, [user.id]);

  const handleCreatePost = async (content) => {
    try {
      const response = await fetch('http://localhost:8080/api/posts', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'X-User-Id': user.id.toString(),
        },
        body: JSON.stringify({ content }),
      });

      const data = await response.json();

      if (!response.ok) {
        throw new Error(data.error || 'Failed to create post');
      }

      setShowCreateModal(false);
      setRefreshing(true);
      fetchFeed();
    } catch (err) {
      setError(err.message || 'Failed to create post. Please try again.');
      console.error('Error creating post:', err);
    }
  };

  const handleToggleLike = async (postId) => {
    try {
      const response = await fetch(`http://localhost:8080/api/likes/toggle/${postId}`, {
        method: 'POST',
        headers: {
          'X-User-Id': user.id.toString(),
        },
      });

      if (!response.ok) {
        throw new Error('Failed to toggle like');
      }

      // Update the post in the local state
      setPosts((prevPosts) =>
        prevPosts.map((post) => {
          if (post.id === postId) {
            return {
              ...post,
              isLiked: !post.isLiked,
              likeCount: post.isLiked ? post.likeCount - 1 : post.likeCount + 1,
            };
          }
          return post;
        })
      );
    } catch (err) {
      setError('Failed to toggle like. Please try again.');
      console.error('Error toggling like:', err);
    }
  };

  const formatDate = (dateString) => {
    const date = new Date(dateString);
    const now = new Date();
    const diffInSeconds = Math.floor((now - date) / 1000);

    if (diffInSeconds < 60) {
      return 'just now';
    } else if (diffInSeconds < 3600) {
      const minutes = Math.floor(diffInSeconds / 60);
      return `${minutes} minute${minutes > 1 ? 's' : ''} ago`;
    } else if (diffInSeconds < 86400) {
      const hours = Math.floor(diffInSeconds / 3600);
      return `${hours} hour${hours > 1 ? 's' : ''} ago`;
    } else {
      const days = Math.floor(diffInSeconds / 86400);
      return `${days} day${days > 1 ? 's' : ''} ago`;
    }
  };

  return (
    <div className="feed-container">
      <header className="feed-header">
        <h1>Social Media Feed</h1>
        <div className="header-actions">
          <span className="username">Welcome, {user.username}!</span>
          <button onClick={onLogout} className="logout-button">
            Logout
          </button>
        </div>
      </header>

      <main className="feed-main">
        {loading ? (
          <div className="loading-container">
            <div className="spinner"></div>
            <p>Loading feed...</p>
          </div>
        ) : error ? (
          <div className="error-container">
            <p>{error}</p>
            <button onClick={fetchFeed} className="retry-button">
              Retry
            </button>
          </div>
        ) : posts.length === 0 ? (
          <div className="empty-feed">
            <p>No posts yet. Be the first to create a post!</p>
          </div>
        ) : (
          <div className="posts-container">
            {posts.map((post) => (
              <div key={post.id} className="post-card">
                <div className="post-header">
                  <span className="post-username">{post.username}</span>
                  <span className="post-time">{formatDate(post.createdAt)}</span>
                </div>
                <div className="post-content">{post.content}</div>
                <div className="post-actions">
                  <button
                    onClick={() => handleToggleLike(post.id)}
                    className={`like-button ${post.isLiked ? 'liked' : ''}`}
                  >
                    <span className="like-icon">{post.isLiked ? '❤️' : '🤍'}</span>
                    <span>{post.likeCount}</span>
                  </button>
                </div>
              </div>
            ))}
          </div>
        )}
      </main>

      <button
        className="create-post-button"
        onClick={() => setShowCreateModal(true)}
        title="Create new post"
      >
        +
      </button>

      {showCreateModal && (
        <CreatePostModal
          onClose={() => setShowCreateModal(false)}
          onSubmit={handleCreatePost}
        />
      )}
    </div>
  );
};

export default Feed;

