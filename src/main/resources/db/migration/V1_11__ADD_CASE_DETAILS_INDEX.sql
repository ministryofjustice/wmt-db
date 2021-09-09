CREATE INDEX ix_case_details_ix_case_details_workload_id
ON app.case_details
USING BTREE (workload_id ASC)
WITH (FILLFACTOR = 100);