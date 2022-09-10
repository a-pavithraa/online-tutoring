import React from 'react';
import { useNavigate } from 'react-router-dom';
import IconButton from '@mui/material/IconButton';
import InboxIcon from '@mui/icons-material/MoveToInbox';
import List from '@mui/material/List';
import ListItem from '@mui/material/ListItem';
import ListItemButton from '@mui/material/ListItemButton';
import ListItemIcon from '@mui/material/ListItemIcon';
import ListItemText from '@mui/material/ListItemText';
import MailIcon from '@mui/icons-material/Mail';
import MenuIcon from '@mui/icons-material/Menu';
import Divider from '@mui/material/Divider';
export const SideDraw = (props)=>{

    const [selectedIndex, setSelectedIndex] = React.useState(0);
    console.log('selectedIndex=='+selectedIndex);
  const navigate = useNavigate();

    const displayPage = (event,index,obj)=>{
      console.log(index.index);
      setSelectedIndex(index.index);
      navigate("/"+obj.text);
    }
    return <div>     
    <List>
      {['Classes','Students', 'Documents', 'Notification', 'Administration','Assessments','Submissions'].map((text, index) => (
        <ListItem key={text} disablePadding selected={selectedIndex === index}>
          <ListItemButton   onClick={(event) => displayPage(event, {index},{text})}>
            <ListItemIcon>
              {index % 2 === 0 ? <InboxIcon /> : <MailIcon />}
            </ListItemIcon>
            <ListItemText primary={text}/>
          </ListItemButton>
        </ListItem>
      ))}
    </List>
    <Divider />
    
  </div>
}
