import React, { useContext } from "react";
import { useNavigate } from "react-router-dom";
import InboxIcon from "@mui/icons-material/MoveToInbox";
import List from "@mui/material/List";
import ListItem from "@mui/material/ListItem";
import ListItemButton from "@mui/material/ListItemButton";
import ListItemIcon from "@mui/material/ListItemIcon";
import ListItemText from "@mui/material/ListItemText";
import MailIcon from "@mui/icons-material/Mail";
import Divider from "@mui/material/Divider";
import AuthContext from "../../store/auth-context";
export const SideDraw = (props) => {
 
  const {selectedMenu,setSelectedMenu} = useContext(AuthContext);

  const navigate = useNavigate();

  const displayPage = (event, index, obj) => {
    setSelectedMenu(index.index);
    navigate("/" + obj.text);
  };
  return (
    <div >
      <List>
        {[
          "Classes",
          "Students",
          "Documents",
          "Notification",
          "Administration",
          "Assessments",
          "Submissions",
        ].map((text, index) => (
          <ListItem
            key={text}
            disablePadding
            selected={selectedMenu === index}
          >
            <ListItemButton
              onClick={(event) => displayPage(event, { index }, { text })}
            >
              <ListItemIcon>
                {index % 2 === 0 ? <InboxIcon /> : <MailIcon />}
              </ListItemIcon>
              <ListItemText primary={text} />
            </ListItemButton>
          </ListItem>
        ))}
      </List>
      <Divider />
    </div>
  );
};
