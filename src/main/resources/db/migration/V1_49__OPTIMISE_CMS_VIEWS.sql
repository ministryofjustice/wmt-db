DROP MATERIALIZED VIEW app.enriched_cms_export_view;

DROP VIEW app.om_cms_export_view;
CREATE OR REPLACE VIEW app.om_cms_export_view (omregionname, omregionid, omlduname, omlduid, omteamname, omteamid, omcontactdate, omworkloadownerid, omname, omgradecode, contactid, contactdescription, contactcode, ompoints, caserefno) AS
SELECT
    r.description AS omregionname, r.id AS omregionid, l.description AS omlduname, l.id AS omlduid, t.description AS omteamname, t.id AS omteamid, a.effective_from AS omcontactdate, wo.id AS omworkloadownerid, CONCAT(om.forename, ' ', om.surname) AS omname, omt.grade_code AS omgradecode, a.contact_id AS contactid, ar.contact_description AS contactdescription, ar.contact_code AS contactcode, a.points AS ompoints, a.case_ref_no AS caserefno
    FROM app.adjustments AS a
    JOIN app.adjustment_reason AS ar
        ON a.adjustment_reason_id = ar.id
    JOIN app.workload_owner AS wo
        ON a.workload_owner_id = wo.id
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
    WHERE a.points < 0;


DROP VIEW app.contact_cms_export_view;
CREATE OR REPLACE VIEW app.contact_cms_export_view (contactregionname, contactregionid, contactlduname, contactlduid, contactteamname, contactteamid, contactdate, contactworkloadownerid, contactname, contactgradecode, contactid, contactdescription, contactcode, contactpoints, caserefno) AS
SELECT
    r.description AS contactregionname, r.id AS contactregionid, l.description AS contactlduname, l.id AS contactlduid, t.description AS contactteamname, t.id AS contactteamid, a.effective_from AS contactdate, wo.id AS contactworkloadownerid, CONCAT(om.forename, ' ', om.surname) AS contactname, omt.grade_code AS contactgradecode, a.contact_id AS contactid, ar.contact_description AS contactdescription, ar.contact_code AS contactcode, a.points AS contactpoints, a.case_ref_no AS caserefno
    FROM app.adjustments AS a
    JOIN app.adjustment_reason AS ar
        ON a.adjustment_reason_id = ar.id
    JOIN app.workload_owner AS wo
        ON a.workload_owner_id = wo.id
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
    WHERE a.points > 0;


CREATE MATERIALIZED VIEW app.enriched_cms_export_view AS
SELECT contactregionname,contactlduname,contactteamname,contactdate,omcontactdate,contactname,contactgradecode,omregionname,omlduname,omteamname,omCMS.contactid,omname,omgradecode,contactCMS.contactdescription,contactCMS.contactcode,contactCMS.contactpoints,omCMS.ompoints,contactCMS.caserefno,omCMS.caserefno as omcaserefno,omCMS.contactdescription as omcontactdescription,omCMS.contactcode as omcontactcode,omCMS.omregionid,omCMS.omlduid,omCMS.omteamid,contactCMS.contactregionid,contactCMS.contactlduid,contactCMS.contactteamid,
       appRegion.description AS cmsregionname,appLdu.description AS cmscontactlduname,appTeam.description AS cmscontactteamname,stagingCMS.contact_staff_name AS cmscontactname,stagingCMS.contact_staff_grade AS cmscontactgradecode
FROM app.om_cms_export_view AS omCMS
         LEFT OUTER JOIN app.contact_cms_export_view AS contactCMS ON omCMS.contactid = contactCMS.contactid
         LEFT OUTER JOIN staging.cms AS stagingCMS ON omCMS.contactid = cast (stagingCMS.contact_id as BIGINT)
         LEFT OUTER JOIN app.team AS appTeam
                         ON stagingCMS.contact_team_key = appTeam.code
         LEFT OUTER JOIN app.ldu AS appLdu
                         ON appTeam.ldu_id = appLdu.id
         LEFT OUTER JOIN app.region AS appRegion
                         ON appLdu.region_id = appRegion.id
WITH NO DATA;