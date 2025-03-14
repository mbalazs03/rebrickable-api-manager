"use client"

import { useEffect, useState } from "react"
import { useParams, useNavigate } from "react-router-dom"
import axios from "axios"
import { Card, CardContent } from "../ui/card"
import { Button } from "../ui/button"
import { ArrowLeft, ExternalLink, Loader2 } from "lucide-react"
import { Tooltip, TooltipContent, TooltipProvider, TooltipTrigger } from "../ui/tooltip"
import { Badge } from "../ui/badge"
import Pagination from "../common/Pagination"

const SetDetails = () => {
  const { setNum } = useParams()
  const [setDetails, setSetDetails] = useState(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)
  const [parts, setParts] = useState([])
  const [partsPage, setPartsPage] = useState(1)
  const [totalParts, setTotalParts] = useState(0)
  const [hasNextPage, setHasNextPage] = useState(false)
  const [hasPrevPage, setHasPrevPage] = useState(false)
  const pageSize = 20
  const navigate = useNavigate()

  useEffect(() => {
    const fetchSetDetails = async () => {
      const token = localStorage.getItem("token")
      if (!token) {
        navigate("/login")
        return
      }

      try {
        const response = await axios.get(`/api/rebrickable/sets/${setNum}`, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        })
        setSetDetails(response.data)
      } catch (error) {
        setError("Hiba az adatok lekérésekor: " + error.message)
      } finally {
        setLoading(false)
      }
    }

    fetchSetDetails()
  }, [setNum, navigate])

  const fetchParts = async (page) => {
    const token = localStorage.getItem("token")
    if (!token) {
      navigate("/login")
      return
    }
    try {
      const response = await axios.get(`/api/rebrickable/sets/${setNum}/parts`, {
        params: {
          page,
          pageSize,
        },
        headers: {
          Authorization: `Bearer ${token}`,
        },
      })
      setParts(response.data.results)
      setTotalParts(response.data.count)
      setHasNextPage(response.data.next !== null)
      setHasPrevPage(response.data.previous !== null)
    } catch (error) {
      setError("Hiba a részletek lekérésekor: " + error.message)
    }
  }

  useEffect(() => {
    fetchParts(partsPage)
  }, [partsPage, setNum])

  const handleBack = () => {
    navigate(-1)
  }

  const handlePageChange = (newPage) => {
    setPartsPage(newPage)
  }

  if (loading)
    return (
      <div className="flex justify-center items-center h-[50vh]">
        <Loader2 className="h-8 w-8 animate-spin text-primary" />
        <span className="ml-2 text-lg">Betöltés...</span>
      </div>
    )

  if (error)
    return (
      <div className="container mx-auto px-4 py-8">
        <div className="bg-destructive/10 text-destructive p-4 rounded-md">{error}</div>
      </div>
    )

  if (!setDetails)
    return (
      <div className="container mx-auto px-4 py-8 text-center">
        <h2 className="text-2xl font-bold">Nem található készlet</h2>
      </div>
    )

  return (
    <div className="container mx-auto px-4 py-8 max-w-6xl">
      <Button variant="outline" onClick={handleBack} className="mb-6">
        <ArrowLeft className="mr-2 h-4 w-4" /> Vissza
      </Button>

      <Card className="overflow-hidden">
        <div className="bg-muted p-6 flex justify-center">
          <img
            src={setDetails.set_img_url || "/placeholder.svg"}
            alt={setDetails.name}
            className="max-h-[400px] object-contain"
          />
        </div>

        <CardContent className="p-6">
          <div className="flex flex-col md:flex-row md:items-center justify-between gap-4 mb-6">
            <h1 className="text-3xl font-bold">{setDetails.name}</h1>
            <a
              href={setDetails.set_url}
              target="_blank"
              rel="noopener noreferrer"
              className="flex items-center text-primary hover:underline"
            >
              Rebrickable <ExternalLink className="ml-1 h-4 w-4" />
            </a>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">
            <div className="flex items-center">
              <div className="bg-primary/10 p-3 px-4 rounded-full mr-3">
                <span className="text-primary font-semibold">#</span>
              </div>
              <div>
                <p className="text-sm text-muted-foreground">Készlet száma</p>
                <p className="font-medium">{setDetails.set_num}</p>
              </div>
            </div>

            <div className="flex items-center">
              <div className="bg-primary/10 p-3 px-4 rounded-full mr-3">
                <span className="text-primary font-semibold">Year</span>
              </div>
              <div>
                <p className="text-sm text-muted-foreground">Kiadás éve</p>
                <p className="font-medium">{setDetails.year}</p>
              </div>
            </div>

            <div className="flex items-center">
              <div className="bg-primary/10 p-3 px-4 rounded-full mr-3">
                <span className="text-primary font-semibold">P</span>
              </div>
              <div>
                <p className="text-sm text-muted-foreground">Elemek száma</p>
                <p className="font-medium">{setDetails.num_parts}</p>
              </div>
            </div>
          </div>

          <div className="mt-8">
            <h2 className="text-2xl font-bold mb-4 flex items-center">
              Alkatrészek
              <Badge variant="outline" className="ml-2">
                {totalParts}
              </Badge>
            </h2>

            {parts.length === 0 ? (
              <p className="text-muted-foreground">Nincs elérhető alkatrész</p>
            ) : (
              <>
                <div className="grid grid-cols-2 sm:grid-cols-3 md:grid-cols-4 lg:grid-cols-5 gap-4">
                  {parts.map((part) => (
                    <TooltipProvider key={part.id}>
                      <Tooltip>
                        <TooltipTrigger asChild>
                          <Card className="overflow-hidden hover:shadow-md transition-shadow">
                            <div className="bg-muted p-3 flex justify-center">
                              <img
                                src={part.part.part_img_url || "/placeholder.svg"}
                                alt={part.part.name}
                                className="h-24 object-contain"
                              />
                            </div>
                            <CardContent className="p-3">
                              <p className="text-xs font-medium truncate">{part.part.name}</p>
                              <div className="flex justify-between mt-1">
                                <span className="text-xs text-muted-foreground">{part.color.name}</span>
                                <Badge variant="secondary" className="text-xs">
                                  {part.quantity}x
                                </Badge>
                              </div>
                            </CardContent>
                          </Card>
                        </TooltipTrigger>
                        <TooltipContent>
                          <p>{part.part.name}</p>
                          <p>Szín: {part.color.name}</p>
                          <p>Mennyiség: {part.quantity}</p>
                        </TooltipContent>
                      </Tooltip>
                    </TooltipProvider>
                  ))}
                </div>

                <div className="mt-8">
                  <Pagination
                    currentPage={partsPage}
                    totalResults={totalParts}
                    pageSize={pageSize}
                    onPageChange={handlePageChange}
                    hasNextPage={hasNextPage}
                    hasPrevPage={hasPrevPage}
                  />
                </div>
              </>
            )}
          </div>
        </CardContent>
      </Card>
    </div>
  )
}

export default SetDetails