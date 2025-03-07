import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import axios from 'axios';
import Pagination from '../common/Pagination';

const SetDetails = () => {
    const { setNum } = useParams();
    const [setDetails, setSetDetails] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [parts, setParts] = useState([]);
    const [partsPage, setPartsPage] = useState(1);
    const [totalParts, setTotalParts] = useState(0);
    const [hasNextPage, setHasNextPage] = useState(false);
    const [hasPrevPage, setHasPrevPage] = useState(false);
    const pageSize = 20;
    const navigate = useNavigate();

    useEffect(() => {
        const fetchSetDetails = async () => {
            const token = localStorage.getItem('token');
            if (!token) {
                navigate('/login');
                return;
            }

            try {
                const response = await axios.get(`/api/rebrickable/sets/${setNum}`, {
                    headers: {
                        Authorization: `Bearer ${token}`
                    }
                });
                setSetDetails(response.data);
            } catch (error) {
                setError('Hiba az adatok lekérésekor: ' + error.message);
            } finally {
                setLoading(false);
            }
        };

        fetchSetDetails();
    }, [setNum, navigate]);

    const fetchParts = async (page) => {
        const token = localStorage.getItem('token');
        if (!token) {
            navigate('/login');
            return;
        }
        try {
            const response = await axios.get(`/api/rebrickable/sets/${setNum}/parts`, {
                params: {
                    page,
                    pageSize
                },
                headers: {
                    Authorization: `Bearer ${token}`
                }
            });
            setParts(response.data.results);
            setTotalParts(response.data.count);
            setHasNextPage(response.data.next !== null);
            setHasPrevPage(response.data.previous !== null);
        } catch (error) {
            setError('Hiba a részletek lekérésekor: ' + error.message);
        }
    };

    useEffect(() => {
        fetchParts(partsPage);
    }, [partsPage, setNum]);

    const handleBack = () => {
        navigate(-1);
    };

    const handlePageChange = (newPage) => {
        setPartsPage(newPage);
    };

    if (loading) return <div className="text-center text-2xl mt-4">Betöltés...</div>;
    if (error) return <div className="text-center text-red-500 mt-4">Hiba: {error}</div>;
    if (!setDetails) return <div className="text-center text-2xl mt-4">Nem található készlet</div>;

    return (
        <div className="container mx-auto px-4 py-8">
            <button
                onClick={handleBack}
                className="mb-4 bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600"
            >
                ← Vissza
            </button>
            
            <div className="bg-white rounded-lg shadow-lg overflow-hidden">
                <div className="relative">
                    <img 
                        src={setDetails.set_img_url} 
                        alt={setDetails.name} 
                        className="w-full h-96 object-contain"
                    />
                </div>
                
                <div className="p-6">
                    <h1 className="text-3xl font-bold mb-4">{setDetails.name}</h1>
                    
                    <div className="grid grid-cols-1 gap-4 mb-6">
                        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                            <div>
                                <p className="text-gray-600">
                                    <span className="font-semibold">Készlet száma:</span> {setDetails.set_num}
                                </p>
                                <p className="text-gray-600">
                                    <span className="font-semibold">Kiadás éve:</span> {setDetails.year}
                                </p>
                            </div>
                            <div>
                                <p className="text-gray-600">
                                    <span className="font-semibold">Elemek száma:</span> {setDetails.num_parts}
                                </p>
                                <a 
                                    href={setDetails.set_url} 
                                    target="_blank" 
                                    rel="noopener noreferrer" 
                                    className="text-blue-500 hover:text-blue-700"
                                >
                                    Részletek a Rebrickable-en →
                                </a>
                            </div>
                        </div>

                        <div className="mt-8">
                            <h2 className="text-2xl font-bold mb-4">Alkatrészek ({totalParts})</h2>
                            {parts.length === 0 ? (
                                <p className="text-gray-600">Nincs elérhető alkatrész</p>
                            ) : (
                                <>
                                    <div className="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-4 xl:grid-cols-5 gap-4">
                                        {parts.map((part) => (
                                            <div key={part.id} className="bg-white shadow-md rounded p-4 flex flex-col items-center">
                                                <img 
                                                    src={part.part.part_img_url} 
                                                    alt={part.part.name} 
                                                    className="w-24 h-24 object-contain mb-2"
                                                />
                                                <h3 className="text-sm font-semibold text-center mb-1">{part.part.name}</h3>
                                                <p className="text-xs text-gray-600">Szín: {part.color.name}</p>
                                                <p className="text-xs text-gray-600">Mennyiség: {part.quantity}</p>
                                            </div>
                                        ))}
                                    </div>
                                    <Pagination 
                                        currentPage={partsPage}
                                        totalResults={totalParts}
                                        pageSize={pageSize}
                                        onPageChange={handlePageChange}
                                        hasNextPage={hasNextPage}
                                        hasPrevPage={hasPrevPage}
                                    />
                                </>
                            )}
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default SetDetails;