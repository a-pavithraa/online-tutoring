import React from 'react';
import Modal from "@mui/material/Modal";
import Button from "@mui/material/Button";
import httpClient from "../../util/http-client";
import { Box, Typography } from "@mui/material";
import { useQuery } from "react-query";
import {
  Header,
  InputFieldsBox,
  ModalStyle,
} from "../ui/Theme";
import {
    AreaChart,
    Area,
    XAxis,
    YAxis,
    CartesianGrid,
    Tooltip,
    ResponsiveContainer,
    Label,
    LineChart,
    Line
  } from "recharts";
  import { curveCardinal } from 'd3-shape';
async function fetchStudentPerformance(studentId){
   
    const res = await httpClient.get(
      `/assessment/studentPerformance?studentId=${studentId}` 
    );
    return res.data;

}

const StudentPerformanceModal = (props)=>{
    const { data, isFetching,status } = useQuery(
        ["studentPerformance",props.studentId],
        () =>
        fetchStudentPerformance(props.studentId),
        {
          refetchOnWindowFocus: false,
        }
      );
      const cardinal = curveCardinal.tension(0.2);
      const dataLoaded = data && data.studentPerformanceList;
      return  <Modal
      hideBackdrop
      open={props.open}
      onClose={props.handleClose}
      aria-labelledby="child-modal-title"
      aria-describedby="child-modal-description"
    >
      
        <Box sx={{ ...ModalStyle }}>
          <div style={{ float: "right" }}>
            <Button
              sx={{ fontWeight: "bolder", color: "red", fontSize: 14 }}
              onClick={props.handleClose}
            >
              X
            </Button>
          </div>
          <InputFieldsBox sx={{ maxWidth: 1500 }}>
          <Header variant="h5" component="h4">
          ASSESSMENT PERFORMANCE OF {props.studentName}
            </Header>
           
        {dataLoaded &&    <div style={{ width: '100%', height: 300 }}>
        <ResponsiveContainer><LineChart
   
      data={data.studentPerformanceList}
      margin={{
        top: 10,
        right: 30,
        left: 0,
        bottom: 0
      }}
    >
      <CartesianGrid strokeDasharray="3 3" />
      <XAxis dataKey="assessmentDate" >
      <Label value="Date" position="bottom" />
        </XAxis>
      <YAxis dataKey="marks">
      <Label
              value="Marks &#8457;"
              angle={-90}
              position="left"
              dy="-10"
            />
        
        </YAxis>
      <Tooltip />
      <Line dataKey="marks"   stroke="#8884d8"
            fill="#8884d8"   dot={{ stroke: 'red', strokeWidth: 1, r: 4,strokeDasharray:''}}/>
    </LineChart>
    </ResponsiveContainer>
    </div>
}
          </InputFieldsBox>
          </Box>
          </Modal>
      }

export default StudentPerformanceModal;