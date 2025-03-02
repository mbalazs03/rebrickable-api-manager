import React, { useState, useEffect } from 'react';
import axios from 'axios';

const SearchLegoSets = () => {
    const [searchCriteria, setSearchCriteria] = useState({
        query: '',
        setNum: '',
        name: '',
        yearFrom: '',
        yearTo: ''
    });
    const [results, setResults] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const [page, setPage] = useState(1);
    const [totalResults, setTotalResults] = useState(0);
    const [hasNextPage, setHasNextPage] = useState(false);
    const [hasPrevPage, setHasPrevPage] = useState(false);
    const pageSize = 12;

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setSearchCriteria(prev => ({
            ...prev,
            [name]: value
        }));
    };

    const handleSearch = async (e, newPage = 1) => {
        e?.preventDefault();
        setLoading(true);
        setError(null);
        setPage(newPage);

        const token = localStorage.getItem('token');

        try {
            const response = await axios.get(`/api/rebrickable/sets/search`, {
                params: {
                    ...searchCriteria,
                    yearFrom: searchCriteria.yearFrom || null,
                    yearTo: searchCriteria.yearTo || null,
                    page: newPage,
                    pageSize
                },
                headers: {
                    Authorization: `Bearer ${token}`
                }
            });
            
            setResults(response.data.results);
            setTotalResults(response.data.count);
            setHasNextPage(response.data.next !== null);
            setHasPrevPage(response.data.previous !== null);
        } catch (error) {
            setError('Hiba történt a lego készletek keresésekor');
        } finally {
            setLoading(false);
        }
    };

    const addToCollection = async (legoSet) => {
      try {
        const token = localStorage.getItem('token');
        await axios.post('/api/rebrickable/sets', legoSet, {
          headers: {
            Authorization: `Bearer ${token}`
          }
        });
        await axios.put(`/api/user/collection/${legoSet.set_num}?owned=true`, {}, {
          headers: {
            Authorization: `Bearer ${token}`
          }
        });
        alert(`${legoSet.name} sikeresen hozzáadva a gyűjteményhez!`);
      } catch (error) {
        alert('Hiba történt a készlet hozzáadásakor');
      }
    };

    const nextPage = (e) => {
        if (hasNextPage) {
            handleSearch(e, page + 1);
        }
    };

    const prevPage = (e) => {
        if (hasPrevPage) {
            handleSearch(e, page - 1);
        }
    };

    const goToPage = (e, pageNum) => {
        if (pageNum >= 1 && pageNum <= Math.ceil(totalResults / pageSize)) {
            handleSearch(e, pageNum);
        }
    };

    const renderPagination = () => {
        if (totalResults === 0) return null;

        const totalPages = Math.ceil(totalResults / pageSize);
        const pageNumbers = [];
        const maxVisiblePages = 5;
        
        let startPage = Math.max(1, page - Math.floor(maxVisiblePages / 2));
        let endPage = Math.min(totalPages, startPage + maxVisiblePages - 1);
        
        if (endPage - startPage + 1 < maxVisiblePages) {
            startPage = Math.max(1, endPage - maxVisiblePages + 1);
        }

        for (let i = startPage; i <= endPage; i++) {
            pageNumbers.push(i);
        }

        return (
            <div className="flex justify-center mt-8 space-x-2">
                <button 
                    onClick={prevPage}
                    disabled={!hasPrevPage}
                    className={`px-4 py-2 rounded ${!hasPrevPage ? 'bg-gray-300' : 'bg-blue-500 hover:bg-blue-600'} text-white`}
                >
                    Előző
                </button>
                
                {startPage > 1 && (
                    <>
                        <button
                            onClick={(e) => goToPage(e, 1)}
                            className="px-4 py-2 rounded bg-blue-500 hover:bg-blue-600 text-white"
                        >
                            1
                        </button>
                        {startPage > 2 && <span className="px-4 py-2">...</span>}
                    </>
                )}

                {pageNumbers.map(num => (
                    <button
                        key={num}
                        onClick={(e) => goToPage(e, num)}
                        className={`px-4 py-2 rounded ${
                            num === page 
                                ? 'bg-blue-700 text-white' 
                                : 'bg-blue-500 hover:bg-blue-600 text-white'
                        }`}
                    >
                        {num}
                    </button>
                ))}

                {endPage < totalPages && (
                    <>
                        {endPage < totalPages - 1 && <span className="px-4 py-2">...</span>}
                        <button
                            onClick={(e) => goToPage(e, totalPages)}
                            className="px-4 py-2 rounded bg-blue-500 hover:bg-blue-600 text-white"
                        >
                            {totalPages}
                        </button>
                    </>
                )}

                <button 
                    onClick={nextPage}
                    disabled={!hasNextPage}
                    className={`px-4 py-2 rounded ${!hasNextPage ? 'bg-gray-300' : 'bg-blue-500 hover:bg-blue-600'} text-white`}
                >
                    Következő
                </button>
            </div>
        );
    };

    return (
        <div className="container mx-auto px-4 py-8">
            <h1 className="text-3xl font-bold mb-4">LEGO Készletek Keresése</h1>
            <form onSubmit={handleSearch} className="mb-8 space-y-4">
                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
                    <div>
                        <label className="block text-sm font-medium text-gray-700">Általános keresés</label>
                        <input
                            type="text"
                            name="query"
                            className="mt-1 block w-full border border-gray-300 rounded-md shadow-sm p-2"
                            value={searchCriteria.query}
                            onChange={handleInputChange}
                            placeholder="Általános keresés..."
                        />
                    </div>
                    <div>
                        <label className="block text-sm font-medium text-gray-700">Készlet száma</label>
                        <input
                            type="text"
                            name="setNum"
                            className="mt-1 block w-full border border-gray-300 rounded-md shadow-sm p-2"
                            value={searchCriteria.setNum}
                            onChange={handleInputChange}
                            placeholder="Készlet száma..."
                        />
                    </div>
                    <div>
                        <label className="block text-sm font-medium text-gray-700">Készlet neve</label>
                        <input
                            type="text"
                            name="name"
                            className="mt-1 block w-full border border-gray-300 rounded-md shadow-sm p-2"
                            value={searchCriteria.name}
                            onChange={handleInputChange}
                            placeholder="Készlet neve..."
                        />
                    </div>
                    <div>
                        <label className="block text-sm font-medium text-gray-700">Év (-tól)</label>
                        <input
                            type="number"
                            name="yearFrom"
                            className="mt-1 block w-full border border-gray-300 rounded-md shadow-sm p-2"
                            value={searchCriteria.yearFrom}
                            onChange={handleInputChange}
                            placeholder="Év -tól..."
                        />
                    </div>
                    <div>
                        <label className="block text-sm font-medium text-gray-700">Év (-ig)</label>
                        <input
                            type="number"
                            name="yearTo"
                            className="mt-1 block w-full border border-gray-300 rounded-md shadow-sm p-2"
                            value={searchCriteria.yearTo}
                            onChange={handleInputChange}
                            placeholder="Év -ig..."
                        />
                    </div>
                </div>
                <button 
                    type="submit" 
                    className="w-full bg-blue-500 text-white py-2 rounded hover:bg-blue-600 mt-4"
                >
                    Keresés
                </button>
            </form>

            {loading && <p className="text-center">Betöltés...</p>}
            {error && <p className="text-center text-red-500">{error}</p>}

            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
                {results.map((set) => (
                    <div key={set.set_num} className="bg-white shadow-md rounded p-4">
                        <img src={set.set_img_url} alt={set.name} className="w-full h-48 object-cover rounded"/>
                        <h2 className="text-xl font-semibold mt-2">{set.name}</h2>
                        <p className="text-gray-600">Készlet száma: {set.set_num}</p>
                        <p className="text-gray-600">Év: {set.year}</p>
                        <p className="text-gray-600">Részek száma: {set.num_parts}</p>
                        <button 
                            onClick={() => addToCollection(set)}
                            className="w-full bg-blue-500 text-white py-2 rounded hover:bg-blue-600 mt-4"
                        >
                            Hozzáadás a gyűjteményhez
                        </button>
                    </div>
                ))}
            </div>

            {renderPagination()}
        </div>
    );
};

export default SearchLegoSets;
