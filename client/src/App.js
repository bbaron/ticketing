import React from "react";
import { BrowserRouter, Route, Link } from "react-router-dom";
import Signup from "./pages/auth/signup";
import "bootstrap/dist/css/bootstrap.css";

const PageOne = () => {
  return (
    <div>
      PageOne
      <Link to="/pageTwo">Nav to page 2</Link>
    </div>
  );
};
const PageTwo = () => {
  return (
    <div>
      PageTwo
      <Link to="/">Nav to page 1</Link>
    </div>
  );
};

function App() {
  return (
    <div className="container" style={{ marginTop: "10px" }}>
      <BrowserRouter>
        <div>
          <Route path="/" exact component={PageOne} />
          <Route path="/pageTwo" component={PageTwo} />
          <Route path="/auth/signup" component={Signup} />
        </div>
      </BrowserRouter>
    </div>
  );
}

export default App;
