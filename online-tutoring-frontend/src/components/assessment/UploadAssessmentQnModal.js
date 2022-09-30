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
async function postQnPaper(formData){
 
  
    await httpClient.post('/assessment/questionPaperUpload', formData, {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
    });

}
const UploadAssessmentQnModal = (props) => {
  const context = useContext(LoginContext);
  const queryClient = useQueryClient();
   
  const { mutate, isLoading } = useMutation(postQnPaper, {
     onSuccess: data => {
       
        const message = "success";
        alert('Document Uploaded');
     //  props.refetch();
     props.setRefetchedData(true);
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
              initialValues={{ file: null }}
              onSubmit={(values) => {
                var formData = new FormData();
                formData.append('assessmentId',props.assessmentId);
                formData.append('file',values.file);
                
             mutate(formData);
             
              }} 
              validationSchema={Yup.object().shape({
                file: Yup.mixed().required(),
              })}
            
            >
              {(props) => (
          <Form >
            <Grid container sx={{ paddingBottom: "10px" }} >
                        <Grid item xs={12} >
                            <Item>
                            <input id="file" name="file" type="file" onChange={(event) => {
                    props.setFieldValue("file", event.currentTarget.files[0]);
                  }} className="form-control" />
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
export default UploadAssessmentQnModal;
