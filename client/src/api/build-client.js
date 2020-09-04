import axios from "axios";
import { getAuthInfo } from "../components/auth-info";

const BASE_URL = "http://localhost:8080";

export default (baseURL = BASE_URL) => {
  const xAuthInfo = getAuthInfo();
  if (!xAuthInfo) {
    return axios.create({
      baseURL: baseURL,
    });
  } else {
    return axios.create({
      baseURL: baseURL,
      headers: {
        "x-auth-info": xAuthInfo,
      },
    });
  }
};
