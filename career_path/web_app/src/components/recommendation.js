import { useState } from 'react';
import axios from 'axios';

const Recommendations = () => {
  const [recommendations, setRecommendations] = useState([]);

  const fetchRecommendations = async () => {
    try {
      const response = await axios.get('http://127.0.0.1:8000/api/recommendations/recommend/', {
        params: {
          query: 'Python',  // Example user query
          user_data: [/* user interaction data */]
        }
      });
      setRecommendations(response.data);
    } catch (error) {
      console.error("Error fetching recommendations:", error);
    }
  };

  return (
    <div>
      <h2>Recommended Resources</h2>
      <button onClick={fetchRecommendations}>Get Recommendations</button>
      <ul>
        {recommendations.collaborative_result && recommendations.collaborative_result.map((rec, idx) => (
          <li key={idx}>{rec}</li>
        ))}
      </ul>
    </div>
  );
};

export default Recommendations;