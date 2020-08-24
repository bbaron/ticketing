import React, { useState } from "react";

import useRequest from "../../hooks/use-request";
import { setAuthInfo } from "../../components/auth-info";

const Signin = ({ history }) => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const { doRequest, errors } = useRequest({
    url: "/api/users/signin",
    method: "post",
    body: {
      email,
      password,
    },
    onSuccess: (response) => {
      setAuthInfo(response.headers["x-auth-info"]);
      history.push("/");
    },
  });

  const onSubmit = async (event) => {
    event.preventDefault();
    doRequest();
  };

  return (
    <form onSubmit={onSubmit}>
      <h1>Sign In</h1>
      <div className="form-group">
        <label htmlFor="user-email">Email Address</label>
        <input
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          type="email"
          className="form-control"
          id="user-email"
          placeholder="Enter email address"
          aria-describedby="email-help"
        />
      </div>
      <div className="form-group">
        <label htmlFor="user-password">Password</label>
        <input
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          type="password"
          className="form-control"
          id="user-password"
          placeholder="Enter password"
          aria-describedby="password-help"
        />
      </div>
      {errors}
      <button className="btn btn-primary">Sign In</button>
    </form>
  );
};

export default Signin;
