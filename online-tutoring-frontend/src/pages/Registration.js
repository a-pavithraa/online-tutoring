import React from 'react';
import ReactDOM from 'react-dom';
import { Formik, Form, useField } from 'formik';

import * as yup from 'yup';
import Button from '@mui/material/Button';
import TextField from '@mui/material/TextField';
import { MySelect, TextInput } from '../components/ui/FormInputs';
import { FormControl, Grid, MenuItem, Typography } from '@mui/material';
import { BootstrapInput, InputFieldsBox, Item } from '../components/ui/Theme';
import moduleClasses from './Registration.module.scss'
import SelectInput from '@mui/material/Select/SelectInput';

const validationSchema = yup.object({
  email: yup
    .string('Enter your email')
    .email('Enter a valid email')
    .required('Email is required'),
    subject: yup
    .string('Enter subject')   
    .required('Subject is required'),
    firstName: yup
    .string('Enter First Name')  
    .max(30)
    .required('Name is required'),
 
});

const Registration = () => {
  const grades =[{id: 1, value:"one"},{id: 2, value:"two"},{id: 3, value:"three"}]
 

  return (
    <InputFieldsBox>
      <Formik
         initialValues={{
           firstName: 'pills',          
           email: '',
           grade: "20",
           subject: '',
           contactNo:''
        
         }}
         validationSchema={validationSchema}
         onSubmit={async (values) => {
            await new Promise((r) => setTimeout(r, 500));
            alert(JSON.stringify(values, null, 2));
          }}
       >
         <Form className={moduleClasses.formCls}>
          <Typography variant="h4" component="h2">STUDENT REGISTRATION</Typography>
            <Grid  container  sx={{paddingBottom:"10px"}} >
            <Grid item xs={12}  sx={{paddingBottom:'15px'}}>
                <Item>
            <TextInput
            fullWidth 
             label="Name"
             name="firstName"
             type="text"
             variant="standard"
             placeholder="Name"
           />
           </Item>
  </Grid>
  <Grid item xs={12}   sx={{paddingBottom:"15px"}}>
  <Item>
  <TextInput fullWidth
             label="Email Address"
             name="email"
             type="email"
             placeholder="Email"
             
             variant="standard"
           />
           </Item>
  </Grid>
  <Grid item xs={12}   sx={{paddingBottom:"15px"}}>
  <Item>
  <TextInput fullWidth
             label="Contact No"
             name="contactNo"
             
             placeholder="Email"
             
             variant="standard"
           />
           </Item>
  </Grid>
  <Grid item xs={12}  sx={{paddingBottom:"15px"}}>
  <Item>
 
           <MySelect 
             label="Grade"
             name="grade"
             type="number"            
            
            >
              <option value={10}>Ten</option>
    <option value={20}>Twenty</option>
    <option value={30}>Thirty</option>
    </MySelect>
               
         
          
           </Item>
  </Grid>
  <Grid item xs={12}   sx={{paddingBottom:"15px"}}>
  <Item>
  <MySelect 
             label="Subject"
             name="subject"
             type="text"
             
            
            >
              <option value="Maths">Maths</option>
    <option value="Science">Science</option>
    <option value="English">English</option>
    </MySelect>
           </Item>
  </Grid>

            </Grid>
            
         
 
          
 
          
           <Button type="submit" variant="contained" color="success" >Submit</Button>
         </Form>
       </Formik>
    </InputFieldsBox>
  );
};

export default Registration;