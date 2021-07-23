ALTER TABLE dbo.daily_archive_data
    ADD COLUMN workload_report_id bigint NOT NULL;

ALTER TABLE dbo.daily_archive_data
    ADD COLUMN team_unique_identifier VARCHAR(511) NOT NULL;


CREATE TABLE dbo.ldu
(
    id bigint NOT NULL GENERATED ALWAYS AS IDENTITY,
    code VARCHAR(511) NOT NULL,
    description VARCHAR(511) NOT NULL
)
WITH (
    OIDS = FALSE
);

ALTER TABLE dbo.ldu
ADD CONSTRAINT pk__ldu__3213e83f1b396795_999674609 PRIMARY KEY (id);


CREATE TABLE IF NOT EXISTS dbo.team
(
    id bigint NOT NULL GENERATED ALWAYS AS IDENTITY,
    code VARCHAR(511) NOT NULL,
    description VARCHAR(511) NOT NULL
)
WITH (
    OIDS = FALSE
);

ALTER TABLE dbo.team
ADD CONSTRAINT pk__team__3213e83feae4a58a_1031674723 PRIMARY KEY (id);

DROP VIEW dbo.archive_data_view;
DROP VIEW dbo.aggregate_offender_managers_view;
DROP VIEW dbo.fortnightly_archive_data_view;
DROP VIEW dbo.fortnightly_aggregate_offender_managers_view;
DROP VIEW dbo.offender_managers_archive_view;
CREATE OR REPLACE VIEW dbo.offender_managers_archive_view
    AS
     SELECT om.id AS om_id,
    w.id AS workload_id,
    om.uniqueidentifier AS unique_identifier,
    om.offendermanagertypeid AS om_type_id,
    omt.name AS grade,
    w.lduid AS workload_ldu_id,
    outeam.name AS team_name,
    outeam.uniqueidentifier AS team_unique_identifier,
    om.forename AS om_forename,
    om.surname AS om_surname,
    w.totalcases AS total_cases,
    w.totalpoints AS total_points,
    w.sdrpoints AS sdr_points,
    w.sdrconversionpoints AS sdr_conversion_points,
    w.paromspoints AS paroms_points,
    w.nominaltarget AS nominal_target,
    w.contractedhoursperweek AS contracted_hours,
    w.hoursreduction AS hours_reduction,
    wr.date AS workload_date,
    wr.id AS workload_report_id
   FROM dbo.offendermanager om
     JOIN dbo.offendermanagertype omt ON om.offendermanagertypeid = omt.id
     JOIN dbo.workload w ON om.id = w.offendermanagerid
     JOIN dbo.organisationalunitworkloadreport ou_wr ON w.organisationalunitworkloadreportid = ou_wr.id
     JOIN dbo.workloadreport wr ON ou_wr.workloadreportid = wr.id
     JOIN dbo.organisationalunit outeam ON w.teamid = outeam.id;



CREATE OR REPLACE VIEW dbo.aggregate_offender_managers_view
    AS
     SELECT unique_identifier,
    om_id,
    om_type_id,
    grade,
    workload_id,
    workload_ldu_id,
    team_name,
    team_unique_identifier,
    concat(om_forename, ' ', om_surname) AS om_name,
    workload_date,
    workload_report_id,
    sum(total_cases) AS total_cases,
    sum(total_points) AS total_points,
    sum(sdr_points) AS sdr_points,
    sum(sdr_conversion_points) AS sdr_conversion_points,
    sum(paroms_points) AS paroms_points,
    sum(nominal_target) AS nominal_target,
    sum(contracted_hours) AS contracted_hours,
    sum(hours_reduction) AS hours_reduction
   FROM dbo.offender_managers_archive_view
  GROUP BY unique_identifier, om_id, om_type_id, grade, workload_id, workload_ldu_id, team_name, om_forename, om_surname, workload_date, workload_report_id, team_unique_identifier;



CREATE OR REPLACE VIEW dbo.archive_data_view
    AS
     SELECT om.unique_identifier,
    om.om_id,
    om.workload_id,
    om.workload_date,
    om.workload_report_id,
    om.om_type_id,
    om.grade,
    ouldu.name AS ldu_name,
    ouldu.uniqueidentifier AS ldu_unique_identifier,
    ouregion.name AS region_name,
    om.team_unique_identifier,
    om.team_name,
    om.om_name,
    om.total_cases,
    om.total_points,
    om.sdr_points,
    om.sdr_conversion_points,
    om.paroms_points,
    om.nominal_target,
    om.contracted_hours,
    om.hours_reduction
   FROM dbo.aggregate_offender_managers_view om
     JOIN dbo.organisationalunit ouldu ON om.workload_ldu_id = ouldu.id
     JOIN dbo.organisationalunit ouregion ON ouldu.parentorganisationalunitid = ouregion.id;

CREATE OR REPLACE VIEW dbo.fortnightly_aggregate_offender_managers_view (unique_identifier, om_id, om_type_id, workload_ldu_id, team_name, fortnight, start_date, end_date, om_name, average_cases, average_points, average_sdr_points, average_sdr_conversion_points, average_paroms_points, average_nominal_target, average_contracted_hours, average_hours_reduction) AS
SELECT
    unique_identifier AS unique_identifier, om_id AS om_id, om_type_id AS om_type_id, workload_ldu_id, team_name AS team_name, ((workload_date::DATE - '2016-08-25'::DATE) / 14) AS fortnight, '2016-08-25'::DATE + (interval '1' day * (- 14 * (((workload_date::DATE - '2016-08-25'::DATE) / 14) + 1))) AS start_date, '2016-08-25'::DATE + (interval '1' day * (- 14 * (((workload_date::DATE - '2016-08-25'::DATE) / 14)))) AS end_date,
    /* , MONTH(workload_date) */
    CONCAT(om_forename, ' ', om_surname) AS om_name, AVG(total_cases) AS average_cases, AVG(total_points) AS average_points, AVG(sdr_points) AS average_sdr_points, AVG(sdr_conversion_points) AS average_sdr_conversion_points, AVG(paroms_points) AS average_paroms_points, AVG(nominal_target) AS average_nominal_target, AVG(contracted_hours) AS average_contracted_hours, AVG(hours_reduction) AS average_hours_reduction
    FROM dbo.offender_managers_archive_view
    GROUP BY unique_identifier, om_id, om_type_id, workload_ldu_id, team_name, om_forename, om_surname, ((workload_date::DATE - '2016-08-25'::DATE) / 14);


CREATE OR REPLACE VIEW dbo.fortnightly_archive_data_view (unique_identifier, om_id, om_type_id, start_date, end_date, ldu_name, team_name, om_name, average_cases, average_points, average_sdr_points, average_sdr_conversion_points, average_paroms_points, average_nominal_target, average_contracted_hours, average_hours_reduction) AS
SELECT
    om.unique_identifier AS unique_identifier, om.om_id AS om_id, om.om_type_id AS om_type_id, om.start_date AS start_date, om.end_date AS end_date, ouldu.name AS ldu_name, om.team_name AS team_name, om.om_name AS om_name, om.average_cases AS average_cases, om.average_points AS average_points, om.average_sdr_points AS average_sdr_points, om.average_sdr_conversion_points AS average_sdr_conversion_points, om.average_paroms_points AS average_paroms_points, om.average_nominal_target AS average_nominal_target, om.average_contracted_hours AS average_contracted_hours, om.average_hours_reduction AS average_hours_reduction
    FROM dbo.fortnightly_aggregate_offender_managers_view AS om
    JOIN dbo.organisationalunit AS ouldu
        ON om.workload_ldu_id = ouldu.id;


