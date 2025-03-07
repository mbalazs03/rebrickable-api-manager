import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import SetCard from '../common/SetCard';
import Pagination from '../common/Pagination';

const CollectionList = () => {
    const [sets, setSets] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const navigate = useNavigate();

    useEffect(() => {
        const token = localStorage.getItem('token');
        if (!token) {
            navigate('/login');
            return;
        }

        axios.get('/api/user/collection', {
            headers: {
                Authorization: `Bearer ${token}`
            }
        })
        .then(response => {
            setSets(response.data);
            setLoading(false);
        })
        .catch(error => {
            setError(error);
            setLoading(false);
        });
    }, [navigate]);

    const token = localStorage.getItem('token');

    const removeFromCollection = async (legoSet) => {
        try {
            await axios.put(`/api/user/collection/${legoSet.set_num}?owned=false`, {}, {
                headers: {
                    Authorization: `Bearer ${token}`
                }
            });
            setSets(sets.filter(set => set['set_num'] !== legoSet.set_num));
        } catch (error) {
            console.error('Hiba történt a készlet eltávolításakor:', error);
        }
    };

    if (loading) {
        return <div className="text-center mt-4">Betöltés...</div>;
    }

    if (error) {
        return <div className="text-center mt-4 text-red-500">Hiba történt: {error.message}</div>;
    }

    return (
        <div className="container mx-auto px-4 py-8">
            <h1 className="text-3xl font-bold mb-4">Saját LEGO Készlet Gyűjtemény</h1>
            {sets.length === 0 ? (
                <p>Nincs megjeleníthető készleted.</p>
            ) : (
                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
                    {sets.map((set) => (
                        <SetCard key={set['set_num']} set={set} onRemoveFromCollection={removeFromCollection}/>
                    ))}
                </div>
            )}
        </div>
    );
};

export default CollectionList;