import React  from "react";
import { ThemeProvider, createTheme } from '@mui/material/styles';
import {lightTheme} from './Theme';

import NavBar from "./Navbar";
import MainContent from "./MainContent";

export const Layout =(props)=>{
    return <ThemeProvider theme={lightTheme}>
        <NavBar/>
        <MainContent content={props.children}/>
        
    </ThemeProvider>

}