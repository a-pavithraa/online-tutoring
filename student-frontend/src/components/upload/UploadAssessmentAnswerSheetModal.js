import React, { useCallback, useContext } from "react";
import Box from "@mui/material/Box";
import Modal from "@mui/material/Modal";
import Button from "@mui/material/Button";

import { CircularProgress, Grid } from "@mui/material";

import { Header, InputFieldsBox, Item, ModalStyle } from "../ui/Theme";
import { Field, FieldArray, Form, Formik } from "formik";

import * as Yup from "yup";

import LoginContext from "../../store/login-context";
import { LoadingButton } from "@mui/lab";
import UploadIcon from "@mui/icons-material/UploadRounded";
import {
  S3Client,
  PutObjectCommand,
  ListObjectsCommand,
  DeleteObjectCommand,
  DeleteObjectsCommand,
} from "@aws-sdk/client-s3";
import {
  BUCKET_NAME,
  COGNITO_ENDPOINT,
  IDENTITY_POOL_ID,
  REGION,
} from "../../util/constants";
import { CognitoIdentityClient } from "@aws-sdk/client-cognito-identity";
import { fromCognitoIdentityPool } from "@aws-sdk/credential-provider-cognito-identity";
import { useMutation, useQueryClient } from "react-query";

const UploadAssessmentAnswerSheetModal = (props) => {
  const { token, cognitoIdPoolIdentity,cognitoId } = useContext(LoginContext);
  const queryClient = useQueryClient();
  const { mutate, isLoading } = useMutation(postAnswerSheet, {
    onSuccess: data => {
      
      props.setRefetchedData(true);
       alert('Document Uploaded');
       
  
    props.handleClose();
      
 },
   onError: () => {
        alert("there was an error");
        props.handleClose();
 },
   onSettled: () => {
   // queryClient.invalidateQueries('assessmentDetails');
     
 }
 });
 async function postAnswerSheet(file) {

  try {
  const s3 = new S3Client({
    region: REGION,
    credentials: fromCognitoIdentityPool({
      client: new CognitoIdentityClient({ region: REGION }),
      identityPoolId: IDENTITY_POOL_ID,
      logins: {
        [COGNITO_ENDPOINT]: token,
      },
    }),
  });
  
  const {teacherId,assessmentId}=props;
  let key =  `${cognitoIdPoolIdentity}/Uploads/${teacherId}/${assessmentId}/${file.name}`;
  console.log(key);
  const uploadParams = {
    Bucket: BUCKET_NAME,
    Key: key,
    Body: file,
    Metadata: {
      'teacherid': `${teacherId}`,
      'assessmentid':`${assessmentId}`,
      'cognitoid': cognitoId
    }
  }; 
  
    await s3.send(new PutObjectCommand(uploadParams));
    
  } catch (err) {
    console.log(err);
   throw err;
  }

}

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
            <Formik
              initialValues={{ file: null }}
              onSubmit={async(values) => {
               

                // Initialize the Amazon Cognito credentials provider
              
               mutate(values.file);
               
              }}
              validationSchema={Yup.object().shape({
                file: Yup.mixed().required(),
              })}
            >
              {(props) => (
                <Form>
                  <Grid container sx={{ paddingBottom: "10px" }}>
                    <Grid item xs={12}>
                      <Item>
                        <span
                          style={{ fontWeight: "bold", paddingRight: "5px" }}
                        >
                          {props.values.file?.name}{" "}
                        </span>

                        <Button
                          component="label"
                          sx={{ fontWeight: "bolder" }}
                          startIcon={<UploadIcon />}
                        >
                          Answer Sheet
                          <input
                            hidden
                            id="file"
                            name="file"
                            type="file"
                            onChange={(event) => {
                              props.setFieldValue(
                                "file",
                                event.currentTarget.files[0]
                              );
                            }}
                          />
                        </Button>
                      </Item>
                    </Grid>
                  </Grid>

                  <LoadingButton
                    variant="contained"
                    loading={isLoading}
                    color="success"
                    type="submit"
                    sx={{ float: "right" }}
                  >
                    Upload
                  </LoadingButton>
                </Form>
              )}
            </Formik>
          </InputFieldsBox>
        </Box>
      }
    </Modal>
  );
};
export default UploadAssessmentAnswerSheetModal;
