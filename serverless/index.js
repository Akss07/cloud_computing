console.log('Loading function');
var aws = require('aws-sdk');
aws.config.update({ region: 'us-east-1' });
var ses = new aws.SES({ region: "us-east-1" });
exports.sendEmail = async (event, context, callback) => {
    if(!event.Records) {
        return callback(new Error('Records not available'));
    }
    for (let i = 0; i < event.Records.length; i++) {
        const message = JSON.parse(event.Records[i].Sns.Message);
        console.log('From SNS:', message);
        const emailAddress = message.email;
        const token = message.token;
        const eventType = message.eventType;
        console.log(emailAddress + " " + token + " " + eventType )
        console.log("Creating email body")
        let emailBody;
        let emailSubject;
        if (eventType === "VERIFY") {
            emailSubject = "Verify Email";
            emailBody = "Please click on this link to get email verified. The link is https://" + process.env.Domain + "/v1/verifyUserEmail/" + emailAddress + "/" + token ;
        }
        let emailSendResult = await sendEmail(emailAddress, emailBody, emailSubject)
    }
};
async function sendEmail(emailAddress, emailBody, emailSubject) {
    console.log("sending email");
    var source = process.env.UserName + "@" + process.env.Domain
    console.log(source)
    console.log(emailAddress)
    console.log(emailSubject)
    var params = {
        Destination: {
            ToAddresses: [emailAddress],
        },
        Message: {
            Body: {
                Text: { Data: emailBody },
            },
            Subject: { Data: emailSubject },
        },
        Source: source,
    };
    try{
        let result = await ses.sendEmail(params).promise();
        console.log("MAIL SENT SUCCESSFULLY!!");
    }catch(e){
        console.log("FAILURE IN SENDING MAIL!!", e);
    }
    return;
}