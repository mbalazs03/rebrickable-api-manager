import React from 'react';
import { Link } from 'react-router-dom';
import Logout from './Logout';

const Navbar = () => {
    const isAuthenticated = localStorage.getItem('auth');
    console.log("✅ Navbar is rendering...");
    return (
        <nav className="bg-gray-800 text-white p-4 flex justify-between">
            <h1 className="text-xl font-bold">LEGO Gyűjtemény</h1>
            <div>
                {isAuthenticated ? (
                    <>
                        <Link to="/collection" className="mr-4">Gyűjtemény</Link>
                        <Logout />
                    </>
                ) : (
                    <>
                        <Link to="/login" className="mr-4">Bejelentkezés</Link>
                        <Link to="/register">Regisztráció</Link>
                    </>
                )}
            </div>
        </nav>
    );
};

export default Navbar;