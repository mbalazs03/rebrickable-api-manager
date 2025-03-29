"use client"

import { useState } from "react"
import { useNavigate, Link } from "react-router-dom"
import axios from "axios"
import { useAuth } from "./AuthContext"
import { useTheme } from "../theme/ThemeContext"
import { Card, CardContent, CardDescription, CardFooter, CardHeader, CardTitle } from "../../components/ui/card"
import { Input } from "../../components/ui/input"
import { Button } from "../../components/ui/button"
import { Label } from "../../components/ui/label"
import { Alert, AlertDescription } from "../../components/ui/alert"
import { Loader2, LogIn, AlertCircle, Home, Moon, Sun } from 'lucide-react'

const Login = () => {
  const [username, setUsername] = useState("")
  const [password, setPassword] = useState("")
  const [errorMessage, setErrorMessage] = useState("")
  const [isLoading, setIsLoading] = useState(false)
  const navigate = useNavigate()
  const { login } = useAuth()
  const { theme, toggleTheme } = useTheme()

  const handleLogin = async (e) => {
    e.preventDefault()
    setIsLoading(true)
    setErrorMessage("")

    try {
      const response = await axios.post("/api/auth/login", { username, password })
      const { token, role } = response.data
      login(token, role, username)
      navigate("/home")
    } catch (error) {
      console.error(error)
      setErrorMessage("Hibás felhasználónév vagy jelszó.")
    } finally {
      setIsLoading(false)
    }
  }

  return (
    <div className="min-h-screen flex flex-col">
      <header className="w-full p-4 flex justify-between items-center">
        <Link 
          to="/" 
          className="flex items-center gap-2 text-primary hover:text-primary/80 transition-colors"
        >
          <Home className="h-5 w-5" />
          <span className="font-medium">Főoldal</span>
        </Link>
        
        <Button 
          variant="ghost" 
          size="icon" 
          onClick={toggleTheme}
          className="rounded-full h-9 w-9 flex items-center justify-center"
        >
          {theme === "light" ? (
            <Moon className="h-5 w-5" />
          ) : (
            <Sun className="h-5 w-5" />
          )}
          <span className="sr-only">Téma váltás</span>
        </Button>
      </header>

      {/* Main content */}
      <div className="container mx-auto px-4 py-8 flex-1 flex items-center justify-center">
        <Card className="w-full max-w-md">
          <CardHeader className="space-y-1">
            <CardTitle className="text-2xl font-bold text-center">Bejelentkezés</CardTitle>
            <CardDescription className="text-center">Jelentkezz be a LEGO gyűjteményed kezeléséhez</CardDescription>
          </CardHeader>
          <CardContent>
            {errorMessage && (
              <Alert variant="destructive" className="mb-4">
                <AlertCircle className="h-4 w-4" />
                <AlertDescription>{errorMessage}</AlertDescription>
              </Alert>
            )}
            <form onSubmit={handleLogin} className="space-y-4">
              <div className="space-y-2">
                <Label htmlFor="username">Felhasználónév</Label>
                <Input
                  id="username"
                  type="text"
                  value={username}
                  onChange={(e) => setUsername(e.target.value)}
                  placeholder="Írd be a felhasználóneved"
                  required
                  autoComplete="username"
                  disabled={isLoading}
                />
              </div>
              <div className="space-y-2">
                <div className="flex items-center justify-between">
                  <Label htmlFor="password">Jelszó</Label>
                  <a
                    href="#"
                    className="text-sm text-primary hover:underline"
                    onClick={(e) => {
                      e.preventDefault()
                      alert("Jelszó visszaállítás funkció fejlesztés alatt áll.")
                    }}
                  >
                    Elfelejtetted?
                  </a>
                </div>
                <Input
                  id="password"
                  type="password"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  placeholder="Írd be a jelszavad"
                  required
                  autoComplete="current-password"
                  disabled={isLoading}
                />
              </div>
              <Button type="submit" className="w-full" disabled={isLoading}>
                {isLoading ? (
                  <>
                    <Loader2 className="mr-2 h-4 w-4 animate-spin" />
                    Bejelentkezés...
                  </>
                ) : (
                  <>
                    <LogIn className="mr-2 h-4 w-4" />
                    Bejelentkezés
                  </>
                )}
              </Button>
            </form>
          </CardContent>
          <CardFooter className="flex flex-col space-y-4">
            <div className="text-center text-sm text-muted-foreground">
              <span>Még nincs fiókod? </span>
              <a
                href="/register"
                className="text-primary hover:underline"
                onClick={(e) => {
                  e.preventDefault()
                  navigate("/register")
                }}
              >
                Regisztrálj
              </a>
            </div>
          </CardFooter>
        </Card>
      </div>
    </div>
  )
}

export default Login