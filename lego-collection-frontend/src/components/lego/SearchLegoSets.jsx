"use client"

import { useState } from "react"
import axios from "axios"
import { Card, CardContent } from "../ui/card"
import { Input } from "../ui/input"
import { Button } from "../ui/button"
import { Checkbox } from "../ui/checkbox"
import { Label } from "../ui/label"
import { Loader2 } from "lucide-react"
import SetCard from "../common/SetCard"
import Pagination from "../common/Pagination"

const SearchLegoSets = () => {
  const [searchCriteria, setSearchCriteria] = useState({
    query: "",
    setNum: "",
    name: "",
    yearFrom: "",
    yearTo: "",
  })
  const [results, setResults] = useState([])
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState(null)
  const [page, setPage] = useState(1)
  const [totalResults, setTotalResults] = useState(0)
  const [hasNextPage, setHasNextPage] = useState(false)
  const [hasPrevPage, setHasPrevPage] = useState(false)
  const [showBuildable, setShowBuildable] = useState(false)

  const pageSize = 12

  const handleInputChange = (e) => {
    const { name, value } = e.target
    setSearchCriteria((prev) => ({ ...prev, [name]: value }))
  }

  const handleSearch = async (e, newPage = 1) => {
    e?.preventDefault()
    setLoading(true)
    setError(null)
    setPage(newPage)

    const token = localStorage.getItem("token")
    const endpoint = showBuildable ? "/api/rebrickable/sets/buildable" : "/api/rebrickable/sets/search"

    try {
      const response = await axios.get(endpoint, {
        params: {
          ...searchCriteria,
          yearFrom: searchCriteria.yearFrom || null,
          yearTo: searchCriteria.yearTo || null,
          page: newPage,
          pageSize,
        },
        headers: {
          Authorization: `Bearer ${token}`,
        },
      })

      setResults(response.data.results || response.data)
      setTotalResults(response.data.count || response.data.length)
      setHasNextPage(response.data.next !== null)
      setHasPrevPage(response.data.previous !== null)
    } catch (error) {
      setError("Hiba történt a LEGO készletek keresésekor")
    } finally {
      setLoading(false)
    }
  }

  const addToCollection = async (legoSet) => {
    try {
      const token = localStorage.getItem("token")
      await axios.post("/api/rebrickable/sets", legoSet, { headers: { Authorization: `Bearer ${token}` } })
      await axios.put(
        `/api/user/collection/${legoSet.set_num}?owned=true`,
        {},
        { headers: { Authorization: `Bearer ${token}` } },
      )
      alert(`${legoSet.name} sikeresen hozzáadva a gyűjteményhez!`)
    } catch (error) {
      alert("Hiba történt a készlet hozzáadásakor")
    }
  }

  const handlePageChange = (newPage) => {
    handleSearch(null, newPage)
  }

  return (
    <div className="container mx-auto px-4 py-8 max-w-7xl">
      <h1 className="text-3xl font-bold mb-6 text-primary">LEGO Készletek Keresése</h1>

      <Card className="mb-8">
        <CardContent className="pt-6">
          <form onSubmit={handleSearch} className="space-y-6">
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
              <div className="space-y-2">
                <Label htmlFor="query">Általános keresés</Label>
                <Input
                  id="query"
                  type="text"
                  name="query"
                  value={searchCriteria.query}
                  onChange={handleInputChange}
                  placeholder="Keresés bármely mezőben"
                />
              </div>
              <div className="space-y-2">
                <Label htmlFor="setNum">Készlet száma</Label>
                <Input
                  id="setNum"
                  type="text"
                  name="setNum"
                  value={searchCriteria.setNum}
                  onChange={handleInputChange}
                  placeholder="pl. 75192"
                />
              </div>
              <div className="space-y-2">
                <Label htmlFor="yearFrom">Év (-tól)</Label>
                <Input
                  id="yearFrom"
                  type="number"
                  name="yearFrom"
                  value={searchCriteria.yearFrom}
                  onChange={handleInputChange}
                  placeholder="pl. 2010"
                />
              </div>
              <div className="space-y-2">
                <Label htmlFor="yearTo">Év (-ig)</Label>
                <Input
                  id="yearTo"
                  type="number"
                  name="yearTo"
                  value={searchCriteria.yearTo}
                  onChange={handleInputChange}
                  placeholder="pl. 2023"
                />
              </div>
              <div className="flex items-center space-y-5">
                <Checkbox
                  id="buildable"
                  className="mr-2 size-5 bg-primary/10 border-primary/10 mt-5"
                  checked={showBuildable}
                  onCheckedChange={() => setShowBuildable((prev) => !prev)}
                />
                <Label htmlFor="buildable">Építhetőség mutatása</Label>
              </div>
              <Button type="submit" className="w-full bg-primary text-white mt-auto" size="lg">
                {loading ? (
                  <>
                    <Loader2 className="mr-2 h-4 w-4 animate-spin" />
                    Keresés folyamatban...
                  </>
                ) : (
                  "Keresés"
                )}
              </Button>
            </div>



          </form>
        </CardContent>
      </Card>

      {loading && !results.length && (
        <div className="flex justify-center items-center py-12">
          <Loader2 className="h-8 w-8 animate-spin text-primary" />
          <span className="ml-2 text-lg">Betöltés...</span>
        </div>
      )}

      {error && <div className="bg-destructive/10 text-destructive p-4 rounded-md mb-6">{error}</div>}

      {results.length > 0 && (
        <>
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
            {results.map((set) => (
              <SetCard
                key={set.set_num || set.set.set_num}
                set={set.set || set}
                completion={set.completionPercentage}
                missingParts={set.missingParts}
                onAddToCollection={addToCollection}
              />
            ))}
          </div>

          <div className="mt-8">
            <Pagination
              currentPage={page}
              totalResults={totalResults}
              pageSize={pageSize}
              onPageChange={handlePageChange}
              hasNextPage={hasNextPage}
              hasPrevPage={hasPrevPage}
            />
          </div>
        </>
      )}

      {!loading && !error && results.length === 0 && (
        <div className="text-center py-12 text-muted-foreground">Nincs találat. Próbálj más keresési feltételeket.</div>
      )}
    </div>
  )
}

export default SearchLegoSets