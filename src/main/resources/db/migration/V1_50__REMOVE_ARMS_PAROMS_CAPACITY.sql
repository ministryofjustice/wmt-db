drop view app.team_capacity_breakdown_view;
CREATE OR REPLACE VIEW app.team_capacity_breakdown_view (id, link_id, forename, surname, grade_code, total_cases, total_t2a_cases, monthly_sdrs, sdr_conversions_last_30_days, total_points, available_points, reduction_hours, cms_adjustment_points, gs_adjustment_points, contracted_hours) AS
SELECT
    t.id AS id, wo.id AS link_id, om.forename, om.surname, omt.grade_code, w.total_filtered_cases AS total_cases, w.total_t2a_cases, w.monthly_sdrs, w.sdr_conversions_last_30_days, wpc.total_points, wpc.available_points, wpc.reduction_hours, wpc.cms_adjustment_points, wpc.gs_adjustment_points, wpc.contracted_hours
    FROM app.workload_points_calculations AS wpc
    JOIN app.workload AS w
        ON wpc.workload_id = w.id
    JOIN app.workload_owner AS wo
        ON w.workload_owner_id = wo.id
    JOIN app.workload_report AS wr
        ON wpc.workload_report_id = wr.id
    JOIN app.team AS t
        ON wo.team_id = t.id
    JOIN app.offender_manager AS om
        ON wo.offender_manager_id = om.id
    JOIN app.offender_manager_type AS omt
        ON om.type_id = omt.id
    WHERE wr.effective_from IS NOT NULL AND wr.effective_to IS NULL;

drop view app.ldu_capacity_breakdown_view;
CREATE OR REPLACE VIEW app.ldu_capacity_breakdown_view (id, link_id, name, grade_code, total_cases, total_t2a_cases, monthly_sdrs, sdr_conversions_last_30_days, total_points, available_points, reduction_hours, cms_adjustment_points, gs_adjustment_points, contracted_hours, count) AS
SELECT
    l.id AS id, t.id AS link_id, t.description AS name, omt.grade_code, SUM(w.total_filtered_cases) AS total_cases, SUM(w.total_t2a_cases) AS total_t2a_cases, SUM(w.monthly_sdrs) AS monthly_sdrs, SUM(w.sdr_conversions_last_30_days) AS sdr_conversions_last_30_days, SUM(wpc.total_points) AS total_points, SUM(wpc.available_points) AS available_points, SUM(wpc.reduction_hours) AS reduction_hours, SUM(wpc.cms_adjustment_points) AS cms_adjustment_points, SUM(wpc.gs_adjustment_points) AS gs_adjustment_points, SUM(wpc.contracted_hours) AS contracted_hours, COUNT(*) AS count
    FROM app.workload_points_calculations AS wpc
    JOIN app.workload AS w
        ON wpc.workload_id = w.id
    JOIN app.workload_owner AS wo
        ON w.workload_owner_id = wo.id
    JOIN app.workload_report AS wr
        ON wpc.workload_report_id = wr.id
    JOIN app.offender_manager AS om
        ON wo.offender_manager_id = om.id
    JOIN app.offender_manager_type AS omt
        ON om.type_id = omt.id
    JOIN app.team AS t
        ON wo.team_id = t.id
    JOIN app.ldu AS l
        ON t.ldu_id = l.id
    WHERE wr.effective_from IS NOT NULL AND wr.effective_to IS NULL
    GROUP BY l.id, t.id, t.description, omt.grade_code;

drop view app.region_capacity_breakdown_view;
CREATE OR REPLACE VIEW app.region_capacity_breakdown_view (id, link_id, name, grade_code, total_cases, total_t2a_cases, monthly_sdrs, sdr_conversions_last_30_days, total_points, available_points, reduction_hours, cms_adjustment_points, gs_adjustment_points, contracted_hours, count) AS
SELECT
    r.id AS id, l.id AS link_id, l.description AS name, omt.grade_code, SUM(w.total_filtered_cases) AS total_cases, SUM(w.total_t2a_cases) AS total_t2a_cases, SUM(w.monthly_sdrs) AS monthly_sdrs, SUM(w.sdr_conversions_last_30_days) AS sdr_conversions_last_30_days, SUM(wpc.total_points) AS total_points, SUM(wpc.available_points) AS available_points, SUM(wpc.reduction_hours) AS reduction_hours, SUM(wpc.cms_adjustment_points) AS cms_adjustment_points, SUM(wpc.gs_adjustment_points) AS gs_adjustment_points, SUM(wpc.contracted_hours) AS contracted_hours, COUNT(*) AS count
    FROM app.workload_points_calculations AS wpc
    JOIN app.workload AS w
        ON wpc.workload_id = w.id
    JOIN app.workload_owner AS wo
        ON w.workload_owner_id = wo.id
    JOIN app.workload_report AS wr
        ON wpc.workload_report_id = wr.id
    JOIN app.offender_manager AS om
        ON wo.offender_manager_id = om.id
    JOIN app.offender_manager_type AS omt
        ON om.type_id = omt.id
    JOIN app.team AS t
        ON wo.team_id = t.id
    JOIN app.ldu AS l
        ON t.ldu_id = l.id
    JOIN app.region AS r
        ON l.region_id = r.id
    WHERE wr.effective_from IS NOT NULL AND wr.effective_to IS NULL
    GROUP BY r.id, l.id, l.description, omt.grade_code;

drop view app.national_capacity_breakdown_view;
CREATE OR REPLACE VIEW app.national_capacity_breakdown_view (link_id, name, grade_code, total_cases, total_t2a_cases, monthly_sdrs, sdr_conversions_last_30_days, total_points, available_points, reduction_hours, cms_adjustment_points, gs_adjustment_points, contracted_hours, count) AS
SELECT
    r.id AS link_id, r.description AS name, omt.grade_code, SUM(w.total_filtered_cases) AS total_cases, SUM(w.total_t2a_cases) AS total_t2a_cases, SUM(w.monthly_sdrs) AS monthly_sdrs, SUM(w.sdr_conversions_last_30_days) AS sdr_conversions_last_30_days, SUM(wpc.total_points) AS total_points, SUM(wpc.available_points) AS available_points, SUM(wpc.reduction_hours) AS reduction_hours, SUM(wpc.cms_adjustment_points) AS cms_adjustment_points, SUM(wpc.gs_adjustment_points) AS gs_adjustment_points, SUM(wpc.contracted_hours) AS contracted_hours, COUNT(*) AS count
    FROM app.workload_points_calculations AS wpc
    JOIN app.workload AS w
        ON wpc.workload_id = w.id
    JOIN app.workload_owner AS wo
        ON w.workload_owner_id = wo.id
    JOIN app.workload_report AS wr
        ON wpc.workload_report_id = wr.id
    JOIN app.offender_manager AS om
        ON wo.offender_manager_id = om.id
    JOIN app.offender_manager_type AS omt
        ON om.type_id = omt.id
    JOIN app.team AS t
        ON wo.team_id = t.id
    JOIN app.ldu AS l
        ON t.ldu_id = l.id
    JOIN app.region AS r
        ON l.region_id = r.id
    WHERE wr.effective_from IS NOT NULL AND wr.effective_to IS NULL
    GROUP BY r.id, r.description, omt.grade_code;