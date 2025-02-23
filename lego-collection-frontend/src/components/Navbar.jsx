import React from 'react';
import { Link } from 'react-router-dom';
import { useAuth } from './AuthContext';

const Navbar = () => {
    const { isAuthenticated, logout } = useAuth();

    const handleLogout = () => {
        logout();
    };

    return (
        <nav className="bg-gray-800 text-white p-4 flex justify-between items-center">
            <Link to="/" className="text-xl font-bold">LEGO Gyűjtemény</Link>
            <div>
                {isAuthenticated ? (
                    <>
                        <Link to="/collection" className="mr-4 hover:text-gray-300">
                            Gyűjtemény
                        </Link>
                        <button 
                            onClick={handleLogout}
                            className="hover:text-gray-300"
                        >
                            Kijelentkezés
                        </button>
                    </>
                ) : (
                    <>
                        <Link to="/login" className="mr-4 hover:text-gray-300">
                            Bejelentkezés
                        </Link>
                        <Link to="/register" className="hover:text-gray-300">
                            Regisztráció
                        </Link>
                    </>
                )}
            </div>
        </nav>
    );
};

export default Navbar;