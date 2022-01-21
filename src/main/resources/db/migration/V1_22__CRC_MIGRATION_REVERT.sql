
CREATE OR REPLACE VIEW app.national_capacity_view (total_points, available_points, reduction_hours, contracted_hours, effective_from, workload_report_id, count) AS
SELECT
    SUM(total_points) AS total_points, SUM(available_points) AS available_points, SUM(reduction_hours) AS reduction_hours, SUM(wpc.contracted_hours) AS contracted_hours, wr.effective_from AS effective_from, wr.id AS workload_report_id, COUNT(*) AS count
    FROM app.workload_points_calculations AS wpc
    JOIN app.workload AS w
        ON wpc.workload_id = w.id
    JOIN app.workload_report AS wr
        ON wpc.workload_report_id = wr.id
    GROUP BY wr.effective_from, wr.id;

drop view app.national_caseload_view;
CREATE OR REPLACE VIEW app.national_caseload_view (link_id, name, grade_code, location, untiered, a3, a2, a1, a0, b3, b2, b1, b0, c3, c2, c1, c0, d3, d2, d1, d0, total_cases, count) AS
SELECT
    r.id AS link_id, r.description AS name, omt.grade_code, tr.location, SUM((CASE
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
    GROUP BY r.id, r.description, omt.grade_code, tr.location;


drop view app.crc_caseload_view;
drop view app.crc_capacity_view;