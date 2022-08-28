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
import AuthContext from "../../store/auth-context";
const NavBar = () => {

 
  const context = useContext(AuthContext)
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



 
  const [anchorEl, setAnchorEl] = React.useState(null);
  const menuOpen = Boolean(anchorEl);
  const handleClick = (event) => {
    setAnchorEl(event.currentTarget);
  };
  const handleMenuClose = () => {
    setAnchorEl(null);
  };

  
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
           
        <IconButton
          color="inherit"
          aria-label="open drawer"
          edge="start"
          onClick={context.switchDrawToggle}
          sx={{ mr: 2, display: { sm: 'none' } }}
        >
          <MenuIcon />
        </IconButton>

     

          {appBarContent}
         
            

<div className={moduleClasses.rightAlignment}>

            <Button
              id="basic-button"
              color="inherit"
              aria-controls={menuOpen ? 'basic-menu' : undefined}
              aria-haspopup="true"
              aria-expanded={menuOpen ? 'true' : undefined}
              sx={{fontSize:"1em"}}
              onClick={handleClick}
            >
              TEST USER
            </Button>
            <Menu
              id="basic-menu"
              anchorEl={anchorEl}
              open={menuOpen}
              onClose={handleMenuClose}
              MenuListProps={{
                'aria-labelledby': 'basic-button',
              }}
            >
              <MenuItem >Preferences</MenuItem>
              <MenuItem >Dashboard</MenuItem>

            </Menu>

          </div>
        </Toolbar>
      
      </AppBar>
    </div>
  );
}

export default NavBar;