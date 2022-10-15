import React, { useState } from "react";
import Box from "@mui/material/Box";
import Modal from "@mui/material/Modal";
import Button from "@mui/material/Button";
import httpClient from "../../util/http-client";
import { Alert, Grid } from "@mui/material";
import { useMutation, useQueryClient } from "react-query";
import { InputFieldsBox, Item, ModalStyle } from "../ui/Theme";
import { Form, Formik } from "formik";

import * as Yup from "yup";
import { delay } from "../../util/functions";
import { LoadingButton } from "@mui/lab";
import UploadIcon from "@mui/icons-material/UploadRounded";

async function postQnPaper(formData){
 
  
    await httpClient.post('/assessment/questionPaperUpload', formData, {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
    });

}
const UploadAssessmentQnModal = (props) => {
 
  const queryClient = useQueryClient();
  const [errorMessage,setErrorMessage]=useState();
   
  const { mutate, isLoading,isSuccess,isError } = useMutation(postQnPaper, {
     onSuccess: data => {
    
     props.setRefetchedData(true);
     delay( props.handleClose,1000);   
       
  },
    onError: (error) => {
      delay( props.handleClose,1000);   
      setErrorMessage(error);
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
          {isSuccess &&<Alert severity="success">Upload Successful!</Alert>}
          {isError &&<Alert severity="error">{errorMessage}</Alert>}
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
                                <span
                            style={{ fontWeight: "bold", paddingRight: "5px" }}>
                           
                            {props.values.file?.name}{" "}
                          </span>
                        
                            <Button  component="label" sx={{fontWeight:"bolder"}} startIcon={<UploadIcon />}>
                 Question Paper 
  <input hidden id="file" name="file" type="file" onChange={(event) => {
                    props.setFieldValue("file", event.currentTarget.files[0]);
                  }} />
</Button>
                           
                            </Item>
                            </Grid>
                            </Grid>
               
                  <LoadingButton  variant="contained"
                   loading={isLoading}
                                color="success"
                                type="submit"
                                disabled={isSuccess}
                                sx={{ float: "right" }}>
                                  Upload
                      
                    </LoadingButton>
</Form>)}
                </Formik>
          </InputFieldsBox>
        </Box>
      }
    </Modal>
  );
};
export default UploadAssessmentQnModal;
