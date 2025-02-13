import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

const Login = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [errorMessage, setErrorMessage] = useState('');
    const navigate = useNavigate();

    const handleLogin = async (e) => {
        e.preventDefault();
        try {
            await axios.get('/api/user/collection', {
                auth: { username, password }
            });
            localStorage.setItem('auth', JSON.stringify({ username, password }));
            navigate('/collection');
        } catch (error) {
            console.error(error);
            setErrorMessage('Hibás felhasználónév vagy jelszó.');
        }
    };

    return (
        <div className="container mx-auto px-4 py-8">
            <h1 className="text-3xl font-bold mb-4">Bejelentkezés</h1>
            {errorMessage && <p className="mb-4 text-red-500">{errorMessage}</p>}
            <form onSubmit={handleLogin} className="max-w-md mx-auto bg-white p-6 rounded shadow">
                <div className="mb-4">
                    <label className="block mb-1 font-bold">Felhasználónév</label>
                    <input
                        type="text"
                        className="w-full border border-gray-300 p-2 rounded"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                        required
                    />
                </div>
                <div className="mb-4">
                    <label className="block mb-1 font-bold">Jelszó</label>
                    <input
                        type="password"
                        className="w-full border border-gray-300 p-2 rounded"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        required
                    />
                </div>
                <button type="submit" className="w-full bg-blue-500 text-white py-2 rounded hover:bg-blue-600">
                    Bejelentkezés
                </button>
            </form>
        </div>
    );
};

export default Login;