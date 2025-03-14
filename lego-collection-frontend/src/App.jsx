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

function App() {
    return (
      <AuthProvider>
        <Router>
          <Navbar />
          <Routes>
            <Route path="/" element={<Login />} />
            <Route path="/login" element={<Login />} />
            <Route path="/register" element={<Registration />} />
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
          </Routes>
        </Router>
      </AuthProvider>
    );
  }
  
  export default App;