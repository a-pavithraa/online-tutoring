import React from "react";
import Box from "@mui/material/Box";
import Modal from "@mui/material/Modal";
import Button from "@mui/material/Button";
import httpClient from "../../util/http-client";
import { CircularProgress, Grid } from "@mui/material";
import { useMutation, useQuery, useQueryClient } from "react-query";
import { Header, InputFieldsBox, Item, ModalBox } from "../ui/Theme";
import { Field, FieldArray, Form, Formik } from "formik";
import { ComboBox } from "../ui/FormInputs";
import moduleClasses from "./Registration.module.scss";
import * as Yup from "yup";

async function getSubjectsAndGrades(teacherId) {
  const { data } = await httpClient.get(
    "/mdm/mapping/gradeAndSubjectsOfTeacher?teacherId=" + teacherId
  );

  return data;
}
async function getRefData(urlSuffix) {
  const res = await httpClient.get("/mdm/referenceData/" + urlSuffix);
  return res.data;
}
function mapperFn(data) {
  const mapObj = [];
  data.forEach((x) => {
    let subjectObj = { value: x.subjectId, label: x.subjectName };
    const collection = mapObj.find((obj) => x.gradeId == obj.grade.value)?.subjects;

    if (collection) {
      collection.push(subjectObj);
    } else {
      let gradeObj = { grade:{value: x.gradeId, label:x.gradeName}, subjects: [subjectObj] };
      mapObj.push(gradeObj);
    }
  });

  return mapObj;
}
async function mapTeacherToSubjectAndGrade(mappingData) {
  await httpClient.post("/mdm/mapping/mapTeacherToGradeAndSubject", mappingData);
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
  const queryClient = useQueryClient();
  const { mutate, isLoading } = useMutation(mapTeacherToSubjectAndGrade, {
    onSuccess: data => {
       console.log(data);
       const message = "success"
       props.handleClose();
 },
   onError: () => {
        alert("there was an error")
 },
   onSettled: () => {
      queryClient.invalidateQueries('create')
 }
 });
  const {
   
    data: subjects   
  } = useQuery("subjects", () => getRefData("subjects"), {
    staleTime: Infinity,
  });
  const {   
    data: grades,
   
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
       
        <ModalBox>
          <div  style={{float:'right'}}>
          <Button sx={{fontWeight:'bolder', color:'red', fontSize:14}} onClick={props.handleClose}>X</Button>
        </div>
          <InputFieldsBox sx={{ maxWidth: 1100 }}>
            {status === "success" && (
              <Formik
                initialValues={{ gradeSubjectMappings: data }}
                // Need to modify. Will add this validation along with removing the already selected grade in the options
                //validationSchema={validationSchema}
                onSubmit={(values) =>{
                
                  
                   
                    
                    const postRequests = [];
                    if(values.gradeSubjectMappings.length>0){
                      values.gradeSubjectMappings.forEach(gradeSubjectObj=>{
                        gradeSubjectObj.subjects.forEach(subject=>{
                          const postRequest ={gradeId:gradeSubjectObj.grade.value,subjectId:subject.value}
                          postRequests.push(postRequest);
                        })
                      })

                    }
                    const mapTeacherRequest = {teacherId:props.id,gradeAndSubjectMappingRequestList:postRequests}
                    const jsonData = JSON.stringify(mapTeacherRequest)
                    console.log(jsonData);
                    mutate(jsonData);
                    
                 
                }
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
                                            name={`gradeSubjectMappings[${index}].grade`}
                                            label="Grade"
                                            isOptionEqualToValue={(
                                              option,
                                              value
                                            ) => option.value === value.value}
                                           
                                            onChange={(value) => {
                                             
                                              props.setFieldValue(
                                                `gradeSubjectMappings[${index}].grade`,
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
                                          sx={{marginLeft:2}}
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
                                    grade: grades[0],
                                    subjects: [],
                                  })
                                } // insert an empty string at a position
                              >
                                ADD
                              </Button>
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
        </ModalBox>
      }
    </Modal>
  );
};

export default TeacherMappingModal;
