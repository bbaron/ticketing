import React, { useEffect } from "react";
import useRequest from "../../hooks/use-request";
import { removeAuthInfo } from "../../components/auth-info";

const Signout = ({ history }) => {
  const { doRequest } = useRequest({
    url: "/api/users/signout",
    method: "post",
    body: {},
    onSuccess: () => {
      removeAuthInfo();
      history.push("/");
    },
  });
  useEffect(() => {
    doRequest();
  }, [doRequest]);
  return <div>Signing you out...</div>;
};

export default Signout;
