DROP VIEW app.cms_export_view;
CREATE MATERIALIZED VIEW app.cms_export_view AS
SELECT contactregionname,contactlduname,contactteamname,contactdate,omcontactdate,contactname,contactgradecode,omregionname,omlduname,omteamname,contactCMS.contactid,omname,omgradecode,contactCMS.contactdescription,contactCMS.contactcode,contactCMS.contactpoints,omCMS.ompoints,contactCMS.caserefno,omCMS.caserefno as omcaserefno,omCMS.contactdescription as omcontactdescription,omCMS.contactcode as omcontactcode,omCMS.omregionid,omCMS.omlduid,omCMS.omteamid,contactCMS.contactregionid,contactCMS.contactlduid,contactCMS.contactteamid
FROM app.contact_cms_export_view AS contactCMS
FULL OUTER JOIN app.om_cms_export_view AS omCMS ON contactCMS.contactid = omCMS.contactid WITH NO DATA;