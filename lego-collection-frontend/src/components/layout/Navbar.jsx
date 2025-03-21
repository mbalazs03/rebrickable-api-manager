"use client"
import { Link, useLocation } from "react-router-dom"
import { useAuth } from "../auth/AuthContext"
import { PackagePlus } from "lucide-react"
import { Button } from "../ui/button"
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuLabel,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from "../ui/dropdown-menu"
import { LogOut, Menu, Package2, Search, ShieldCheck, User } from "lucide-react"

const Navbar = () => {
  const { isAuthenticated, role, logout } = useAuth()
  const location = useLocation()

  if (location.pathname === '/login' || location.pathname === '/register' || location.pathname === '/') {
    return null;
  }

  const handleLogout = () => {
    logout()
  }

  const isActive = (path) => {
    return location.pathname === path
  }

  return (
    <header className="sticky top-0 z-50 w-full bg-primary/10 border-b backdrop-blur supports-[backdrop-filter]:bg-background/60">
      <div className="flex h-16 items-center bg-primary/10 w-full justify-between">
        <Link to="/collection" className="flex items-center gap-2 mx-10">
          <img src="/logo33.png" alt="Lego Collection" className="h-10 w-10 -ml-2" />
          <span className="text-xl font-bold">LEGO Gyűjtemény</span>
        </Link>

        <nav className="hidden md:flex items-center gap-6 mx-10">
          {isAuthenticated ? (
            <>
              <Link
                to="/collection"
                className={`text-sm font-medium transition-colors hover:text-primary ${isActive("/collection") ? "text-primary" : "text-muted-foreground"}`}
              >
                Gyűjtemény
              </Link>
              <Link
                to="/search"
                className={`text-sm font-medium transition-colors hover:text-primary ${isActive("/search") ? "text-primary" : "text-muted-foreground"}`}
              >
                Keresés
              </Link>
              {role === "ADMIN" && (
                <Link
                  to="/admin"
                  className={`text-sm font-medium transition-colors hover:text-primary ${isActive("/admin") ? "text-primary" : "text-muted-foreground"}`}
                >
                  Admin Panel
                </Link>
              )}
              <Button variant="ghost" size="sm" onClick={handleLogout}>
                <LogOut className="h-4 w-4 mr-2" />
                Kijelentkezés
              </Button>
            </>
          ) : (
            <>
              <Link to="/login">
                <Button variant="ghost" size="sm">
                  Bejelentkezés
                </Button>
              </Link>
              <Link to="/register">
                <Button size="sm">Regisztráció</Button>
              </Link>
            </>
          )}
        </nav>

        <DropdownMenu>
          <DropdownMenuTrigger asChild className="md:hidden mx-4">
            <Button variant="outline" size="icon">
              <Menu className="h-5 w-5" />
              <span className="sr-only">Menü</span>
            </Button>
          </DropdownMenuTrigger>
          <DropdownMenuContent align="end" className="w-56 mx-auto">
            <DropdownMenuLabel>LEGO Gyűjtemény</DropdownMenuLabel>
            <DropdownMenuSeparator />
            {isAuthenticated ? (
              <>
                <DropdownMenuItem asChild>
                  <Link to="/collection" className="flex items-center">
                    <Package2 className="mr-2 h-4 w-4" />
                    <span>Gyűjtemény</span>
                  </Link>
                </DropdownMenuItem>
                <DropdownMenuItem asChild>
                  <Link to="/search" className="flex items-center">
                    <Search className="mr-2 h-4 w-4" />
                    <span>Keresés</span>
                  </Link>
                </DropdownMenuItem>
                {role === "ADMIN" && (
                  <DropdownMenuItem asChild>
                    <Link to="/admin" className="flex items-center">
                      <ShieldCheck className="mr-2 h-4 w-4" />
                      <span>Admin Panel</span>
                    </Link>
                  </DropdownMenuItem>
                )}
                <DropdownMenuSeparator />
                <DropdownMenuItem onClick={handleLogout} className="text-destructive focus:text-destructive">
                  <LogOut className="mr-2 h-4 w-4" />
                  <span>Kijelentkezés</span>
                </DropdownMenuItem>
              </>
            ) : (
              <>
                <DropdownMenuItem asChild>
                  <Link to="/login" className="flex items-center">
                    <User className="mr-2 h-4 w-4" />
                    <span>Bejelentkezés</span>
                  </Link>
                </DropdownMenuItem>
                <DropdownMenuItem asChild>
                  <Link to="/register" className="flex items-center">
                    <User className="mr-2 h-4 w-4" />
                    <span>Regisztráció</span>
                  </Link>
                </DropdownMenuItem>
              </>
            )}
          </DropdownMenuContent>
        </DropdownMenu>
      </div>
    </header>
  )
}

export default Navbar