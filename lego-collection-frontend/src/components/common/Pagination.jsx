"use client"
import {
  Pagination as PaginationRoot,
  PaginationContent,
  PaginationEllipsis,
  PaginationItem,
  PaginationLink,
  PaginationNext,
  PaginationPrevious,
} from "../ui/pagination"

const Pagination = ({ currentPage, totalResults, pageSize, onPageChange, hasNextPage, hasPrevPage }) => {
  const totalPages = Math.ceil(totalResults / pageSize)
  const maxVisiblePages = 5

  if (totalResults === 0) return null

  let startPage = Math.max(1, currentPage - Math.floor(maxVisiblePages / 2))
  const endPage = Math.min(totalPages, startPage + maxVisiblePages - 1)

  if (endPage - startPage + 1 < maxVisiblePages) {
    startPage = Math.max(1, endPage - maxVisiblePages + 1)
  }

  const pageNumbers = []
  for (let i = startPage; i <= endPage; i++) {
    pageNumbers.push(i)
  }

  return (
    <div className="flex flex-col items-center gap-2">
      <div className="text-sm text-muted-foreground">
        Összesen {totalResults} találat, {currentPage} / {totalPages} oldal
      </div>
      <PaginationRoot>
        <PaginationContent>
          <PaginationItem>
            <PaginationPrevious
              onClick={() => onPageChange(currentPage - 1)}
              className={!hasPrevPage ? "pointer-events-none opacity-50" : "cursor-pointer"}
              aria-disabled={!hasPrevPage}
            />
          </PaginationItem>

          {startPage > 1 && (
            <>
              <PaginationItem>
                <PaginationLink onClick={() => onPageChange(1)} isActive={currentPage === 1}>
                  1
                </PaginationLink>
              </PaginationItem>
              {startPage > 2 && (
                <PaginationItem>
                  <PaginationEllipsis />
                </PaginationItem>
              )}
            </>
          )}

          {pageNumbers.map((num) => (
            <PaginationItem key={num}>
              <PaginationLink onClick={() => onPageChange(num)} isActive={num === currentPage}>
                {num}
              </PaginationLink>
            </PaginationItem>
          ))}

          {endPage < totalPages && (
            <>
              {endPage < totalPages - 1 && (
                <PaginationItem>
                  <PaginationEllipsis />
                </PaginationItem>
              )}
              <PaginationItem>
                <PaginationLink onClick={() => onPageChange(totalPages)} isActive={currentPage === totalPages}>
                  {totalPages}
                </PaginationLink>
              </PaginationItem>
            </>
          )}

          <PaginationItem>
            <PaginationNext
              onClick={() => onPageChange(currentPage + 1)}
              className={!hasNextPage ? "pointer-events-none opacity-50" : "cursor-pointer"}
              aria-disabled={!hasNextPage}
            />
          </PaginationItem>
        </PaginationContent>
      </PaginationRoot>
    </div>
  )
}

export default Pagination