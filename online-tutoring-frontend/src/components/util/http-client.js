import axios from "axios";
const httpClient= axios.create({
  baseURL: "http://localhost:9090/",
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