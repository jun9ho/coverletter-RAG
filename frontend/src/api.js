import axios from "axios";

const api = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL,
  headers: {
    "Content-Type": "application/json",
  },
});

export const createExperience = async (data) => {
  const response = await api.post("/api/experiences", data);
  return response.data;
};

export const generateCoverLetter = async (data) => {
  const response = await api.post("/api/cover-letters/generate", data);
  return response.data;
};

export const extractExperiences = async (data) => {
  const response = await api.post("/api/documents/extract-experiences", data);
  return response.data;
};
export default api;