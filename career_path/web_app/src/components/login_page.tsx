import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";

const Login_page: React.FC = () => {
  const [credentials, setCredentials] = useState({ username: "", password: "" });
  const navigate = useNavigate();

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => 
    setCredentials({ ...credentials, [e.target.name]: e.target.value });

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    console.log("Sending request with credentials:", credentials);
  
    try {
      const response = await axios.post(
        "http://127.0.0.1:8000/api/users/login/",
        credentials,
        { headers: { "Content-Type": "application/json" } }
      );
  
      console.log("Login Successful!", response.data);
      localStorage.setItem("token", response.data.access);
      alert("Login Successful!");
      navigate("/homepage");
    } catch (error) {
      console.error("Login Failed:",error);
      alert( "Invalid Credentials");
    }
  };

  return (
    <div className="min-h-screen bg-gray-100 flex items-center justify-center p-4 text-black">
      <div className="max-w-md w-full bg-white rounded-xl shadow-lg p-8">
        <h2 className="text-2xl font-bold text-gray-900 mb-6 text-center">Sign In</h2>
        
        <form className="space-y-4" onSubmit={handleSubmit}>
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Username</label>
            <input 
              type="text"
              name="username" // Ensures correct field for Django API
              className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 outline-none transition-all"
              placeholder="Enter your username"
              onChange={handleChange} required 
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Password</label>
            <input 
              type="password" 
              name="password"
              className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 outline-none transition-all"
              placeholder="••••••••"
              onChange={handleChange} required 
            />
          </div>

          <div className="flex items-center justify-between">
            <label className="flex items-center">
              <input type="checkbox" className="rounded border-gray-300 text-indigo-600 focus:ring-indigo-500"/>
              <span className="ml-2 text-sm text-gray-600">Remember me</span>
            </label>
            <a href="#" className="text-sm text-indigo-600 hover:text-indigo-500">Forgot password?</a>
          </div>

          <button className="w-full bg-indigo-600 hover:bg-indigo-700 text-white font-medium py-2.5 rounded-lg transition-colors">
            Sign In
          </button>
        </form>

        <div className="mt-6 text-center text-sm text-gray-600">
          Don't have an account? 
          <a href="/signup_page" className="text-indigo-600 hover:text-indigo-500 font-medium">Sign up</a>
        </div>
      </div>
    </div>
  );
}

export default Login_page;