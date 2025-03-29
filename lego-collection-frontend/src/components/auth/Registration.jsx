"use client"

import { useState } from "react"
import { useNavigate, Link } from "react-router-dom"
import axios from "axios"
import { useTheme } from "../theme/ThemeContext"
import { Card, CardContent, CardDescription, CardFooter, CardHeader, CardTitle } from "../../components/ui/card"
import { Input } from "../../components/ui/input"
import { Button } from "../../components/ui/button"
import { Label } from "../../components/ui/label"
import { Alert, AlertDescription } from "../../components/ui/alert"
import { Loader2, UserPlus, CheckCircle2, AlertCircle, Home, Moon, Sun } from "lucide-react"

const Registration = () => {
  const [username, setUsername] = useState("")
  const [email, setEmail] = useState("")
  const [password, setPassword] = useState("")
  const [message, setMessage] = useState("")
  const [isSuccess, setIsSuccess] = useState(false)
  const [isLoading, setIsLoading] = useState(false)
  const navigate = useNavigate()
  const { theme, toggleTheme } = useTheme()

  const handleSubmit = async (e) => {
    e.preventDefault()
    setIsLoading(true)
    setMessage("")

    try {
      await axios.post("/api/auth/register", {
        username,
        email,
        password,
      })
      setIsSuccess(true)
      setMessage("Sikeres regisztráció! Most már be tudsz jelentkezni.")
      setTimeout(() => {
        navigate("/login")
      }, 2000)
    } catch (error) {
      console.error(error)
      setIsSuccess(false)
      if (error.response && error.response.data && error.response.data.message) {
        setMessage(error.response.data.message)
      } else {
        setMessage("Hiba történt a regisztráció során.")
      }
    } finally {
      setIsLoading(false)
    }
  }

  return (
    <div className="min-h-screen flex flex-col">
      <header className="w-full p-4 flex justify-between items-center">
        <Link to="/" className="flex items-center gap-2 text-primary hover:text-primary/80 transition-colors">
          <Home className="h-5 w-5" />
          <span className="font-medium">Főoldal</span>
        </Link>

        <Button
          variant="ghost"
          size="icon"
          onClick={toggleTheme}
          className="rounded-full h-9 w-9 flex items-center justify-center"
        >
          {theme === "light" ? <Moon className="h-5 w-5" /> : <Sun className="h-5 w-5" />}
          <span className="sr-only">Téma váltás</span>
        </Button>
      </header>

      <div className="container mx-auto px-4 py-8 flex-1 flex items-center justify-center">
        <Card className="w-full max-w-md">
          <CardHeader className="space-y-1">
            <CardTitle className="text-2xl font-bold text-center">Regisztráció</CardTitle>
            <CardDescription className="text-center">
              Hozz létre egy fiókot a LEGO gyűjteményed kezeléséhez
            </CardDescription>
          </CardHeader>
          <CardContent>
            {message && (
              <Alert
                variant={isSuccess ? "default" : "destructive"}
                className={`mb-4 ${isSuccess ? "bg-green-50 text-green-800 border-green-200 dark:bg-green-900/20 dark:text-green-300 dark:border-green-800" : ""}`}
              >
                {isSuccess ? (
                  <CheckCircle2 className="h-4 w-4 text-green-600 dark:text-green-400" />
                ) : (
                  <AlertCircle className="h-4 w-4" />
                )}
                <AlertDescription>{message}</AlertDescription>
              </Alert>
            )}
            <form onSubmit={handleSubmit} className="space-y-4">
              <div className="space-y-2">
                <Label htmlFor="username">Felhasználónév</Label>
                <Input
                  id="username"
                  type="text"
                  value={username}
                  onChange={(e) => setUsername(e.target.value)}
                  placeholder="Válassz egy felhasználónevet"
                  required
                  autoComplete="username"
                  disabled={isLoading || isSuccess}
                />
              </div>
              <div className="space-y-2">
                <Label htmlFor="email">Email cím</Label>
                <Input
                  id="email"
                  type="email"
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                  placeholder="email@pelda.hu"
                  required
                  autoComplete="email"
                  disabled={isLoading || isSuccess}
                />
              </div>
              <div className="space-y-2">
                <Label htmlFor="password">Jelszó</Label>
                <Input
                  id="password"
                  type="password"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  placeholder="Válassz egy biztonságos jelszót"
                  required
                  autoComplete="new-password"
                  disabled={isLoading || isSuccess}
                />
                <p className="text-xs text-muted-foreground">A jelszónak legalább 8 karakterből kell állnia.</p>
              </div>
              <Button type="submit" className="w-full" disabled={isLoading || isSuccess}>
                {isLoading ? (
                  <>
                    <Loader2 className="mr-2 h-4 w-4 animate-spin" />
                    Regisztráció...
                  </>
                ) : isSuccess ? (
                  <>
                    <CheckCircle2 className="mr-2 h-4 w-4" />
                    Sikeres regisztráció
                  </>
                ) : (
                  <>
                    <UserPlus className="mr-2 h-4 w-4" />
                    Regisztráció
                  </>
                )}
              </Button>
            </form>
          </CardContent>
          <CardFooter className="flex flex-col space-y-4">
            <div className="text-center text-sm text-muted-foreground">
              <span>Már van fiókod? </span>
              <a
                href="/login"
                className="text-primary hover:underline"
                onClick={(e) => {
                  e.preventDefault()
                  navigate("/login")
                }}
              >
                Jelentkezz be
              </a>
            </div>
          </CardFooter>
        </Card>
      </div>
    </div>
  )
}

export default Registration