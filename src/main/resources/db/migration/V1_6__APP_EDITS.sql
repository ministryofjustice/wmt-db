
drop view app.individual_case_overview;
CREATE OR REPLACE VIEW app.individual_case_overview (workload_owner_id, team_id, ldu_id, region_id, grade_code, of_name,om_key , team_name, ldu_name, region_name, region_code, ldu_code, team_code, available_points, total_points, total_cases, contracted_hours, reduction_hours, cms_adjustment_points) AS
SELECT
    wo.id AS workload_owner_id,
    t.id AS team_id,
    l.id AS ldu_id,
    r.id AS region_id,
    om_type.grade_code AS grade_code,
    CONCAT(om.forename, ' ', om.surname) AS of_name,
    om.key AS om_key,
    t.description AS team_name,
    l.description AS ldu_name,
    r.description AS region_name,
    r.code AS region_code,
    l.code AS ldu_code,
    t.code AS team_code,
    wpc.available_points AS available_points,
    wpc.total_points AS total_points,
    (w.total_filtered_cases + w.total_t2a_cases) AS total_cases,
    wpc.contracted_hours AS contracted_hours,
    wpc.reduction_hours AS reduction_hours,
    wpc.cms_adjustment_points AS cms_adjustment_points
    FROM app.workload_owner AS wo
    JOIN app.team AS t
        ON wo.team_id = t.id
    JOIN app.ldu AS l
        ON t.ldu_id = l.id
    JOIN app.region AS r
        ON r.id = l.region_id
    JOIN app.workload AS w
        ON wo.id = w.workload_owner_id
    JOIN app.workload_points_calculations AS wpc
        ON wpc.workload_id = w.id
    JOIN app.workload_report AS wr
        ON wr.id = wpc.workload_report_id
    JOIN app.offender_manager AS om
        ON om.id = wo.offender_manager_id
    JOIN app.offender_manager_type AS om_type
        ON om_type.id = om.type_id
    WHERE wr.effective_from IS NOT NULL AND wr.effective_to IS NULL;



drop view app.region_caseload_view;
CREATE OR REPLACE VIEW app.region_caseload_view (id, link_id, name, region_name, region_code, ldu_code, grade_code, location, untiered, a3, a2, a1, a0, b3, b2, b1, b0, c3, c2, c1, c0, d3, d2, d1, d0, total_cases, count) AS
SELECT
    l.region_id AS id,
    l.id AS link_id,
    l.description AS name,
    r.description AS region_name,
    r.code as region_code,
    l.code as ldu_code,
    omt.grade_code,
    tr.location,
    SUM((CASE
        WHEN tr.tier_number = 0 THEN tr.total_filtered_cases
        ELSE 0
    END) + (CASE
        WHEN tr.tier_number = 0 THEN tr.t2a_total_cases
        ELSE 0
    END)) AS untiered, SUM((CASE
        WHEN tr.tier_number = 1 THEN tr.total_filtered_cases
        ELSE 0
    END) + (CASE
        WHEN tr.tier_number = 1 THEN tr.t2a_total_cases
        ELSE 0
    END)) AS a3, SUM((CASE
        WHEN tr.tier_number = 2 THEN tr.total_filtered_cases
        ELSE 0
    END) + (CASE
        WHEN tr.tier_number = 2 THEN tr.t2a_total_cases
        ELSE 0
    END)) AS a2, SUM((CASE
        WHEN tr.tier_number = 3 THEN tr.total_filtered_cases
        ELSE 0
    END) + (CASE
        WHEN tr.tier_number = 3 THEN tr.t2a_total_cases
        ELSE 0
    END)) AS a1, SUM((CASE
        WHEN tr.tier_number = 4 THEN tr.total_filtered_cases
        ELSE 0
    END) + (CASE
        WHEN tr.tier_number = 4 THEN tr.t2a_total_cases
        ELSE 0
    END)) AS a0, SUM((CASE
        WHEN tr.tier_number = 5 THEN tr.total_filtered_cases
        ELSE 0
    END) + (CASE
        WHEN tr.tier_number = 5 THEN tr.t2a_total_cases
        ELSE 0
    END)) AS b3, SUM((CASE
        WHEN tr.tier_number = 6 THEN tr.total_filtered_cases
        ELSE 0
    END) + (CASE
        WHEN tr.tier_number = 6 THEN tr.t2a_total_cases
        ELSE 0
    END)) AS b2, SUM((CASE
        WHEN tr.tier_number = 7 THEN tr.total_filtered_cases
        ELSE 0
    END) + (CASE
        WHEN tr.tier_number = 7 THEN tr.t2a_total_cases
        ELSE 0
    END)) AS b1, SUM((CASE
        WHEN tr.tier_number = 8 THEN tr.total_filtered_cases
        ELSE 0
    END) + (CASE
        WHEN tr.tier_number = 8 THEN tr.t2a_total_cases
        ELSE 0
    END)) AS b0, SUM((CASE
        WHEN tr.tier_number = 9 THEN tr.total_filtered_cases
        ELSE 0
    END) + (CASE
        WHEN tr.tier_number = 9 THEN tr.t2a_total_cases
        ELSE 0
    END)) AS c3, SUM((CASE
        WHEN tr.tier_number = 10 THEN tr.total_filtered_cases
        ELSE 0
    END) + (CASE
        WHEN tr.tier_number = 10 THEN tr.t2a_total_cases
        ELSE 0
    END)) AS c2, SUM((CASE
        WHEN tr.tier_number = 11 THEN tr.total_filtered_cases
        ELSE 0
    END) + (CASE
        WHEN tr.tier_number = 11 THEN tr.t2a_total_cases
        ELSE 0
    END)) AS c1, SUM((CASE
        WHEN tr.tier_number = 12 THEN tr.total_filtered_cases
        ELSE 0
    END) + (CASE
        WHEN tr.tier_number = 12 THEN tr.t2a_total_cases
        ELSE 0
    END)) AS c0, SUM((CASE
        WHEN tr.tier_number = 13 THEN tr.total_filtered_cases
        ELSE 0
    END) + (CASE
        WHEN tr.tier_number = 13 THEN tr.t2a_total_cases
        ELSE 0
    END)) AS d3, SUM((CASE
        WHEN tr.tier_number = 14 THEN tr.total_filtered_cases
        ELSE 0
    END) + (CASE
        WHEN tr.tier_number = 14 THEN tr.t2a_total_cases
        ELSE 0
    END)) AS d2, SUM((CASE
        WHEN tr.tier_number = 15 THEN tr.total_filtered_cases
        ELSE 0
    END) + (CASE
        WHEN tr.tier_number = 15 THEN tr.t2a_total_cases
        ELSE 0
    END)) AS d1, SUM((CASE
        WHEN tr.tier_number = 16 THEN tr.total_filtered_cases
        ELSE 0
    END) + (CASE
        WHEN tr.tier_number = 16 THEN tr.t2a_total_cases
        ELSE 0
    END)) AS d0, SUM(tr.total_filtered_cases + tr.t2a_total_cases) AS total_cases, COUNT(*) AS count
    FROM app.tiers AS tr
    JOIN app.workload AS w
        ON tr.workload_id = w.id
    JOIN app.workload_points_calculations AS wpc
        ON wpc.workload_id = w.id
    JOIN app.workload_report AS wr
        ON wr.id = wpc.workload_report_id
    JOIN app.workload_owner AS wo
        ON wo.id = w.workload_owner_id
    JOIN app.team AS t
        ON t.id = wo.team_id
    JOIN app.ldu AS l
        ON l.id = t.ldu_id
    JOIN app.region AS r
        ON r.id = l.region_id
    JOIN app.offender_manager AS om
        ON om.id = wo.offender_manager_id
    JOIN app.offender_manager_type AS omt
        ON omt.id = om.type_id
    WHERE wr.effective_from IS NOT NULL AND wr.effective_to IS NULL
    GROUP BY l.region_id, l.id, r.description, l.description, omt.grade_code, tr.location, r.code, l.code;


drop view app.reductions_notes_dashboard;
CREATE OR REPLACE VIEW app.reductions_notes_dashboard (workload_owner_id, team_id, ldu_id, region_id, region_code, ldu_code, team_code, region_name, ldu_name, team_name, name, om_key, contracted_hours, reduction_reason, amount, start_date, end_date, reduction_status, additional_notes, grade_code) AS
SELECT
    wo.id AS workload_owner_id,
    team.id AS team_id,
    ldu.id AS ldu_id,
    region.id AS region_id,
    region.code AS region_code,
    ldu.code AS ldu_code,
    team.code AS team_code,
    region.description AS region_name,
    ldu.description AS ldu_name,
    team.description AS team_name,
    CONCAT(om.forename, ' ', om.surname) AS name,
    om.key as om_key,
    wo.contracted_hours AS contracted_hours,
    rr.reason_short_name AS reduction_reason,
    r.hours AS amount,
    r.effective_from AS start_date,
    r.effective_to AS end_date,
    r.status AS reduction_status,
    regexp_replace(r.notes, '[\n\r]+', ' ', 'g') AS additional_notes,
    omt.grade_code AS grade_code
    FROM app.workload_owner AS wo
    JOIN app.team AS team
        ON wo.team_id = team.id
    JOIN app.ldu AS ldu
        ON team.ldu_id = ldu.id
    JOIN app.region AS region
        ON region.id = ldu.region_id
    JOIN app.workload AS w
        ON wo.id = w.workload_owner_id
    JOIN app.workload_points_calculations AS wpc
        ON wpc.workload_id = w.id
    JOIN app.workload_report AS wr
        ON wr.id = wpc.workload_report_id
    JOIN app.offender_manager AS om
        ON om.id = wo.offender_manager_id
    JOIN app.offender_manager_type AS omt
        ON om.type_id = omt.id
    JOIN app.reductions AS r
        ON r.workload_owner_id = wo.id
    JOIN app.reduction_reason AS rr
        ON r.reduction_reason_id = rr.id
    WHERE wr.effective_from IS NOT NULL AND wr.effective_to IS NULL;