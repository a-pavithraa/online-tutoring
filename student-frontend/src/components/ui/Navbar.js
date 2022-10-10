import React, { useContext } from "react";

import AppBar from '@mui/material/AppBar';
import TextField from '@mui/material/TextField';
import { Toolbar, Typography } from "@mui/material";
import moduleClasses from './NavBar.module.scss';

import IconButton from '@mui/material/IconButton';
import MenuIcon from '@mui/icons-material/Menu';


import Button from '@mui/material/Button';
import Menu from '@mui/material/Menu';
import MenuItem from '@mui/material/MenuItem';

import { styled } from "@mui/system";

import LoginContext from "../../store/login-context";
const NavBar = () => {

 

  const loginContext = useContext(LoginContext);
  const CssTextField = styled(TextField)({
    '& label.Mui-focused': {
      color: 'green',
    },
    '& .MuiInput-underline:after': {
      borderBottomColor: 'green',
    },
    '& .MuiOutlinedInput-root': {
      '& fieldset': {
        borderColor: 'red',
      },
      '&:hover fieldset': {
        borderColor: 'yellow',
      },
      '&.Mui-focused fieldset': {
        borderColor: 'green',
        color: 'white'
      },
    },
  });



 
  

  
  let appBarContent = <Typography component="div" variant="h4" sx={{ paddingLeft: "10px" }}>SHIKSHA </Typography>



  return (
    <div >
      <AppBar position="static" sx={{
        fontWeight: "bolder",
        width: '100%',
        fontSize: '20px',
        marginBottom: `20px`
      }}>

        <Toolbar>
           
  

     

          {appBarContent}
         
            

{loginContext.isLoggedIn  && <div className={moduleClasses.rightAlignment}>

            <Button
              id="basic-button"
              color="inherit"            
            
              sx={{fontSize:"1em"}}
              onClick={loginContext.logout}
            >
             LOGOUT
            </Button>
          

          </div>
}
        </Toolbar>
      
      </AppBar>
    </div>
  );
}

export default NavBar;