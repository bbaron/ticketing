#!/bin/bash

session=$(tr -dc 'a-zA-Z0-9' </dev/urandom | fold -w 8 | head -n 1)
#email="$session@asdf.com"
email="asdf@asdf.com"
content_type="Content-Type:application/json"
port='8080'
HTTP='http --body --check-status --ignore-stdin --timeout=2.5'
JQ='jq -r'

# signin
auth_url='/api/users/signin'
auth_header=''
jwt=$($HTTP POST ":${port}${auth_url}" "$content_type" "email=$email" 'password=asdf' | $JQ .jwt)
if [[ "$jwt" == 'null' ]]; then
  echo "POST $auth_url failed, trying signup"
  auth_url='/api/users/signup'
  jwt=$($HTTP POST ":${port}${auth_url}" "$content_type" "email=$email" 'password=asdf' | $JQ .jwt)
  if [[ "$jwt" == 'null' ]]; then
    echo "POST $auth_url failed"
    exit 1
  fi
fi
auth_header="x-auth-info:$jwt"
echo "signed in jwt = $jwt"
$HTTP ":$port/api/users/currentuser" "$auth_header"

# create ticket
ticket_id=$($HTTP POST ":$port/api/tickets" "$content_type" "$auth_header" "title=concert ($session)" 'price=10' | $JQ .id)
if [[ "$ticket_id" == 'null' ]]; then
  echo 'ticket_id is null'
  exit 1
fi
echo "created ticket_id = $ticket_id"
$HTTP ":$port/api/tickets/$ticket_id" "$auth_header"

# update ticket
if $HTTP PUT ":$port/api/tickets/$ticket_id" "$content_type" "$auth_header" "title=ballgame ($session)" 'price=15'; then
  echo "updated ticket_id = $ticket_id"
  $HTTP ":$port/api/tickets/$ticket_id" "$auth_header"
else
  echo "update $ticket_id failed"
  exit 1
fi

# create order
order_id=$($HTTP POST ":$port/api/orders" "$auth_header" "$content_type" "ticketId=$ticket_id" | $JQ .id)
if [[ "$order_id" == 'null' ]]; then
  echo 'order_id is null'
  exit 1
fi
echo "created order_id = $order_id"
echo "order $order_id"
if ! $HTTP ":$port/api/orders/$order_id" "$auth_header"; then
  echo "get $order_id failed"
  exit 1
fi
echo "ticket $ticket_id"
if ! $HTTP ":$port/api/tickets/$ticket_id" "$auth_header"; then
  echo "get $ticket_id failed"
  exit 1
fi

# cancel order
if $HTTP DELETE ":$port/api/orders/$order_id" "$auth_header"; then
  echo "cancelled order_id = $order_id"
else
  echo "cancel order $order_id failed"
  exit 1
fi
echo "order $order_id"
if ! $HTTP ":$port/api/orders/$order_id" "$auth_header"; then
  echo "get $order_id failed"
  exit 1
fi
echo "ticket $ticket_id"
if ! $HTTP ":$port/api/tickets/$ticket_id" "$auth_header"; then
  echo "get $ticket_id failed"
  exit 1
fi
