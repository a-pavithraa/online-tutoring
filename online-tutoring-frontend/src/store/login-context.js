import React, { useState, useEffect, useCallback } from 'react';
import { useQuery } from 'react-query';
import { useNavigate } from 'react-router-dom';
import { TEACHER_GROUP } from '../util/constants';
import httpClient from '../util/http-client';

let logoutTimer;

const LoginContext = React.createContext({
  token: '',
  isLoggedIn: false, 
  userName: '',
  reqHeader:{},
  teacherId:'',
  login: (token) => { },
  logout: () => { },
  subscribe: () => { }
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
async function getTeacherDetails(userName) {
  const { data } = await httpClient.get(
    "/mdm/admin/teacher/" + userName,
    {
      staleTime: Infinity,
    }
  );
    console.log(data);
  return data;
}
export const LoginContextProvider = (props) => {
  const tokenData = retrieveStoredToken();
  const[isTeacher,setIsTeacher]=useState(false);


  let initialToken;
  if (tokenData) {
    initialToken = tokenData.token;
  }

  const [token, setToken] = useState(initialToken);  
  const navigate=useNavigate();
  const [reqHeader,setReqHeader]=useState({});
  const [loggedInUser,setLoggedInUser]=useState();
  const { data } = useQuery(['orgData', { userName: loggedInUser }], ()=>getTeacherDetails(loggedInUser), { enabled: isTeacher });


  const userIsLoggedIn =!!token && !!data;

  const logoutHandler = useCallback(() => {
    setToken(null);
    localStorage.removeItem('jwtToken');
    localStorage.removeItem('expirationTime');
    if (logoutTimer) {
      clearTimeout(logoutTimer);
    }
  
  });
  
  useEffect(()=>{
   
    if(userIsLoggedIn )
    navigate("/Classes");
    
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
    setLoggedInUser(userName);
    const isTeacher = !!tokenObject["cognito:groups"].find(x=>x===TEACHER_GROUP);
    console.log(isTeacher);
    setIsTeacher(true);
    
   
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
    reqHeader:reqHeader,
    teacherId:data?.id


  };

  return (
    <LoginContext.Provider value={contextValue}>
      {props.children}
    </LoginContext.Provider>
  );
};

export default LoginContext;
