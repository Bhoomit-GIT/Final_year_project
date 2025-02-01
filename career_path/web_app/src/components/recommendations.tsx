import React, { useState } from "react";
import { fetchRecommendations } from "../api/api";

const Recommendations: React.FC = () => {
  const [query, setQuery] = useState<string>("");
  interface Recommendation {
    title: string;
    tags: string[];
  }

  interface UserData {
    id: number;
    name: string;
  }

  const [recommendations, setRecommendations] = useState<Recommendation[][]>([]);

  const getRecommendations = async () => {
    try {
      const userData: UserData = { id: 1, name: "user-name" }; // Replace with actual user data
      const response = await fetchRecommendations(query, userData); // Pass user data if needed
      setRecommendations(response.data.content_based_result);
    } catch (error) {
      console.error("Error fetching recommendations:", error);
    }
  };

  return (
    <div>
      <h2>Recommended Resources</h2>
      <input 
        type="text" 
        placeholder="Enter a topic..." 
        value={query} 
        onChange={(e) => setQuery(e.target.value)}
      />
      <button onClick={getRecommendations}>Get Recommendations</button>
      
      <ul>
        {recommendations.map((rec, index) => (
          <li key={index}>{rec[0].title} - {rec[0].tags}</li>
        ))}
      </ul>
    </div>
  );
};

export default Recommendations;