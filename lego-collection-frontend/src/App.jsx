import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { AuthProvider } from './components/auth/AuthContext';
import ProtectedRoute from './components/auth/ProtectedRoute';
import Navbar from './components/layout/Navbar';
import Login from './components/auth/Login';
import Registration from './components/auth/Registration';
import SearchLegoSets from './components/lego/SearchLegoSets';
import SetDetails from './components/lego/SetDetails';
import CollectionList from './components/lego/CollectionList';
import AdminPanel from './components/layout/AdminPanel';
import CreateSetForm from './components/lego/CreateSetForm';
import { ThemeProvider } from "./components/theme/ThemeContext"
import DefaultHomePage from './components/layout/HomePage';
import AuthenticatedHomePage from './components/layout/AuthenticatedHomePage';

function App() {
  return (
    <ThemeProvider>
      <AuthProvider>
        <Router>
          <Navbar />
          <Routes>
            <Route path="/" element={<DefaultHomePage />} />
            <Route path="/login" element={<Login />} />
            <Route path="/register" element={<Registration />} />
            <Route path='/home' element={
              <ProtectedRoute>
                <AuthenticatedHomePage />
              </ProtectedRoute>
            } />
            <Route path="/collection" element={
              <ProtectedRoute>
                <CollectionList />
              </ProtectedRoute>
            } />
            <Route path="/admin" element={
              <ProtectedRoute>
                <AdminPanel />
              </ProtectedRoute>
            } />
            <Route path="/search" element={
              <ProtectedRoute>
                <SearchLegoSets />
              </ProtectedRoute>
            } />
            <Route path="/set/:setNum" element={
              <ProtectedRoute>
                <SetDetails />
              </ProtectedRoute>
            } />
            <Route path="/create-set" element={
              <ProtectedRoute>
                <CreateSetForm />
              </ProtectedRoute>
            } />
          </Routes>
        </Router>
      </AuthProvider>
    </ThemeProvider>
  );
}

export default App;