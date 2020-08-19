#!/bin/bash

session="asdf$$"
email="$session@asdf.com"
content_type="Content-Type:application/json"
port='8080'
HTTP='http --body'
JQ='jq -r'

# signup
user_id=$($HTTP "--session=$session" POST ":$port/api/users/signup" "$content_type"  "email=$email" 'password=asdf' | $JQ .id)
echo "signed up user_id = $user_id"
$HTTP "--session=$session" ":$port/api/users/currentuser"

# create ticket
ticket_id=$($HTTP "--session=$session" POST ":$port/api/tickets" "$content_type"  "title=concert ($session)" 'price=10' | $JQ .id)
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
