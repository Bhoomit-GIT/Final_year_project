import React, { useEffect, useState } from "react";
import mermaid from "mermaid";
import { fetchRoadmap } from "../api/api";

const Roadmap: React.FC = () => {
  const [roadmapCode, setRoadmapCode] = useState<string>("");

  useEffect(() => {
    mermaid.initialize({ startOnLoad: true });
    mermaid.contentLoaded();

    fetchRoadmap()
      .then(response => setRoadmapCode(response.data.mermaid_code))
      .catch(error => console.error("Error fetching roadmap:", error));
  }, []);

  return (
    <div>
      <h2>Learning Roadmap</h2>
      <pre className="mermaid">{roadmapCode}</pre>
    </div>
  );
};
export default Roadmap;