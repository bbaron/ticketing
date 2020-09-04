import React, { useState } from "react";
import buildClient from "../api/build-client";

export default ({ url, method, body, onSuccess }) => {
  const [errors, setErrors] = useState(null);

  const doRequest = async (props = {}) => {
    try {
      setErrors(null);
      let client = buildClient();
      // if (url.startsWith("/api/payments")) {
      //   client = buildClient("http://localhost:8080");
      // } else {
      //   client = buildClient();
      // }
      const response = await client[method](url, {
        ...body,
        ...props,
      });
      if (onSuccess) {
        onSuccess(response);
      }
      return response.data;
    } catch (err) {
      if (!err.response) {
        console.error(err);
        return;
      }
      const res = err.response;
      let errors = res.data.errors;
      if (!errors) {
        let message;
        if (!res.data.message) {
          message = `Something unforgivable happened on the server, status = ${res.status}`;
        } else {
          message = res.data.message;
        }
        errors = [{ message: message }];
        // setErrors({errors: [{message: message}]});
      }
      setErrors(
        <div className="alert alert-danger">
          <h4>Oh Snap...</h4>
          <ul className="my-0">
            {errors.map((err) =>
              err.field ? (
                <li
                  key={err.field + err.message}
                >{`${err.field}: ${err.message}`}</li>
              ) : (
                <li key={err.message}>{err.message}</li>
              )
            )}
          </ul>
        </div>
      );
    }
  };

  return { doRequest, errors };
};
