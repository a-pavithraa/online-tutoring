import {
  Button,
  CircularProgress,
  Paper,
  Table,
  TableBody,
  TableHead,
  TableRow,
} from "@mui/material";
import React, { useCallback, useContext, useState } from "react";

import LoginContext from "../../store/login-context";
import {
  Header,
  InputFieldsBox,
  StyledTableCell,
  StyledTableContainer,
  StyledTableRow,
} from "../ui/Theme";

import Dialog from "@mui/material/Dialog";
import DialogActions from "@mui/material/DialogActions";
import DialogContent from "@mui/material/DialogContent";
import DialogContentText from "@mui/material/DialogContentText";
import DialogTitle from "@mui/material/DialogTitle";
import useMediaQuery from "@mui/material/useMediaQuery";
import UploadAssessmentAnswerSheetModal from "./UploadAssessmentAnswerSheetModal";
import { fromCognitoIdentityPool  } from "@aws-sdk/credential-providers";
import { DynamoDBClient } from "@aws-sdk/client-dynamodb";
import { DynamoDBDocumentClient, GetCommand } from "@aws-sdk/lib-dynamodb";
import { useTheme } from "@mui/material/styles";


import PaginationComponent from "../ui/PaginationComponent";
import { useQuery } from "react-query";
import {
  COGNITO_ENDPOINT,
  IDENTITY_POOL_ID,
  REGION,
  TABLE_NAME,
} from "../../util/constants";
import UploadedAnswerSheet from "./UploadedAnswerSheet";


const PendingAssessmentList = (props) => {
  const {token,cognitoId,cognitoIdPoolIdentity} = useContext(LoginContext);
  const [refetchedData,setRefetchedData]=useState(false);
  // Tried reusing cognitoIdPoolIdentity from context but it fails sometimes
  const getPendingAssessments = useCallback(async () => {
    const credentials = fromCognitoIdentityPool({
      identityPoolId: IDENTITY_POOL_ID,
      region: REGION,
      logins: {     
        [COGNITO_ENDPOINT]: token,
      },
      clientConfig: { region: REGION },
    });
    const opts = {
      region: REGION,
      credentials: credentials,
    };
    
    const ddbClient = new DynamoDBClient(opts);
    const marshallOptions = {   
      
      removeUndefinedValues: true
    };

    const unmarshallOptions = {   
      wrapNumbers: false
    };

    const translateConfig = { marshallOptions, unmarshallOptions };

    // Create the DynamoDB document client.
    const ddbDocClient = DynamoDBDocumentClient.from(
      ddbClient,
      translateConfig
    );
    try {
      const params = {
        TableName: TABLE_NAME,
        region: REGION,
        credentials: credentials,
        Key: {
          cognitoId: cognitoId,
        },
      };
     const data = await ddbDocClient.send(new GetCommand(params));
     if(data.Item){

      const convertedData = [data.Item];
     /**
    const convertedData =[{
      "subject": "Science",
      "studentId": 2,
      "dueDate": "11/10/22",
      "ttl": 1665504739,
      "teacherId": 2,
      "cognitoId": "9b3d32bb-03bb-42c9-b94b-bbc3d143ae2b",
      "assessmentId": 22
  }];
 */
      console.log("Success :", data);
      return (convertedData.flatMap((x) => x));
}

    } catch (err) {
      console.log("Error", err);
    }
  },[token]);

  const { data, isFetching, status, refetch } = useQuery(
    ["assessmentDetails"],
    () =>
    getPendingAssessments(),
    {
      refetchOnWindowFocus: false,
    }
  );

  const [open, setOpen] = useState(false);
  
  const [assessmentId, setAssessmentId] = useState();
  const [teacherId, setTeacherId] = useState();

  const handleClose = () => {
    setOpen(false);
  };

  const uploadAnswerSheet = () => {
    handleDialogClose();
    setOpen(true);
  };

  const [dialogOpen, setDialogOpen] = React.useState(false);
  const theme = useTheme();
  const fullScreen = useMediaQuery(theme.breakpoints.down("md"));

  const handleDialogOpen = (assessmentId, teacherId) => {
    setAssessmentId(assessmentId);
    setTeacherId(teacherId);
    setDialogOpen(true);
  };

  const handleDialogClose = () => {
    setDialogOpen(false);
  };

  return (
    <InputFieldsBox sx={{ width: "70%", overflow: "hidden" }}>
      <Header variant="h4" component="h2">
        PENDING ASSESSMENTS
      </Header>
      {open && (
        <UploadAssessmentAnswerSheetModal
          assessmentId={assessmentId}
          teacherId={teacherId}
          open={open}
          handleClose={handleClose}
          setRefetchedData={setRefetchedData}
        />
      )}
      <Dialog
        fullScreen={fullScreen}
        open={dialogOpen}
        onClose={handleDialogClose}
        aria-labelledby="responsive-dialog-title"
      >
        <DialogTitle id="responsive-dialog-title">
          {"Upload Question Paper?"}
        </DialogTitle>
        <DialogContent sx={{ background: "white", color: "black" }}>
          <DialogContentText sx={{ marginTop: "5px" }}>
            <b>
              
               Please ensure that you have verified your answers before submitting. Do you want to submit the answer sheet?
            </b>
          </DialogContentText>
        </DialogContent>
        <DialogActions sx={{ background: theme.palette.grey }}>
          <Button
            variant="contained"
            color="secondary"
            autoFocus
            onClick={handleDialogClose}
          >
            Disagree
          </Button>
          <Button variant="contained" onClick={uploadAnswerSheet} autoFocus>
            Agree
          </Button>
        </DialogActions>
      </Dialog>
      <StyledTableContainer component={Paper}>
        <Table stickyHeader aria-label="sticky table">
          <TableHead>
            <TableRow>
              <StyledTableCell>Subject</StyledTableCell>
              <StyledTableCell>Due Date</StyledTableCell>
              <StyledTableCell>Answer Sheet</StyledTableCell>
            </TableRow>
          </TableHead>
          <TableBody>
          {isFetching && (
              <StyledTableRow>
                <StyledTableCell colSpan={4}>
                  <CircularProgress />
                </StyledTableCell>
              </StyledTableRow>
            )}
            {data &&
              data.length > 0 &&
              data.map((row) => (
                <StyledTableRow
                  key={row.assessmentId}
                  sx={{ "&:last-child td, &:last-child th": { border: 0 } }}
                >
                  <StyledTableCell component="th" scope="row">
                    {row.subject}
                  </StyledTableCell>
                  <StyledTableCell>{row.dueDate}</StyledTableCell>

                  <StyledTableCell>
               
      <UploadedAnswerSheet assessmentId={row.assessmentId} teacherId={row.teacherId} handleDialogOpen={handleDialogOpen} refetchedData={refetchedData}/>

                  
                  </StyledTableCell>
                </StyledTableRow>
              ))}
            {(!isFetching && (!data || data.length == 0)) && (
              <StyledTableRow>
                <StyledTableCell colSpan={6}>
                  No Assessments found
                </StyledTableCell>
              </StyledTableRow>
            )}
          </TableBody>
        </Table>
      </StyledTableContainer>
      {data && <PaginationComponent count={data.length} rowsPerPage={5} />}
    </InputFieldsBox>
  );
};
export default PendingAssessmentList;
