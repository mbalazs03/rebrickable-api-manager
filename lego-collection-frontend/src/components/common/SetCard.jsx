"use client"

import React from "react"
import { useNavigate } from "react-router-dom"
import { Card, CardContent, CardFooter } from "../ui/card"
import { Button } from "../ui/button"
import { Badge } from "../ui/badge"
import { Collapsible, CollapsibleContent, CollapsibleTrigger } from "../ui/collapsible"
import { ChevronDown, ChevronUp, Plus, Trash2 } from "lucide-react"
import { Tooltip, TooltipContent, TooltipProvider, TooltipTrigger } from "../ui/tooltip"
import logo33 from "../../assets/logo33.png"

const SetCard = ({ set, completion, missingParts, onAddToCollection, onRemoveFromCollection }) => {
  const navigate = useNavigate()
  const [isOpen, setIsOpen] = React.useState(false)
  const [showCopyToast, setShowCopyToast] = React.useState(false)

  const getCompletionColor = (percentage) => {
    if (percentage === 100) return "bg-green-500"
    if (percentage > 75) return "bg-green-400"
    if (percentage > 50) return "bg-yellow-400"
    if (percentage > 25) return "bg-orange-400"
    return "bg-red-500"
  }

  const imageUrl = set.set_img_url ? set.set_img_url : logo33;

  return (
    <Card className="overflow-hidden h-full flex flex-col hover:shadow-md transition-shadow">
      <div className="relative cursor-pointer" onClick={() => navigate(`/set/${set.set_num}`)}>
        <img
          src={imageUrl}
          alt={set.name}
          className="w-full h-48 object-contain bg-muted p-2"
        />
      </div>

      <CardContent className="p-4 flex-grow">
        <div className="flex items-center gap-2 mb-1">
          <h2
            className="text-lg font-semibold line-clamp-2 cursor-pointer hover:text-primary flex-grow"
            onClick={() => navigate(`/set/${set.set_num}`)}
          >
            {set.name}
          </h2>
          <TooltipProvider>
            <Tooltip>
              <TooltipTrigger asChild>
                <Badge 
                  variant="secondary" 
                  className="font-mono text-xs bg-muted text-muted-foreground text-white border-none bg-primary/90 cursor-pointer hover:bg-primary/70 whitespace-nowrap"
                  onClick={(e) => {
                    e.stopPropagation();
                    navigator.clipboard.writeText(set.set_num);
                    setShowCopyToast(true);
                    setTimeout(() => setShowCopyToast(false), 2000);
                  }}
                >
                  {set.set_num}
                </Badge>
              </TooltipTrigger>
              <TooltipContent>
                <p>Kattints a másoláshoz</p>
              </TooltipContent>
            </Tooltip>
          </TooltipProvider>
        </div>
        {showCopyToast && (
          <div className="fixed bottom-4 right-4 bg-black/80 text-white px-4 py-2 rounded-lg shadow-lg z-50">
            Készlet száma másolva!
          </div>
        )}
        <div className="flex justify-between items-center text-sm text-muted-foreground mb-2">
          <span>Év: {set.year}</span>
          <span>{set.num_parts} db</span>
        </div>

        {completion !== undefined && (
          <div className="mt-3">
            <div className="flex justify-between items-center mb-1 text-sm">
              <span>Építhetőség:</span>
              <span className="font-medium">{completion.toFixed(1)}%</span>
            </div>
            <div className="w-full bg-muted rounded-full h-2 mb-2">
              <div
                style={{ width: `${completion}%` }}
                className={`h-2 rounded-full transition-all duration-300 ease-in-out ${getCompletionColor(completion)}`}
              ></div>
            </div>

            {missingParts && missingParts.length > 0 && (
              <Button 
                variant="outline" 
                size="sm" 
                className="w-full text-xs mt-2"
                onClick={() => {
                  const csvContent = "Hiányzó alkatrészek\n" + missingParts.join("\n");
                  
                  const blob = new Blob([csvContent], { type: 'text/csv;charset=utf-8;' });
                  const link = document.createElement("a");
                  const url = URL.createObjectURL(blob);
                  
                  link.setAttribute("href", url);
                  link.setAttribute("download", `hianyzo_alkatreszek_${set.set_num}.csv`);
                  document.body.appendChild(link);
                  
                  link.click();
                  document.body.removeChild(link);
                }}
              >
                Hiányzó alkatrészek exportálása
              </Button>
            )}
          </div>
        )}
      </CardContent>

      <CardFooter className="p-4 pt-0">
        {onAddToCollection && (
          <TooltipProvider>
            <Tooltip>
              <TooltipTrigger asChild>
                <Button onClick={() => onAddToCollection(set)} className="w-full" variant="default">
                  <Plus className="mr-2 h-4 w-4" />
                  Hozzáadás
                </Button>
              </TooltipTrigger>
              <TooltipContent>
                <p>Hozzáadás a gyűjteményhez</p>
              </TooltipContent>
            </Tooltip>
          </TooltipProvider>
        )}

        {onRemoveFromCollection && (
          <TooltipProvider>
            <Tooltip>
              <TooltipTrigger asChild>
                <Button onClick={() => onRemoveFromCollection(set)} className="w-full" variant="destructive">
                  <Trash2 className="mr-2 h-4 w-4" />
                  Eltávolítás
                </Button>
              </TooltipTrigger>
              <TooltipContent>
                <p>Eltávolítás a gyűjteményből</p>
              </TooltipContent>
            </Tooltip>
          </TooltipProvider>
        )}
      </CardFooter>
    </Card>
  )
}

export default SetCard