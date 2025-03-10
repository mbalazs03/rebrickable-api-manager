import React from 'react';
import { useNavigate } from 'react-router-dom';

const SetCard = ({ set, completion, missingParts, onAddToCollection, onRemoveFromCollection }) => {
    const navigate = useNavigate();

    return (
        <div className="bg-white shadow-md rounded-lg overflow-hidden">
            <img 
                src={set.set_img_url} 
                alt={set.name} 
                className="w-full h-48 object-cover cursor-pointer"
                onClick={() => navigate(`/set/${set.set_num}`)}
            />
            <div className="p-4">
                <h2 className="text-xl font-semibold">{set.name}</h2>
                <p className="text-gray-600">Év: {set.year}</p>
                <p className="text-gray-600">Részek száma: {set.num_parts}</p>

                {completion !== undefined && (
                    <div className="mt-2">
                        <div className="w-full bg-gray-200 rounded-full h-2">
                            <div
                                style={{ width: `${completion}%` }}
                                className={`h-2 rounded-full transition-width duration-300 ease-in-out ${completion === 100 ? 'bg-green-500' : completion > 50 ? 'bg-yellow-400' : 'bg-red-500'}`}
                            ></div>
                        </div>
                        <p className="text-sm font-medium mt-1">{completion.toFixed(1)}% építhető</p>

                        {missingParts && missingParts.length > 0 && (
                            <details className="text-sm mt-2">
                                <summary className="cursor-pointer text-blue-500 hover:text-blue-700">Hiányzó alkatrészek</summary>
                                <ul className="pl-4 list-disc text-gray-700">
                                    {missingParts.map((part, index) => <li key={index}>{part}</li>)}
                                </ul>
                            </details>
                        )}
                    </div>
                )}

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
        </div>
    );
};

export default SetCard;
