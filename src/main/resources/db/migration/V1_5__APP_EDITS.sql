
drop view app.team_archive_data;
CREATE OR REPLACE VIEW app.team_archive_data (workload_date, workload_report_id, workload_id, ldu_id, region_name, ldu_name, team_name, team_id, team_code, link_id, om_name, om_key, grade_code, total_cases, total_filtered_cases, total_t2a_cases, monthly_sdrs, sdr_conversions_last_30_days, paroms_completed_last_30_days, total_points, available_points, hours_reduction, cms_adjustment_points, gs_adjustment_points, contracted_hours, arms_total_cases, paroms_points, sdr_points, sdr_conversion_points, nominal_target) AS
SELECT
    wr.effective_from AS workload_date,
    w.workload_report_id,
    w.id AS workload_id,
    l.id AS ldu_id,
    r.description AS region_name,
    l.description AS ldu_name,
    t.description AS team_name,
    t.id AS team_id,
    t.code AS team_code,
wo.id AS link_id,
CONCAT(om.forename, ' ', om.surname) AS om_name,
om.key AS om_key,
omt.grade_code,
w.total_cases AS total_cases,
w.total_filtered_cases AS total_filtered_cases,
w.total_t2a_cases,
w.monthly_sdrs,
w.sdr_conversions_last_30_days,
w.paroms_completed_last_30_days,
wpc.total_points AS total_points,
wpc.available_points AS available_points,
wpc.reduction_hours AS hours_reduction,
wpc.cms_adjustment_points AS cms_adjustment_points,
wpc.gs_adjustment_points AS gs_adjustment_points,
wpc.contracted_hours AS contracted_hours,
wpc.arms_total_cases AS arms_total_cases,
wpc.paroms_points AS paroms_points,
wpc.sdr_points AS sdr_points,
wpc.sdr_conversion_points AS sdr_conversion_points,
wpc.nominal_target AS nominal_target
    FROM app.workload_points_calculations AS wpc
    JOIN app.workload AS w
        ON wpc.workload_id = w.id
    JOIN app.workload_owner AS wo
        ON w.workload_owner_id = wo.id
    JOIN app.workload_report AS wr
        ON w.workload_report_id = wr.id
    JOIN app.team AS t
        ON wo.team_id = t.id
    JOIN app.ldu AS l
        ON t.ldu_id = l.id
    JOIN app.region AS r
        ON l.region_id = r.id
    JOIN app.offender_manager AS om
        ON wo.offender_manager_id = om.id
    JOIN app.offender_manager_type AS omt
        ON om.type_id = omt.id;