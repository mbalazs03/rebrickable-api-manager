import React, { useEffect, useState } from 'react';
import axios from 'axios';

const CollectionList = () => {
    const [sets, setSets] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        axios.get('/api/user/collection', {
            auth: {
                username: 'legoFan',
                password: 'secret123'
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
    }, []);

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
                        <div key={set.setNum} className="bg-white shadow-md rounded p-4">
                            <img src={set.setImgUrl} alt={set.name} className="w-full h-48 object-cover rounded"/>
                            <h2 className="text-xl font-semibold mt-2">{set.name}</h2>
                            <p className="text-gray-600">Év: {set.year}</p>
                            <p className="text-gray-600">Részek száma: {set.numParts}</p>
                        </div>
                    ))}
                </div>
            )}
        </div>
    );
};

export default CollectionList;
