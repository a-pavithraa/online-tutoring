import axios from "axios";
const httpClient= axios.create({
  //baseURL: "https://pills.saaralkaatru.com/",
  baseURL: process.env.REACT_APP_SERVER_URL,
  headers: {
    "Content-type": "application/json",
    
  }
});
httpClient.interceptors.request.use(function (config) {
  const token = localStorage.getItem('jwtToken');
  config.headers.Authorization =  token ? `Bearer ${token}` : '';
  return config;
});
export default httpClient;