import React, { useContext, useState } from 'react';

import { Formik, Form } from "formik";
import * as yup from "yup";
import Button from "@mui/material/Button";
import { Header, InputFieldsBox, Item, LoginBox, TextInputGrid } from '../components/ui/Theme';
import { CircularProgress, Grid } from '@mui/material';
import { TextInput } from '../components/ui/FormInputs';
import LoginContext from '../store/login-context';
import { useNavigate } from 'react-router-dom';
import * as AmazonCognitoIdentity from 'amazon-cognito-identity-js';
import { CLIENT_ID, USER_POOL_ID } from '../util/constants';
const validationSchema = yup.object({
    userName: yup
        .string('Enter your user name')       
        .required('User Name is required'),
   
    password: yup
        .string('Enter Password')       
        .required('Passwird is required'),

});
const Login =(props)=>{
    const context = useContext(LoginContext);
    const navigate = useNavigate();
    const [progress,setProgress]=useState(false);

    const loginHandler = (userName,password) => {

       setProgress(true);
        var authenticationData = {
          Username: userName,
          Password: password,
        };
        var authenticationDetails = new AmazonCognitoIdentity.AuthenticationDetails(authenticationData);
        var poolData = {
          UserPoolId: USER_POOL_ID,
          ClientId: CLIENT_ID
        };
        var userPool = new AmazonCognitoIdentity.CognitoUserPool(poolData);
        var userData = {
          Username: userName,
          Pool: userPool
        };
        var cognitoUser = new AmazonCognitoIdentity.CognitoUser(userData);
    
    
        cognitoUser.authenticateUser(authenticationDetails, {
          onSuccess: function (result) {
            var accessToken = result.getAccessToken().getJwtToken();
            var idToken = result.idToken.jwtToken;
            console.log(accessToken);
            localStorage.setItem('jwtToken', accessToken);
            setProgress(false);        
            const expirationTime = new Date(
              new Date().getTime() + +3600 * 1000
            );
    
            context.login(idToken,accessToken, expirationTime);        
         
    
            /* Use the idToken for Logins Map when Federating User Pools with identity pools or when passing through an Authorization Header to an API Gateway Authorizer */
    
    
          },
    
          onFailure: function (err) {
            alert('Invalid credentials');
            setProgress(false);
           
    
          },
    
        });
      }
    return ( <LoginBox>
        <Formik
            initialValues={{
                userName: '',
                password:''                
              

            }}
            validationSchema={validationSchema}
            onSubmit={async (values) => {
             
               
                 loginHandler(values.userName,values.password);
                 
              
            }}
        >
      {props =>  
             <Form style={{width:"100%"}}>
                <Header variant="h4" component="h2" sx={{textAlign:'center'}}>LOGIN</Header>
                <Grid container sx={{  paddingRight: "10px", paddingBottom: "15px", paddingTop: "5px"}} >
                    <TextInputGrid item xs={12}  >
                        <Item>
                            <TextInput
                                fullWidth
                                label="User Name"
                                name="userName"
                                type="text"
                                variant="outlined"
                                placeholder="Name"
                            />
                        </Item>
                    </TextInputGrid>
                    
                    
                    <TextInputGrid item xs={12} >
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
                    </TextInputGrid>
                  
                   
                  

                </Grid>
               
               {progress?<CircularProgress/>:<Button type="submit" variant="contained" color="success" sx={{float:'right'}} >Login</Button>} 
        

            </Form>}
      
        </Formik>
    </LoginBox>);
}
export default Login;