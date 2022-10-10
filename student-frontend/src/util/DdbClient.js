import { DynamoDBClient } from "@aws-sdk/client-dynamodb";
import { REGION } from "./constants";
import { DynamoDBDocumentClient } from "@aws-sdk/lib-dynamodb";
export const ddbClient = new DynamoDBClient({ region: REGION });
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
  
  export { ddbDocClient };
