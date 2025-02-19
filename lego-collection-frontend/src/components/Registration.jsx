import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

const Registration = () => {
    const [username, setUsername] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [message, setMessage] = useState('');
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            await axios.post('/api/auth/register', {
                username,
                email,
                password,
            });
            setMessage('Sikeres regisztráció! Most már be tudsz jelentkezni.');
            setTimeout(() => {
                navigate('/login');
            }, 2000);
        } catch (error) {
            console.error(error);
            setMessage('Hiba történt a regisztráció során.');
        }
    };

    return (
        <div className="container mx-auto px-4 py-8">
            <h1 className="text-3xl font-bold mb-4">Regisztráció</h1>
            {message && <p className="mb-4 text-green-600">{message}</p>}
            <form onSubmit={handleSubmit} className="max-w-md mx-auto bg-white p-6 rounded shadow">
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
                    <label className="block mb-1 font-bold">Email</label>
                    <input
                        type="email"
                        className="w-full border border-gray-300 p-2 rounded"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
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
                    Regisztráció
                </button>
            </form>
        </div>
    );
};

export default Registration;