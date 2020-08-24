#!/bin/bash

#session=$(tr -dc 'a-zA-Z0-9' < /dev/urandom | fold -w 8 | head -n 1)
#email="$session@asdf.com"
session='asdf'
email="asdf@asdf.com"
content_type="Content-Type:application/json"
port='8080'
HTTP='http --body'
JQ='jq -r'

# signup
user_id=$($HTTP "--session=$session" POST ":$port/api/users/signin" "$content_type"  "email=$email" 'password=asdf' | $JQ .id)
echo "signed up user_id = $user_id"
$HTTP "--session=$session" ":$port/api/users/currentuser"

# create ticket
ticket_id=$($HTTP "--session=$session" POST ":$port/api/tickets" "$content_type"  "title=concert ($session)" 'price=10' | $JQ .id)
if [[ "$ticket_id" = 'null' ]]
then
  echo 'ticket_id is null'
  exit 1
else
  echo "ticket_id is $ticket_id"
  exit 0
fi
echo "created ticket_id = $ticket_id"
$HTTP "--session=$session" ":$port/api/tickets/$ticket_id"

# update ticket
$HTTP "--session=$session" PUT ":$port/api/tickets/$ticket_id" "$content_type"  "title=ballgame ($session)" 'price=15' > /dev/null
echo "updated ticket_id = $ticket_id"
$HTTP "--session=$session" ":$port/api/tickets/$ticket_id"

# create order
order_id=$($HTTP "--session=$session" POST ":$port/api/orders" "$content_type"  "ticketId=$ticket_id" | $JQ .id)
echo "created order_id = $order_id"
$HTTP "--session=$session" ":$port/api/orders/$order_id"

# cancel order
$HTTP "--session=$session" DELETE ":$port/api/orders/$order_id" > /dev/null
echo "cancelled order_id = $order_id"
$HTTP "--session=$session" ":$port/api/orders/$order_id"
