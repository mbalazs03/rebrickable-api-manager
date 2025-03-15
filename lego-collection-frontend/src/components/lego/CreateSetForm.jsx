"use client"

import { useState } from "react"
import axios from "axios"
import { useNavigate } from "react-router-dom"
import { useAuth } from "../auth/AuthContext"
import { Card, CardContent, CardDescription, CardFooter, CardHeader, CardTitle } from "../../components/ui/card"
import { Input } from "../../components/ui/input"
import { Button } from "../../components/ui/button"
import { Label } from "../../components/ui/label"
import { Alert, AlertDescription } from "../../components/ui/alert"
import {
  Loader2,
  AlertCircle,
  Upload,
  ImageIcon,
  Trash2,
  CheckCircle2,
  Info,
  Calendar,
  Hash,
  Package2,
  Link,
  Clock,
  PlusCircle,
} from "lucide-react"
import { Tooltip, TooltipContent, TooltipProvider, TooltipTrigger } from "../../components/ui/tooltip"

const CreateSetForm = () => {
  const [setNum, setSetNum] = useState("")
  const [name, setName] = useState("")
  const [year, setYear] = useState("")
  const [themeId, setThemeId] = useState("")
  const [numParts, setNumParts] = useState("")
  const [setImgUrl, setSetImgUrl] = useState("")
  const [setUrl, setSetUrl] = useState("")
  const [lastModifiedDt, setLastModifiedDt] = useState("")
  const [message, setMessage] = useState("")
  const [isError, setIsError] = useState(false)
  const [isLoading, setIsLoading] = useState(false)
  const [imageFile, setImageFile] = useState(null)
  const [imagePreview, setImagePreview] = useState(null)
  const [formStep, setFormStep] = useState(0)

  const navigate = useNavigate()
  const { username } = useAuth()

  const handleImageChange = (e) => {
    const file = e.target.files[0]
    if (file) {
      setImageFile(file)

      const reader = new FileReader()
      reader.onloadend = () => {
        setImagePreview(reader.result)
      }
      reader.readAsDataURL(file)
    }
  }

    const uploadImage = async () => {
        if (!imageFile) return null
        const formData = new FormData()
        formData.append("image", imageFile)

        try {
            const token = localStorage.getItem("token")
            const response = await axios.post("/api/upload/image", formData, {
                headers: {
                Authorization: `Bearer ${token}`,
                "Content-Type": "multipart/form-data",
                },
            })
            return response.data.imageUrl
        } catch (error) {
            console.error("Error uploading image:", error)
            throw error
        }
  }

  const handleSubmit = async (e) => {
    e.preventDefault()

    if (formStep <= 2) {
      nextStep()
      return
    }

    setIsLoading(true)
    setMessage("")
    setIsError(false)

    if (!username) {
      setMessage("Nincs bejelentkezett felhasználó. Kérjük, jelentkezzen be és próbálja újra.")
      setIsError(true)
      setIsLoading(false)
      return
    }

    try {
      let imageUrl = setImgUrl
      if (imageFile) {
        try {
          imageUrl = await uploadImage()
        } catch (error) {
          setMessage("Hiba történt a kép feltöltése során. A készlet létrehozása folytatódik kép nélkül.")
          setIsError(true)
        }
      }

      const newSet = {
        setNum,
        name,
        year: Number.parseInt(year),
        themeId: themeId ? Number.parseInt(themeId) : undefined,
        numParts: numParts ? Number.parseInt(numParts) : undefined,
        setImgUrl: imageUrl,
        setUrl,
        lastModifiedDt,
        owner: username,
      }

      const token = localStorage.getItem("token")
      await axios.post("/api/rebrickable/sets", newSet, {
        headers: { Authorization: `Bearer ${token}` },
      })

      setMessage("Készlet sikeresen létrehozva!")
      setTimeout(() => {
        navigate("/collection")
      }, 1500)
    } catch (error) {
      console.error("Error creating set:", error)
      setMessage("Hiba történt a készlet létrehozása során.")
      setIsError(true)
    } finally {
      setIsLoading(false)
    }
  }

  const nextStep = () => {
    if (formStep <= 2) setFormStep(formStep + 1)
  }

  const prevStep = () => {
    if (formStep > 0) setFormStep(formStep - 1)
  }

  return (
    <div className="container mx-auto px-4 py-8 max-w-3xl">
      <Card className="border-none shadow-lg">
        <CardHeader className="bg-primary/5 rounded-t-lg pb-4">
          <div className="flex items-center gap-2">
            <div className="bg-primary/10 p-2 rounded-full">
              <PlusCircle className="h-6 w-6 text-primary" />
            </div>
            <div>
              <CardTitle className="text-2xl">Új LEGO készlet létrehozása</CardTitle>
              <CardDescription className="text-muted-foreground">Adja meg az új készlet adatait</CardDescription>
            </div>
          </div>
        </CardHeader>

        <CardContent className="pt-6">
          {message && (
            <Alert
              variant={isError ? "destructive" : "default"}
              className={`mb-6 ${isError ? "" : "bg-green-50 text-green-800 border-green-200"}`}
            >
              {isError ? <AlertCircle className="h-4 w-4" /> : <CheckCircle2 className="h-4 w-4 text-green-600" />}
              <AlertDescription>{message}</AlertDescription>
            </Alert>
          )}

          <form onSubmit={handleSubmit}>
            <div className="mb-6">
              <div className="flex justify-between mb-2">
                <div className="flex space-x-2">
                  <div className={`w-3 h-3 rounded-full ${formStep >= 0 ? "bg-primary" : "bg-muted"}`}></div>
                  <div className={`w-3 h-3 rounded-full ${formStep >= 1 ? "bg-primary" : "bg-muted"}`}></div>
                  <div className={`w-3 h-3 rounded-full ${formStep >= 2 ? "bg-primary" : "bg-muted"}`}></div>
                </div>
                <span className="text-sm text-muted-foreground">Lépés {formStep + 1}/3</span>
              </div>
              <div className="w-full bg-muted h-1 rounded-full overflow-hidden">
                <div
                  className="bg-primary h-1 transition-all duration-300 ease-in-out"
                  style={{ width: `${((formStep + 1) / 3) * 100}%` }}
                ></div>
              </div>
            </div>

            {/* Step 1: Basic Information */}
            <div className={formStep === 0 ? "block" : "hidden"}>
              <h3 className="text-lg font-medium mb-4 flex items-center">
                <Info className="mr-2 h-5 w-5 text-primary" />
                Alapadatok
              </h3>

              <div className="space-y-4">
                <div className="flex items-start space-x-4">
                  <div className="bg-primary/10 p-2 rounded-full mt-1">
                    <Hash className="h-4 w-4 text-primary" />
                  </div>
                  <div className="flex-1 space-y-1">
                    <Label htmlFor="setNum" className="text-sm font-medium">
                      Készlet száma
                      <span className="text-destructive ml-1">*</span>
                    </Label>
                    <Input
                      id="setNum"
                      type="text"
                      value={setNum}
                      onChange={(e) => setSetNum(e.target.value)}
                      placeholder="pl. 75192"
                      required
                      disabled={isLoading}
                      className="border-muted"
                    />
                    <p className="text-xs text-muted-foreground">A készlet egyedi azonosítója</p>
                  </div>
                </div>

                <div className="flex items-start space-x-4">
                  <div className="bg-primary/10 p-2 rounded-full mt-1">
                    <Package2 className="h-4 w-4 text-primary" />
                  </div>
                  <div className="flex-1 space-y-1">
                    <Label htmlFor="name" className="text-sm font-medium">
                      Készlet neve
                      <span className="text-destructive ml-1">*</span>
                    </Label>
                    <Input
                      id="name"
                      type="text"
                      value={name}
                      onChange={(e) => setName(e.target.value)}
                      placeholder="pl. Millennium Falcon"
                      required
                      disabled={isLoading}
                      className="border-muted"
                    />
                  </div>
                </div>

                <div className="flex items-start space-x-4">
                  <div className="bg-primary/10 p-2 rounded-full mt-1">
                    <Calendar className="h-4 w-4 text-primary" />
                  </div>
                  <div className="flex-1 space-y-1">
                    <Label htmlFor="year" className="text-sm font-medium">
                      Kiadás éve
                      <span className="text-destructive ml-1">*</span>
                    </Label>
                    <Input
                      id="year"
                      type="number"
                      value={year}
                      onChange={(e) => setYear(e.target.value)}
                      placeholder="pl. 2023"
                      required
                      disabled={isLoading}
                      className="border-muted"
                    />
                  </div>
                </div>
              </div>
            </div>

            {/* Step 2: Additional Details */}
            <div className={formStep === 1 ? "block" : "hidden"}>
              <h3 className="text-lg font-medium mb-4 flex items-center">
                <Info className="mr-2 h-5 w-5 text-primary" />
                További adatok
              </h3>

              <div className="space-y-4">
                <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                  <div className="space-y-1">
                    <Label htmlFor="themeId" className="text-sm font-medium">
                      Téma ID
                    </Label>
                    <Input
                      id="themeId"
                      type="number"
                      value={themeId}
                      onChange={(e) => setThemeId(e.target.value)}
                      placeholder="pl. 158"
                      disabled={isLoading}
                      className="border-muted"
                    />
                  </div>

                  <div className="space-y-1">
                    <Label htmlFor="numParts" className="text-sm font-medium">
                      Elemek száma
                    </Label>
                    <Input
                      id="numParts"
                      type="number"
                      value={numParts}
                      onChange={(e) => setNumParts(e.target.value)}
                      placeholder="pl. 7541"
                      disabled={isLoading}
                      className="border-muted"
                    />
                  </div>
                </div>

                <div className="space-y-1">
                  <div className="flex items-center justify-between">
                    <Label htmlFor="setUrl" className="text-sm font-medium">
                      Készlet URL
                    </Label>
                    <TooltipProvider>
                      <Tooltip>
                        <TooltipTrigger asChild>
                          <Info className="h-4 w-4 text-muted-foreground" />
                        </TooltipTrigger>
                        <TooltipContent>
                          <p>A készlet hivatalos oldala</p>
                        </TooltipContent>
                      </Tooltip>
                    </TooltipProvider>
                  </div>
                  <div className="flex">
                    <div className="bg-muted flex items-center px-3 rounded-l-md border border-r-0 border-input">
                      <Link className="h-4 w-4 text-muted-foreground" />
                    </div>
                    <Input
                      id="setUrl"
                      type="text"
                      value={setUrl}
                      onChange={(e) => setSetUrl(e.target.value)}
                      placeholder="pl. https://rebrickable.com/sets/75192-1"
                      disabled={isLoading}
                      className="border-muted rounded-l-none"
                    />
                  </div>
                </div>

                <div className="space-y-1">
                  <div className="flex items-center justify-between">
                    <Label htmlFor="lastModifiedDt" className="text-sm font-medium">
                      Utolsó módosítás dátuma
                    </Label>
                    <TooltipProvider>
                      <Tooltip>
                        <TooltipTrigger asChild>
                          <Info className="h-4 w-4 text-muted-foreground" />
                        </TooltipTrigger>
                        <TooltipContent>
                          <p>YYYY-MM-DD formátumban</p>
                        </TooltipContent>
                      </Tooltip>
                    </TooltipProvider>
                  </div>
                  <div className="flex">
                    <div className="bg-muted flex items-center px-3 rounded-l-md border border-r-0 border-input">
                      <Clock className="h-4 w-4 text-muted-foreground" />
                    </div>
                    <Input
                      id="lastModifiedDt"
                      type="text"
                      value={lastModifiedDt}
                      onChange={(e) => setLastModifiedDt(e.target.value)}
                      placeholder="pl. 2023-01-01"
                      disabled={isLoading}
                      className="border-muted rounded-l-none"
                    />
                  </div>
                </div>
              </div>
            </div>

            {/* Step 3: Image Upload */}
            <div className={formStep === 2 ? "block" : "hidden"}>
              <h3 className="text-lg font-medium mb-4 flex items-center">
                <ImageIcon className="mr-2 h-5 w-5 text-primary" />
                Készlet képe
              </h3>

              <div className="space-y-4">
                <div className="bg-muted/30 rounded-lg p-4 border border-dashed border-muted-foreground/50">
                  <div className="space-y-2">
                    <Label htmlFor="setImgUrl" className="text-sm font-medium">
                      Készlet kép URL (opcionális)
                    </Label>
                    <Input
                      id="setImgUrl"
                      type="text"
                      value={setImgUrl}
                      onChange={(e) => setSetImgUrl(e.target.value)}
                      placeholder="pl. https://example.com/image.jpg"
                      disabled={isLoading || imageFile}
                      className="border-muted"
                    />
                    <p className="text-xs text-muted-foreground">Adjon meg egy URL-t, vagy töltsön fel egy képet</p>
                  </div>

                  <div className="my-4 h-[1px] w-full bg-border"></div>

                  <div className="text-center">
                    <p className="text-sm font-medium mb-2">vagy</p>
                    <div className="flex flex-col items-center gap-2">
                      <Button
                        type="button"
                        variant={imageFile ? "secondary" : "default"}
                        onClick={() => document.getElementById("imageUpload").click()}
                        disabled={isLoading}
                        className="w-full max-w-xs"
                      >
                        <Upload className="mr-2 h-4 w-4" />
                        {imageFile ? "Másik kép kiválasztása" : "Kép feltöltése"}
                      </Button>
                      {imageFile && (
                        <Button
                          type="button"
                          variant="outline"
                          onClick={() => {
                            setImageFile(null)
                            setImagePreview(null)
                          }}
                          disabled={isLoading}
                          className="text-destructive hover:text-destructive"
                        >
                          <Trash2 className="mr-2 h-4 w-4" />
                          Kép törlése
                        </Button>
                      )}
                      <input
                        id="imageUpload"
                        type="file"
                        accept="image/*"
                        onChange={handleImageChange}
                        className="hidden"
                        disabled={isLoading}
                      />
                    </div>
                  </div>
                </div>

                {imagePreview && (
                  <div className="mt-4 bg-muted/20 rounded-lg p-4 border">
                    <p className="text-sm font-medium mb-2 flex items-center">
                      <ImageIcon className="h-4 w-4 mr-2 text-primary" />
                      Kép előnézet
                    </p>
                    <div className="flex justify-center bg-white rounded-md p-2">
                      <img src={imagePreview || "/placeholder.svg"} alt="Preview" className="max-h-64 object-contain" />
                    </div>
                  </div>
                )}
              </div>
            </div>

            <div className="mt-8 flex justify-between">
              <Button type="button" variant="outline" onClick={prevStep} disabled={formStep === 0 || isLoading}>
                Vissza
              </Button>

              {formStep <= 2 ? (
                <Button
                  type="button"
                  onClick={nextStep}
                  disabled={isLoading || (formStep === 0 && (!setNum || !name || !year))}
                >
                  Következő
                </Button>
              ) : (
                <Button type="submit" disabled={isLoading || !setNum || !name || !year}>
                  {isLoading ? (
                    <>
                      <Loader2 className="mr-2 h-4 w-4 animate-spin" />
                      Létrehozás...
                    </>
                  ) : (
                    "Készlet létrehozása"
                  )}
                </Button>
              )}
            </div>
          </form>
        </CardContent>

        <CardFooter className="bg-muted/20 rounded-b-lg pt-4 text-center text-sm text-muted-foreground">
          <p className="w-full">
            A <span className="text-destructive">*</span>-gal jelölt mezők kitöltése kötelező
          </p>
        </CardFooter>
      </Card>
    </div>
  )
}

export default CreateSetForm