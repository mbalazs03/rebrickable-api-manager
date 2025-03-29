import React from 'react';
import { Link } from 'react-router-dom';
import { Button } from '../ui/button';
import { Package2, ArrowRight, Blocks } from 'lucide-react';

const DefaultHomePage = () => {
  return (
    <div className="container mx-auto px-4 py-12 max-w-7xl">
      <div className="flex flex-col items-center text-center space-y-8">
        <div className="rounded-full bg-primary/10 p-1 mb-4">
          <img src="/logo33.png" alt="Lego Collection" className="h-28 w-28" />
        </div>
        
        <h1 className="text-4xl md:text-5xl font-bold text-primary">
          Üdvözöljük a LEGO Gyűjteményben!
        </h1>
        
        <p className="text-lg text-muted-foreground max-w-2xl mx-auto mb-6">
          Fedezze fel a legújabb LEGO készleteket, és kezdje el építeni saját gyűjteményét!
          Rendszerezze, kövesse nyomon és bővítse kollekcióját egyszerűen.
        </p>
        
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4 w-full max-w-md">
          <Link to="/login" className="w-full">
            <Button className="w-full" size="lg">
              Bejelentkezés
              <ArrowRight className="ml-2 h-4 w-4" />
            </Button>
          </Link>
          <Link to="/register" className="w-full">
            <Button variant="outline" className="w-full" size="lg">
              Regisztráció
            </Button>
          </Link>
        </div>
        
        <div className="mt-16 grid grid-cols-1 md:grid-cols-3 gap-8 w-full">
          <div className="bg-muted/50 rounded-lg p-6 text-center">
            <div className="rounded-full bg-primary/10 p-3 mx-auto mb-4 w-fit">
              <Package2 className="h-6 w-6 text-primary" />
            </div>
            <h3 className="text-xl font-medium mb-2">Gyűjtemény kezelés</h3>
            <p className="text-muted-foreground">Tartsa nyilván az összes LEGO készletét egy helyen</p>
          </div>
          
          <div className="bg-muted/50 rounded-lg p-6 text-center">
            <div className="rounded-full bg-primary/10 p-3 mx-auto mb-4 w-fit">
              <svg className="h-6 w-6 text-primary" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                <path d="M21 21L15 15M17 10C17 13.866 13.866 17 10 17C6.13401 17 3 13.866 3 10C3 6.13401 6.13401 3 10 3C13.866 3 17 6.13401 17 10Z" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>
              </svg>
            </div>
            <h3 className="text-xl font-medium mb-2">Készlet keresés</h3>
            <p className="text-muted-foreground">Keressen a teljes LEGO katalógusban egyszerűen</p>
          </div>
          
          <div className="bg-muted/50 rounded-lg p-6 text-center">
            <div className="rounded-full bg-primary/10 p-3 mx-auto mb-4 w-fit">
              <svg className="h-6 w-6 text-primary" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                <path d="M9 12H15M9 16H15M9 8H15M5 21H19C20.1046 21 21 20.1046 21 19V5C21 3.89543 20.1046 3 19 3H5C3.89543 3 3 3.89543 3 5V19C3 20.1046 3.89543 21 5 21Z" stroke="currentColor" strokeWidth="2" strokeLinecap="round"/>
              </svg>
            </div>
            <h3 className="text-xl font-medium mb-2">Részletes információk</h3>
            <p className="text-muted-foreground">Fedezze fel a készletek részletes adatait</p>
          </div>
        </div>
      </div>
    </div>
  );
};

export default DefaultHomePage;
