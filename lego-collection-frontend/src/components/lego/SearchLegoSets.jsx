import React, { useState } from 'react';
import axios from 'axios';
import SetCard from '../common/SetCard';
import Pagination from '../common/Pagination';

const SearchLegoSets = () => {
    const [searchCriteria, setSearchCriteria] = useState({
        query: '', setNum: '', name: '', yearFrom: '', yearTo: ''
    });
    const [results, setResults] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const [page, setPage] = useState(1);
    const [totalResults, setTotalResults] = useState(0);
    const [hasNextPage, setHasNextPage] = useState(false);
    const [hasPrevPage, setHasPrevPage] = useState(false);
    const [showBuildable, setShowBuildable] = useState(false);

    const pageSize = 12;

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setSearchCriteria(prev => ({ ...prev, [name]: value }));
    };

    const handleSearch = async (e, newPage = 1) => {
        e?.preventDefault();
        setLoading(true);
        setError(null);
        setPage(newPage);

        const token = localStorage.getItem('token');
        const endpoint = showBuildable ? '/api/rebrickable/sets/buildable' : '/api/rebrickable/sets/search';

        try {
            const response = await axios.get(endpoint, {
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

            setResults(response.data.results || response.data);
            setTotalResults(response.data.count || response.data.length);
            setHasNextPage(response.data.next !== null);
            setHasPrevPage(response.data.previous !== null);
        } catch (error) {
            setError('Hiba történt a LEGO készletek keresésekor');
        } finally {
            setLoading(false);
        }
    };

    const addToCollection = async (legoSet) => {
        try {
            const token = localStorage.getItem('token');
            await axios.post('/api/rebrickable/sets', legoSet, { headers: { Authorization: `Bearer ${token}` } });
            await axios.put(`/api/user/collection/${legoSet.set_num}?owned=true`, {}, { headers: { Authorization: `Bearer ${token}` } });
            alert(`${legoSet.name} sikeresen hozzáadva a gyűjteményhez!`);
        } catch (error) {
            alert('Hiba történt a készlet hozzáadásakor');
        }
    };

    const handlePageChange = (newPage) => {
        handleSearch(null, newPage);
    };

    return (
        <div className="container mx-auto px-4 py-8">
            <h1 className="text-3xl font-bold mb-4">LEGO Készletek Keresése</h1>
            <form onSubmit={handleSearch} className="mb-8 space-y-4">
                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
                    <input type="text" name="query" value={searchCriteria.query} onChange={handleInputChange} placeholder="Általános keresés" className="border p-2 rounded" />
                    <input type="text" name="setNum" value={searchCriteria.setNum} onChange={handleInputChange} placeholder="Készlet száma" className="border rounded p-2" />
                    <input type="text" name="name" value={searchCriteria.name} onChange={handleInputChange} placeholder="Készlet neve" className="border rounded p-2" />
                    <input type="number" name="yearFrom" value={searchCriteria.yearFrom} onChange={handleInputChange} placeholder="Év (-tól)" className="border rounded p-2" />
                    <input type="number" name="yearTo" value={searchCriteria.yearTo} onChange={handleInputChange} placeholder="Év (-ig)" className="border rounded p-2" />
                </div>
                <label className="flex items-center">
                    <input type="checkbox" checked={showBuildable} onChange={() => setShowBuildable(prev => !prev)} />
                    <span className="ml-2">Építhetőség mutatása</span>
                </label>
                <button type="submit" className="w-full bg-blue-500 text-white py-2 rounded hover:bg-blue-600 mt-4">
                    Keresés
                </button>
            </form>

            {loading && <p className="text-center">Betöltés...</p>}
            {error && <p className="text-center text-red-500">{error}</p>}

            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
                {results.map(set => (
                    <SetCard
                        key={set.set_num || set.set.set_num}
                        set={set.set || set}
                        completion={set.completionPercentage}
                        missingParts={set.missingParts}
                        onAddToCollection={addToCollection}
                    />
                ))}
            </div>

            <Pagination
                currentPage={page}
                totalResults={totalResults}
                pageSize={pageSize}
                onPageChange={handlePageChange}
                hasNextPage={hasNextPage}
                hasPrevPage={hasPrevPage}
            />
        </div>
    );
};

export default SearchLegoSets;