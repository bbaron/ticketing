import React, { useState } from "react";
import { useHistory } from "react-router-dom";
import useRequest from "../../hooks/use-request";

const TicketNew = ({ onTicketsUpdated }) => {
  const [title, setTitle] = useState("");
  const [price, setPrice] = useState("20");
  const history = useHistory();
  const { doRequest, errors } = useRequest({
    url: "/api/tickets",
    method: "post",
    body: { title, price },
    onSuccess: () => {
      history.push("/");
      onTicketsUpdated();
    },
  });

  const onBlur = () => {
    const value = parseFloat(price);
    if (isNaN(value)) {
      return;
    }
    setPrice(value.toFixed(0));
  };

  const onSubmit = (event) => {
    event.preventDefault();
    doRequest();
  };

  return (
    <div>
      <h1>Create a Ticket</h1>
      <form onSubmit={onSubmit}>
        <div className="form-group">
          <label htmlFor="title">Title</label>
          <input
            id="title"
            className="form-control"
            value={title}
            onChange={(e) => setTitle(e.target.value)}
          />
        </div>
        <div className="form-group">
          <label htmlFor="price">price</label>
          <input
            id="price"
            className="form-control"
            value={price}
            onBlur={onBlur}
            type="number"
            min="1"
            onChange={(e) => setPrice(e.target.value)}
          />
        </div>
        {errors}
        <button className="btn btn-primary">Submit</button>
      </form>
    </div>
  );
};

export default TicketNew;
