import React from 'react';
import { useNavigate } from 'react-router-dom';

const LegoSetCard = ({ set, onAddToCollection, onRemoveFromCollection }) => {
    const navigate = useNavigate();

    return (
        <div className="bg-white shadow-md rounded p-4">
            <img 
                src={set.set_img_url} 
                alt={set.name} 
                className="w-full h-48 object-cover rounded cursor-pointer"
                onClick={() => navigate(`/set/${set.set_num}`)}
            />
            <h2 className="text-xl font-semibold mt-2">{set.name}</h2>
            <p className="text-gray-600">Év: {set.year}</p>
            <p className="text-gray-600">Részek száma: {set.num_parts}</p>

            {onAddToCollection && (
                <button 
                    onClick={() => onAddToCollection(set)}
                    className="w-full bg-green-500 text-white py-2 rounded hover:bg-green-600 mt-4"
                >
                    Hozzáadás a gyűjteményhez
                </button>
            )}

            {onRemoveFromCollection && (
                <button 
                    onClick={() => onRemoveFromCollection(set)}
                    className="w-full bg-red-500 text-white py-2 rounded hover:bg-red-600 mt-4"
                >
                    Eltávolítás a gyűjteményből
                </button>
            )}
        </div>
    );
};

export default LegoSetCard;