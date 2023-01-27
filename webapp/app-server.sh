#!/bin/bash

sudo apt-get update
sudo apt-get upgrade -y
sudo apt-get install nginx -y
sudo apt-get clean
sudo apt-get install openjdk-11-jre-headless -y
sudo sudo apt-get install mysql-client-5.5 -y

sudo chmod +x /home/ubuntu/workspace/application/cloudapp-0.0.1-SNAPSHOT.jar
sudo chmod +x /home/ubuntu/workspace/application/application-demo.properties
sudo chmod +x /home/ubuntu/workspace/myapp.service
sudo chmod +x /home/ubuntu/workspace/cloudwatch-config.json

sudo cp /home/ubuntu/workspace/myapp.service /etc/systemd/system/
sudo chmod +x /etc/systemd/system/myapp.service

sudo mkdir /home/ubuntu/logs/

sudo cp /home/ubuntu/workspace/cloudwatch-config.json /opt/
sudo chmod +x /opt/cloudwatch-config.json

sudo wget https://s3.us-east-1.amazonaws.com/amazoncloudwatch-agent-us-east-1/ubuntu/amd64/latest/amazon-cloudwatch-agent.deb

sudo dpkg -i -E ./amazon-cloudwatch-agent.deb

sudo apt-get install amazon-cloudwatch-agent -y

sudo systemctl daemon-reload

sudo systemctl enable /etc/systemd/system/myapp.service

sudo systemctl start myapp.service






