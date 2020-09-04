import React, { useEffect, useState } from "react";
import StripeCheckout from "react-stripe-checkout";
import { useParams, useHistory } from "react-router-dom";
import api from "../../api/build-client";
import useRequest from "../../hooks/use-request";

const OrderShow = ({ currentUser, onTicketsUpdated, onOrdersUpdated }) => {
  const history = useHistory();
  const stripeKey =
    "pk_test_51HMK5JC0jjNENKUNYdWJ3S8La9OmNPlmhl4dML0hdlWS7hNcA43I15evgVosEpaiOFgVyAL0nHvWMPJ48bmnnBh600iXqKONgL";
  const [order, setOrder] = useState({});
  const [timeLeft, setTimeLeft] = useState(0);
  const { orderId } = useParams();
  useEffect(() => {
    (async function () {
      const { data } = await api().get(`/api/orders/${orderId}`);
      setOrder(data);
    })();
  }, [orderId]);
  useEffect(() => {
    if (!order) return;
    const findTimeLeft = () => {
      const msLeft = new Date(order.expiresAt) - new Date();
      setTimeLeft(Math.round(msLeft / 1000));
    };
    findTimeLeft();
    const timerId = setInterval(findTimeLeft, 1000);
    return () => {
      clearInterval(timerId);
    };
  }, [order]);
  const { doRequest, errors } = useRequest({
    url: "/api/payments",
    method: "post",
    body: {
      orderId: order.id,
    },
    onSuccess: (payment) => {
      console.log(payment);
      onTicketsUpdated();
      onOrdersUpdated();
      history.push("/orders");
    },
  });
  if (!order.id) {
    return <div>Loading...</div>;
  }
  if (timeLeft < 0) {
    return <div>Order expired</div>;
  }

  const onToken = (token) => {
    console.log(`stripe token: ${token.id}`);
    doRequest({ token: token.id });
  };
  return (
    <div>
      <p>{timeLeft} seconds until orders expired</p>
      <StripeCheckout
        token={onToken}
        stripeKey={stripeKey}
        amount={order.ticket.price * 100}
        email={currentUser.email}
      />
      {errors}
    </div>
  );
};

export default OrderShow;
