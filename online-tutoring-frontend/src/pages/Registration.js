import React, { useState } from 'react';
import { useTheme } from '@mui/material/styles';
import StudentRegistration from '../components/registration/StudentRegistration';
import Tabs from '@mui/material/Tabs';
import Tab from '@mui/material/Tab';
import Box from '@mui/material/Box';
import TabPanel from '../components/ui/TabPanel';
import TeacherRegistration from '../components/registration/TeacherRegistration';
function a11yProps(index) {
  return {
    id: `simple-tab-${index}`,
    'aria-controls': `simple-tabpanel-${index}`,
  };
}

const Registration = () => {
  const [selectedTab,setSelectedTab]=useState(0);
  const theme = useTheme();
  const handleTabChange = (event, newValue) => {
    setSelectedTab(newValue);
  };
 return  (
  <Box sx={{ width: '100%' }}>
    <Tabs
      value={selectedTab}
      onChange={handleTabChange}
      textColor="secondary"
      indicatorColor="secondary"
      
    >
      <Tab {...a11yProps(0)} label="Student" />
      <Tab {...a11yProps(1)} label="Teacher" />
     
    </Tabs>
   
        <TabPanel  value={selectedTab} index={0} dir={theme.direction}>
         <StudentRegistration/>
        </TabPanel>
        <TabPanel  value={selectedTab} index={1} dir={theme.direction}>
          <TeacherRegistration/>
        </TabPanel>
       
     
  </Box>
);
  
};

export default Registration;