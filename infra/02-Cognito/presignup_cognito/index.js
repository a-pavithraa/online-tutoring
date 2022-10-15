exports.handler = (event, context, callback) => {
    // Set the user pool autoConfirmUser flag after validating the email domain
    event.response.autoConfirmUser = true;  
    // Return to Amazon Cognito
    callback(null, event);
};