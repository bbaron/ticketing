import axios from "axios";
import { getAuthInfo } from "../components/auth-info";

const BASE_URL = "http://localhost:8080";

export default () => {
  const xAuthInfo = getAuthInfo();
  if (!xAuthInfo) {
    return axios.create({
      baseURL: BASE_URL,
    });
  } else {
    return axios.create({
      baseURL: BASE_URL,
      headers: {
        "x-auth-info": xAuthInfo,
      },
    });
  }
};
