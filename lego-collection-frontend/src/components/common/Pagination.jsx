import React from 'react';

const Pagination = ({ 
    currentPage, 
    totalResults, 
    pageSize, 
    onPageChange,
    hasNextPage,
    hasPrevPage 
}) => {
    const totalPages = Math.ceil(totalResults / pageSize);
    const maxVisiblePages = 5;
    
    if (totalResults === 0) return null;
    
    let startPage = Math.max(1, currentPage - Math.floor(maxVisiblePages / 2));
    let endPage = Math.min(totalPages, startPage + maxVisiblePages - 1);
    
    if (endPage - startPage + 1 < maxVisiblePages) {
        startPage = Math.max(1, endPage - maxVisiblePages + 1);
    }

    const pageNumbers = [];
    for (let i = startPage; i <= endPage; i++) {
        pageNumbers.push(i);
    }

    return (
        <div className="flex justify-center mt-8 space-x-2">
            <button 
                onClick={() => onPageChange(currentPage - 1)}
                disabled={!hasPrevPage}
                className={`px-4 py-2 rounded ${!hasPrevPage ? 'bg-gray-300' : 'bg-blue-500 hover:bg-blue-600'} text-white`}
            >
                Előző
            </button>
            
            {startPage > 1 && (
                <>
                    <button
                        onClick={() => onPageChange(1)}
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
                    onClick={() => onPageChange(num)}
                    className={`px-4 py-2 rounded ${
                        num === currentPage 
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
                        onClick={() => onPageChange(totalPages)}
                        className="px-4 py-2 rounded bg-blue-500 hover:bg-blue-600 text-white"
                    >
                        {totalPages}
                    </button>
                </>
            )}

            <button 
                onClick={() => onPageChange(currentPage + 1)}
                disabled={!hasNextPage}
                className={`px-4 py-2 rounded ${!hasNextPage ? 'bg-gray-300' : 'bg-blue-500 hover:bg-blue-600'} text-white`}
            >
                Következő
            </button>
        </div>
    );
};

export default Pagination; 