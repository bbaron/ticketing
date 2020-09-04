import React from "react";
import { Link } from "react-router-dom";

const OrderIndex = ({ orders }) => {
  const OrderLink = ({ order }) => {
    if (order.status === "Created") {
      return <Link to={`/orders/${order.id}`}>{order.ticket.title}</Link>;
    }
    return order.ticket.title;
  };
  const orderList = orders.map((order) => {
    return (
      <tr key={order.id}>
        <td>
          <OrderLink order={order} />
        </td>
        <td>{order.status}</td>
      </tr>
    );
  });
  return (
    <div>
      <h1>Orders</h1>
      <table className="table">
        <thead>
          <tr>
            <th>Title</th>
            <th>Status</th>
          </tr>
        </thead>
        <tbody>{orderList}</tbody>
      </table>
    </div>
  );
};

export default OrderIndex;
