#!/bin/bash

echo "FROM EMAIL: $FROM_EMAIL"
echo "TO EMAIL: $TO_EMAIL"

aws ses verify-email-identity --email-address ${FROM_EMAIL} --endpoint-url=http://localhost:4566
aws ses verify-email-identity --email-address ${TO_EMAIL} --endpoint-url=http://localhost:4566