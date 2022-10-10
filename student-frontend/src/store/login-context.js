import React, { useState, useEffect, useCallback } from 'react';

import { useLocation, useNavigate } from 'react-router-dom';
import { STUDENT_GROUP, TEACHER_GROUP } from '../util/constants';


let logoutTimer;

const LoginContext = React.createContext({
  token: '',
  isLoggedIn: false, 
  userName: '',
  cognitoId:'',
  
  login: (token) => { },
  logout: () => { }

});

const calculateRemainingTime = (expirationTime) => {
  const currentTime = new Date().getTime();
  const adjExpirationTime = new Date(expirationTime).getTime();

  const remainingDuration = adjExpirationTime - currentTime;

  return remainingDuration;
};

const retrieveStoredToken = () => {
  const storedToken = localStorage.getItem('jwtToken');
  const storedExpirationDate = localStorage.getItem('expirationTime');

  const remainingTime = calculateRemainingTime(storedExpirationDate);

  if (remainingTime <= 3600) {
    localStorage.removeItem('jwtToken');
    localStorage.removeItem('expirationTime');
    return null;
  }

  return {
    token: storedToken,
    duration: remainingTime,
  };
};

export const LoginContextProvider = (props) => {
  const tokenData = retrieveStoredToken();
  const[isStudent,setIsStudent]=useState(false);
  const { location } = useLocation();

  let initialToken;
  if (tokenData) {
    initialToken = tokenData.token;
  }

  const [token, setToken] = useState(initialToken);  
  const navigate=useNavigate();
  const [cognitoId,setCognitoId]=useState();
  const [loggedInUser,setLoggedInUser]=useState();
  



  const userIsLoggedIn =!!token ;

  const logoutHandler = useCallback(() => {
    setToken(null);
    localStorage.removeItem('jwtToken');
    localStorage.removeItem('expirationTime');
    if (logoutTimer) {
      clearTimeout(logoutTimer);
    }
  
  });
  
  useEffect(()=>{
    
    if(userIsLoggedIn && isStudent )
    navigate("/");
    
  },[userIsLoggedIn])

  function parseToken(token){
    if (!token) { return; }
    const base64Url = token.split('.')[1];
    const base64 = base64Url.replace('-', '+').replace('_', '/');
    const jsonObj = JSON.parse(window.atob(base64));
    console.log("parsing user name");
    console.log(jsonObj);
    return jsonObj;
  }

  

  const loginHandler = async (idToken,accessToken, expirationTime) => {
    setToken(idToken);
    const tokenObject = parseToken(idToken);
    const userName = tokenObject["cognito:username"];
    const cognitoId = tokenObject["sub"];
    setLoggedInUser(userName);
    setCognitoId(cognitoId);
    const isStudent = !!tokenObject["cognito:groups"].find(x=>x===STUDENT_GROUP);
    console.log(isStudent);
    setIsStudent(isStudent);
    
   
    localStorage.setItem('jwtToken', accessToken);
   
    localStorage.setItem('expirationTime', expirationTime);

    const remainingTime = calculateRemainingTime(expirationTime);

    logoutTimer = setTimeout(logoutHandler, remainingTime);

    

  };

  useEffect(() => {
    if (tokenData) {
     
      logoutTimer = setTimeout(logoutHandler, tokenData.duration);
    }
  }, [tokenData, logoutHandler]);
  

  const contextValue = {
    token: token,
    isLoggedIn: userIsLoggedIn,
    userName:loggedInUser,
    login: loginHandler,
    logout: logoutHandler,  
    cognitoId:cognitoId,
   


  };

  return (
    <LoginContext.Provider value={contextValue}>
      {props.children}
    </LoginContext.Provider>
  );
};

export default LoginContext;
