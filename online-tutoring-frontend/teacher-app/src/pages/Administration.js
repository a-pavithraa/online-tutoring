import React, { useState } from 'react';
import { useTheme } from '@mui/material/styles';
import StudentRegistration from '../components/administration/StudentRegistration';
import Tabs from '@mui/material/Tabs';
import Tab from '@mui/material/Tab';
import Box from '@mui/material/Box';
import TabPanel from '../components/ui/TabPanel';
import TeacherRegistration from '../components/administration/TeacherRegistration';
import Teachers from '../components/administration/Teachers';
function a11yProps(index) {
  return {
    id: `simple-tab-${index}`,
    'aria-controls': `simple-tabpanel-${index}`,
  };
}

const Administration = () => {
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
      <Tab {...a11yProps(0)} label="New Student" />
      <Tab {...a11yProps(1)} label="New Teacher" />
      <Tab {...a11yProps(2)} label="View/Edit Teacher Info" />
     
    </Tabs>
   
        <TabPanel  value={selectedTab} index={0} dir={theme.direction}>
         <StudentRegistration/>
        </TabPanel>
        <TabPanel  value={selectedTab} index={1} dir={theme.direction}>
          <TeacherRegistration/>
        </TabPanel>
        <TabPanel  value={selectedTab} index={2} dir={theme.direction}>
          <Teachers/>
        </TabPanel>
        
       
     
  </Box>
);
  
};

export default Administration;