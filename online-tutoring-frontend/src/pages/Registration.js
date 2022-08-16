import React from 'react';
import ReactDOM from 'react-dom';
import { Formik, Form, useField } from 'formik';

import * as yup from 'yup';
import Button from '@mui/material/Button';
import TextField from '@mui/material/TextField';
import { TextInput } from '../components/ui/FormInputs';
import { Grid } from '@mui/material';
import { InputFieldsBox, Item } from '../components/ui/Theme';
import moduleClasses from './Registration.module.scss'

const validationSchema = yup.object({
  email: yup
    .string('Enter your email')
    .email('Enter a valid email')
    .required('Email is required')
 
});

const Registration = () => {
 

  return (
    <InputFieldsBox>
      <Formik
         initialValues={{
           firstName: '',          
           email: '',
           grade: '',
           subject: ''
        
         }}
         validationSchema={validationSchema}
         onSubmit={async (values) => {
            await new Promise((r) => setTimeout(r, 500));
            alert(JSON.stringify(values, null, 2));
          }}
       >
         <Form className={moduleClasses.formCls}>
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
  <Grid item xs={12}  sx={{paddingBottom:"15px"}}>
  <Item>
  <TextInput fullWidth
             label="Grade"
             name="grade"
             type="number"
             placeholder="Grade"
             
             variant="standard"
           />
           </Item>
  </Grid>
  <Grid item xs={12}   sx={{paddingBottom:"15px"}}>
  <Item>
  <TextInput fullWidth
             label="Subject"
             name="subject"
             type="text"
             placeholder="Subject"
             
             variant="standard"
           />
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