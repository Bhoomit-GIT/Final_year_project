import React from "react";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import Roadmap from "../src/components/roadmap";
import Recommendations from "./components/recommendations";


const App: React.FC = () => {
  return (
    <Router>

      <Routes>
        <Route path="/" element={<Roadmap />} />
        <Route path="/recommendations" element={<Recommendations />} />

      </Routes>
    </Router>
  );
};

export default App;