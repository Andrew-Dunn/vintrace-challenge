import React from 'react';
import './App.css';
import {BrowserRouter, Routes, Route} from "react-router-dom";
import WinePage from "./routes/WinePage";

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="wine/:lotCode" element={<WinePage />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
