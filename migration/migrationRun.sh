#!/usr/bin/env bash

psql -a -q -f disable_foreign_keys.sql

aws dms start-replication-task --replication-task-arn $REPLICATION_TASK_ARN --start-replication-task-type reload-target


taskStatus=$(aws dms describe-replication-tasks | jq -r '.ReplicationTasks[] | select(.ReplicationTaskArn == env.REPLICATION_TASK_ARN) | .Status')


while [[ $taskStatus != "stopped" ]];
do
  sleep 30s
  taskStatus=$(aws dms describe-replication-tasks | jq -r '.ReplicationTasks[] | select(.ReplicationTaskArn == env.REPLICATION_TASK_ARN) | .Status')
  echo "DMS full load task status is: $taskStatus"
done


psql -a -q -f enable_foreign_keys.sql

echo "Completed"