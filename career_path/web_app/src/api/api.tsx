import axios from "axios";

const API_CRUD_URL = "http://127.0.0.1:8000/career_path"; // Django Backend
const API_SIGNUP_IN_LOGIN_URL = "http://127.0.0.1:8000/api/users";

const API_BASE_URL = "http://127.0.0.1:8000/api";


// User Authenticat

// Fetch Roadmap Data
export const fetchRoadmap = () => axios.get(`${API_CRUD_URL}/roadmaps/generate/`);

// Fetch Recommendations
interface UserData {
  id: number;
  name: string;
  // Add other fields as necessary
}

export const fetchRecommendations = (query: string, userData: UserData) => 
  axios.get(`${API_CRUD_URL}/recommendations/recommend/`, { params: { query, user_data: userData } });

export const signupUser = (userData: { username: string; email: string; password: string }) => 
  axios.post(`${API_SIGNUP_IN_LOGIN_URL}/signup/`, userData);

export const loginUser = (credentials: { username: string; password: string }) => 
  axios.post(`${API_SIGNUP_IN_LOGIN_URL}/login/`, credentials);

export const fetchUserProfile = (token: string) =>
  axios.get(`${API_BASE_URL}/users/profile/`, {
    headers: { Authorization: `Bearer ${token}` }
  });

interface UserProfileData {
  // Define the structure of the user profile data
  username: string;
  email: string;
  // Add other fields as necessary
}

export const updateUserProfile = (token: string, data: UserProfileData) =>
  axios.put(`${API_BASE_URL}/users/profile/`, data, {
    headers: { Authorization: `Bearer ${token}`, "Content-Type": "application/json" }
  });