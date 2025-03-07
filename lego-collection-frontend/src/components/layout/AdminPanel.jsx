import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { useAuth } from '../auth/AuthContext';
import { useNavigate } from 'react-router-dom';

const AdminPanel = () => {
    const { isAuthenticated, role } = useAuth();
    const [users, setUsers] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const navigate = useNavigate();

    useEffect(() => {
        if (!isAuthenticated || role !== 'ADMIN') {
            navigate('/login');
        }

        const token = localStorage.getItem('token');

        axios.get('/api/admin/users', {
            headers: { Authorization: `Bearer ${token}` }
        })
        .then(response => {
            setUsers(response.data);
            setLoading(false);
        })
        .catch(error => {
            setError(error);
            setLoading(false);
        });
    }, [isAuthenticated, role, navigate]);

    const promoteToAdmin = async (userId) => {
        const token = localStorage.getItem('token');

        try {
            await axios.post(`/api/admin/promote/${userId}`, {}, {
                headers: { Authorization: `Bearer ${token}` }
            });

            setUsers(users.map(user => 
                user.id === userId ? { ...user, role: 'ADMIN' } : user
            ));
        } catch (error) {
            console.error("Hiba történt az admin jogosultság kiosztásakor:", error);
        }
    };

    const revokeAdmin = async (userId) => {
        const token = localStorage.getItem('token');
        
        try {
            await axios.post(`/api/admin/revoke/${userId}`, {}, {
                headers: { Authorization: `Bearer ${token}` }
            });
            
            setUsers(users.map(user => 
                user.id === userId ? { ...user, role: 'USER' } : user
            ));
        } catch (error) {
            console.error("Hiba történt az admin jogosultság visszavonásakor:", error);
        }
    };

    if (loading) return <p className="text-center mt-4">Betöltés...</p>;
    if (error) return <p className="text-center mt-4 text-red-500">Hiba történt: {error.message}</p>;

    return (
        <div className="container mx-auto px-4 py-8">
            <h1 className="text-3xl font-bold mb-4">Admin Panel - Felhasználók kezelése</h1>
            <table className="w-full border-collapse border border-gray-300">
                <thead>
                    <tr className="bg-gray-200">
                        <th className="border border-gray-300 px-4 py-2">Felhasználónév</th>
                        <th className="border border-gray-300 px-4 py-2">Email</th>
                        <th className="border border-gray-300 px-4 py-2">Szerepkör</th>
                        <th className="border border-gray-300 px-4 py-2">Műveletek</th>
                    </tr>
                </thead>
                <tbody>
                    {users.map(user => (
                        <tr key={user.id} className="text-center">
                            <td className="border border-gray-300 px-4 py-2">{user.username}</td>
                            <td className="border border-gray-300 px-4 py-2">{user.email}</td>
                            <td className="border border-gray-300 px-4 py-2">
                                {user.role.startsWith('ROLE_') ? user.role.substring(5) : user.role}
                            </td>
                            <td className="border border-gray-300 px-4 py-2">
                                {user.role !== 'ADMIN' && (
                                    <button
                                        onClick={() => promoteToAdmin(user.id)}
                                        className="bg-blue-500 text-white px-3 py-1 rounded hover:bg-blue-600"
                                    >
                                        Adminná tesz
                                    </button>
                                )}
                                {user.role === 'ADMIN' && (
                                    <button
                                        onClick={() => revokeAdmin(user.id)}
                                        className="bg-red-500 text-white px-3 py-1 rounded hover:bg-red-600"
                                    >
                                        Admin jogosultság visszavonása
                                    </button>
                                )}
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
};

export default AdminPanel;
