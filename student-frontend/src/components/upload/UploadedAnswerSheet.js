import { ListObjectsCommand, S3Client } from "@aws-sdk/client-s3";
import { fromCognitoIdentityPool } from "@aws-sdk/credential-providers";
import React, { useCallback, useContext } from "react";
import LoginContext from "../../store/login-context";
import { CognitoIdentityClient } from "@aws-sdk/client-cognito-identity";
import { Button, CircularProgress, LinearProgress } from "@mui/material";
import { BUCKET_NAME, COGNITO_ENDPOINT, IDENTITY_POOL_ID, REGION } from "../../util/constants";
import { useQuery } from "react-query";
import UploadIcon from "@mui/icons-material/UploadOutlined";
const UploadedAnswerSheet = ({teacherId,assessmentId,handleDialogOpen,refetchedData})=>{
    const { token, cognitoId,cognitoIdPoolIdentity } = useContext(LoginContext);
    async function checkAlreadySubmitted() {
        const s3 = new S3Client({
            region: REGION,
            credentials: fromCognitoIdentityPool({
              client: new CognitoIdentityClient({ region: REGION }),
              identityPoolId: IDENTITY_POOL_ID,
              clientConfig: { region: REGION },
              logins: {
                [COGNITO_ENDPOINT]: token,
              },
            }),
          });
          let key =  `${cognitoIdPoolIdentity}/Uploads/${teacherId}/${assessmentId}/`;
          console.log('before calling s3');
          const data = await s3.send(
            new ListObjectsCommand({
              Prefix: key,
              Bucket: BUCKET_NAME,
            })
        );
     
        let response = <Button
        variant="contained"
        color="secondary"
        startIcon={<UploadIcon />}
        onClick={() =>
          handleDialogOpen(assessmentId, teacherId)
        }
      >
        Upload
      </Button>
        if (data.Contents && data.Contents.length >=1) {
          let fileName =data.Contents[0].Key;
          if(fileName.indexOf("/">-1))
             fileName = fileName.substring(fileName.lastIndexOf("/")+1);
            response=<b>{fileName}</b>
        }
        return response;
    }
    const { data, isFetching, status, refetch } = useQuery(
        ["uploaded",refetchedData],
        () =>
        checkAlreadySubmitted(),
        {
          refetchOnWindowFocus: false,
        }
      );
      return <>
       {isFetching  && <LinearProgress/>}
       {status==='success' &&!isFetching && data }
      </>
     

}
export default UploadedAnswerSheet;