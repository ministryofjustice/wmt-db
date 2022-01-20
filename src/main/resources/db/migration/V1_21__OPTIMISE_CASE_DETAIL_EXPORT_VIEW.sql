
drop view app.case_details_export_view;
CREATE MATERIALIZED VIEW app.case_details_export_view (regionname, regionid, lduname, lduid, teamname, teamid, workloadid, workloadownerid, tiercode, rowtype, casereferenceno, casetype, offendermanagername, gradecode) AS
SELECT
    r.description AS regionname, r.id AS regionid, l.description AS lduname, l.id AS lduid, t.description AS teamname, t.id AS teamid, w.id AS workloadid, w.workload_owner_id AS workloadownerid, cc.category_name AS tiercode, rtd.row_type_full_name AS rowtype, case_ref_no AS casereferenceno, location AS casetype, CONCAT(om.forename, ' ', om.surname) AS offendermanagername, omt.grade_code AS gradecode
    FROM app.case_details AS c
    JOIN app.row_type_definitions AS rtd
        ON c.row_type = rtd.row_type
    JOIN app.workload AS w
        ON c.workload_id = w.id
    JOIN app.workload_owner AS wo
        ON w.workload_owner_id = wo.id
    JOIN app.offender_manager AS om
        ON wo.offender_manager_id = om.id
    JOIN app.offender_manager_type AS omt
        ON om.type_id = omt.id
    JOIN app.team AS t
        ON wo.team_id = t.id
    JOIN app.ldu AS l
        ON l.id = t.ldu_id
    JOIN app.region AS r
        ON r.id = l.region_id
    JOIN app.workload_report AS wr
        ON w.workload_report_id = wr.id
    JOIN app.case_category AS cc
        ON c.tier_code = cc.category_id
    WHERE wr.effective_from IS NOT NULL AND wr.effective_to IS NULL WITH DATA;