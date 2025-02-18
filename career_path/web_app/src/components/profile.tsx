import React, { useEffect, useState } from "react";
import { fetchUserProfile, updateUserProfile } from "../api/api";

const Profile: React.FC = () => {
  const [profile, setProfile] = useState({ username: "", email: "", phone_number: "", date_of_birth: "" });
  const [editMode, setEditMode] = useState(false);
  const token = localStorage.getItem("token");

  useEffect(() => {
    const loadProfile = async () => {
      if (!token) return alert("Not authorized!");
      try {
        const response = await fetchUserProfile(token);
        setProfile(response.data);
      } catch (error) {
        console.error("Error fetching profile:", error);
      }
    };
    loadProfile();
  }, [token]);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => 
    setProfile({ ...profile, [e.target.name]: e.target.value });

  const handleUpdate = async () => {
    try {
      const response = await updateUserProfile(token!, profile);
      setProfile(response.data);
      setEditMode(false);
      alert("Profile updated successfully!");
    } catch (error) {
      console.error("Error updating profile:", error);
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center">
      <div className="w-full max-w-md bg-white p-6 rounded-lg shadow-md">
        <h2 className="text-xl font-bold text-center">User Profile</h2>
        <div className="mt-4">
          <label className="block text-sm font-medium">Username</label>
          <input type="text" name="username" value={profile.username} className="w-full px-3 py-2 border rounded-md" disabled />
        </div>
        <div className="mt-4">
          <label className="block text-sm font-medium">Email</label>
          <input type="email" name="email" value={profile.email} className="w-full px-3 py-2 border rounded-md" disabled />
        </div>
        <div className="mt-4">
          <label className="block text-sm font-medium">Phone Number</label>
          <input type="text" name="phone_number" value={profile.phone_number} onChange={handleChange} className="w-full px-3 py-2 border rounded-md" disabled={!editMode} />
        </div>
        <div className="mt-4">
          <label className="block text-sm font-medium">Date of Birth</label>
          <input type="date" name="date_of_birth" value={profile.date_of_birth} onChange={handleChange} className="w-full px-3 py-2 border rounded-md" disabled={!editMode} />
        </div>
        {editMode ? (
          <button onClick={handleUpdate} className="w-full bg-blue-500 text-white py-2 rounded-md mt-4">Save Changes</button>
        ) : (
          <button onClick={() => setEditMode(true)} className="w-full bg-green-500 text-white py-2 rounded-md mt-4">Edit Profile</button>
        )}
      </div>
    </div>
  );
};

export default Profile;