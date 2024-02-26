import axios from "axios";

const userProfileAPI = axios.create({
    baseURL: 'http://localhost:8081/api/v1/user-profile',
    timeout: 5000,
    headers: {
        "Content-Type": "application/json"
    }
});

export default userProfileAPI;