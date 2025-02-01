import axios from "axios";

const API_BASE_URL = "http://127.0.0.1:8000/career_path"; // Django Backend

// User Authenticat

// Fetch Roadmap Data
export const fetchRoadmap = () => axios.get(`${API_BASE_URL}/roadmaps/generate/`);

// Fetch Recommendations
interface UserData {
  id: number;
  name: string;
  // Add other fields as necessary
}

export const fetchRecommendations = (query: string, userData: UserData) => 
  axios.get(`${API_BASE_URL}/recommendations/recommend/`, { params: { query, user_data: userData } });