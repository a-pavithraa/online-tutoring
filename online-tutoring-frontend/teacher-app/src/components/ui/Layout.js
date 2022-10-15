import React from "react";

import MainContent from "./MainContent";
import { Outlet } from "react-router-dom";

export const Layout = (props) => {
  return (
    <MainContent>
      <Outlet />
    </MainContent>
  );
};
