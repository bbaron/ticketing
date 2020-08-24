import React, { useEffect, useState } from "react";
import { BrowserRouter, Route, Switch } from "react-router-dom";
import "bootstrap/dist/css/bootstrap.css";
import api from "./api/build-client";
import Signup from "./pages/auth/signup";
import Signin from "./pages/auth/signin";
import Header from "./components/header";
import LandingPage from "./pages";
import Signout from "./pages/auth/signout";

function App() {
  const [currentUser, setCurrentUser] = useState(null);
  const [refreshUser, setRefreshUser] = useState(true);
  useEffect(() => {
    if (!refreshUser) return;
    (async function () {
      const { data } = await api().get("/api/users/currentuser");
      setCurrentUser(data["currentUser"]);
      setRefreshUser(false);
    })();
  }, [refreshUser]);
  return (
    <div className="container" style={{ marginTop: "10px" }}>
      <BrowserRouter>
        <Header currentUser={currentUser} />
        <Switch>
          <Route path="/" exact component={LandingPage} />
          <Route path="/auth/signup" component={Signup} />
          <Route path="/auth/signin" component={Signin} />
          <Route path="/auth/signout" component={Signout} />
        </Switch>
      </BrowserRouter>
    </div>
  );
}

export default App;
