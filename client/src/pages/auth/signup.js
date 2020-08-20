import React from "react";
import { useState } from "react";
import axios from "axios";

const Signup = () => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [emailErrors, setEmailErrors] = useState([]);
  const [passwordErrors, setPasswordErrors] = useState([]);
  const [globalErrors, setGlobalErrors] = useState([]);

  const onSubmit = async (event) => {
    event.preventDefault();
    try {
      const response = await axios.post(
        "http://localhost:8080/api/users/signup",
        {
          email,
          password,
        }
      );
      console.log(response.data);
    } catch (err) {
      const res = err.response;
      console.log(res);
      if (!res.data.errors) {
        console.error("Unspecified error:", res.status, res.data);
        let message;
        if (!res.data.message) {
          message = `Something unforgivable happened on the server, status = ${res.status}`;
        } else {
          message = res.data.message;
        }
        setGlobalErrors([{ message }]);
      } else {
        setEmailErrors(res.data.errors.filter((e) => e.field === "email"));
        setPasswordErrors(
          res.data.errors.filter((e) => e.field === "password")
        );
        setGlobalErrors(res.data.errors.filter((e) => !e.field));
      }
    }
  };

  const Errors = ({ errors, id }) => {
    if (errors.length > 0) {
      return (
        <small id={id} className="form-text text-muted">
          {errors.map((err) => err.message).join("; ")}
        </small>
      );
    }
    return null;
  };

  return (
    <form onSubmit={onSubmit}>
      <h1>Sign Up</h1>
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
        <Errors errors={emailErrors} id="email-help" />
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
        <Errors errors={passwordErrors} id="password-help" />
      </div>
      {globalErrors.length > 0 && (
        <div className="alert alert-danger">
          <h4>Oh Snap...</h4>
          <ul className="my-0">
            {globalErrors.map((err) => (
              <li key={err.message}>{err.message}</li>
            ))}
          </ul>
        </div>
      )}
      <button className="btn btn-primary">Sign Up</button>
    </form>
  );
};

export default Signup;
