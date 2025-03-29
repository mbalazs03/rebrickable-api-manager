"use client"
import { useNavigate } from "react-router-dom"
import { Button } from "../ui/button"
import { Package2, Search, Plus, ArrowRight } from "lucide-react"

const AuthenticatedHomePage = () => {
  const navigate = useNavigate();
  const username = localStorage.getItem("username");

  return (
    <div className="container mx-auto px-4 py-8 max-w-7xl">
      <div className="flex flex-col md:flex-row md:items-center justify-between gap-4 mb-8">
        <h1 className="text-3xl font-bold text-primary">Üdv, {username}!</h1>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 gap-6 mb-10">
        <div className="bg-primary/10 rounded-lg p-8 flex flex-col items-center text-center">
          <Package2 className="h-12 w-12 text-primary mb-4" />
          <h2 className="text-xl font-medium mb-2">Gyűjteményem</h2>
          <p className="text-muted-foreground mb-6">Tekintse meg és kezelje a már meglévő LEGO készleteit</p>
          <Button onClick={() => navigate("/collection")} className="mt-auto">
            Gyűjtemény megtekintése
            <ArrowRight className="ml-2 h-4 w-4" />
          </Button>
        </div>

        <div className="bg-muted/50 rounded-lg p-8 flex flex-col items-center text-center">
          <Plus className="h-12 w-12 text-primary mb-4" />
          <h2 className="text-xl font-medium mb-2">Új készletek hozzáadása</h2>
          <p className="text-muted-foreground mb-6">Bővítse gyűjteményét új LEGO készletekkel</p>
          <Button variant="outline" onClick={() => navigate("/search")} className="mt-auto">
            Készletek keresése
          </Button>
        </div>
      </div>

      <div className="bg-muted/30 rounded-lg p-6 md:p-8">
        <div className="flex flex-col md:flex-row md:items-center justify-between gap-4">
          <div>
            <h2 className="text-2xl font-bold mb-2">Fedezze fel a legújabb készleteket</h2>
            <p className="text-muted-foreground">
              Böngésszen a legfrissebb LEGO kiadások között és adja hozzá őket a gyűjteményéhez
            </p>
          </div>
          <Button onClick={() => navigate("/search")} className="shrink-0">
            Böngészés
            <ArrowRight className="ml-2 h-4 w-4" />
          </Button>
        </div>
      </div>

      <div className="mt-10 grid grid-cols-1 md:grid-cols-3 gap-6">
        <div className="border rounded-lg p-6 hover:bg-muted/20 transition-colors">
          <div className="flex items-center gap-3 mb-4">
            <div className="rounded-full bg-primary/10 p-2">
              <svg className="h-5 w-5 text-primary" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                <path
                  d="M12 6V12L16 14M22 12C22 17.5228 17.5228 22 12 22C6.47715 22 2 17.5228 2 12C2 6.47715 6.47715 2 12 2C17.5228 2 22 6.47715 22 12Z"
                  stroke="currentColor"
                  strokeWidth="2"
                  strokeLinecap="round"
                  strokeLinejoin="round"
                />
              </svg>
            </div>
            <h3 className="font-medium">Gyors hozzáférés</h3>
          </div>
          <p className="text-sm text-muted-foreground">
            Gyorsan kezelje gyűjteményét és találja meg a keresett készleteket
          </p>
        </div>

        <div className="border rounded-lg p-6 hover:bg-muted/20 transition-colors">
          <div className="flex items-center gap-3 mb-4">
            <div className="rounded-full bg-primary/10 p-2">
              <svg className="h-5 w-5 text-primary" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                <path
                  d="M9 12L11 14L15 10M21 12C21 16.9706 16.9706 21 12 21C7.02944 21 3 16.9706 3 12C3 7.02944 7.02944 3 12 3C16.9706 3 21 7.02944 21 12Z"
                  stroke="currentColor"
                  strokeWidth="2"
                  strokeLinecap="round"
                  strokeLinejoin="round"
                />
              </svg>
            </div>
            <h3 className="font-medium">Egyszerű kezelés</h3>
          </div>
          <p className="text-sm text-muted-foreground">
            Könnyen adhat hozzá és távolíthat el készleteket a gyűjteményéből
          </p>
        </div>

        <div className="border rounded-lg p-6 hover:bg-muted/20 transition-colors">
          <div className="flex items-center gap-3 mb-4">
            <div className="rounded-full bg-primary/10 p-2">
              <svg className="h-5 w-5 text-primary" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                <path
                  d="M12 16V12M12 8H12.01M22 12C22 17.5228 17.5228 22 12 22C6.47715 22 2 17.5228 2 12C2 6.47715 6.47715 2 12 2C17.5228 2 22 6.47715 22 12Z"
                  stroke="currentColor"
                  strokeWidth="2"
                  strokeLinecap="round"
                  strokeLinejoin="round"
                />
              </svg>
            </div>
            <h3 className="font-medium">Részletes információk</h3>
          </div>
          <p className="text-sm text-muted-foreground">Minden készletről részletes adatokat és képeket talál</p>
        </div>
      </div>
    </div>
  )
}

export default AuthenticatedHomePage