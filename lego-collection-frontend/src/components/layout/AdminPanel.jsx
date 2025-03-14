"use client"

import { useEffect, useState } from "react"
import axios from "axios"
import { useAuth } from "../auth/AuthContext"
import { useNavigate } from "react-router-dom"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "../../components/ui/card"
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "../../components/ui/table"
import { Button } from "../../components/ui/button"
import { Badge } from "../../components/ui/badge"
import { Alert, AlertDescription, AlertTitle } from "../../components/ui/alert"
import { Loader2, ShieldAlert, ShieldCheck, UserCog } from "lucide-react"
import { Tooltip, TooltipContent, TooltipProvider, TooltipTrigger } from "../../components/ui/tooltip"

const AdminPanel = () => {
  const { isAuthenticated, role } = useAuth()
  const [users, setUsers] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)
  const [actionInProgress, setActionInProgress] = useState(null)
  const navigate = useNavigate()

  useEffect(() => {
    if (!isAuthenticated || role !== "ADMIN") {
      navigate("/login")
      return
    }

    const fetchUsers = async () => {
      const token = localStorage.getItem("token")

      try {
        const response = await axios.get("/api/admin/users", {
          headers: { Authorization: `Bearer ${token}` },
        })
        setUsers(response.data)
      } catch (error) {
        setError(error)
      } finally {
        setLoading(false)
      }
    }

    fetchUsers()
  }, [isAuthenticated, role, navigate])

  const promoteToAdmin = async (userId) => {
    setActionInProgress(userId)
    const token = localStorage.getItem("token")

    try {
      await axios.post(
        `/api/admin/promote/${userId}`,
        {},
        {
          headers: { Authorization: `Bearer ${token}` },
        },
      )

      setUsers(users.map((user) => (user.id === userId ? { ...user, role: "ADMIN" } : user)))
    } catch (error) {
      console.error("Hiba történt az admin jogosultság kiosztásakor:", error)
    } finally {
      setActionInProgress(null)
    }
  }

  const revokeAdmin = async (userId) => {
    setActionInProgress(userId)
    const token = localStorage.getItem("token")

    try {
      await axios.post(
        `/api/admin/revoke/${userId}`,
        {},
        {
          headers: { Authorization: `Bearer ${token}` },
        },
      )

      setUsers(users.map((user) => (user.id === userId ? { ...user, role: "USER" } : user)))
    } catch (error) {
      console.error("Hiba történt az admin jogosultság visszavonásakor:", error)
    } finally {
      setActionInProgress(null)
    }
  }

  const formatRole = (role) => {
    if (!role) return "USER"
    return role.startsWith("ROLE_") ? role.substring(5) : role
  }

  if (loading) {
    return (
      <div className="container mx-auto px-4 py-8 max-w-6xl">
        <Card>
          <CardHeader>
            <CardTitle className="text-2xl">Admin Panel - Felhasználók kezelése</CardTitle>
            <CardDescription>Felhasználók betöltése...</CardDescription>
          </CardHeader>
          <CardContent className="flex justify-center py-8">
            <Loader2 className="h-8 w-8 animate-spin text-primary" />
          </CardContent>
        </Card>
      </div>
    )
  }

  if (error) {
    return (
      <div className="container mx-auto px-4 py-8 max-w-6xl">
        <Alert variant="destructive">
          <ShieldAlert className="h-4 w-4" />
          <AlertTitle>Hiba történt</AlertTitle>
          <AlertDescription>
            {error.message || "Nem sikerült betölteni a felhasználókat. Kérjük, próbálja újra később."}
          </AlertDescription>
        </Alert>
      </div>
    )
  }

  return (
    <div className="container mx-auto px-4 py-8 max-w-6xl">
      <Card>
        <CardHeader>
          <CardTitle className="text-2xl flex items-center gap-2">
            <UserCog className="h-6 w-6" />
            Admin Panel - Felhasználók kezelése
          </CardTitle>
          <CardDescription>Kezelje a felhasználókat és módosítsa a jogosultságaikat</CardDescription>
        </CardHeader>
        <CardContent>
          <div className="rounded-md border">
            <Table>
              <TableHeader>
                <TableRow>
                  <TableHead>Felhasználónév</TableHead>
                  <TableHead>Email</TableHead>
                  <TableHead>Szerepkör</TableHead>
                  <TableHead className="text-right">Műveletek</TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {users.length === 0 ? (
                  <TableRow>
                    <TableCell colSpan={4} className="text-center py-6 text-muted-foreground">
                      Nincsenek felhasználók
                    </TableCell>
                  </TableRow>
                ) : (
                  users.map((user) => (
                    <TableRow key={user.id}>
                      <TableCell className="font-medium">{user.username}</TableCell>
                      <TableCell>{user.email}</TableCell>
                      <TableCell>
                        <Badge variant={formatRole(user.role) === "ADMIN" ? "default" : "secondary"}>
                          {formatRole(user.role) === "ADMIN" ? (
                            <span className="flex items-center gap-1">
                              <ShieldCheck className="h-3 w-3" />
                              Admin
                            </span>
                          ) : (
                            "Felhasználó"
                          )}
                        </Badge>
                      </TableCell>
                      <TableCell className="text-right">
                        <TooltipProvider>
                          {formatRole(user.role) !== "ADMIN" ? (
                            <Tooltip>
                              <TooltipTrigger asChild>
                                <Button
                                  variant="outline"
                                  size="sm"
                                  onClick={() => promoteToAdmin(user.id)}
                                  disabled={actionInProgress === user.id}
                                >
                                  {actionInProgress === user.id ? (
                                    <Loader2 className="h-4 w-4 animate-spin mr-1" />
                                  ) : (
                                    <ShieldCheck className="h-4 w-4 mr-1" />
                                  )}
                                  Adminná tesz
                                </Button>
                              </TooltipTrigger>
                              <TooltipContent>
                                <p>Admin jogosultságok hozzáadása</p>
                              </TooltipContent>
                            </Tooltip>
                          ) : (
                            <Tooltip>
                              <TooltipTrigger asChild>
                                <Button
                                  variant="destructive"
                                  size="sm"
                                  onClick={() => revokeAdmin(user.id)}
                                  disabled={actionInProgress === user.id}
                                >
                                  {actionInProgress === user.id ? (
                                    <Loader2 className="h-4 w-4 animate-spin mr-1" />
                                  ) : (
                                    <ShieldAlert className="h-4 w-4 mr-1" />
                                  )}
                                  Visszavonás
                                </Button>
                              </TooltipTrigger>
                              <TooltipContent>
                                <p>Admin jogosultság visszavonása</p>
                              </TooltipContent>
                            </Tooltip>
                          )}
                        </TooltipProvider>
                      </TableCell>
                    </TableRow>
                  ))
                )}
              </TableBody>
            </Table>
          </div>
        </CardContent>
      </Card>
    </div>
  )
}

export default AdminPanel