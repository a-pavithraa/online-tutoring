import React, { useContext } from "react";
import Modal from "@mui/material/Modal";
import Button from "@mui/material/Button";
import httpClient from "../../util/http-client";
import { Box, CircularProgress, Fab, Grid } from "@mui/material";
import { useMutation, useQueryClient } from "react-query";
import {
  InputFieldsBox,
  Item,
  ModalBox,
  ModalGridItem,
  ModalStyle,
} from "../ui/Theme";
import { Form, Formik } from "formik";
import { TextInput } from "../ui/FormInputs";

import * as Yup from "yup";
import LoginContext from "../../store/login-context";
import AddIcon from "@mui/icons-material/Add";

async function postQnPaper(formData) {
  await httpClient.post("/assessment/updateSubmittedAssessment", formData, {
    headers: {
      "Content-Type": "multipart/form-data",
    },
  });
}
const UpdateSubmissionModal = (props) => {
  const context = useContext(LoginContext);
  const queryClient = useQueryClient();

  const { mutate, isLoading } = useMutation(postQnPaper, {
    onSuccess: (data) => {
      const message = "success";
      alert("Document Uploaded");
      //  props.refetch();
      props.setRefetchedData(true);
      props.handleClose();
    },
    onError: () => {
      alert("there was an error");
      props.handleClose();
    },
    onSettled: () => {
      queryClient.invalidateQueries("create");
    },
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
            <Formik
              initialValues={{ file: null }}
              onSubmit={(values) => {
                var formData = new FormData();
                console.log(props);
                formData.append("assessmentId", props.rowData.assessmentId);
                formData.append("studentId", props.rowData.studentId);
                formData.append("cognitoStudentId", props.rowData.cognitoId);
                formData.append("studentEmail", props.rowData.studentMailId);
                formData.append("correctedDocument", values.file);
                formData.append("marks", values.marks);

                mutate(formData);
              }}
              validationSchema={Yup.object().shape({
                file: Yup.mixed().required(),
                marks: Yup.number().required(),
              })}
            >
              {(props) => (
                <Form>
                  <Grid
                    container
                    sx={{ paddingBottom: "10px", border: "none" }}
                  >
                    <ModalGridItem item xs={12}>
                      <Item>
                        <TextInput
                          fullWidth
                          label="Marks"
                          name="marks"
                          variant="outlined"
                          placeholder="Marks"
                        />
                      </Item>
                    </ModalGridItem>
                    <ModalGridItem item xs={12}>
                      <Item sx={{ float: "left" }}>
                        {
                          <span
                            style={{ fontWeight: "bold", paddingRight: "5px" }}
                          >
                           
                            {props.values.file?.name}{" "}
                          </span>
                        }
                        <label htmlFor="file">
                          <input
                            id="file"
                            name="file"
                            type="file"
                            onChange={(event) => {
                              props.setFieldValue(
                                "file",
                                event.currentTarget.files[0]
                              );
                            }}
                            className="form-control"
                            style={{ display: "none" }}
                          />

                          <Fab
                            color="primary"
                            size="small"
                            component="div"
                            aria-label="add"
                            variant="extended"
                          >
                            <AddIcon /> Upload Corrected Document
                          </Fab>
                        </label>
                      </Item>
                    </ModalGridItem>
                  </Grid>

                  {isLoading ? (
                    <CircularProgress />
                  ) : (
                    <Button
                      variant="contained"
                      color="success"
                      type="submit"
                      sx={{ float: "right" }}
                    >
                      Submit
                    </Button>
                  )}
                </Form>
              )}
            </Formik>
          </InputFieldsBox>
        </Box>
      }
    </Modal>
  );
};
export default UpdateSubmissionModal;
