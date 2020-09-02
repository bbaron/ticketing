import React from "react";

const LandingPage = ({ currentUser }) => {
  const email = currentUser ? currentUser.email : "Anonymous";
  return <h1>Landing Page: {email}</h1>;
};

export default LandingPage;
