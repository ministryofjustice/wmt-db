CREATE INDEX ix_workload_points_calculations_workload_report_id
    ON app.workload_points_calculations
    USING btree (workload_report_id)
    WITH (fillfactor='100')