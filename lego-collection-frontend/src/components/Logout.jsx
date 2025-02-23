import React from 'react';
import { useNavigate } from 'react-router-dom';

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