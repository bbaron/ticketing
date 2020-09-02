import React, { useEffect } from "react";
import { useHistory } from "react-router-dom";

const Signout = ({ onUserChange }) => {
  let history = useHistory();
  useEffect(() => {
    history.push("/");
    onUserChange(null);
  });
  return <div>Signing you out...</div>;
};

export default Signout;
