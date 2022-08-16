import { Grid, Typography } from '@mui/material';
import React from 'react';
const classesJson=[
{
    id: '1',
    grade: 'VI',
    subject: 'Math',
    students:'View Students',
    nextAssessment: '16/08/2022'

},
{
    id: '2',
    grade: 'VI',
    subject: 'Math',
    students:'View Students',
    nextAssessment: '16/08/2022'

},
{
    id: '3',
    grade: 'VI',
    subject: 'Math',
    students:'View Students',
    nextAssessment: '16/08/2022'

},
{
    id: '4',
    grade: 'VI',
    subject: 'Math',
    students:'View Students',
    nextAssessment: '16/08/2022'

}
];
export const Classes = (props)=>{
    return  <Grid item xs={12} sm container sx={{ marginLeft: "20px", marginBottom: "10px" }}>
         <Typography gutterBottom variant="h5" component="div">
          CLASSES
        </Typography>
        
        </Grid>
}