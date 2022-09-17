import React, {  } from 'react';
import { Formik, Form } from 'formik';

import * as yup from 'yup';
import Button from '@mui/material/Button';
import { TextInput } from '../ui/FormInputs';
import { CircularProgress, Grid } from '@mui/material';
import { Header, InputFieldsBox, Item } from '../ui/Theme';
import moduleClasses from './Registration.module.scss';
import httpClient from '../util/http-client';
import { useMutation, useQueryClient } from 'react-query';




const validationSchema = yup.object({
    emailId: yup
        .string('Enter your email')
        .email('Enter a valid email')
        .required('Email is required'),
   
    fullName: yup
        .string('Enter Full Name')
        .max(30)
        .required('Name is required'),

});
async function getRefData(urlSuffix) {
    const res = await httpClient.get("/referenceData/" + urlSuffix);  
   return res.data;

}
async function createTeacher(teacherData){
    await httpClient.post("/admin/teacher" ,teacherData);  

}
const TeacherRegistration = () => {
    // const grades =[{id: 1, value:"one"},{id: 2, value:"two"},{id: 3, value:"three"}];
  
    const queryClient = useQueryClient();
   
     const { mutate, isLoading } = useMutation(createTeacher, {
        onSuccess: data => {
           console.log(data);
           const message = "success"
           alert(message)
     },
       onError: () => {
            alert("there was an error")
     },
       onSettled: () => {
          queryClient.invalidateQueries('create')
     }
     });
  

    return (
        <InputFieldsBox>
            <Formik
                initialValues={{
                    fullName: '',
                    password:'',                  
                    emailId: '',                  
                    phoneNo: ''

                }}
                validationSchema={validationSchema}
                onSubmit={async (values) => {
                 
                    console.log(values);
                   
                    const studentData = JSON.stringify(values, null, 2);
                    console.log(studentData);
                  
                    const newData = {
                        ...values,
                       
                        userName: values.emailId


                    }
                    var jsonData =JSON.stringify(newData, null, 2);
                    mutate(jsonData);
                }}
            >
          {props =>  
                 <Form className={moduleClasses.formCls}>
                    <Header variant="h4" component="h2">TEACHER REGISTRATION</Header>
                    <Grid container sx={{ paddingBottom: "10px" }} >
                        <Grid item xs={12} className={moduleClasses.textInput}>
                            <Item>
                                <TextInput
                                    fullWidth
                                    label="Teacher Name"
                                    name="fullName"
                                    type="text"
                                    variant="outlined"
                                    placeholder="Name"
                                />
                            </Item>
                        </Grid>
                        
                        
                        <Grid item xs={12} className={moduleClasses.textInput}>
                            <Item>
                                <TextInput fullWidth
                                    label="Email Address"
                                    name="emailId"
                                    type="email"
                                    placeholder="Email"
                                    variant="outlined"
                                />
                            </Item>
                        </Grid>
                        <Grid item xs={6} className={moduleClasses.textInput}>
                            <Item>
                                <TextInput
                                    fullWidth
                                    label="Password"
                                    name="password"
                                    type="password"
                                    variant="outlined"
                                    placeholder="Password"
                                />
                            </Item>
                        </Grid>
                        <Grid item xs={6} className={moduleClasses.textInput}>
                            <Item>
                                <TextInput fullWidth
                                    label="Contact No"
                                    name="phoneNo"
                                    placeholder="Phone"
                                    variant="outlined"
                                />
                            </Item>
                        </Grid>
                       
                      

                    </Grid>
                    {isLoading ? (
              <CircularProgress />
            ) : (
                    <Button type="submit" variant="contained" color="success" >Submit</Button>
            )}

                </Form>}
          
            </Formik>
        </InputFieldsBox>
    );
};

export default TeacherRegistration;