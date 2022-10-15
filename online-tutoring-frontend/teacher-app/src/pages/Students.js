import { Typography } from '@mui/material';
import React from 'react';
import StudentsList from '../components/students/StudentsList';

const Students =(props)=>{
    return(
        <>
       
    <StudentsList {...props}>
    </StudentsList>
    </>
    )

}

export default Students;