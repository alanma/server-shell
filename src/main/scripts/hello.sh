#! /bin/sh

HOST="localhost"
PORT="9797"

ssh -p $PORT $HOST test << EOF
hello --msg $1
EOF
