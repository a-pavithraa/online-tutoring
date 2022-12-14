import React, { useContext, useState } from "react";
import Box from "@mui/material/Box";
import Modal from "@mui/material/Modal";
import Button from "@mui/material/Button";
import httpClient from "../../util/http-client";
import { Alert, Grid } from "@mui/material";
import { useMutation, useQueryClient } from "react-query";
import { InputFieldsBox, Item, ModalStyle } from "../ui/Theme";
import { Form, Formik } from "formik";
import {  DateField } from "../ui/FormInputs";

import moment from "moment/moment";
import { delay, getCurrentTime } from "../../util/functions";
import LoginContext from "../../store/login-context";
import { LoadingButton } from "@mui/lab";


async function createAssessment(createAssessmentRequest){
 
  console.log(createAssessmentRequest);
  await httpClient.post("/assessment/assessment" ,createAssessmentRequest);  

}
const ScheduleAssessmentModal = (props) => {
  const context = useContext(LoginContext);
  const queryClient = useQueryClient();
  const [errorMessage,setErrorMessage]=useState();
   
  const { mutate, isLoading,isSuccess,isError } = useMutation(createAssessment, {
     onSuccess: data => {
      delay( props.handleClose,1000);   
      
       
  },
    onError: (error) => {
        setErrorMessage(error);
        delay( props.handleClose,1000);   
  },
    onSettled: () => {
    queryClient.invalidateQueries('assessmentDetails')
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
        <Box sx={{ ...ModalStyle }}>
         
          <div style={{ float: "right" }}>
            <Button
              sx={{ fontWeight: "bolder", color: "red", fontSize: 14 }}
              onClick={props.handleClose}
            >
              X
            </Button>
          </div>
          <InputFieldsBox sx={{ maxWidth: 1100 }}>
          {isSuccess &&<Alert severity="success">Assessment Scheduled!</Alert>}
          {isError &&<Alert severity="error">{errorMessage}</Alert>}
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
               
                <LoadingButton loading={isLoading}  variant="contained"
                                color="success"
                                type="submit"
                                disabled={isSuccess}
                                sx={{ float: "right" }}>
                                  Submit
                      
                    </LoadingButton>
</Form>)}
                </Formik>
          </InputFieldsBox>
        </Box>
      }
    </Modal>
  );
};
export default ScheduleAssessmentModal;
