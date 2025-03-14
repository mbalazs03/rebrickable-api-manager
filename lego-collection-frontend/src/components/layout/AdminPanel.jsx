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
import { Input } from "../../components/ui/input"
import { Label } from "../../components/ui/label"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "../../components/ui/select"
import { Loader2, ShieldAlert, ShieldCheck, UserCog, Trash2, User, UserPlus } from "lucide-react"
import { Tooltip, TooltipContent, TooltipProvider, TooltipTrigger } from "../../components/ui/tooltip"

const AdminPanel = () => {
  const { isAuthenticated, role } = useAuth()
  const [users, setUsers] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)
  const [actionInProgress, setActionInProgress] = useState(null)
  const [newUser, setNewUser] = useState({
    username: "",
    email: "",
    password: "",
    role: "USER",
  })
  const [createError, setCreateError] = useState(null)
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
      await axios.post(`/api/admin/promote/${userId}`, {}, { headers: { Authorization: `Bearer ${token}` } })
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
      await axios.post(`/api/admin/revoke/${userId}`, {}, { headers: { Authorization: `Bearer ${token}` } })
      setUsers(users.map((user) => (user.id === userId ? { ...user, role: "USER" } : user)))
    } catch (error) {
      console.error("Hiba történt az admin jogosultság visszavonásakor:", error)
    } finally {
      setActionInProgress(null)
    }
  }

  const deleteUser = async (userId) => {
    if (!window.confirm("Biztosan törölni szeretné a felhasználót?")) return
    setActionInProgress(userId)
    const token = localStorage.getItem("token")

    try {
      await axios.delete(`/api/admin/users/${userId}`, {
        headers: { Authorization: `Bearer ${token}` },
      })
      setUsers(users.filter((user) => user.id !== userId))
    } catch (error) {
      console.error("Hiba történt a felhasználó törlésekor:", error)
    } finally {
      setActionInProgress(null)
    }
  }

  const impersonateUser = async (userId) => {
    setActionInProgress(userId)
    const token = localStorage.getItem("token")

    try {
      const response = await axios.post(
        `/api/admin/impersonate/${userId}`,
        {},
        { headers: { Authorization: `Bearer ${token}` } },
      )
      const impersonationToken = response.data.token
      localStorage.setItem("token", impersonationToken)
      alert("Sikeres átváltás a felhasználó fiókjára. Az oldal újratöltődik.")
      navigate("/collection")
    } catch (error) {
      console.error("Hiba történt az átváltás során:", error)
    } finally {
      setActionInProgress(null)
    }
  }

  const createUser = async (e) => {
    e.preventDefault()
    setActionInProgress("create")
    const token = localStorage.getItem("token")

    try {
      const response = await axios.post(`/api/admin/create`, newUser, {
        headers: {
          Authorization: `Bearer ${token}`,
          "Content-Type": "application/json",
        },
      })
      setUsers([...users, response.data])
      setNewUser({ username: "", email: "", password: "", role: "USER" })
      setCreateError(null)
    } catch (error) {
      console.error("Hiba történt a felhasználó létrehozásakor:", error)
      setCreateError(error)
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
          <div className="rounded-md border mb-8">
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
                      <TableCell className="text-right space-x-2">
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
                                <p>Admin jogosultság hozzáadása</p>
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
                          <Tooltip>
                            <TooltipTrigger asChild>
                              <Button
                                variant="outline"
                                size="sm"
                                onClick={() => impersonateUser(user.id)}
                                disabled={actionInProgress === user.id}
                              >
                                {actionInProgress === user.id ? (
                                  <Loader2 className="h-4 w-4 animate-spin mr-1" />
                                ) : (
                                  <User className="h-4 w-4 mr-1" />
                                )}
                                Bejelentkezés
                              </Button>
                            </TooltipTrigger>
                            <TooltipContent>
                              <p>Átváltás a felhasználó fiókjára</p>
                            </TooltipContent>
                          </Tooltip>
                          <Tooltip>
                            <TooltipTrigger asChild>
                              <Button
                                variant="destructive"
                                size="sm"
                                onClick={() => deleteUser(user.id)}
                                disabled={actionInProgress === user.id}
                              >
                                {actionInProgress === user.id ? (
                                  <Loader2 className="h-4 w-4 animate-spin mr-1" />
                                ) : (
                                  <Trash2 className="h-4 w-4 mr-1" />
                                )}
                                Törlés
                              </Button>
                            </TooltipTrigger>
                            <TooltipContent>
                              <p>Felhasználó törlése</p>
                            </TooltipContent>
                          </Tooltip>
                        </TooltipProvider>
                      </TableCell>
                    </TableRow>
                  ))
                )}
              </TableBody>
            </Table>
          </div>

          <Card className="mt-8">
            <CardHeader>
              <CardTitle className="text-xl flex items-center gap-2">
                <UserPlus className="h-5 w-5" />
                Új felhasználó létrehozása
              </CardTitle>
              <CardDescription>Adja meg az új felhasználó adatait</CardDescription>
            </CardHeader>
            <CardContent>
              {createError && (
                <Alert variant="destructive" className="mb-4">
                  <ShieldAlert className="h-4 w-4" />
                  <AlertTitle>Hiba történt</AlertTitle>
                  <AlertDescription>
                    {createError.message || "Nem sikerült létrehozni a felhasználót."}
                  </AlertDescription>
                </Alert>
              )}
              <form onSubmit={createUser} className="space-y-4">
                <div className="space-y-2">
                  <Label htmlFor="username">Felhasználónév</Label>
                  <Input
                    id="username"
                    type="text"
                    value={newUser.username}
                    onChange={(e) => setNewUser({ ...newUser, username: e.target.value })}
                    required
                    placeholder="Adja meg a felhasználónevet"
                  />
                </div>
                <div className="space-y-2">
                  <Label htmlFor="email">Email</Label>
                  <Input
                    id="email"
                    type="email"
                    value={newUser.email}
                    onChange={(e) => setNewUser({ ...newUser, email: e.target.value })}
                    required
                    placeholder="pelda@email.hu"
                  />
                </div>
                <div className="space-y-2">
                  <Label htmlFor="password">Jelszó</Label>
                  <Input
                    id="password"
                    type="password"
                    value={newUser.password}
                    onChange={(e) => setNewUser({ ...newUser, password: e.target.value })}
                    required
                    placeholder="Adjon meg egy jelszót"
                  />
                </div>
                <div className="space-y-2">
                  <Label htmlFor="role">Szerepkör</Label>
                  <Select value={newUser.role} onValueChange={(value) => setNewUser({ ...newUser, role: value })}>
                    <SelectTrigger id="role">
                      <SelectValue placeholder="Válasszon szerepkört" />
                    </SelectTrigger>
                    <SelectContent>
                      <SelectItem value="USER">Felhasználó</SelectItem>
                      <SelectItem value="ADMIN">Admin</SelectItem>
                    </SelectContent>
                  </Select>
                </div>
                <Button type="submit" disabled={actionInProgress === "create"} className="mt-2">
                  {actionInProgress === "create" ? (
                    <>
                      <Loader2 className="h-4 w-4 animate-spin mr-2" />
                      Létrehozás...
                    </>
                  ) : (
                    <>
                      <UserPlus className="h-4 w-4 mr-2" />
                      Felhasználó létrehozása
                    </>
                  )}
                </Button>
              </form>
            </CardContent>
          </Card>
        </CardContent>
      </Card>
    </div>
  )
}

export default AdminPanel