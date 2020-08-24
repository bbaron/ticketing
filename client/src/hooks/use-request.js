import axios from "axios";
import React, { useState } from "react";

export default ({ url, method, body, onSuccess }) => {
  const [errors, setErrors] = useState(null);

  const doRequest = async () => {
    try {
      setErrors(null);
      const response = await axios[method](url, body);
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
      if (!res.data.errors) {
        let message;
        if (!res.data.message) {
          message = `Something unforgivable happened on the server, status = ${res.status}`;
        } else {
          message = res.data.message;
        }
        setErrors([{ message }]);
      } else {
        setErrors(
          <div className="alert alert-danger">
            <h4>Oh Snap...</h4>
            <ul className="my-0">
              {res.data.errors.map((err) =>
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
    }
  };

  return { doRequest, errors };
};
