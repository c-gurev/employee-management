#!/bin/bash

echo "Waiting for Oracle DB to become ready..."

docker cp ./src/main/resources/sql/check_instance.sql oracle-db:/tmp/check_instance.sql

RETRIES=10
SUCCESS=0
for ((i=1; i<=RETRIES; i++)); do
  STATUS=$(docker exec oracle-db bash -c "sqlplus -s sys/pass@oracle-db:1521/XE as sysdba <<EOF
@/tmp/check_instance.sql
EXIT
EOF")

  if [[ "${STATUS//[[:space:]]/}" == "OPEN" ]]; then
    echo "Oracle DB is ready after $i attempts"
    SUCCESS=1
    break
  fi

  echo "Attempt $i/$RETRIES: DB not ready yet, retrying in 5 seconds..."
  sleep 5
done

if [ $SUCCESS -eq 0 ]; then
  echo "Oracle DB is not ready after $RETRIES retries, exiting"
  exit 1
fi

echo "Copying and executing init.sql..."
docker cp ./src/main/resources/sql/init.sql oracle-db:/tmp/init.sql
docker exec oracle-db bash -c "sqlplus -s emp_mngr/mypwd@oracle-db:1521/XEPDB1 <<EOF
@/tmp/init.sql
EXIT
EOF"
