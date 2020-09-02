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

function App() {
  const [currentUser, setCurrentUser] = useState(null);

  useEffect(() => {
    (async function () {
      const response = await api().get("/api/users/currentuser");
      setCurrentUser(response.data["currentUser"]);
    })();
  }, []);

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
    <div className="container" style={{ marginTop: "10px" }}>
      <BrowserRouter>
        <Header currentUser={currentUser} />
        <Switch>
          <Route path="/" exact>
            <LandingPage currentUser={currentUser} />
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
        </Switch>
      </BrowserRouter>
    </div>
  );
}

export default App;
