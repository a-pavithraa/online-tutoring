import React, { useEffect, useState } from 'react';
import ReactDOM from 'react-dom';
import { Formik, Form, useField } from 'formik';

import * as yup from 'yup';
import Button from '@mui/material/Button';
import TextField from '@mui/material/TextField';
import { MySelect, TextInput,ReactSelect, ComboBox } from '../ui/FormInputs';
import { FormControl, Grid, MenuItem, Typography } from '@mui/material';
import { BootstrapInput, InputFieldsBox, Item } from '../ui/Theme';
import moduleClasses from './Registration.module.scss';
import httpClient from '../util/http-client';
import { useMutation, useQuery, useQueryClient } from 'react-query';




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
async function createStudent(studentData){
    await httpClient.post("/admin/student" ,studentData);  

}
const TeacherRegistration = () => {
    // const grades =[{id: 1, value:"one"},{id: 2, value:"two"},{id: 3, value:"three"}];
  
    const queryClient = useQueryClient();
     const { status:subjectsFetchedStatus, data:subjects , error:subError, isFetching:subjectsFetching }  = useQuery('subjects', ()=>getRefData('subjects'),{ staleTime: Infinity }); 
     const { status:gradesFetchedStatus, data:grades , error:gradeError, isFetching:gradesFetching }  = useQuery('grades', ()=>getRefData('grades'),{ staleTime: Infinity }); 
     const { mutate, isLoading } = useMutation(createStudent, {
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
                    parentName:'',
                    emailId: '',
                    grade: grades && grades[0],
                    subjects:subjects && [subjects[0]],
                    phoneNo: ''

                }}
                validationSchema={validationSchema}
                onSubmit={async (values) => {
                 
                    console.log(values);
                   
                    const studentData = JSON.stringify(values, null, 2);
                    console.log(studentData);
                    const subjectsSelected = values.subjects.map(subject=>subject.value);
                    const newData = {
                        ...values,
                        subjects:subjectsSelected,
                        userName: values.emailId


                    }
                    var jsonData =JSON.stringify(newData, null, 2);
                 //   mutate(jsonData);
                }}
            >
          {props =>  
                 <Form className={moduleClasses.formCls}>
                    <Typography variant="h4" component="h2">STUDENT REGISTRATION</Typography>
                    <Grid container sx={{ paddingBottom: "10px" }} >
                        <Grid item xs={6} className={moduleClasses.textInput}>
                            <Item>
                                <TextInput
                                    fullWidth
                                    label="Student Name"
                                    name="fullName"
                                    type="text"
                                    variant="outlined"
                                    placeholder="Name"
                                />
                            </Item>
                        </Grid>
                        <Grid item xs={6} className={moduleClasses.textInput}>
                            <Item>
                                <TextInput
                                    fullWidth
                                    label="Parent Name"
                                    name="parentName"
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
                        <Grid item xs={6} className={moduleClasses.textInput}>
                            <Item>

                                <ComboBox                                
                                    label="Grade"
                                    name="grade" 
                                    onChange={value=>{console.log(value);props.setFieldValue('grade',value)}}                                                   
                                    options={grades}
                                />
                                                    
                            </Item>
                        </Grid>
                        <Grid item xs={6} className={moduleClasses.textInput}>
                            <Item>
                           

                                <ComboBox                                
                                    label="Subjects"
                                    name="subjects"  
                                    multiple
                                    onChange={values=>props.setFieldValue('subjects',values)}                                                   
                                    options={subjects}
                                />
                            </Item>
                        </Grid>
                      

                    </Grid>

                    <Button type="submit" variant="contained" color="success" >Submit</Button>
                </Form>}
          
            </Formik>
        </InputFieldsBox>
    );
};

export default TeacherRegistration;