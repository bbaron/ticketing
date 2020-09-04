import React, { useEffect, useState } from "react";
import { useParams, useHistory } from "react-router-dom";
import api from "../../api/build-client";
import useRequest from "../../hooks/use-request";

const TicketShow = () => {
  const history = useHistory();
  const [ticket, setTicket] = useState(null);
  const { ticketId } = useParams();
  const { doRequest, errors } = useRequest({
    url: "/api/orders?expiration=1200",
    method: "post",
    body: {
      ticketId: ticketId,
    },
    onSuccess: ({ data }) => history.push(`/orders/${data.id}`),
  });
  useEffect(() => {
    (async function () {
      const { data } = await api().get(`/api/tickets/${ticketId}`);
      setTicket(data);
    })();
  }, [ticketId]);

  return ticket ? (
    <div>
      <h1>{ticket.title}</h1>
      <h4>${ticket.price}.00</h4>
      {errors}
      <button onClick={() => doRequest()} className="btn btn-primary">
        Purchase
      </button>
    </div>
  ) : (
    <div>Loading...</div>
  );
};

export default TicketShow;
