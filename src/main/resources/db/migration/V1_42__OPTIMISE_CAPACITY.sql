
CREATE OR REPLACE VIEW app.national_capacity_view (total_points, available_points, reduction_hours, contracted_hours, effective_from, workload_report_id, count) AS
SELECT
    SUM(total_points) AS total_points, SUM(available_points) AS available_points, SUM(reduction_hours) AS reduction_hours, SUM(wpc.contracted_hours) AS contracted_hours, wr.effective_from AS effective_from, wr.id AS workload_report_id, COUNT(*) AS count
    FROM app.workload_points_calculations AS wpc
    JOIN app.workload_report AS wr
        ON wpc.workload_report_id = wr.id
    GROUP BY wr.effective_from, wr.id;
