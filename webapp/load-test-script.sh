#!/bin/bash

echo "Load test has been started"  

call_api () {
  for i in {1..10000}
  do
    curl --write-out "%{http_code}\n" -k https://prod.seattlelife.me/healthz --silent
  done
}


for i in {1..40}
do
  call_api
done

#end load
echo "Load test has been completed" 
