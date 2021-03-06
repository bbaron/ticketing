import React, { useEffect, useState } from "react";
import { BrowserRouter, Route, Switch } from "react-router-dom";
import "bootstrap/dist/css/bootstrap.css";
import api from "./api/build-client";
import Signup from "./pages/auth/signup";
import Signin from "./pages/auth/signin";
import Header from "./components/header";
import LandingPage from "./pages";
import Signout from "./pages/auth/signout";
import { removeAuthInfo, setAuthInfo } from "./components/auth-info";
import TicketNew from "./pages/tickets/new";
import TicketShow from "./pages/tickets/show";
import OrderShow from "./pages/orders/show";
import OrderIndex from "./pages/orders";

function App() {
  const [currentUser, setCurrentUser] = useState(null);
  const [tickets, setTickets] = useState([]);
  const [orders, setOrders] = useState([]);

  useEffect(() => {
    (async function () {
      const response = await api().get("/api/users/currentuser");
      setCurrentUser(response.data["currentUser"]);
    })();
  }, []);

  const fetchTickets = async () => {
    const response = await api().get("/api/tickets");
    setTickets(response.data["tickets"]);
  };

  useEffect(() => {
    fetchTickets();
  }, []);

  const onTicketsUpdated = () => {
    fetchTickets();
  };

  const fetchOrders = async () => {
    const response = await api().get("/api/orders");
    console.log("fetched orders", response.data["orders"]);
    setOrders(response.data["orders"]);
  };

  useEffect(() => {
    fetchOrders();
  }, []);

  const onOrdersUpdated = () => {
    fetchOrders();
  };

  const NotFound = () => <h1>404 Not Found</h1>;

  const onUserChange = (user) => {
    if (!user) {
      removeAuthInfo();
      setCurrentUser(null);
    } else {
      setAuthInfo(user["jwt"]);
      setCurrentUser(user["currentUser"]);
    }
  };
  return (
    <div>
      <BrowserRouter>
        <Header currentUser={currentUser} />
        <div className="container" style={{ marginTop: "10px" }}>
          <Switch>
            <Route path="/" exact>
              <LandingPage currentUser={currentUser} tickets={tickets} />
            </Route>
            <Route path="/orders" exact>
              <OrderIndex orders={orders} />
            </Route>
            <Route path="/auth/signin">
              <Signin onUserChange={onUserChange} />
            </Route>
            <Route path="/auth/signup">
              <Signup onUserChange={onUserChange} />
            </Route>
            <Route path="/auth/signout">
              <Signout onUserChange={onUserChange} />
            </Route>
            <Route path="/tickets/new">
              <TicketNew onTicketsUpdated={onTicketsUpdated} />
            </Route>
            <Route path="/tickets/:ticketId">
              <TicketShow />
            </Route>
            <Route path="/orders/:orderId">
              <OrderShow
                currentUser={currentUser}
                onTicketsUpdated={onTicketsUpdated}
                onOrdersUpdated={onOrdersUpdated}
              />
            </Route>
            <Route component={NotFound} />
          </Switch>
        </div>
      </BrowserRouter>
    </div>
  );
}

export default App;
