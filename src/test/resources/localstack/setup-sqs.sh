#!/usr/bin/env bash
set -e
export TERM=ansi
export AWS_ACCESS_KEY_ID=foobar
export AWS_SECRET_ACCESS_KEY=foobar
export AWS_DEFAULT_REGION=eu-west-2
export PAGER=

aws --endpoint-url=http://localhost:4566 sqs create-queue --queue-name hmpps_workload_s3_extract_event_queue
aws --endpoint-url=http://localhost:4566 sqs create-queue --queue-name hmpps_workload_s3_extract_event_dlq

aws --endpoint-url=http://localhost:4566 sqs set-queue-attributes --queue-url "http://localhost:4566/queue/hmpps_workload_s3_extract_event_queue" --attributes '{"RedrivePolicy":"{\"maxReceiveCount\":\"3\", \"deadLetterTargetArn\":\"arn:aws:sqs:eu-west-2:000000000000:hmpps_workload_s3_extract_event_dlq\"}"}'

echo Queues are Ready