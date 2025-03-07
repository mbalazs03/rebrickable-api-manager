import React, { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from './AuthContext';

const Logout = () => {
    const navigate = useNavigate();

    const handleLogout = () => {
        localStorage.removeItem('token');
        navigate('/login');
    };

    return (
        <button 
            onClick={handleLogout}
            className="text-white hover:text-gray-300"
        >
            Kijelentkezés
        </button>
    );
};

export default Logout;