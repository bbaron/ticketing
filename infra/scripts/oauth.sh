#!/bin/bash

session=$(tr -dc 'a-zA-Z0-9' </dev/urandom | fold -w 8 | head -n 1)
email="asdf@asdf.com"
password='asdf'
content_type="Content-Type:application/json"
port='8080'
HTTP='http --body --check-status --ignore-stdin'
JQ='jq -r'

$HTTP POST ":${port}/api/users/signup" "$content_type" "email=$email" "password=$password"
if (($? == 4))
then
  echo "$email already signed up"
fi

access_token=$($HTTP POST ":$port/oauth/token?grant_type=password&username=$email&password=$password&scope=read" -a 'client:secret' | $JQ .access_token)
if [[ "$access_token" == 'null' ]]; then
  echo 'access_token is null'
  exit 1
fi
echo "access_token = $access_token"
auth_header="authorization:Bearer $access_token"

http POST ":$port/api/tickets" "$content_type" "$auth_header" "title=concert ($session)" 'price=10'
#ticket_id=$($HTTP POST ":$port/api/tickets" "$content_type" "$auth_header" "title=concert ($session)" 'price=10' | $JQ .id)
#if [[ "$ticket_id" == 'null' ]]; then
#  echo 'ticket_id is null'
#  exit 1
#fi
#echo "created ticket_id = $ticket_id"
#$HTTP ":$port/api/tickets/$ticket_id" "$auth_header"

