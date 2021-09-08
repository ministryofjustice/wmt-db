CREATE INDEX ix_adjustments_ix_adjustments_workload_owner_id
ON app.adjustments
USING BTREE (workload_owner_id ASC) INCLUDE(status)
WITH (FILLFACTOR = 100);