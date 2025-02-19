import React from 'react';
import { Navigate } from 'react-router-dom';

const ProtectedRoute = ({ children }) => {
    const authData = localStorage.getItem('auth');
    if (!authData) {
        return <Navigate to="/login" replace />;
    }
    return children;
};

export default ProtectedRoute;