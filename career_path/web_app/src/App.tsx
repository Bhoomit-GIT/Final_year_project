import React from "react";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
// import Roadmap from "../src/components/roadmap";
import Homepage from "../src/components/homepage";
import Login_page from "./components/login_page";
import Signup from "./components/signup_page";
import Recommendations from "./components/recommendations";
import Profile from "./components/profile";


const App: React.FC = () => {
  return (
    <Router>

      <Routes>
        <Route path="/" element={<Homepage />} />
        <Route path="/recommendations" element={<Recommendations />} />
        <Route path="/login_page" element={<Login_page />} />
        <Route path="/homepage" element={<Homepage />} />
        <Route path="/signup_page" element={<Signup />} />
        <Route path="/profile" element={<Profile />} />

      </Routes>
    </Router>
  );
};

export default App;