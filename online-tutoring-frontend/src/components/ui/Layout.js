import React  from "react";
import { ThemeProvider, createTheme } from '@mui/material/styles';
import {lightTheme} from './Theme';

import NavBar from "./Navbar";
import MainContent from "./MainContent";
import { Outlet } from "react-router-dom";

export const Layout =(props)=>{
    return <ThemeProvider theme={lightTheme}>
      
        <MainContent>
            <Outlet/>
            </MainContent>
        
    </ThemeProvider>

}