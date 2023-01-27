# AWS CloudFormation
### AWS CLI: https://docs.aws.amazon.com/cli/latest/reference/cloudformation/index.html

- Configure AWS CLI

- Create named profile using AWS CLI command ``aws configure --profile dev``

- Set dev env by using command ``set AWS_PROFILE=dev``

- To set different region ``set AWS_REGION=your_aws_region``

### To list all your profile names, use the aws configure list-profiles command.
`aws configure list-profiles`

### To list all configuration data, use the aws configure list command. This command displays the AWS CLI name of all settings you've configured, their values, and where the configuration was retrieved from.
`aws configure list`

###  To create a stack
#### `aws cloudformation create-stack --stack-name myVpcStack --template-body file://csye6225-infra.yml`

### To create a stack with param
`aws cloudformation create-stack --stack-name myvpcwithparam --template-body file://csye6225-infra.yaml --parameters ParameterKey=VpcCidrBlock,ParameterValue="10.1.1.0/24"`

### To update a stack
`aws cloudformation update-stack --stack-name myvpc --template-body file://csye6225-infra.yml`

### to delete a stack
`aws cloudformation delete-stack --stack-name myVpcStack`