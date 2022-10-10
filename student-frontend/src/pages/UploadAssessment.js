import React, { useContext, useEffect } from "react";

import { IDENTITY_POOL_ID, COGNITO_ENDPOINT, REGION } from "../util/constants";
import LoginContext from "../store/login-context";
import { fromCognitoIdentity,fromCognitoIdentityPool,fromTemporaryCredentials  } from "@aws-sdk/credential-providers";
import { GetCommand } from "@aws-sdk/lib-dynamodb";
import { DynamoDBClient } from "@aws-sdk/client-dynamodb";
import { DynamoDBDocumentClient } from "@aws-sdk/lib-dynamodb";
const UploadAssessment = (props) => {
  const {token,cognitoId} = useContext(LoginContext);
  useEffect(() => {
    const credentials = fromCognitoIdentityPool({
        identityPoolId: IDENTITY_POOL_ID,
        region: REGION,

       // your identity pool id here
      logins: {
        // Change the key below according to the specific region your user pool is in.
        [COGNITO_ENDPOINT]: token,
      },
      clientConfig: { region : REGION},
    }); 
    const opts = {
        region: REGION,
        credentials: credentials
    }  
    const ddbClient = new DynamoDBClient(opts);
const marshallOptions = {
    // Whether to automatically convert empty strings, blobs, and sets to `null`.
    convertEmptyValues: false, // false, by default.
    // Whether to remove undefined values while marshalling.
    removeUndefinedValues: false, // false, by default.
    // Whether to convert typeof object to map attribute.
    convertClassInstanceToMap: false, // false, by default.
  };
  
  const unmarshallOptions = {
    // Whether to return numbers as a string instead of converting them to native JavaScript numbers.
    wrapNumbers: false, // false, by default.
  };
  
  const translateConfig = { marshallOptions, unmarshallOptions };
  
  // Create the DynamoDB document client.
  const ddbDocClient = DynamoDBDocumentClient.from(ddbClient, translateConfig);
   
   
        async function getDynamoDBItems(){
            try {
                const params = {
                    TableName: "StudentNotification",
                    region:REGION,
                    credentials: credentials,
                    Key: {
                      'cognitoId': cognitoId
                    },
                  };
                const data = await ddbDocClient.send(new GetCommand(params));
                console.log("Success :", data.Item);
              } catch (err) {
                console.log("Error", err);
              }
        
        }
        getDynamoDBItems();
        console.log(cognitoId);
       
   
  }, []);
  return <h1>Upload Assessment</h1>;
};

export default UploadAssessment;
