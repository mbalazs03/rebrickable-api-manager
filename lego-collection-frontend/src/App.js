import React from 'react';
import CollectionList from './CollectionList';
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Registration from "./Registration";
import Login from "./Login";

function App() {
  return (
      <Router>
          <Routes>
              <Route path="/" element={<Login />} />
              <Route path="/login" element={<Login />} />
              <Route path="/register" element={<Registration />} />
              <Route path="/collection" element={<CollectionList />} />
          </Routes>
      </Router>
  );
}

export default App;
