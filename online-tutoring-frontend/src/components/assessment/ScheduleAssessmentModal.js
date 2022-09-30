import React, { useContext } from "react";
import Box from "@mui/material/Box";
import Modal from "@mui/material/Modal";
import Button from "@mui/material/Button";
import httpClient from "../util/http-client";
import { CircularProgress, Grid } from "@mui/material";
import { useMutation, useQuery, useQueryClient } from "react-query";
import { Header, InputFieldsBox, Item } from "../ui/Theme";
import { Field, FieldArray, Form, Formik } from "formik";
import {  DateField } from "../ui/FormInputs";

import * as Yup from "yup";
import moment from "moment/moment";
import { getCurrentTime } from "../util/functions";
import LoginContext from "../../store/login-context";

const style = {
  position: "absolute",
  top: "50%",
  left: "50%",
  transform: "translate(-50%, -50%)",

  bgcolor: "background.paper",
  border: "2px solid #000",
  boxShadow: 24,
  pt: 2,
  px: 4,
  pb: 3,
};
async function createAssessment(createAssessmentRequest){
 
  console.log(createAssessmentRequest);
  await httpClient.post("/assessment/assessment" ,createAssessmentRequest);  

}
const ScheduleAssessmentModal = (props) => {
  const context = useContext(LoginContext);
  const queryClient = useQueryClient();
   
  const { mutate, isLoading } = useMutation(createAssessment, {
     onSuccess: data => {
       
        const message = "success";
        alert(message);
        props.handleClose();
  },
    onError: () => {
         alert("there was an error");
         props.handleClose();
  },
    onSettled: () => {
       queryClient.invalidateQueries('create')
  }
  });
  return (
    <Modal
      hideBackdrop
      open={props.open}
      onClose={props.handleClose}
      aria-labelledby="child-modal-title"
      aria-describedby="child-modal-description"
    >
      {
        <Box sx={{ ...style }}>
          <div style={{ float: "right" }}>
            <Button
              sx={{ fontWeight: "bolder", color: "red", fontSize: 14 }}
              onClick={props.handleClose}
            >
              X
            </Button>
          </div>
          <InputFieldsBox sx={{ maxWidth: 1100 }}>
          <Formik
                initialValues={{assessmentDate: getCurrentTime() }}
                onSubmit={async (values) =>{
                  const createAssessmentRequest ={
                    gradeId:props.gradeId,
                    subjectId:props.subjectId,
                    teacherId:context.teacherId,
                    assessmentDate:moment(values.assessmentDate).format("YYYY-MM-DD HH:mm")
                
                  };
                  mutate(JSON.stringify(createAssessmentRequest));
                }
            }
            >
              {(props) => (
          <Form >
            <Grid container sx={{ paddingBottom: "10px" }} >
                        <Grid item xs={12} >
                            <Item>
                            <DateField   label="Assessment Date"
                    name="assessmentDate"
                    disablePast
                    selected={props.values.assessmentDate}
                    onChange={(value) =>
                      props.setFieldValue('assessmentDate',value)
                    }
                  
                    variant="outlined"
                    placeholder="Assessment"/>
                            </Item>
                            </Grid>
                            </Grid>
               
                   {isLoading?<CircularProgress/>:<Button  variant="contained"
                                color="success"
                                type="submit"
                                sx={{ float: "right" }}>
                                  Submit
                      
                    </Button>} 
</Form>)}
                </Formik>
          </InputFieldsBox>
        </Box>
      }
    </Modal>
  );
};
export default ScheduleAssessmentModal;
