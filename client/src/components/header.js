import React from "react";
import { Link, NavLink } from "react-router-dom";

const Header = ({ currentUser }) => {
  console.log("In header, currentUser:", currentUser);
  const links = [
    !currentUser && { label: "Sign Up", href: "/auth/signup" },
    !currentUser && { label: "Sign In", href: "/auth/signin" },
    currentUser && { label: "Sign Out", href: "/auth/signout" },
  ]
    .filter((link) => link)
    .map(({ label, href }, key) => {
      return (
        <li key={key} className="nav-item">
          <NavLink className="nav-link" to={href}>
            {label}
          </NavLink>
        </li>
      );
    });
  return (
    <nav className="navbar navbar-light bg-light">
      <Link className="navbar-brand" to="/">
        GitTix
      </Link>
      <div className="d-flex justify-content-end">
        <ul className="nav d-flex align-items-center">{links}</ul>
      </div>
    </nav>
  );
};

export default Header;
