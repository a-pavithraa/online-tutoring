import axios from "axios";
const httpClient= axios.create({
  baseURL: "http://localhost:8090/mdm",
  headers: {
    "Content-type": "application/json"
  }
});
export default httpClient;