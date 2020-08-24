import React, { useEffect } from "react";
import useRequest from "../../hooks/use-request";
import { removeAuthInfo } from "../../components/auth-info";
import { useHistory } from "react-router-dom";

const Signout = ({ setRefreshUser }) => {
  let history = useHistory();
  const { doRequest } = useRequest({
    url: "/api/users/signout",
    method: "post",
    body: {},
    onSuccess: () => {
      removeAuthInfo();
      history.push("/");
      setRefreshUser(true);
    },
  });
  useEffect(() => {
    doRequest();
  }, [doRequest]);
  return <div>Signing you out...</div>;
};

export default Signout;
