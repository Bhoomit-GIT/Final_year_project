import { useState, useEffect } from 'react';
import axios from 'axios';

const Roadmap = () => {
  const [roadmap, setRoadmap] = useState("");

  useEffect(() => {
    axios.get('http://127.0.0.1:8000/api/roadmaps/generate/')
      .then(response => setRoadmap(response.data.mermaid_code))
      .catch(error => console.error("Error fetching roadmap:", error));
  }, []);

  return (
    <div>
      <h2>Learning Roadmap</h2>
      <pre className="mermaid">{roadmap}</pre>
    </div>
  );
};

export default Roadmap;