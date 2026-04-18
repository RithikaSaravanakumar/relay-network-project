import React, { useEffect } from 'react';
import { Navigate, useNavigate } from 'react-router-dom';
import { validateToken } from '../api';

export default function ProtectedRoute({ children }) {
  const token = localStorage.getItem('relay_token');
  const navigate = useNavigate();

  useEffect(() => {
    if (token) {
      validateToken().catch(() => {
        localStorage.removeItem('relay_token');
        localStorage.removeItem('relay_user');
        navigate('/login');
      });
    }
  }, [token, navigate]);
  
  if (!token) {
    return <Navigate to="/login" replace />;
  }

  return children;
}
