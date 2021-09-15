CREATE INDEX ix_workload_points_ix_workload_points_effective_to
ON app.workload_points
USING BTREE (effective_to ASC)
WITH (FILLFACTOR = 100);