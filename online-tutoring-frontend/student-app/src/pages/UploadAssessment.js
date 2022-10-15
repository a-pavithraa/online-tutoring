import React, { useContext, useEffect, useState } from "react";

import { IDENTITY_POOL_ID, COGNITO_ENDPOINT, REGION } from "../util/constants";
import LoginContext from "../store/login-context";
import { fromCognitoIdentity,fromCognitoIdentityPool,fromTemporaryCredentials  } from "@aws-sdk/credential-providers";
import { GetCommand } from "@aws-sdk/lib-dynamodb";
import { DynamoDBClient } from "@aws-sdk/client-dynamodb";
import { DynamoDBDocumentClient } from "@aws-sdk/lib-dynamodb";
import PendingAssessmentList from "../components/upload/PendingAssessments";
const UploadAssessment = (props) => {
  
 
 
  return  <>
 <PendingAssessmentList />
  </>;
};

export default UploadAssessment;
