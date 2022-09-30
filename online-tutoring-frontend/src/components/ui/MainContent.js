import * as React from 'react';
import Box from '@mui/material/Box';
import CssBaseline from '@mui/material/CssBaseline';
import Drawer from '@mui/material/Drawer';

import { SideDraw } from './SideDraw';
import AuthContext from '../../store/auth-context';


const drawerWidth = 240;

function MainContent(props) {
  const { window } = props;
  const context = React.useContext(AuthContext);
 
  const mobileOpen = context.mobileOpen;
  const [selectedIndex, setSelectedIndex] = React.useState(0);
  console.log('selectedIndex==' + selectedIndex);
  
  const container = window !== undefined ? () => window().document.body : undefined;

  return (
    <Box sx={{ display: 'flex' }}>
      <CssBaseline />
     
  <Box
        component="nav"
        sx={{ width: { sm: drawerWidth }, flexShrink: { sm: 0 } }}
        aria-label="mailbox folders"
      >
        {/* The implementation can be swapped with js to avoid SEO duplication of links. */}
        <Drawer
          container={container}
          variant="temporary"
          open={mobileOpen}
          onClose={context.switchDrawToggle}
          ModalProps={{
            keepMounted: true, // Better open performance on mobile.
          }}
          sx={{
            display: { xs: 'block', sm: 'none' },
            '& .MuiDrawer-paper': { boxSizing: 'border-box', width: drawerWidth },
          }}
        >
          <SideDraw />
        </Drawer>
        <Drawer
          variant="permanent"
          sx={{
            display: { xs: 'none', sm: 'block' },
            '& .MuiDrawer-paper': { boxSizing: 'border-box', width: drawerWidth, marginTop: '70px' },
          }}
          open
        >
          <SideDraw />
        </Drawer>
      </Box> 
      <Box
        component="main"
        sx={{ flexGrow: 1, p: 3, width: { sm: `calc(100% - ${drawerWidth}px)` } }}
      >
        {props.children}

      </Box>
    </Box>
  );
}


export default MainContent;
