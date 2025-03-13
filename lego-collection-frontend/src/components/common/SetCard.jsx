"use client"

import React from "react"
import { useNavigate } from "react-router-dom"
import { Card, CardContent, CardFooter } from "../ui/card"
import { Button } from "../ui/button"
import { Badge } from "../ui/badge"
import { Collapsible, CollapsibleContent, CollapsibleTrigger } from "../ui/collapsible"
import { ChevronDown, ChevronUp, Plus, Trash2 } from "lucide-react"
import { Tooltip, TooltipContent, TooltipProvider, TooltipTrigger } from "../ui/tooltip"

const SetCard = ({ set, completion, missingParts, onAddToCollection, onRemoveFromCollection }) => {
  const navigate = useNavigate()
  const [isOpen, setIsOpen] = React.useState(false)

  const getCompletionColor = (percentage) => {
    if (percentage === 100) return "bg-green-500"
    if (percentage > 75) return "bg-green-400"
    if (percentage > 50) return "bg-yellow-400"
    if (percentage > 25) return "bg-orange-400"
    return "bg-red-500"
  }

  return (
    <Card className="overflow-hidden h-full flex flex-col hover:shadow-md transition-shadow">
      <div className="relative cursor-pointer" onClick={() => navigate(`/set/${set.set_num}`)}>
        <img
          src={set.set_img_url || "/placeholder.svg"}
          alt={set.name}
          className="w-full h-48 object-contain bg-muted p-2"
        />
        <div className="absolute top-2 right-2">
          <Badge variant="secondary" className="font-mono">
            {set.set_num}
          </Badge>
        </div>
      </div>

      <CardContent className="p-4 flex-grow">
        <h2
          className="text-lg font-semibold mb-1 line-clamp-2 cursor-pointer hover:text-primary"
          onClick={() => navigate(`/set/${set.set_num}`)}
        >
          {set.name}
        </h2>
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
              <Collapsible open={isOpen} onOpenChange={setIsOpen} className="mt-2">
                <CollapsibleTrigger asChild>
                  <Button variant="outline" size="sm" className="w-full text-xs">
                    Hiányzó alkatrészek{" "}
                    {isOpen ? <ChevronUp className="h-3 w-3 ml-1" /> : <ChevronDown className="h-3 w-3 ml-1" />}
                  </Button>
                </CollapsibleTrigger>
                <CollapsibleContent>
                  <ul className="pl-4 mt-2 text-xs text-muted-foreground space-y-1">
                    {missingParts.map((part, index) => (
                      <li key={index} className="list-disc">
                        {part}
                      </li>
                    ))}
                  </ul>
                </CollapsibleContent>
              </Collapsible>
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