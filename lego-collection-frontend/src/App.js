import React from 'react';
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import CollectionList from './components/CollectionList';
import Registration from "./components/Registration";
import Login from "./components/Login";
import Navbar from "./components/Navbar";
import ProtectedRoute from "./components/ProtectedRoute";

function App() {
  return (
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
        </Routes>
      </Router>
  );
}

export default App;