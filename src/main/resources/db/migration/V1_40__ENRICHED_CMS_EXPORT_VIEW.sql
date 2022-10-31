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