import React from "react";
import Box from "@mui/material/Box";
import Modal from "@mui/material/Modal";
import Button from "@mui/material/Button";
import httpClient from "../util/http-client";
import { CircularProgress, Grid } from "@mui/material";
import { useQuery } from "react-query";
import { Header, InputFieldsBox, Item } from "../ui/Theme";
import { Field, FieldArray, Form, Formik } from "formik";
import { ComboBox } from "../ui/FormInputs";
import moduleClasses from "./Registration.module.scss";
import * as Yup from "yup";
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
async function getSubjectsAndGrades(teacherId) {
  const { data } = await httpClient.get(
    "/mapping/gradeAndSubjectsOfTeacher?teacherId=" + teacherId,
    {
      staleTime: Infinity,
    }
  );

  return data;
}
async function getRefData(urlSuffix) {
  const res = await httpClient.get("/referenceData/" + urlSuffix);
  return res.data;
}
function mapperFn(data) {
  const mapObj = [];
  data.forEach((x) => {
    let subjectObj = { value: x.subjectId, label: x.subjectName };
    const collection = mapObj.find((obj) => x.gradeId == obj.gradeId)?.subjects;

    if (collection) {
      collection.push(subjectObj);
    } else {
      let gradeObj = { gradeId: x.gradeId, subjects: [subjectObj] };
      mapObj.push(gradeObj);
    }
  });

  return mapObj;
}
const validationSchema =Yup.object({
  gradeSubjectMappings: Yup.array().of(
    Yup.object().shape({
      gradeId: Yup.string().required("Grade required"),
      subjects: Yup.array().min(
        1,
        "The error message if length === 0 | 1"
      ),
    })
  ),
})
const TeacherMappingModal = (props) => {
  const { status, data, error, isFetching } = useQuery(
    "teacherMappingModel_" + props.id,
    () => getSubjectsAndGrades(props.id),
    {
      refetchOnWindowFocus: false,
      select: mapperFn,
    }
  );
  const {
    status: subjectsFetchedStatus,
    data: subjects,
    error: subError,
    isFetching: subjectsFetching,
  } = useQuery("subjects", () => getRefData("subjects"), {
    staleTime: Infinity,
  });
  const {
    status: gradesFetchedStatus,
    data: grades,
    error: gradeError,
    isFetching: gradesFetching,
  } = useQuery("grades", () => getRefData("grades"), { staleTime: Infinity });
  return (
    <Modal
      hideBackdrop
      open={props.open}
      onClose={props.handleClose}
      aria-labelledby="child-modal-title"
      aria-describedby="child-modal-description"
    >
      {
       
        <Box sx={{ ...style}}>
          <div  style={{float:'right'}}>
          <Button sx={{fontWeight:'bolder', color:'red', fontSize:14}} onClick={props.handleClose}>X</Button>
        </div>
          <InputFieldsBox sx={{ maxWidth: 1100 }}>
            {status === "success" && (
              <Formik
                initialValues={{ gradeSubjectMappings: data }}
                // Need to modify. Will add this validation along with removing the already selected grade in the options
                //validationSchema={validationSchema}
                onSubmit={(values) =>
                  setTimeout(() => {
                    alert(JSON.stringify(values, null, 2));
                  }, 500)
                }
              >
                {(props) => (
                  <Form style={{ width: "100%" }}>
                    <Grid container sx={{ paddingBottom: "10px" }}>
                      <Grid item xs={12}>
                      <Header
            variant="h5"
            component="h3"
            
          >
            EDIT GRADES/SUBJECTS
          </Header>

                      </Grid>
                      <FieldArray
                        name="gradeSubjectMappings"
                        render={(arrayHelpers) => (
                          <>
                            {props.values.gradeSubjectMappings &&
                            props.values.gradeSubjectMappings.length > 0
                              ? props.values.gradeSubjectMappings.map(
                                  (gradeSubjectMapping, index) => (
                                    <React.Fragment key={index}>
                                      <Grid
                                        item
                                        xs={12}
                                        md={4}
                                        className={moduleClasses.textInput}
                                      >
                                        <Item>
                                          <ComboBox
                                            name={`gradeSubjectMappings[${index}].gradeId`}
                                            label="Grade"
                                            value={
                                              grades
                                                ? grades.find(
                                                    (option) =>
                                                      option.value ===
                                                      props.values
                                                        .gradeSubjectMappings[
                                                        index
                                                      ].gradeId
                                                  )
                                                : ""
                                            }
                                            onChange={(value) => {
                                              props.setFieldValue(
                                                `gradeSubjectMappings[${index}].gradeId`,
                                                value
                                              );
                                            }}
                                            options={grades}
                                          />
                                        </Item>
                                      </Grid>
                                      <Grid
                                        item
                                        xs={12}
                                        md={6}
                                        className={moduleClasses.textInput}
                                      >
                                        <Item>
                                          <ComboBox
                                            label="Subjects"
                                            name={`gradeSubjectMappings[${index}].subjects`}
                                            isOptionEqualToValue={(
                                              option,
                                              value
                                            ) => option.value === value.value}
                                            multiple
                                            onChange={(values) =>
                                              props.setFieldValue(
                                                `gradeSubjectMappings[${index}].subjects`,
                                                values
                                              )
                                            }
                                            options={subjects}
                                          />
                                        </Item>
                                      </Grid>
                                      <Grid
                                        item
                                        xs={2}
                                        sx={{
                                          display: "flex",
                                          alignItems: "center",
                                        }}
                                      >
                                        <Button
                                          color="secondary"
                                          variant="contained"
                                          onClick={() =>
                                            arrayHelpers.remove(index)
                                          } // remove a friend from the list
                                        >
                                          Remove
                                        </Button>
                                      </Grid>
                                    </React.Fragment>
                                  )
                                )
                              : ""}
                            <div style={{ float: "right" }}>
                              <Button
                                variant="contained"
                                type="button"
                                onClick={() =>
                                  arrayHelpers.push({
                                    gradeId: "1",
                                    subjects: [],
                                  })
                                } // insert an empty string at a position
                              >
                                ADD
                              </Button>
                              <Button
                                variant="contained"
                                color="success"
                                type="submit"
                                sx={{ float: "right" }}
                              >
                                Submit
                              </Button>
                              &nbsp;
                            </div>
                          </>
                        )}
                      />
                    </Grid>
                  </Form>
                )}
              </Formik>
            )}
          </InputFieldsBox>
        </Box>
      }
    </Modal>
  );
};

export default TeacherMappingModal;
