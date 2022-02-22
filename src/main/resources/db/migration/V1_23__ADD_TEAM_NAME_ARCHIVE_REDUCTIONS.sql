
drop view app.reductions_archive_view;
CREATE OR REPLACE VIEW app.reductions_archive_view (om_name, hours_reduced, reduction_id, comments, last_updated_date, reduction_added_by, reduction_reason, start_date, end_date, reduction_status, teamName) AS
SELECT
    CONCAT(om.forename, ' ', om.surname) AS om_name, r.hours AS hours_reduced, r.id AS reduction_id, r.notes AS comments, r.updated_date AS last_updated_date, u.name AS reduction_added_by, rr.reason_short_name AS reduction_reason, r.effective_from AS start_date, r.effective_to AS end_date, r.status AS reduction_status, t.description AS teamName
    FROM app.workload_owner AS wo
    JOIN app.offender_manager AS om
        ON om.id = wo.offender_manager_id
    JOIN app.reductions AS r
        ON r.workload_owner_id = wo.id
    JOIN app.reduction_reason AS rr
        ON r.reduction_reason_id = rr.id
    JOIN app.users AS u
        ON r.user_id = u.id
    JOIN app.team AS t
        ON wo.team_id = t.id;