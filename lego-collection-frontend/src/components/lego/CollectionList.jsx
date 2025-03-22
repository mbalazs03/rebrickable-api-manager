"use client"

import { useEffect, useState } from "react"
import { useNavigate } from "react-router-dom"
import axios from "axios"
import { Loader2, Package2 } from "lucide-react"
import SetCard from "../common/SetCard"
import { Button } from "../ui/button"
import { Alert, AlertDescription, AlertTitle } from "../ui/alert"
import LoadingSpinner from "../common/LoadingSpinner"

const CollectionList = () => {
  const [sets, setSets] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)
  const navigate = useNavigate()

  useEffect(() => {
    const token = localStorage.getItem("token")
    if (!token) {
      navigate("/login")
      return
    }

    const fetchData = async () => {
      try {
        const response = await axios.get("/api/user/collection", {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        })
        
        await new Promise(resolve => setTimeout(resolve, 1000))
        
        setSets(response.data)
      } catch (error) {
        setError(error)
      } finally {
        setLoading(false)
      }
    }

    fetchData()
  }, [navigate])

  const token = localStorage.getItem("token")

  const removeFromCollection = async (legoSet) => {
    try {
      await axios.put(
        `/api/user/collection/${legoSet.set_num}?owned=false`,
        {},
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        },
      )
      setSets(sets.filter((set) => set["set_num"] !== legoSet.set_num))
    } catch (error) {
      console.error("Hiba történt a készlet eltávolításakor:", error)
    }
  }

  if (loading) {
    return <LoadingSpinner text="Készlet betöltése..." />
  }

  if (error) {
    return (
      <div className="container mx-auto px-4 py-8">
        <Alert variant="destructive">
          <AlertTitle>Hiba történt</AlertTitle>
          <AlertDescription>{error.message}</AlertDescription>
        </Alert>
      </div>
    )
  }

  return (
    <div className="container mx-auto px-4 py-8 max-w-7xl">
      <div className="flex flex-col md:flex-row md:items-center justify-between gap-4 mb-8">
        <h1 className="text-3xl font-bold text-primary">Saját LEGO Készlet Gyűjtemény</h1>
        <Button onClick={() => navigate("/search")}>Új készlet keresése</Button>
      </div>

      {sets.length === 0 ? (
        <div className="text-center py-16 bg-muted/50 rounded-lg">
          <Package2 className="mx-auto h-12 w-12 text-muted-foreground mb-4" />
          <h2 className="text-xl font-medium mb-2">Nincs megjeleníthető készleted</h2>
          <p className="text-muted-foreground mb-6">Kezdd el a gyűjteményed építését új készletek hozzáadásával</p>
          <Button onClick={() => navigate("/search")}>Készletek keresése</Button>
        </div>
      ) : (
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
          {sets.map((set) => (
            <SetCard key={set["set_num"]} set={set} onRemoveFromCollection={removeFromCollection} />
          ))}
        </div>
      )}
    </div>
  )
}

export default CollectionList