import moment from "moment";
export const getCurrentTime=()=>{
    const currentTime = new Date();    
const convertTime = moment(currentTime).format("MM/DD/YYYY HH:mm");
const convertTimeObject = new Date(convertTime);
return convertTimeObject;
}


export const delay = (fn,timer)=>{
    setTimeout(fn, timer);
  
  }