
-- ------------ Write CREATE-DATABASE-stage scripts -----------

CREATE SCHEMA IF NOT EXISTS dbo;



-- ------------ Write CREATE-TABLE-stage scripts -----------

CREATE TABLE dbo.archive_reduction_data(
    id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY,
    om_name VARCHAR(511) NOT NULL,
    hours_reduced DOUBLE PRECISION NOT NULL,
    comments TEXT,
    last_updated_date TIMESTAMP WITH TIME ZONE NOT NULL,
    reduction_added_by VARCHAR(200)
)
        WITH (
        OIDS=FALSE
        );



CREATE TABLE dbo.capacityabsoluteom(
    id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY,
    offendermanagertypeid BIGINT NOT NULL,
    gt110 BIGINT NOT NULL,
    gt100 BIGINT NOT NULL,
    lt100 BIGINT NOT NULL,
    total DOUBLE PRECISION NOT NULL,
    reportperiod TIMESTAMP WITH TIME ZONE NOT NULL
)
        WITH (
        OIDS=FALSE
        );



CREATE TABLE dbo.capacityaveragedirectorate(
    id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY,
    directorateid BIGINT NOT NULL,
    capacity DOUBLE PRECISION NOT NULL,
    sdr BIGINT NOT NULL DEFAULT (0),
    reportperiod TIMESTAMP WITH TIME ZONE NOT NULL
)
        WITH (
        OIDS=FALSE
        );



CREATE TABLE dbo.capacityaverageldu(
    id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY,
    directorateid BIGINT NOT NULL,
    lduid BIGINT NOT NULL,
    capacity DOUBLE PRECISION NOT NULL,
    sdr BIGINT NOT NULL DEFAULT (0),
    reportperiod TIMESTAMP WITH TIME ZONE NOT NULL
)
        WITH (
        OIDS=FALSE
        );



CREATE TABLE dbo.capacityaverageom(
    id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY,
    directorateid BIGINT NOT NULL DEFAULT (0),
    lduid BIGINT NOT NULL DEFAULT (0),
    offendermanagertypeid BIGINT NOT NULL,
    gt110 BIGINT NOT NULL,
    gt100 BIGINT NOT NULL,
    lt100 BIGINT NOT NULL,
    total DOUBLE PRECISION NOT NULL,
    reportperiod TIMESTAMP WITH TIME ZONE NOT NULL
)
        WITH (
        OIDS=FALSE
        );



CREATE TABLE dbo.capacityperiodofficer(
    id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY,
    offendermanagerid BIGINT NOT NULL,
    offendermanagertypeid BIGINT NOT NULL,
    mincapacity BIGINT NOT NULL,
    maxcapacity BIGINT NOT NULL,
    avgcapacity BIGINT NOT NULL,
    reportperiod TIMESTAMP WITH TIME ZONE NOT NULL
)
        WITH (
        OIDS=FALSE
        );



CREATE TABLE dbo.caseloadofficer(
    id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY,
    casecountdesc VARCHAR(50) NOT NULL,
    offendermanagertypeid BIGINT NOT NULL,
    omcountabs DOUBLE PRECISION NOT NULL,
    omcountavg DOUBLE PRECISION NOT NULL,
    totalomcountabs DOUBLE PRECISION NOT NULL,
    totalomcountavg DOUBLE PRECISION NOT NULL,
    reportperiod TIMESTAMP WITH TIME ZONE NOT NULL
)
        WITH (
        OIDS=FALSE
        );



CREATE TABLE dbo.daily_archive_data(
    id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY,
    unique_identifier VARCHAR(50),
    om_id BIGINT NOT NULL,
    workload_id BIGINT NOT NULL,
    workload_date TIMESTAMP WITH TIME ZONE NOT NULL,
    om_type_id BIGINT NOT NULL,
    region_name VARCHAR(255) NOT NULL,
    grade VARCHAR(255) NOT NULL,
    ldu_name VARCHAR(255) NOT NULL,
    ldu_unique_identifier VARCHAR(50) NOT NULL,
    team_name VARCHAR(255) NOT NULL,
    om_name VARCHAR(511) NOT NULL,
    total_cases BIGINT,
    total_points BIGINT,
    sdr_points BIGINT,
    sdr_conversion_points BIGINT,
    paroms_points BIGINT,
    nominal_target BIGINT,
    contracted_hours NUMERIC(38,6),
    hours_reduction NUMERIC(38,6)
)
        WITH (
        OIDS=FALSE
        );



CREATE TABLE dbo.databaseupdateresulttype(
    id BIGINT NOT NULL,
    description VARCHAR(50) NOT NULL
)
        WITH (
        OIDS=FALSE
        );



CREATE TABLE dbo.databaseupdatescriptlog(
    id UUID NOT NULL,
    databaseupdateversionlogid UUID NOT NULL,
    scriptname VARCHAR(255) NOT NULL,
    svnrevision NUMERIC(20,0) NOT NULL,
    svnaction VARCHAR(50) NOT NULL,
    svnauthor VARCHAR(255) NOT NULL,
    sqlscript TEXT,
    resulttypeid BIGINT NOT NULL,
    resultmessage TEXT,
    createddate TIMESTAMP WITH TIME ZONE NOT NULL
)
        WITH (
        OIDS=FALSE
        );



CREATE TABLE dbo.databaseupdateversionlog(
    id UUID NOT NULL,
    svnrevisionfrom NUMERIC(20,0) NOT NULL,
    svnrevisionto NUMERIC(20,0) NOT NULL,
    resulttypeid BIGINT NOT NULL,
    resultmessage TEXT,
    createddate TIMESTAMP WITH TIME ZONE NOT NULL
)
        WITH (
        OIDS=FALSE
        );



CREATE TABLE dbo.deliverytype(
    id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL
)
        WITH (
        OIDS=FALSE
        );



CREATE TABLE dbo.displaysettings(
    id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY,
    monthsbeforearchivingnotes BIGINT NOT NULL,
    displayworkloadinhours boolean NOT NULL DEFAULT FALSE,
    pointsperhour DOUBLE PRECISION NOT NULL DEFAULT (1)
)
        WITH (
        OIDS=FALSE
        );



CREATE TABLE dbo.fortnightly_archive_data(
    id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY,
    unique_identifier VARCHAR(50),
    om_id BIGINT NOT NULL,
    om_type_id BIGINT NOT NULL,
    start_date TIMESTAMP WITH TIME ZONE,
    end_date TIMESTAMP WITH TIME ZONE,
    ldu_name VARCHAR(255) NOT NULL,
    team_name VARCHAR(255) NOT NULL,
    om_name VARCHAR(511) NOT NULL,
    average_cases BIGINT,
    average_points BIGINT,
    average_sdr_points BIGINT,
    average_sdr_conversion_points BIGINT,
    average_paroms_points BIGINT,
    average_nominal_target BIGINT,
    average_contracted_hours NUMERIC(38,6),
    average_hours_reduction NUMERIC(38,6)
)
        WITH (
        OIDS=FALSE
        );



CREATE TABLE dbo.inactivecase(
    id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY,
    isdeleted boolean NOT NULL DEFAULT FALSE,
    flag VARCHAR(255),
    tier VARCHAR(255),
    crn VARCHAR(255),
    workloadid BIGINT NOT NULL
)
        WITH (
        OIDS=FALSE
        );



CREATE TABLE dbo.logging(
    id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY,
    message TEXT NOT NULL,
    loggingtypeid BIGINT NOT NULL,
    createdby VARCHAR(100) NOT NULL,
    createddate TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT timezone('utc', now())
)
        WITH (
        OIDS=FALSE
        );



CREATE TABLE dbo.messages(
    id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY,
    importantmessages TEXT NOT NULL,
    isdeleted boolean NOT NULL DEFAULT FALSE,
    createdby VARCHAR(50) NOT NULL DEFAULT 'sa',
    createddate TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT timezone('utc', now())
)
        WITH (
        OIDS=FALSE
        );



CREATE TABLE dbo.msreplication_options(
    optname VARCHAR(128) NOT NULL,
    value boolean NOT NULL DEFAULT FALSE,
    major_version BIGINT NOT NULL,
    minor_version BIGINT NOT NULL,
    revision BIGINT NOT NULL,
    install_failures BIGINT NOT NULL
)
        WITH (
        OIDS=FALSE
        );



CREATE TABLE dbo.note(
    id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY,
    offendermanagerid BIGINT,
    organisationalunitid BIGINT,
    notes TEXT,
    lastupdateuserid BIGINT,
    lastupdatedatetime TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT timezone('utc', now()),
    hoursreduced DOUBLE PRECISION NOT NULL,
    offendermanagerparentid BIGINT
)
        WITH (
        OIDS=FALSE
        );



CREATE TABLE dbo.offendermanager(
    id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY,
    offendermanagertypeid BIGINT NOT NULL,
    uniqueidentifier VARCHAR(50),
    forename VARCHAR(255),
    surname VARCHAR(255),
    notes TEXT,
    isdeleted boolean NOT NULL DEFAULT FALSE,
    lastupdateuserid BIGINT,
    lastupdatedatetime TIMESTAMP WITH TIME ZONE
)
        WITH (
        OIDS=FALSE
        );



CREATE TABLE dbo.offendermanagertype(
    id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    isdeleted boolean NOT NULL DEFAULT FALSE
)
        WITH (
        OIDS=FALSE
        );



CREATE TABLE dbo.organisationalunit(
    id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY,
    organisationalunittypeid BIGINT NOT NULL,
    uniqueidentifier VARCHAR(50),
    parentorganisationalunitid BIGINT,
    name VARCHAR(255) NOT NULL,
    abbreviation VARCHAR(50),
    isdeleted boolean NOT NULL DEFAULT FALSE,
    lastupdateuserid BIGINT,
    lastupdatedatetime TIMESTAMP WITH TIME ZONE,
    notes TEXT,
    deliverytypeid BIGINT
)
        WITH (
        OIDS=FALSE
        );



CREATE TABLE dbo.organisationalunittype(
    id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    isdeleted boolean NOT NULL DEFAULT FALSE,
    parentorganisationalunittypeid BIGINT
)
        WITH (
        OIDS=FALSE
        );



CREATE TABLE dbo.organisationalunitworkloadreport(
    id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY,
    organisationalunitid BIGINT NOT NULL,
    workloadreportid BIGINT NOT NULL,
    isdeleted boolean NOT NULL DEFAULT FALSE
)
        WITH (
        OIDS=FALSE
        );



CREATE TABLE dbo.requirementdetails(
    id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY(START WITH 12),
    requirementworkloadid BIGINT NOT NULL,
    tier VARCHAR(50),
    crn VARCHAR(50)
)
        WITH (
        OIDS=FALSE
        );



CREATE TABLE dbo.requirementtype(
    id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY(START WITH 12),
    parentrequirementtypeid BIGINT,
    code VARCHAR(255) NOT NULL,
    description VARCHAR(255),
    originalcode VARCHAR(255) NOT NULL,
    originalparentrequirementtypeid BIGINT,
    createdbyuserid BIGINT NOT NULL,
    createddatetime TIMESTAMP WITH TIME ZONE NOT NULL,
    modifiedbyuserid BIGINT,
    modifieddatetime TIMESTAMP WITH TIME ZONE,
    isdeleted boolean NOT NULL DEFAULT FALSE,
    defaultchildrequirementtypeid BIGINT
)
        WITH (
        OIDS=FALSE
        );



CREATE TABLE dbo.requirementworkload(
    id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY(START WITH 12),
    workloadid BIGINT NOT NULL,
    requirementtypeid BIGINT NOT NULL,
    count BIGINT NOT NULL,
    points BIGINT NOT NULL
)
        WITH (
        OIDS=FALSE
        );



CREATE TABLE dbo.requirementworkloadpoints(
    id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY(START WITH 12),
    workloadpointsid BIGINT NOT NULL,
    requirementtypeid BIGINT NOT NULL,
    points BIGINT NOT NULL
)
        WITH (
        OIDS=FALSE
        );



CREATE TABLE dbo.roles(
    id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY,
    code VARCHAR(50) NOT NULL,
    name TEXT NOT NULL,
    isdeleted boolean NOT NULL DEFAULT FALSE
)
        WITH (
        OIDS=FALSE
        );



CREATE TABLE dbo.spt_fallback_db(
    xserver_name VARCHAR(30) NOT NULL,
    xdttm_ins TIMESTAMP WITH TIME ZONE NOT NULL,
    xdttm_last_ins_upd TIMESTAMP WITH TIME ZONE NOT NULL,
    xfallback_dbid NUMERIC(5,0),
    name VARCHAR(30) NOT NULL,
    dbid NUMERIC(5,0) NOT NULL,
    status NUMERIC(5,0) NOT NULL,
    version NUMERIC(5,0) NOT NULL
)
        WITH (
        OIDS=FALSE
        );



CREATE TABLE dbo.spt_fallback_dev(
    xserver_name VARCHAR(30) NOT NULL,
    xdttm_ins TIMESTAMP WITH TIME ZONE NOT NULL,
    xdttm_last_ins_upd TIMESTAMP WITH TIME ZONE NOT NULL,
    xfallback_low BIGINT,
    xfallback_drive CHAR(2),
    low BIGINT NOT NULL,
    high BIGINT NOT NULL,
    status NUMERIC(5,0) NOT NULL,
    name VARCHAR(30) NOT NULL,
    phyname VARCHAR(127) NOT NULL
)
        WITH (
        OIDS=FALSE
        );



CREATE TABLE dbo.spt_fallback_usg(
    xserver_name VARCHAR(30) NOT NULL,
    xdttm_ins TIMESTAMP WITH TIME ZONE NOT NULL,
    xdttm_last_ins_upd TIMESTAMP WITH TIME ZONE NOT NULL,
    xfallback_vstart BIGINT,
    dbid NUMERIC(5,0) NOT NULL,
    segmap BIGINT NOT NULL,
    lstart BIGINT NOT NULL,
    sizepg BIGINT NOT NULL,
    vstart BIGINT NOT NULL
)
        WITH (
        OIDS=FALSE
        );



CREATE TABLE dbo.spt_monitor(
    lastrun TIMESTAMP WITH TIME ZONE NOT NULL,
    cpu_busy BIGINT NOT NULL,
    io_busy BIGINT NOT NULL,
    idle BIGINT NOT NULL,
    pack_received BIGINT NOT NULL,
    pack_sent BIGINT NOT NULL,
    connections BIGINT NOT NULL,
    pack_errors BIGINT NOT NULL,
    total_read BIGINT NOT NULL,
    total_write BIGINT NOT NULL,
    total_errors BIGINT NOT NULL
)
        WITH (
        OIDS=FALSE
        );



CREATE TABLE dbo.userroles(
    usersid BIGINT NOT NULL,
    rolesid BIGINT NOT NULL
)
        WITH (
        OIDS=FALSE
        );



CREATE TABLE dbo.users(
    id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY,
    username VARCHAR(255) NOT NULL,
    isdeleted boolean NOT NULL DEFAULT FALSE,
    lastlogindatetime TIMESTAMP WITH TIME ZONE,
    createdbyuserid BIGINT NOT NULL DEFAULT (8),
    createddatetime TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT '2014-04-29 00:00:00.000',
    modifiedbyuserid BIGINT,
    modifieddatetime TIMESTAMP WITH TIME ZONE,
    fullname VARCHAR(200)
)
        WITH (
        OIDS=FALSE
        );



CREATE TABLE dbo.workload(
    id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY,
    organisationalunitworkloadreportid BIGINT NOT NULL,
    trustid BIGINT,
    directorateid BIGINT,
    lduid BIGINT,
    teamid BIGINT,
    offendermanagerid BIGINT NOT NULL,
    comtier0 BIGINT NOT NULL DEFAULT (0),
    comtier0o BIGINT NOT NULL DEFAULT (0),
    comtier0w BIGINT NOT NULL DEFAULT (0),
    comtier0u BIGINT NOT NULL DEFAULT (0),
    comtier1 BIGINT NOT NULL DEFAULT (0),
    comtier1cp BIGINT NOT NULL DEFAULT (0),
    comtier1o BIGINT NOT NULL DEFAULT (0),
    comtier1w BIGINT NOT NULL DEFAULT (0),
    comtier1u BIGINT NOT NULL DEFAULT (0),
    comtier2 BIGINT NOT NULL DEFAULT (0),
    comtier2o BIGINT NOT NULL DEFAULT (0),
    comtier2w BIGINT NOT NULL DEFAULT (0),
    comtier2u BIGINT NOT NULL DEFAULT (0),
    comtier3n BIGINT NOT NULL DEFAULT (0),
    comtier3no BIGINT NOT NULL DEFAULT (0),
    comtier3nw BIGINT NOT NULL DEFAULT (0),
    comtier3nu BIGINT NOT NULL DEFAULT (0),
    comtier3d BIGINT NOT NULL DEFAULT (0),
    comtier3do BIGINT NOT NULL DEFAULT (0),
    comtier3dw BIGINT NOT NULL DEFAULT (0),
    comtier3du BIGINT NOT NULL DEFAULT (0),
    comtier4 BIGINT NOT NULL DEFAULT (0),
    comtier4o BIGINT NOT NULL DEFAULT (0),
    comtier4w BIGINT NOT NULL DEFAULT (0),
    comtier4u BIGINT NOT NULL DEFAULT (0),
    custier0 BIGINT NOT NULL DEFAULT (0),
    custier0o BIGINT NOT NULL DEFAULT (0),
    custier0w BIGINT NOT NULL DEFAULT (0),
    custier0u BIGINT NOT NULL DEFAULT (0),
    custier1 BIGINT NOT NULL DEFAULT (0),
    custier1o BIGINT NOT NULL DEFAULT (0),
    custier1w BIGINT NOT NULL DEFAULT (0),
    custier1u BIGINT NOT NULL DEFAULT (0),
    custier2 BIGINT NOT NULL DEFAULT (0),
    custier2o BIGINT NOT NULL DEFAULT (0),
    custier2w BIGINT NOT NULL DEFAULT (0),
    custier2u BIGINT NOT NULL DEFAULT (0),
    custier3 BIGINT NOT NULL DEFAULT (0),
    custier3o BIGINT NOT NULL DEFAULT (0),
    custier3w BIGINT NOT NULL DEFAULT (0),
    custier3u BIGINT NOT NULL DEFAULT (0),
    custier4 BIGINT NOT NULL DEFAULT (0),
    custier4o BIGINT NOT NULL DEFAULT (0),
    custier4w BIGINT NOT NULL DEFAULT (0),
    custier4u BIGINT NOT NULL DEFAULT (0),
    totalcases BIGINT NOT NULL DEFAULT (0),
    totalcasesinactive BIGINT NOT NULL DEFAULT (0),
    contractedhoursperweek NUMERIC(8,2),
    hoursreduction NUMERIC(8,2),
    availability BIGINT NOT NULL DEFAULT (100),
    monthlysdrs BIGINT NOT NULL DEFAULT (0),
    sdrduenext30days BIGINT NOT NULL DEFAULT (0),
    sdrconversionslast30days BIGINT NOT NULL DEFAULT (0),
    activewarrants BIGINT NOT NULL DEFAULT (0),
    overdueterminations BIGINT NOT NULL DEFAULT (0),
    upw BIGINT NOT NULL DEFAULT (0),
    ordercount BIGINT NOT NULL DEFAULT (0),
    totalpoints BIGINT NOT NULL DEFAULT (0),
    sdrpoints BIGINT NOT NULL DEFAULT (0),
    sdrconversionpoints BIGINT NOT NULL DEFAULT (0),
    nominaltarget BIGINT NOT NULL DEFAULT (0),
    currentteam boolean DEFAULT FALSE,
    isdeleted boolean NOT NULL DEFAULT FALSE,
    totalcasesppo BIGINT,
    paromscompletedlast30days BIGINT,
    paromsduenext30days BIGINT,
    paromspoints BIGINT NOT NULL,
    commappal1 BIGINT NOT NULL DEFAULT (0),
    commappal2 BIGINT NOT NULL DEFAULT (0),
    commappal3 BIGINT NOT NULL DEFAULT (0),
    cusmappal1 BIGINT NOT NULL DEFAULT (0),
    cusmappal2 BIGINT NOT NULL DEFAULT (0),
    cusmappal3 BIGINT NOT NULL DEFAULT (0)
)
        WITH (
        OIDS=FALSE
        );



CREATE TABLE dbo.workloadpoints(
    id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY,
    comtier1 NUMERIC(3,0) NOT NULL DEFAULT (0),
    comtier1cp NUMERIC(3,0) NOT NULL DEFAULT (0),
    comtier1cpenabled boolean NOT NULL DEFAULT FALSE,
    comtier2 NUMERIC(3,0) NOT NULL DEFAULT (0),
    comtier3n NUMERIC(3,0) NOT NULL DEFAULT (0),
    comtier3d NUMERIC(3,0) NOT NULL DEFAULT (0),
    comtier4 NUMERIC(3,0) NOT NULL DEFAULT (0),
    custier1 NUMERIC(3,0) NOT NULL DEFAULT (0),
    custier2 NUMERIC(3,0) NOT NULL DEFAULT (0),
    custier3 NUMERIC(3,0) NOT NULL DEFAULT (0),
    custier4 NUMERIC(3,0) NOT NULL DEFAULT (0),
    weightingo NUMERIC(5,2) NOT NULL DEFAULT (0),
    weightingw NUMERIC(5,2) NOT NULL DEFAULT (0),
    weightingu NUMERIC(5,2) NOT NULL DEFAULT (0),
    sdr BIGINT NOT NULL DEFAULT (0),
    sdrconversions BIGINT NOT NULL DEFAULT (0),
    nominaltarget BIGINT NOT NULL DEFAULT (0),
    nominaltargetpso BIGINT NOT NULL DEFAULT (0),
    defaultcontractedhours DOUBLE PRECISION NOT NULL DEFAULT (0),
    defaultcontractedhourspso DOUBLE PRECISION NOT NULL DEFAULT (0),
    isdeleted boolean NOT NULL DEFAULT FALSE,
    comtier3denabled boolean NOT NULL DEFAULT FALSE,
    parom BIGINT NOT NULL,
    paromenabled boolean NOT NULL DEFAULT FALSE,
    createdbyuserid BIGINT NOT NULL,
    createddatetime TIMESTAMP WITH TIME ZONE NOT NULL,
    commappal1 NUMERIC(3,0) NOT NULL DEFAULT (0),
    commappal2 NUMERIC(3,0) NOT NULL DEFAULT (0),
    commappal3 NUMERIC(3,0) NOT NULL DEFAULT (0),
    cusmappal1 NUMERIC(3,0) NOT NULL DEFAULT (0),
    cusmappal2 NUMERIC(3,0) NOT NULL DEFAULT (0),
    cusmappal3 NUMERIC(3,0) NOT NULL DEFAULT (0)
)
        WITH (
        OIDS=FALSE
        );



CREATE TABLE dbo.workloadreport(
    id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY,
    date TIMESTAMP WITH TIME ZONE NOT NULL,
    workloadpointsid BIGINT NOT NULL,
    isdeleted boolean NOT NULL DEFAULT FALSE
)
        WITH (
        OIDS=FALSE
        );


-- ------------ Write CREATE-FUNCTION-stage scripts -----------

CREATE OR REPLACE FUNCTION dbo.availablepoints(
     IN par_nominalpoints NUMERIC,
     IN par_offendermanagertypeid NUMERIC,
     IN par_contractedhours DOUBLE PRECISION,
     IN par_reducedhours DOUBLE PRECISION,
     IN par_podefaulthours DOUBLE PRECISION,
     IN par_psodefaulthours DOUBLE PRECISION)
RETURNS DOUBLE PRECISION
AS
$BODY$
BEGIN
    RETURN COALESCE((par_NominalPoints * (CASE
        WHEN par_OffenderManagerTypeId IN (4, 5) THEN 0
        ELSE (COALESCE(par_ContractedHours,
        CASE
            WHEN par_OffenderManagerTypeId IN (2, 3, 7) THEN par_PSODefaultHours
            ELSE par_PODefaultHours
        END))
    END /
    CASE
        WHEN par_OffenderManagerTypeId IN (2, 3, 7) THEN par_PSODefaultHours
        ELSE par_PODefaultHours
    END)) * ((CASE
        WHEN par_OffenderManagerTypeId IN (4, 5) THEN 0
        ELSE (COALESCE(par_ContractedHours,
        CASE
            WHEN par_OffenderManagerTypeId IN (2, 3, 7) THEN par_PSODefaultHours
            ELSE par_PODefaultHours
        END))
    END -
    CASE
        WHEN par_OffenderManagerTypeId IN (4, 5) THEN 0
        ELSE (COALESCE(par_ReducedHours, 0))
    END) / NULLIF(CASE
        WHEN par_OffenderManagerTypeId IN (4, 5) THEN 0
        ELSE (COALESCE(par_ContractedHours,
        CASE
            WHEN par_OffenderManagerTypeId IN (2, 3, 7) THEN par_PSODefaultHours
            ELSE par_PODefaultHours
        END))
    END, 0)), 0);
END;
/* [dbo].[CapacityPointsPerc] source */
$BODY$
LANGUAGE  plpgsql;

CREATE OR REPLACE FUNCTION dbo.calccaseloadpoavg(
     IN par_lower NUMERIC,
     IN par_upper NUMERIC,
     IN par_months NUMERIC)
RETURNS NUMERIC
AS
$BODY$
DECLARE
    var_Result NUMERIC(10, 0);
BEGIN
    WITH caseload
    AS (SELECT
        offendermanagerid, SUM(totalcases) AS totalcases, SUM(totalcasesinactive) AS totalcasesinactive
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE date BETWEEN dbo.getfirstdayofmonth(timezone('utc', now()) + (par_months::NUMERIC || ' MONTH')::INTERVAL) AND dbo.getlastdayofmonth(timezone('utc', now()) + (par_months::NUMERIC || ' MONTH')::INTERVAL)) AND offendermanagertypeid IN (1, 6)
        GROUP BY offendermanagerid, workloadreportid), list
    AS (SELECT
        offendermanagerid, AVG(totalcases - totalcasesinactive) AS avgcaseload
        FROM caseload
        GROUP BY offendermanagerid)
    SELECT
        COUNT('x')
        INTO var_Result
        FROM list
        WHERE avgcaseload BETWEEN par_lower AND par_upper;
    RETURN var_Result;
END;
/* dbo.CapacityReportDirectorate source */
$BODY$
LANGUAGE  plpgsql;



CREATE OR REPLACE FUNCTION dbo.calccaseloadpsoavg(
     IN par_lower NUMERIC,
     IN par_upper NUMERIC,
     IN par_months NUMERIC)
RETURNS NUMERIC
AS
$BODY$
DECLARE
    var_Result NUMERIC(10, 0);
BEGIN
    WITH caseload
    AS (SELECT
        offendermanagerid, SUM(totalcases) AS totalcases, SUM(totalcasesinactive) AS totalcasesinactive
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE date BETWEEN dbo.getfirstdayofmonth(timezone('utc', now()) + (par_months::NUMERIC || ' MONTH')::INTERVAL) AND dbo.getlastdayofmonth(timezone('utc', now()) + (par_months::NUMERIC || ' MONTH')::INTERVAL)) AND offendermanagertypeid IN (2, 7)
        GROUP BY offendermanagerid, workloadreportid), list
    AS (SELECT
        offendermanagerid, AVG(totalcases - totalcasesinactive) AS avgcaseload
        FROM caseload
        GROUP BY offendermanagerid)
    SELECT
        COUNT('x')
        INTO var_Result
        FROM list
        WHERE avgcaseload BETWEEN par_lower AND par_upper;
    RETURN var_Result;
END;
/* dbo.QAPCaseloadPSOStageAvg1 source */
$BODY$
LANGUAGE  plpgsql;



CREATE OR REPLACE FUNCTION dbo.capacityom(
     IN par_contractedhoursperweek DOUBLE PRECISION,
     IN par_calchoursreduced DOUBLE PRECISION)
RETURNS DOUBLE PRECISION
AS
$BODY$
BEGIN
    RETURN ROUND(CASE
        WHEN par_ContractedHoursPerWeek > 0 AND par_CalcHoursReduced < par_ContractedHoursPerWeek THEN ((par_ContractedHoursPerWeek - par_CalcHoursReduced) / NULLIF(par_ContractedHoursPerWeek, 0)) * 100
        ELSE 0
    END, 0);
END;
/* [dbo].[CapacityPoints] source */
$BODY$
LANGUAGE  plpgsql;



CREATE OR REPLACE FUNCTION dbo.capacitypoints(
     IN par_availablepoints DOUBLE PRECISION,
     IN par_points NUMERIC,
     IN par_pointssdrs NUMERIC,
     IN par_pointssdrconverions NUMERIC,
     IN par_pointsparoms NUMERIC,
     IN par_pointsrequirements NUMERIC)
RETURNS DOUBLE PRECISION
AS
$BODY$
BEGIN
    RETURN ROUND(par_AvailablePoints - (par_Points + par_PointsSDRs + par_PointsSDRConverions + par_PointsPAROMS + par_PointsRequirements), 0);
END;
/* dbo.WorkloadReportOfficer source */
$BODY$
LANGUAGE  plpgsql;



CREATE OR REPLACE FUNCTION dbo.capacitypointsperc(
     IN par_availablepoints DOUBLE PRECISION,
     IN par_points NUMERIC,
     IN par_pointssdrs NUMERIC,
     IN par_pointssdrconversions NUMERIC,
     IN par_pointsparoms NUMERIC,
     IN par_pointsrequirements NUMERIC)
RETURNS DOUBLE PRECISION
AS
$BODY$
BEGIN
    RETURN ROUND(COALESCE((par_Points + par_PointsSDRs + par_PointsSDRConversions + par_PointsPAROMS + par_PointsRequirements) / NULLIF(par_AvailablePoints, 0) * 100, 0), 0);
END;
/* [dbo].[CapacityOM] source */
$BODY$
LANGUAGE  plpgsql;



CREATE OR REPLACE FUNCTION dbo.getfirstdayofmonth(
     IN par_indate TIMESTAMP WITH TIME ZONE)
RETURNS TIMESTAMP WITH TIME ZONE
AS
$BODY$
DECLARE
    var_OutDate TIMESTAMP WITH TIME ZONE;
BEGIN
    SELECT
        0 + ((DATE_PART('year', par_InDate::TIMESTAMP) - DATE_PART('year', 0::TIMESTAMP)) * 12 + (DATE_PART('month', par_InDate::TIMESTAMP) - DATE_PART('month', 0::TIMESTAMP))::NUMERIC || ' MONTH')::INTERVAL
        INTO var_OutDate;
    RETURN var_OutDate;
END;
$BODY$
LANGUAGE  plpgsql;



CREATE OR REPLACE FUNCTION dbo.getlastdayofmonth(
     IN par_indate TIMESTAMP WITH TIME ZONE)
RETURNS TIMESTAMP WITH TIME ZONE
AS
$BODY$
DECLARE
    var_OutDate TIMESTAMP WITH TIME ZONE;
BEGIN
    SELECT
        0 + ((DATE_PART('year', par_InDate::TIMESTAMP) - DATE_PART('year', 0::TIMESTAMP)) * 12 + (DATE_PART('month', par_InDate::TIMESTAMP) - DATE_PART('month', 0::TIMESTAMP)) + 1::NUMERIC || ' MONTH')::INTERVAL + (- 3::NUMERIC || ' MILLISECOND')::INTERVAL
        INTO var_OutDate;
    RETURN var_OutDate;
END;
$BODY$
LANGUAGE  plpgsql;



CREATE OR REPLACE FUNCTION dbo.getomt3casescount(
     IN par_requestedomtypeid NUMERIC,
     IN par_omtypeid NUMERIC,
     IN par_comcases NUMERIC,
     IN par_comcaseso NUMERIC,
     IN par_comcasesw NUMERIC,
     IN par_comcasesu NUMERIC,
     IN par_comcasesd NUMERIC,
     IN par_comcasesdo NUMERIC,
     IN par_comcasesdw NUMERIC,
     IN par_comcasesdu NUMERIC,
     IN par_cuscases NUMERIC,
     IN par_cuscaseso NUMERIC,
     IN par_cuscasesw NUMERIC,
     IN par_cuscasesu NUMERIC)
RETURNS NUMERIC
AS
$BODY$
BEGIN
    /* IF (@RequestedOMTypeId <> @OMTypeId) BEGIN RETURN 0 END */
    IF ((par_RequestedOMTypeId = 2 AND par_OMTypeId IN (2, 7)) OR (par_RequestedOMTypeId <> 2 AND par_OMTypeId NOT IN (2, 7))) THEN
        RETURN (par_comCases - (par_comCasesO + par_comCasesW + par_comCasesU)) + (par_comCasesD - (par_comCasesDO + par_comCasesDW + par_comCasesDU)) + (par_cusCases - (par_cusCasesO + par_cusCasesW + par_cusCasesU));
    END IF;
    RETURN 0;
END;
$BODY$
LANGUAGE  plpgsql;



CREATE OR REPLACE FUNCTION dbo.getomtcasescount(
     IN par_requestedomtypeid NUMERIC,
     IN par_omtypeid NUMERIC,
     IN par_comcases NUMERIC,
     IN par_comcaseso NUMERIC,
     IN par_comcasesw NUMERIC,
     IN par_comcasesu NUMERIC,
     IN par_cuscases NUMERIC,
     IN par_cuscaseso NUMERIC,
     IN par_cuscasesw NUMERIC,
     IN par_cuscasesu NUMERIC)
RETURNS NUMERIC
AS
$BODY$
BEGIN
    /* IF (@RequestedOMTypeId <> @OMTypeId) BEGIN RETURN 0 END */
    IF ((par_RequestedOMTypeId = 2 AND par_OMTypeId IN (2, 7)) OR (par_RequestedOMTypeId <> 2 AND par_OMTypeId NOT IN (2, 7))) THEN
        RETURN (par_comCases - (par_comCasesO + par_comCasesW + par_comCasesU)) + (par_cusCases - (par_cusCasesO + par_cusCasesW + par_cusCasesU));
    END IF;
    RETURN 0;
END;
/* [dbo].[GetOMT3CasesCount] source */
$BODY$
LANGUAGE  plpgsql;

CREATE OR REPLACE VIEW dbo.workloadreportofficer (workloadid, workloadreportid, workloadreportdate, directorateid, lduid, lduname, teamid, teamname, offendermanagerid, offendermanagername, offendermanagernotes, offendermanagernotescreatedby, offendermanagernotesdatetime, offendermanagertypeid, offendermanagerupdateusername, offendermanagerupdatedatetime, comtier0, comtier0o, comtier0w, comtier0u, comtier1, comtier1cp, comtier1o, comtier1w, comtier1u, comtier2, comtier2o, comtier2w, comtier2u, comtier3n, comtier3no, comtier3nw, comtier3nu, comtier3d, comtier3do, comtier3dw, comtier3du, comtier4, comtier4o, comtier4w, comtier4u, custier0, custier0o, custier0w, custier0u, custier1, custier1o, custier1w, custier1u, custier2, custier2o, custier2w, custier2u, custier3, custier3o, custier3w, custier3u, custier4, custier4o, custier4w, custier4u, activewarrants, overdueterminations, upw, ordercount, totalcases, totalcasesinactive, totalcasesppo, commappal1, commappal2, commappal3, cusmappal1, cusmappal2, cusmappal3, monthlysdrs, sdrduenext30days, sdrconversionslast30days, paromsduenext30days, paromscompletedlast30days, contractedhoursperweek, hoursreduction, totalpoints, sdrpoints, sdrconversionpoints, paromspoints, requirementspoints, requirementscount, availablepoints, capacityorgunit, capacitypoints, capacitypercentage, capacitypercentagecases, capacitypercentagereports, capacitypercentagerequirements, nominaltarget, defaultcontractedhours, defaultcontractedhourspso) AS
SELECT
    wl.id AS workloadid, wr.id AS workloadreportid, wr.date AS workloadreportdate, directorate.id AS directorateid, ldu.id AS lduid, ldu.name AS lduname, team.id AS teamid, team.name AS teamname, om.id AS offendermanagerid, om.forename || ' ' || om.surname AS offendermanagername, n.notes AS offendermanagernotes, n.createdby AS offendermanagernotescreatedby, n.lastupdatedatetime AS offendermanagernotesdatetime, om.offendermanagertypeid, u.username AS offendermanagerupdateusername, om.lastupdatedatetime AS offendermanagerupdatedatetime, wl.comtier0, wl.comtier0o, wl.comtier0w, wl.comtier0u, wl.comtier1, wl.comtier1cp, wl.comtier1o, wl.comtier1w, wl.comtier1u, wl.comtier2, wl.comtier2o, wl.comtier2w, wl.comtier2u, wl.comtier3n, wl.comtier3no, wl.comtier3nw, wl.comtier3nu, wl.comtier3d, wl.comtier3do, wl.comtier3dw, wl.comtier3du, wl.comtier4, wl.comtier4o, wl.comtier4w, wl.comtier4u, wl.custier0, wl.custier0o, wl.custier0w, wl.custier0u, wl.custier1, wl.custier1o, wl.custier1w, wl.custier1u, wl.custier2, wl.custier2o, wl.custier2w, wl.custier2u, wl.custier3, wl.custier3o, wl.custier3w, wl.custier3u, wl.custier4, wl.custier4o, wl.custier4w, wl.custier4u, wl.activewarrants, wl.overdueterminations, wl.upw, wl.ordercount, wl.totalcases, wl.totalcasesinactive, wl.totalcasesppo AS totalcasesppo, wl.commappal1 AS commappal1, wl.commappal2 AS commappal2, wl.commappal3 AS commappal3, wl.cusmappal1 AS cusmappal1, wl.cusmappal2 AS cusmappal2, wl.cusmappal3 AS cusmappal3, wl.monthlysdrs, wl.sdrduenext30days, wl.sdrconversionslast30days, wl.paromsduenext30days, wl.paromscompletedlast30days, wl.contractedhoursperweek, wl.hoursreduction, wl.totalpoints, wl.sdrpoints, wl.sdrconversionpoints, wl.paromspoints, SUM(COALESCE(rw.points, 0)) AS requirementspoints, SUM(COALESCE(rw.count, 0)) AS requirementscount, dbo.availablepoints(wl.nominaltarget, om.offendermanagertypeid, wl.contractedhoursperweek, wl.hoursreduction, wp.defaultcontractedhours, wp.defaultcontractedhourspso) AS availablepoints, dbo.capacityom(wl.contractedhoursperweek, wl.hoursreduction) AS capacityorgunit, dbo.capacitypoints(dbo.availablepoints(wl.nominaltarget, om.offendermanagertypeid, wl.contractedhoursperweek, wl.hoursreduction, wp.defaultcontractedhours, wp.defaultcontractedhourspso), wl.totalpoints, wl.sdrpoints, wl.sdrconversionpoints, wl.paromspoints, SUM(COALESCE(rw.points, 0))) AS capacitypoints, dbo.capacitypointsperc(dbo.availablepoints(wl.nominaltarget, om.offendermanagertypeid, wl.contractedhoursperweek, wl.hoursreduction, wp.defaultcontractedhours, wp.defaultcontractedhourspso), wl.totalpoints, wl.sdrpoints, wl.sdrconversionpoints, wl.paromspoints, SUM(COALESCE(rw.points, 0))) AS capacitypercentage, dbo.capacitypointsperc(dbo.availablepoints(wl.nominaltarget, om.offendermanagertypeid, wl.contractedhoursperweek, wl.hoursreduction, wp.defaultcontractedhours, wp.defaultcontractedhourspso), wl.totalpoints, 0, 0, 0, 0) AS capacitypercentagecases, dbo.capacitypointsperc(dbo.availablepoints(wl.nominaltarget, om.offendermanagertypeid, wl.contractedhoursperweek, wl.hoursreduction, wp.defaultcontractedhours, wp.defaultcontractedhourspso), 0, wl.sdrpoints, wl.sdrconversionpoints, wl.paromspoints, 0) AS capacitypercentagereports, dbo.capacitypointsperc(SUM(dbo.availablepoints(wl.nominaltarget, om.offendermanagertypeid, wl.contractedhoursperweek, wl.hoursreduction, wp.defaultcontractedhours, wp.defaultcontractedhourspso)), 0, 0, 0, 0, SUM(COALESCE(rw.points, 0))) AS capacitypercentagerequirements, wl.nominaltarget, wp.defaultcontractedhours, wp.defaultcontractedhourspso
    FROM dbo.workload AS wl
    INNER JOIN dbo.organisationalunit AS team
        ON team.id = wl.teamid
    INNER JOIN dbo.organisationalunit AS ldu
        ON ldu.id = team.parentorganisationalunitid
    INNER JOIN dbo.organisationalunit AS directorate
        ON directorate.id = ldu.parentorganisationalunitid
    INNER JOIN dbo.organisationalunit AS trust
        ON trust.id = directorate.parentorganisationalunitid
    LEFT OUTER JOIN dbo.users AS u
        ON u.id = team.lastupdateuserid
    INNER JOIN dbo.offendermanager AS om
        ON om.id = wl.offendermanagerid
    INNER JOIN dbo.organisationalunitworkloadreport AS owr
        ON owr.id = wl.organisationalunitworkloadreportid
    INNER JOIN dbo.workloadreport AS wr
        ON wr.id = owr.workloadreportid
    LEFT OUTER JOIN (SELECT
        defaultcontractedhours, defaultcontractedhourspso
        FROM dbo.workloadpoints
        WHERE isdeleted = FALSE
        ORDER BY createddatetime DESC NULLS FIRST
        LIMIT 1) AS wp
        ON 1 = 1
    LEFT OUTER JOIN (SELECT
        m.id, offendermanagerid, offendermanagerparentid, lastupdatedatetime, notes, u.username AS createdby
        FROM (SELECT
            id, offendermanagerid, offendermanagerparentid, lastupdatedatetime, notes, lastupdateuserid, ROW_NUMBER() OVER (PARTITION BY offendermanagerid, offendermanagerparentid ORDER BY lastupdatedatetime) AS n
            FROM dbo.note
            WHERE offendermanagerid IS NOT NULL) AS m
        LEFT OUTER JOIN dbo.users AS u
            ON m.lastupdateuserid = u.id
        WHERE n = 1) AS n
        ON n.offendermanagerid = om.id AND n.offendermanagerparentid = team.id
    LEFT OUTER JOIN dbo.requirementworkload AS rw
        ON rw.workloadid = wl.id
    GROUP BY wl.id, wr.id, wr.date, directorate.id, ldu.id, ldu.name, team.id, team.name, om.id, om.forename || ' ' || om.surname, n.notes, n.createdby, n.lastupdatedatetime, om.offendermanagertypeid, u.username, om.lastupdatedatetime, wl.comtier0, wl.comtier0o, wl.comtier0w, wl.comtier0u, wl.comtier1, wl.comtier1cp, wl.comtier1o, wl.comtier1w, wl.comtier1u, wl.comtier2, wl.comtier2o, wl.comtier2w, wl.comtier2u, wl.comtier3n, wl.comtier3no, wl.comtier3nw, wl.comtier3nu, wl.comtier3d, wl.comtier3do, wl.comtier3dw, wl.comtier3du, wl.comtier4, wl.comtier4o, wl.comtier4w, wl.comtier4u, wl.custier0, wl.custier0o, wl.custier0w, wl.custier0u, wl.custier1, wl.custier1o, wl.custier1w, wl.custier1u, wl.custier2, wl.custier2o, wl.custier2w, wl.custier2u, wl.custier3, wl.custier3o, wl.custier3w, wl.custier3u, wl.custier4, wl.custier4o, wl.custier4w, wl.custier4u, wl.activewarrants, wl.overdueterminations, wl.upw, wl.ordercount, wl.totalcases, wl.totalcasesinactive, wl.totalcasesppo, wl.commappal1, wl.commappal2, wl.commappal3, wl.cusmappal1, wl.cusmappal2, wl.cusmappal3, wl.monthlysdrs, wl.sdrduenext30days, wl.sdrconversionslast30days, wl.paromsduenext30days, wl.paromscompletedlast30days, wl.contractedhoursperweek, wl.hoursreduction, wl.totalpoints, wl.sdrpoints, wl.sdrconversionpoints, wl.paromspoints, wl.nominaltarget, wp.defaultcontractedhours, wp.defaultcontractedhourspso;

-- ------------ Write CREATE-VIEW-stage scripts -----------

/* dbo.offender_managers_archive_view source */;
CREATE OR REPLACE VIEW dbo.offender_managers_archive_view (om_id, workload_id, unique_identifier, om_type_id, grade, workload_ldu_id, team_name, om_forename, om_surname, total_cases, total_points, sdr_points, sdr_conversion_points, paroms_points, nominal_target, contracted_hours, hours_reduction, workload_date) AS
SELECT
    om.id AS om_id, w.id AS workload_id, om.uniqueidentifier AS unique_identifier, om.offendermanagertypeid AS om_type_id, omt.name AS grade, w.lduid AS workload_ldu_id, outeam.name AS team_name, om.forename AS om_forename, om.surname AS om_surname, w.totalcases AS total_cases, w.totalpoints AS total_points, w.sdrpoints AS sdr_points, w.sdrconversionpoints AS sdr_conversion_points, w.paromspoints AS paroms_points, w.nominaltarget AS nominal_target, w.contractedhoursperweek AS contracted_hours, w.hoursreduction AS hours_reduction, wr.date AS workload_date
    FROM dbo.offendermanager AS om
    JOIN dbo.offendermanagertype AS omt
        ON om.offendermanagertypeid = omt.id
    JOIN dbo.workload AS w
        ON om.id = w.offendermanagerid
    JOIN dbo.organisationalunitworkloadreport AS ou_wr
        ON w.organisationalunitworkloadreportid = ou_wr.id
    JOIN dbo.workloadreport AS wr
        ON ou_wr.workloadreportid = wr.id
    JOIN dbo.organisationalunit AS outeam
        ON w.teamid = outeam.id;

CREATE OR REPLACE VIEW dbo.aggregate_offender_managers_view (unique_identifier, om_id, om_type_id, grade, workload_id, workload_ldu_id, team_name, om_name, workload_date, total_cases, total_points, sdr_points, sdr_conversion_points, paroms_points, nominal_target, contracted_hours, hours_reduction) AS
SELECT
    unique_identifier AS unique_identifier, om_id AS om_id, om_type_id AS om_type_id, grade AS grade, workload_id, workload_ldu_id, team_name AS team_name, CONCAT(om_forename, ' ', om_surname) AS om_name, workload_date AS workload_date, SUM(total_cases) AS total_cases, SUM(total_points) AS total_points, SUM(sdr_points) AS sdr_points, SUM(sdr_conversion_points) AS sdr_conversion_points, SUM(paroms_points) AS paroms_points, SUM(nominal_target) AS nominal_target, SUM(contracted_hours) AS contracted_hours, SUM(hours_reduction) AS hours_reduction
    FROM dbo.offender_managers_archive_view
    GROUP BY unique_identifier, om_id, om_type_id, grade, workload_id, workload_ldu_id, team_name, om_forename, om_surname, workload_date;
/* dbo.archive_data_view source */;



CREATE OR REPLACE VIEW dbo.archive_data_view (unique_identifier, om_id, workload_id, workload_date, om_type_id, grade, ldu_name, ldu_unique_identifier, region_name, team_name, om_name, total_cases, total_points, sdr_points, sdr_conversion_points, paroms_points, nominal_target, contracted_hours, hours_reduction) AS
SELECT
    om.unique_identifier AS unique_identifier, om.om_id AS om_id, om.workload_id AS workload_id, om.workload_date AS workload_date, om.om_type_id AS om_type_id, om.grade AS grade, ouldu.name AS ldu_name, ouldu.uniqueidentifier AS ldu_unique_identifier, ouregion.name AS region_name, om.team_name AS team_name, om.om_name AS om_name, om.total_cases AS total_cases, om.total_points AS total_points, om.sdr_points AS sdr_points, om.sdr_conversion_points AS sdr_conversion_points, om.paroms_points AS paroms_points, om.nominal_target AS nominal_target, om.contracted_hours AS contracted_hours, om.hours_reduction AS hours_reduction
    FROM dbo.aggregate_offender_managers_view AS om
    JOIN dbo.organisationalunit AS ouldu
        ON om.workload_ldu_id = ouldu.id
    JOIN dbo.organisationalunit AS ouregion
        ON ouldu.parentorganisationalunitid = ouregion.id;
/* dbo.archive_reductions_view source */;



CREATE OR REPLACE VIEW dbo.archive_reductions_view (om_name, hours_reduced, comments, last_updated_date, reduction_added_by) AS
SELECT
    CONCAT(om.forename, ' ', om.surname) AS om_name, hoursreduced AS hours_reduced, n.notes AS comments, n.lastupdatedatetime AS last_updated_date, fullname AS reduction_added_by
    FROM dbo.note AS n
    LEFT OUTER JOIN dbo.offendermanager AS om
        ON n.offendermanagerid = om.id
    JOIN dbo.users AS om_creator
        ON n.lastupdateuserid = om_creator.id;
/* dbo.fortnightly_aggregate_offender_managers_view source */;



CREATE OR REPLACE VIEW dbo.capacityreportdirectorate (workloadreportid, trustid, trustname, directorateid, directoratename, workloadpoints, sdrpoints, sdrconversionpoints, paromspoints, requirementpoints, availablepoints, capacityperc, workloadpointspo, sdrpointspo, sdrconversionpointspo, paromspointspo, requirementpointspo, availablepointspo, capacitypercpo, workloadpointspso, sdrpointspso, sdrconversionpointspso, paromspointspso, requirementpointspso, availablepointspso, capacitypercpso) AS
SELECT
    wr.id AS workloadreportid, trust.id AS trustid, trust.name AS trustname, directorate.id AS directorateid, directorate.name AS directoratename, SUM(wl.totalpoints) AS workloadpoints, SUM(wl.sdrpoints) AS sdrpoints, SUM(wl.sdrconversionpoints) AS sdrconversionpoints, SUM(wl.paromspoints) AS paromspoints, SUM(COALESCE(rw.points, 0)) AS requirementpoints, SUM(dbo.availablepoints(wl.nominaltarget, om.offendermanagertypeid, wl.contractedhoursperweek, wl.hoursreduction, wp.defaultcontractedhours, wp.defaultcontractedhourspso)) AS availablepoints, dbo.capacitypointsperc(SUM(dbo.availablepoints(wl.nominaltarget, om.offendermanagertypeid, wl.contractedhoursperweek, wl.hoursreduction, wp.defaultcontractedhours, wp.defaultcontractedhourspso)), SUM(wl.totalpoints), SUM(wl.sdrpoints), SUM(wl.sdrconversionpoints), SUM(wl.paromspoints), SUM(COALESCE(rw.points, 0))) AS capacityperc, SUM(wl.totalpoints *
    CASE
        WHEN om.offendermanagertypeid NOT IN (2, 7) THEN 1
        ELSE 0
    END) AS workloadpointspo, SUM(wl.sdrpoints *
    CASE
        WHEN om.offendermanagertypeid NOT IN (2, 7) THEN 1
        ELSE 0
    END) AS sdrpointspo, SUM(wl.sdrconversionpoints *
    CASE
        WHEN om.offendermanagertypeid NOT IN (2, 7) THEN 1
        ELSE 0
    END) AS sdrconversionpointspo, SUM(wl.paromspoints *
    CASE
        WHEN om.offendermanagertypeid NOT IN (2, 7) THEN 1
        ELSE 0
    END) AS paromspointspo, SUM(COALESCE(rw.points, 0) *
    CASE
        WHEN om.offendermanagertypeid NOT IN (2, 7) THEN 1
        ELSE 0
    END) AS requirementpointspo, SUM(dbo.availablepoints(wl.nominaltarget, om.offendermanagertypeid, wl.contractedhoursperweek, wl.hoursreduction, wp.defaultcontractedhours, wp.defaultcontractedhourspso) *
    CASE
        WHEN om.offendermanagertypeid NOT IN (2, 7) THEN 1
        ELSE 0
    END) AS availablepointspo, dbo.capacitypointsperc(SUM(dbo.availablepoints(wl.nominaltarget, om.offendermanagertypeid, wl.contractedhoursperweek, wl.hoursreduction, wp.defaultcontractedhours, wp.defaultcontractedhourspso) *
    CASE
        WHEN om.offendermanagertypeid != 2 THEN 1
        ELSE 0
    END), SUM(wl.totalpoints *
    CASE
        WHEN om.offendermanagertypeid NOT IN (2, 7) THEN 1
        ELSE 0
    END), SUM(wl.sdrpoints *
    CASE
        WHEN om.offendermanagertypeid NOT IN (2, 7) THEN 1
        ELSE 0
    END), SUM(wl.sdrconversionpoints *
    CASE
        WHEN om.offendermanagertypeid NOT IN (2, 7) THEN 1
        ELSE 0
    END), SUM(wl.paromspoints *
    CASE
        WHEN om.offendermanagertypeid NOT IN (2, 7) THEN 1
        ELSE 0
    END), SUM(COALESCE(rw.points, 0) *
    CASE
        WHEN om.offendermanagertypeid NOT IN (2, 7) THEN 1
        ELSE 0
    END)) AS capacitypercpo, SUM(wl.totalpoints *
    CASE
        WHEN om.offendermanagertypeid IN (2, 7) THEN 1
        ELSE 0
    END) AS workloadpointspso, SUM(wl.sdrpoints *
    CASE
        WHEN om.offendermanagertypeid IN (2, 7) THEN 1
        ELSE 0
    END) AS sdrpointspso, SUM(wl.sdrconversionpoints *
    CASE
        WHEN om.offendermanagertypeid IN (2, 7) THEN 1
        ELSE 0
    END) AS sdrconversionpointspso, SUM(wl.paromspoints *
    CASE
        WHEN om.offendermanagertypeid IN (2, 7) THEN 1
        ELSE 0
    END) AS paromspointspso, SUM(COALESCE(rw.points, 0) *
    CASE
        WHEN om.offendermanagertypeid IN (2, 7) THEN 1
        ELSE 0
    END) AS requirementpointspso, SUM(dbo.availablepoints(wl.nominaltarget, om.offendermanagertypeid, wl.contractedhoursperweek, wl.hoursreduction, wp.defaultcontractedhours, wp.defaultcontractedhourspso) *
    CASE
        WHEN om.offendermanagertypeid IN (2, 7) THEN 1
        ELSE 0
    END) AS availablepointspso, dbo.capacitypointsperc(SUM(dbo.availablepoints(wl.nominaltarget, om.offendermanagertypeid, wl.contractedhoursperweek, wl.hoursreduction, wp.defaultcontractedhours, wp.defaultcontractedhourspso) *
    CASE
        WHEN om.offendermanagertypeid IN (2, 7) THEN 1
        ELSE 0
    END), SUM(wl.totalpoints *
    CASE
        WHEN om.offendermanagertypeid IN (2, 7) THEN 1
        ELSE 0
    END), SUM(wl.sdrpoints *
    CASE
        WHEN om.offendermanagertypeid IN (2, 7) THEN 1
        ELSE 0
    END), SUM(wl.sdrconversionpoints *
    CASE
        WHEN om.offendermanagertypeid = 2 THEN 1
        ELSE 0
    END), SUM(wl.paromspoints *
    CASE
        WHEN om.offendermanagertypeid IN (2, 7) THEN 1
        ELSE 0
    END), SUM(COALESCE(rw.points, 0) *
    CASE
        WHEN om.offendermanagertypeid IN (2, 7) THEN 1
        ELSE 0
    END)) AS capacitypercpso
    FROM dbo.workload AS wl
    INNER JOIN dbo.organisationalunit AS team
        ON team.id = wl.teamid
    INNER JOIN dbo.organisationalunit AS ldu
        ON ldu.id = team.parentorganisationalunitid
    INNER JOIN dbo.organisationalunit AS directorate
        ON directorate.id = ldu.parentorganisationalunitid
    INNER JOIN dbo.organisationalunit AS trust
        ON trust.id = directorate.parentorganisationalunitid
    INNER JOIN dbo.offendermanager AS om
        ON om.id = wl.offendermanagerid
    INNER JOIN dbo.organisationalunitworkloadreport AS owr
        ON owr.id = wl.organisationalunitworkloadreportid
    INNER JOIN dbo.workloadreport AS wr
        ON wr.id = owr.workloadreportid
    LEFT OUTER JOIN (SELECT
        defaultcontractedhours, defaultcontractedhourspso
        FROM dbo.workloadpoints
        WHERE isdeleted = FALSE
        ORDER BY createddatetime DESC NULLS FIRST
        LIMIT 1) AS wp
        ON 1 = 1
    LEFT OUTER JOIN dbo.requirementworkload AS rw
        ON rw.workloadid = wl.id
    GROUP BY wr.id, trust.id, trust.name, directorate.id, directorate.name;
/* dbo.CapacityReportLDU source */;



CREATE OR REPLACE VIEW dbo.capacityreportldu (workloadreportid, trustid, trustname, directorateid, directoratename, lduid, lduname, workloadpoints, sdrpoints, sdrconversionpoints, paromspoints, requirementpoints, availablepoints, capacityperc, workloadpointspo, sdrpointspo, sdrconversionpointspo, paromspointspo, requirementpointspo, availablepointspo, capacitypercpo, workloadpointspso, sdrpointspso, sdrconversionpointspso, paromspointspso, requirementpointspso, availablepointspso, capacitypercpso) AS
SELECT
    wr.id AS workloadreportid, trust.id AS trustid, trust.name AS trustname, directorate.id AS directorateid, directorate.name AS directoratename, ldu.id AS lduid, ldu.name AS lduname, SUM(wl.totalpoints) AS workloadpoints, SUM(wl.sdrpoints) AS sdrpoints, SUM(wl.sdrconversionpoints) AS sdrconversionpoints, SUM(wl.paromspoints) AS paromspoints, SUM(COALESCE(rw.points, 0)) AS requirementpoints, SUM(dbo.availablepoints(wl.nominaltarget, om.offendermanagertypeid, wl.contractedhoursperweek, wl.hoursreduction, wp.defaultcontractedhours, wp.defaultcontractedhourspso)) AS availablepoints, dbo.capacitypointsperc(SUM(dbo.availablepoints(wl.nominaltarget, om.offendermanagertypeid, wl.contractedhoursperweek, wl.hoursreduction, wp.defaultcontractedhours, wp.defaultcontractedhourspso)), SUM(wl.totalpoints), SUM(wl.sdrpoints), SUM(wl.sdrconversionpoints), SUM(wl.paromspoints), SUM(COALESCE(rw.points, 0))) AS capacityperc, SUM(wl.totalpoints *
    CASE
        WHEN om.offendermanagertypeid NOT IN (2, 7) THEN 1
        ELSE 0
    END) AS workloadpointspo, SUM(wl.sdrpoints *
    CASE
        WHEN om.offendermanagertypeid NOT IN (2, 7) THEN 1
        ELSE 0
    END) AS sdrpointspo, SUM(wl.sdrconversionpoints *
    CASE
        WHEN om.offendermanagertypeid NOT IN (2, 7) THEN 1
        ELSE 0
    END) AS sdrconversionpointspo, SUM(wl.paromspoints *
    CASE
        WHEN om.offendermanagertypeid NOT IN (2, 7) THEN 1
        ELSE 0
    END) AS paromspointspo, SUM(COALESCE(rw.points, 0) *
    CASE
        WHEN om.offendermanagertypeid NOT IN (2, 7) THEN 1
        ELSE 0
    END) AS requirementpointspo, SUM(dbo.availablepoints(wl.nominaltarget, om.offendermanagertypeid, wl.contractedhoursperweek, wl.hoursreduction, wp.defaultcontractedhours, wp.defaultcontractedhourspso) *
    CASE
        WHEN om.offendermanagertypeid NOT IN (2, 7) THEN 1
        ELSE 0
    END) AS availablepointspo, dbo.capacitypointsperc(SUM(dbo.availablepoints(wl.nominaltarget, om.offendermanagertypeid, wl.contractedhoursperweek, wl.hoursreduction, wp.defaultcontractedhours, wp.defaultcontractedhourspso) *
    CASE
        WHEN om.offendermanagertypeid NOT IN (2, 7) THEN 1
        ELSE 0
    END), SUM(wl.totalpoints *
    CASE
        WHEN om.offendermanagertypeid NOT IN (2, 7) THEN 1
        ELSE 0
    END), SUM(wl.sdrpoints *
    CASE
        WHEN om.offendermanagertypeid NOT IN (2, 7) THEN 1
        ELSE 0
    END), SUM(wl.sdrconversionpoints *
    CASE
        WHEN om.offendermanagertypeid NOT IN (2, 7) THEN 1
        ELSE 0
    END), SUM(wl.paromspoints *
    CASE
        WHEN om.offendermanagertypeid NOT IN (2, 7) THEN 1
        ELSE 0
    END), SUM(COALESCE(rw.points, 0) *
    CASE
        WHEN om.offendermanagertypeid NOT IN (2, 7) THEN 1
        ELSE 0
    END)) AS capacitypercpo, SUM(wl.totalpoints *
    CASE
        WHEN om.offendermanagertypeid IN (2, 7) THEN 1
        ELSE 0
    END) AS workloadpointspso, SUM(wl.sdrpoints *
    CASE
        WHEN om.offendermanagertypeid IN (2, 7) THEN 1
        ELSE 0
    END) AS sdrpointspso, SUM(wl.sdrconversionpoints *
    CASE
        WHEN om.offendermanagertypeid IN (2, 7) THEN 1
        ELSE 0
    END) AS sdrconversionpointspso, SUM(wl.paromspoints *
    CASE
        WHEN om.offendermanagertypeid IN (2, 7) THEN 1
        ELSE 0
    END) AS paromspointspso, SUM(COALESCE(rw.points, 0) *
    CASE
        WHEN om.offendermanagertypeid IN (2, 7) THEN 1
        ELSE 0
    END) AS requirementpointspso, SUM(dbo.availablepoints(wl.nominaltarget, om.offendermanagertypeid, wl.contractedhoursperweek, wl.hoursreduction, wp.defaultcontractedhours, wp.defaultcontractedhourspso) *
    CASE
        WHEN om.offendermanagertypeid IN (2, 7) THEN 1
        ELSE 0
    END) AS availablepointspso, dbo.capacitypointsperc(SUM(dbo.availablepoints(wl.nominaltarget, om.offendermanagertypeid, wl.contractedhoursperweek, wl.hoursreduction, wp.defaultcontractedhours, wp.defaultcontractedhourspso) *
    CASE
        WHEN om.offendermanagertypeid IN (2, 7) THEN 1
        ELSE 0
    END), SUM(wl.totalpoints *
    CASE
        WHEN om.offendermanagertypeid IN (2, 7) THEN 1
        ELSE 0
    END), SUM(wl.sdrpoints *
    CASE
        WHEN om.offendermanagertypeid IN (2, 7) THEN 1
        ELSE 0
    END), SUM(wl.sdrconversionpoints *
    CASE
        WHEN om.offendermanagertypeid IN (2, 7) THEN 1
        ELSE 0
    END), SUM(wl.paromspoints *
    CASE
        WHEN om.offendermanagertypeid IN (2, 7) THEN 1
        ELSE 0
    END), SUM(COALESCE(rw.points, 0) *
    CASE
        WHEN om.offendermanagertypeid IN (2, 7) THEN 1
        ELSE 0
    END)) AS capacitypercpso
    FROM dbo.workload AS wl
    INNER JOIN dbo.organisationalunit AS team
        ON team.id = wl.teamid
    INNER JOIN dbo.organisationalunit AS ldu
        ON ldu.id = team.parentorganisationalunitid
    INNER JOIN dbo.organisationalunit AS directorate
        ON directorate.id = ldu.parentorganisationalunitid
    INNER JOIN dbo.organisationalunit AS trust
        ON trust.id = directorate.parentorganisationalunitid
    INNER JOIN dbo.offendermanager AS om
        ON om.id = wl.offendermanagerid
    INNER JOIN dbo.organisationalunitworkloadreport AS owr
        ON owr.id = wl.organisationalunitworkloadreportid
    INNER JOIN dbo.workloadreport AS wr
        ON wr.id = owr.workloadreportid
    LEFT OUTER JOIN (SELECT
        defaultcontractedhours, defaultcontractedhourspso
        FROM dbo.workloadpoints
        WHERE isdeleted = FALSE
        ORDER BY createddatetime DESC NULLS FIRST
        LIMIT 1) AS wp
        ON 1 = 1
    LEFT OUTER JOIN dbo.requirementworkload AS rw
        ON rw.workloadid = wl.id
    GROUP BY wr.id, trust.id, trust.name, directorate.id, directorate.name, ldu.id, ldu.name;
/* dbo.CapacityReportTeam source */;



CREATE OR REPLACE VIEW dbo.capacityreportteam (workloadreportid, trustid, trustname, directorateid, directoratename, lduid, lduname, teamid, teamname, workloadpoints, sdrpoints, sdrconversionpoints, paromspoints, requirementpoints, availablepoints, capacityperc, workloadpointspo, sdrpointspo, sdrconversionpointspo, paromspointspo, requirementpointspo, availablepointspo, capacitypercpo, workloadpointspso, sdrpointspso, sdrconversionpointspso, paromspointspso, requirementpointspso, availablepointspso, capacitypercpso) AS
SELECT
    wr.id AS workloadreportid, trust.id AS trustid, trust.name AS trustname, directorate.id AS directorateid, directorate.name AS directoratename, ldu.id AS lduid, ldu.name AS lduname, team.id AS teamid, team.name AS teamname, SUM(wl.totalpoints) AS workloadpoints, SUM(wl.sdrpoints) AS sdrpoints, SUM(wl.sdrconversionpoints) AS sdrconversionpoints, SUM(wl.paromspoints) AS paromspoints, SUM(COALESCE(rw.points, 0)) AS requirementpoints, SUM(dbo.availablepoints(wl.nominaltarget, om.offendermanagertypeid, wl.contractedhoursperweek, wl.hoursreduction, wp.defaultcontractedhours, wp.defaultcontractedhourspso)) AS availablepoints, dbo.capacitypointsperc(SUM(dbo.availablepoints(wl.nominaltarget, om.offendermanagertypeid, wl.contractedhoursperweek, wl.hoursreduction, wp.defaultcontractedhours, wp.defaultcontractedhourspso)), SUM(wl.totalpoints), SUM(wl.sdrpoints), SUM(wl.sdrconversionpoints), SUM(wl.paromspoints), SUM(COALESCE(rw.points, 0))) AS capacityperc, SUM(wl.totalpoints *
    CASE
        WHEN om.offendermanagertypeid NOT IN (2, 7) THEN 1
        ELSE 0
    END) AS workloadpointspo, SUM(wl.sdrpoints *
    CASE
        WHEN om.offendermanagertypeid NOT IN (2, 7) THEN 1
        ELSE 0
    END) AS sdrpointspo, SUM(wl.sdrconversionpoints *
    CASE
        WHEN om.offendermanagertypeid NOT IN (2, 7) THEN 1
        ELSE 0
    END) AS sdrconversionpointspo, SUM(wl.paromspoints *
    CASE
        WHEN om.offendermanagertypeid NOT IN (2, 7) THEN 1
        ELSE 0
    END) AS paromspointspo, SUM(COALESCE(rw.points, 0) *
    CASE
        WHEN om.offendermanagertypeid NOT IN (2, 7) THEN 1
        ELSE 0
    END) AS requirementpointspo, SUM(dbo.availablepoints(wl.nominaltarget, om.offendermanagertypeid, wl.contractedhoursperweek, wl.hoursreduction, wp.defaultcontractedhours, wp.defaultcontractedhourspso) *
    CASE
        WHEN om.offendermanagertypeid NOT IN (2, 7) THEN 1
        ELSE 0
    END) AS availablepointspo, dbo.capacitypointsperc(SUM(dbo.availablepoints(wl.nominaltarget, om.offendermanagertypeid, wl.contractedhoursperweek, wl.hoursreduction, wp.defaultcontractedhours, wp.defaultcontractedhourspso) *
    CASE
        WHEN om.offendermanagertypeid NOT IN (2, 7) THEN 1
        ELSE 0
    END), SUM(wl.totalpoints *
    CASE
        WHEN om.offendermanagertypeid NOT IN (2, 7) THEN 1
        ELSE 0
    END), SUM(wl.sdrpoints *
    CASE
        WHEN om.offendermanagertypeid NOT IN (2, 7) THEN 1
        ELSE 0
    END), SUM(wl.sdrconversionpoints *
    CASE
        WHEN om.offendermanagertypeid NOT IN (2, 7) THEN 1
        ELSE 0
    END), SUM(wl.paromspoints *
    CASE
        WHEN om.offendermanagertypeid NOT IN (2, 7) THEN 1
        ELSE 0
    END), SUM(COALESCE(rw.points, 0) *
    CASE
        WHEN om.offendermanagertypeid NOT IN (2, 7) THEN 1
        ELSE 0
    END)) AS capacitypercpo, SUM(wl.totalpoints *
    CASE
        WHEN om.offendermanagertypeid IN (2, 7) THEN 1
        ELSE 0
    END) AS workloadpointspso, SUM(wl.sdrpoints *
    CASE
        WHEN om.offendermanagertypeid IN (2, 7) THEN 1
        ELSE 0
    END) AS sdrpointspso, SUM(wl.sdrconversionpoints *
    CASE
        WHEN om.offendermanagertypeid IN (2, 7) THEN 1
        ELSE 0
    END) AS sdrconversionpointspso, SUM(wl.paromspoints *
    CASE
        WHEN om.offendermanagertypeid IN (2, 7) THEN 1
        ELSE 0
    END) AS paromspointspso, SUM(COALESCE(rw.points, 0) *
    CASE
        WHEN om.offendermanagertypeid IN (2, 7) THEN 1
        ELSE 0
    END) AS requirementpointspso, SUM(dbo.availablepoints(wl.nominaltarget, om.offendermanagertypeid, wl.contractedhoursperweek, wl.hoursreduction, wp.defaultcontractedhours, wp.defaultcontractedhourspso) *
    CASE
        WHEN om.offendermanagertypeid IN (2, 7) THEN 1
        ELSE 0
    END) AS availablepointspso, dbo.capacitypointsperc(SUM(dbo.availablepoints(wl.nominaltarget, om.offendermanagertypeid, wl.contractedhoursperweek, wl.hoursreduction, wp.defaultcontractedhours, wp.defaultcontractedhourspso) *
    CASE
        WHEN om.offendermanagertypeid IN (2, 7) THEN 1
        ELSE 0
    END), SUM(wl.totalpoints *
    CASE
        WHEN om.offendermanagertypeid IN (2, 7) THEN 1
        ELSE 0
    END), SUM(wl.sdrpoints *
    CASE
        WHEN om.offendermanagertypeid IN (2, 7) THEN 1
        ELSE 0
    END), SUM(wl.sdrconversionpoints *
    CASE
        WHEN om.offendermanagertypeid IN (2, 7) THEN 1
        ELSE 0
    END), SUM(wl.paromspoints *
    CASE
        WHEN om.offendermanagertypeid IN (2, 7) THEN 1
        ELSE 0
    END), SUM(COALESCE(rw.points, 0) *
    CASE
        WHEN om.offendermanagertypeid IN (2, 7) THEN 1
        ELSE 0
    END)) AS capacitypercpso
    FROM dbo.workload AS wl
    INNER JOIN dbo.organisationalunit AS team
        ON team.id = wl.teamid
    INNER JOIN dbo.organisationalunit AS ldu
        ON ldu.id = team.parentorganisationalunitid
    INNER JOIN dbo.organisationalunit AS directorate
        ON directorate.id = ldu.parentorganisationalunitid
    INNER JOIN dbo.organisationalunit AS trust
        ON trust.id = directorate.parentorganisationalunitid
    INNER JOIN dbo.offendermanager AS om
        ON om.id = wl.offendermanagerid
    INNER JOIN dbo.organisationalunitworkloadreport AS owr
        ON owr.id = wl.organisationalunitworkloadreportid
    INNER JOIN dbo.workloadreport AS wr
        ON wr.id = owr.workloadreportid
    LEFT OUTER JOIN (SELECT
        defaultcontractedhours, defaultcontractedhourspso
        FROM dbo.workloadpoints
        WHERE isdeleted = FALSE
        ORDER BY createddatetime DESC NULLS FIRST
        LIMIT 1) AS wp
        ON 1 = 1
    LEFT OUTER JOIN dbo.requirementworkload AS rw
        ON rw.workloadid = wl.id
    GROUP BY wr.id, trust.id, trust.name, directorate.id, directorate.name, ldu.id, ldu.name, team.id, team.name;
/* dbo.CapacityReportTrust source */;



CREATE OR REPLACE VIEW dbo.capacityreporttrust (workloadreportid, trustid, trustname, workloadpoints, sdrpoints, sdrconversionpoints, paromspoints, requirementpoints, availablepoints, capacityperc, workloadpointspo, sdrpointspo, sdrconversionpointspo, paromspointspo, requirementpointspo, availablepointspo, capacitypercpo, workloadpointspso, sdrpointspso, sdrconversionpointspso, paromspointspso, requirementpointspso, availablepointspso, capacitypercpso) AS
SELECT
    wr.id AS workloadreportid, trust.id AS trustid, trust.name AS trustname, SUM(wl.totalpoints) AS workloadpoints, SUM(wl.sdrpoints) AS sdrpoints, SUM(wl.sdrconversionpoints) AS sdrconversionpoints, SUM(wl.paromspoints) AS paromspoints, SUM(COALESCE(rw.points, 0)) AS requirementpoints, SUM(dbo.availablepoints(wl.nominaltarget, om.offendermanagertypeid, wl.contractedhoursperweek, wl.hoursreduction, wp.defaultcontractedhours, wp.defaultcontractedhourspso)) AS availablepoints, dbo.capacitypointsperc(SUM(dbo.availablepoints(wl.nominaltarget, om.offendermanagertypeid, wl.contractedhoursperweek, wl.hoursreduction, wp.defaultcontractedhours, wp.defaultcontractedhourspso)), SUM(wl.totalpoints), SUM(wl.sdrpoints), SUM(wl.sdrconversionpoints), SUM(wl.paromspoints), SUM(COALESCE(rw.points, 0))) AS capacityperc, SUM(wl.totalpoints *
    CASE
        WHEN om.offendermanagertypeid NOT IN (2, 7) THEN 1
        ELSE 0
    END) AS workloadpointspo, SUM(wl.sdrpoints *
    CASE
        WHEN om.offendermanagertypeid NOT IN (2, 7) THEN 1
        ELSE 0
    END) AS sdrpointspo, SUM(wl.sdrconversionpoints *
    CASE
        WHEN om.offendermanagertypeid NOT IN (2, 7) THEN 1
        ELSE 0
    END) AS sdrconversionpointspo, SUM(wl.paromspoints *
    CASE
        WHEN om.offendermanagertypeid NOT IN (2, 7) THEN 1
        ELSE 0
    END) AS paromspointspo, SUM(COALESCE(rw.points, 0) *
    CASE
        WHEN om.offendermanagertypeid NOT IN (2, 7) THEN 1
        ELSE 0
    END) AS requirementpointspo, SUM(dbo.availablepoints(wl.nominaltarget, om.offendermanagertypeid, wl.contractedhoursperweek, wl.hoursreduction, wp.defaultcontractedhours, wp.defaultcontractedhourspso) *
    CASE
        WHEN om.offendermanagertypeid NOT IN (2, 7) THEN 1
        ELSE 0
    END) AS availablepointspo, dbo.capacitypointsperc(SUM(dbo.availablepoints(wl.nominaltarget, om.offendermanagertypeid, wl.contractedhoursperweek, wl.hoursreduction, wp.defaultcontractedhours, wp.defaultcontractedhourspso) *
    CASE
        WHEN om.offendermanagertypeid NOT IN (2, 7) THEN 1
        ELSE 0
    END), SUM(wl.totalpoints *
    CASE
        WHEN om.offendermanagertypeid NOT IN (2, 7) THEN 1
        ELSE 0
    END), SUM(wl.sdrpoints *
    CASE
        WHEN om.offendermanagertypeid NOT IN (2, 7) THEN 1
        ELSE 0
    END), SUM(wl.sdrconversionpoints *
    CASE
        WHEN om.offendermanagertypeid NOT IN (2, 7) THEN 1
        ELSE 0
    END), SUM(wl.paromspoints *
    CASE
        WHEN om.offendermanagertypeid NOT IN (2, 7) THEN 1
        ELSE 0
    END), SUM(COALESCE(rw.points, 0) *
    CASE
        WHEN om.offendermanagertypeid NOT IN (2, 7) THEN 1
        ELSE 0
    END)) AS capacitypercpo, SUM(wl.totalpoints *
    CASE
        WHEN om.offendermanagertypeid IN (2, 7) THEN 1
        ELSE 0
    END) AS workloadpointspso, SUM(wl.sdrpoints *
    CASE
        WHEN om.offendermanagertypeid IN (2, 7) THEN 1
        ELSE 0
    END) AS sdrpointspso, SUM(wl.sdrconversionpoints *
    CASE
        WHEN om.offendermanagertypeid IN (2, 7) THEN 1
        ELSE 0
    END) AS sdrconversionpointspso, SUM(wl.paromspoints *
    CASE
        WHEN om.offendermanagertypeid IN (2, 7) THEN 1
        ELSE 0
    END) AS paromspointspso, SUM(COALESCE(rw.points, 0) *
    CASE
        WHEN om.offendermanagertypeid IN (2, 7) THEN 1
        ELSE 0
    END) AS requirementpointspso, SUM(dbo.availablepoints(wl.nominaltarget, om.offendermanagertypeid, wl.contractedhoursperweek, wl.hoursreduction, wp.defaultcontractedhours, wp.defaultcontractedhourspso) *
    CASE
        WHEN om.offendermanagertypeid IN (2, 7) THEN 1
        ELSE 0
    END) AS availablepointspso, dbo.capacitypointsperc(SUM(dbo.availablepoints(wl.nominaltarget, om.offendermanagertypeid, wl.contractedhoursperweek, wl.hoursreduction, wp.defaultcontractedhours, wp.defaultcontractedhourspso) *
    CASE
        WHEN om.offendermanagertypeid IN (2, 7) THEN 1
        ELSE 0
    END), SUM(wl.totalpoints *
    CASE
        WHEN om.offendermanagertypeid IN (2, 7) THEN 1
        ELSE 0
    END), SUM(wl.sdrpoints *
    CASE
        WHEN om.offendermanagertypeid IN (2, 7) THEN 1
        ELSE 0
    END), SUM(wl.sdrconversionpoints *
    CASE
        WHEN om.offendermanagertypeid IN (2, 7) THEN 1
        ELSE 0
    END), SUM(wl.paromspoints *
    CASE
        WHEN om.offendermanagertypeid IN (2, 7) THEN 1
        ELSE 0
    END), SUM(COALESCE(rw.points, 0) *
    CASE
        WHEN om.offendermanagertypeid IN (2, 7) THEN 1
        ELSE 0
    END)) AS capacitypercpso
    FROM dbo.workload AS wl
    INNER JOIN dbo.organisationalunit AS team
        ON team.id = wl.teamid
    INNER JOIN dbo.organisationalunit AS ldu
        ON ldu.id = team.parentorganisationalunitid
    INNER JOIN dbo.organisationalunit AS directorate
        ON directorate.id = ldu.parentorganisationalunitid
    INNER JOIN dbo.organisationalunit AS trust
        ON trust.id = directorate.parentorganisationalunitid
    INNER JOIN dbo.offendermanager AS om
        ON om.id = wl.offendermanagerid
    INNER JOIN dbo.organisationalunitworkloadreport AS owr
        ON owr.id = wl.organisationalunitworkloadreportid
    INNER JOIN dbo.workloadreport AS wr
        ON wr.id = owr.workloadreportid
    LEFT OUTER JOIN (SELECT
        defaultcontractedhours, defaultcontractedhourspso
        FROM dbo.workloadpoints
        WHERE isdeleted = FALSE
        ORDER BY createddatetime DESC NULLS FIRST
        LIMIT 1) AS wp
        ON 1 = 1
    LEFT OUTER JOIN dbo.requirementworkload AS rw
        ON rw.workloadid = wl.id
    GROUP BY wr.id, trust.id, trust.name;
/* [dbo].[GetOMTCasesCount] source */;

CREATE OR REPLACE VIEW dbo.workloadreportdirectorate (workloadreportid, workloadreportdate, trustid, directorateid, directoratename, ompot1casescount, ompot2casescount, ompot3casescount, ompot4casescount, ompsot1casescount, ompsot2casescount, ompsot3casescount, ompsot4casescount, comtier0count, comtier0ocount, comtier0wcount, comtier0ucount, comtier1count, comtier1cpcount, comtier1ocount, comtier1wcount, comtier1ucount, comtier2count, comtier2ocount, comtier2wcount, comtier2ucount, comtier3count, comtier3ocount, comtier3wcount, comtier3ucount, comtier3dcount, comtier3docount, comtier3dwcount, comtier3ducount, comtier4count, comtier4ocount, comtier4wcount, comtier4ucount, custier0count, custier0ocount, custier0wcount, custier0ucount, custier1count, custier1ocount, custier1wcount, custier1ucount, custier2count, custier2ocount, custier2wcount, custier2ucount, custier3count, custier3ocount, custier3wcount, custier3ucount, custier4count, custier4ocount, custier4wcount, custier4ucount, activewarrants, overdueterminations, upw, ordercount, monthlysdrs, sdrduenext30days, sdrconversionslast30days, paromsduenext30days, paromscompletedlast30days, totalcases, totalcasesinactive, totalcasesppo, commappal1, commappal2, commappal3, cusmappal1, cusmappal2, cusmappal3, contractedhours, reducedhours, workloadpoints, sdrpoints, paromspoints, sdrconversionpoints, requirementspoints, requirementscount, availablepoints, capacityorgunit, capacitypoints, capacitypercentage, capacitypercentagecases, capacitypercentagereports, capacitypercentagerequirements) AS
WITH requirements
AS (SELECT
    wr.id AS workloadreportid, directorate.id AS directorateid, SUM(COALESCE(rw.points, 0)) AS requirementspoints, SUM(COALESCE(rw.count, 0)) AS requirementscount
    FROM dbo.requirementworkload AS rw
    INNER JOIN dbo.workload AS wl
        ON rw.workloadid = wl.id
    INNER JOIN dbo.organisationalunitworkloadreport AS owr
        ON owr.id = wl.organisationalunitworkloadreportid
    INNER JOIN dbo.workloadreport AS wr
        ON wr.id = owr.workloadreportid
    INNER JOIN dbo.organisationalunit AS team
        ON team.id = wl.teamid
    INNER JOIN dbo.organisationalunit AS ldu
        ON ldu.id = team.parentorganisationalunitid
    INNER JOIN dbo.organisationalunit AS directorate
        ON directorate.id = ldu.parentorganisationalunitid
    GROUP BY wr.id, directorate.id), orgunits
AS (SELECT
    team.id AS teamid, trust.id AS trustid, directorate.id AS directorateid, directorate.name AS directoratename
    /* , */
    /* Directorate.Notes as DirectorateNotes, */
    /* U.Username as DirectorateUpdateUsername, */
    /* Directorate.LastUpdateDateTime as DirectorateUpdateDateTime */
    FROM dbo.organisationalunit AS team
    INNER JOIN dbo.organisationalunit AS ldu
        ON ldu.id = team.parentorganisationalunitid
    INNER JOIN dbo.organisationalunit AS directorate
        ON directorate.id = ldu.parentorganisationalunitid
    INNER JOIN dbo.organisationalunit AS trust
        ON trust.id = directorate.parentorganisationalunitid
    /* left join [Users] as U on U.Id = Directorate.LastUpdateUserId */
    WHERE team.isdeleted = FALSE AND ldu.isdeleted = FALSE AND directorate.isdeleted = FALSE AND trust.isdeleted = FALSE)
SELECT
    wr.id AS workloadreportid, wr.date AS workloadreportdate, orgunits.trustid, orgunits.directorateid, orgunits.directoratename,
    /* OrgUnits.DirectorateNotes, */
    /* OrgUnits.DirectorateUpdateUsername, */
    /* OrgUnits.DirectorateUpdateDateTime, */
    SUM(dbo.getomtcasescount(1::NUMERIC(10, 0), om.offendermanagertypeid, wl.comtier1, wl.comtier1o, wl.comtier1w, wl.comtier1u, wl.custier1, wl.custier1o, wl.custier1w, wl.custier1u)) AS ompot1casescount, SUM(dbo.getomtcasescount(1::NUMERIC(10, 0), om.offendermanagertypeid, wl.comtier2, wl.comtier2o, wl.comtier2w, wl.comtier2u, wl.custier2, wl.custier2o, wl.custier2w, wl.custier2u)) AS ompot2casescount, SUM(dbo.getomt3casescount(1::NUMERIC(10, 0), om.offendermanagertypeid, wl.comtier3n, wl.comtier3no, wl.comtier3nw, wl.comtier3nu, wl.comtier3d, wl.comtier3do, wl.comtier3dw, wl.comtier3du, wl.custier3, wl.custier3o, wl.custier3w, wl.custier3u)) AS ompot3casescount, SUM(dbo.getomtcasescount(1::NUMERIC(10, 0), om.offendermanagertypeid, wl.comtier4, wl.comtier4o, wl.comtier4w, wl.comtier4u, wl.custier4, wl.custier4o, wl.custier4w, wl.custier4u)) AS ompot4casescount, SUM(dbo.getomtcasescount(2::NUMERIC(10, 0), om.offendermanagertypeid, wl.comtier1, wl.comtier1o, wl.comtier1w, wl.comtier1u, wl.custier1, wl.custier1o, wl.custier1w, wl.custier1u)) AS ompsot1casescount, SUM(dbo.getomtcasescount(2::NUMERIC(10, 0), om.offendermanagertypeid, wl.comtier2, wl.comtier2o, wl.comtier2w, wl.comtier2u, wl.custier2, wl.custier2o, wl.custier2w, wl.custier2u)) AS ompsot2casescount, SUM(dbo.getomt3casescount(2::NUMERIC(10, 0), om.offendermanagertypeid, wl.comtier3n, wl.comtier3no, wl.comtier3nw, wl.comtier3nu, wl.comtier3d, wl.comtier3do, wl.comtier3dw, wl.comtier3du, wl.custier3, wl.custier3o, wl.custier3w, wl.custier3u)) AS ompsot3casescount, SUM(dbo.getomtcasescount(2::NUMERIC(10, 0), om.offendermanagertypeid, wl.comtier4, wl.comtier4o, wl.comtier4w, wl.comtier4u, wl.custier4, wl.custier4o, wl.custier4w, wl.custier4u)) AS ompsot4casescount, SUM(wl.comtier0) AS comtier0count, SUM(wl.comtier0o) AS comtier0ocount, SUM(wl.comtier0w) AS comtier0wcount, SUM(wl.comtier0u) AS comtier0ucount, SUM(wl.comtier1) AS comtier1count, SUM(wl.comtier1cp) AS comtier1cpcount, SUM(wl.comtier1o) AS comtier1ocount, SUM(wl.comtier1w) AS comtier1wcount, SUM(wl.comtier1u) AS comtier1ucount, SUM(wl.comtier2) AS comtier2count, SUM(wl.comtier2o) AS comtier2ocount, SUM(wl.comtier2w) AS comtier2wcount, SUM(wl.comtier2u) AS comtier2ucount, SUM(wl.comtier3n) AS comtier3count, SUM(wl.comtier3no) AS comtier3ocount, SUM(wl.comtier3nw) AS comtier3wcount, SUM(wl.comtier3nu) AS comtier3ucount, SUM(wl.comtier3d) AS comtier3dcount, SUM(wl.comtier3do) AS comtier3docount, SUM(wl.comtier3dw) AS comtier3dwcount, SUM(wl.comtier3du) AS comtier3ducount, SUM(wl.comtier4) AS comtier4count, SUM(wl.comtier4o) AS comtier4ocount, SUM(wl.comtier4w) AS comtier4wcount, SUM(wl.comtier4u) AS comtier4ucount, SUM(wl.custier0) AS custier0count, SUM(wl.custier0o) AS custier0ocount, SUM(wl.custier0w) AS custier0wcount, SUM(wl.custier0u) AS custier0ucount, SUM(wl.custier1) AS custier1count, SUM(wl.custier1o) AS custier1ocount, SUM(wl.custier1w) AS custier1wcount, SUM(wl.custier1u) AS custier1ucount, SUM(wl.custier2) AS custier2count, SUM(wl.custier2o) AS custier2ocount, SUM(wl.custier2w) AS custier2wcount, SUM(wl.custier2u) AS custier2ucount, SUM(wl.custier3) AS custier3count, SUM(wl.custier3o) AS custier3ocount, SUM(wl.custier3w) AS custier3wcount, SUM(wl.custier3u) AS custier3ucount, SUM(wl.custier4) AS custier4count, SUM(wl.custier4o) AS custier4ocount, SUM(wl.custier4w) AS custier4wcount, SUM(wl.custier4u) AS custier4ucount, SUM(wl.activewarrants) AS activewarrants, SUM(wl.overdueterminations) AS overdueterminations, SUM(wl.upw) AS upw, SUM(wl.ordercount) AS ordercount, SUM(wl.monthlysdrs) AS monthlysdrs, SUM(wl.sdrduenext30days) AS sdrduenext30days, SUM(wl.sdrconversionslast30days) AS sdrconversionslast30days, SUM(wl.paromsduenext30days) AS paromsduenext30days, SUM(wl.paromscompletedlast30days) AS paromscompletedlast30days, SUM(wl.totalcases) AS totalcases, SUM(wl.totalcasesinactive) AS totalcasesinactive, SUM(wl.totalcasesppo) AS totalcasesppo, SUM(wl.commappal1) AS commappal1, SUM(wl.commappal2) AS commappal2, SUM(wl.commappal3) AS commappal3, SUM(wl.cusmappal1) AS cusmappal1, SUM(wl.cusmappal2) AS cusmappal2, SUM(wl.cusmappal3) AS cusmappal3, SUM(wl.contractedhoursperweek) AS contractedhours, SUM(wl.hoursreduction) AS reducedhours, SUM(wl.totalpoints) AS workloadpoints, SUM(wl.sdrpoints) AS sdrpoints, SUM(wl.paromspoints) AS paromspoints, SUM(wl.sdrconversionpoints) AS sdrconversionpoints, COALESCE(r.requirementspoints, 0) AS requirementspoints, COALESCE(r.requirementscount, 0) AS requirementscount, SUM(dbo.availablepoints(wl.nominaltarget, om.offendermanagertypeid, wl.contractedhoursperweek, wl.hoursreduction, wp.defaultcontractedhours, wp.defaultcontractedhourspso)) AS availablepoints, dbo.capacityom(SUM(wl.contractedhoursperweek), SUM(wl.hoursreduction)) AS capacityorgunit, dbo.capacitypoints(SUM(dbo.availablepoints(wl.nominaltarget, om.offendermanagertypeid, wl.contractedhoursperweek, wl.hoursreduction, wp.defaultcontractedhours, wp.defaultcontractedhourspso)), SUM(wl.totalpoints), SUM(wl.sdrpoints), SUM(wl.sdrconversionpoints), SUM(wl.paromspoints), COALESCE(r.requirementspoints, 0)) AS capacitypoints, dbo.capacitypointsperc(SUM(dbo.availablepoints(wl.nominaltarget, om.offendermanagertypeid, wl.contractedhoursperweek, wl.hoursreduction, wp.defaultcontractedhours, wp.defaultcontractedhourspso)), SUM(wl.totalpoints), SUM(wl.sdrpoints), SUM(wl.sdrconversionpoints), SUM(wl.paromspoints), COALESCE(r.requirementspoints, 0)) AS capacitypercentage, dbo.capacitypointsperc(SUM(dbo.availablepoints(wl.nominaltarget, om.offendermanagertypeid, wl.contractedhoursperweek, wl.hoursreduction, wp.defaultcontractedhours, wp.defaultcontractedhourspso)), SUM(wl.totalpoints), 0, 0, 0, 0) AS capacitypercentagecases, dbo.capacitypointsperc(SUM(dbo.availablepoints(wl.nominaltarget, om.offendermanagertypeid, wl.contractedhoursperweek, wl.hoursreduction, wp.defaultcontractedhours, wp.defaultcontractedhourspso)), 0, SUM(wl.sdrpoints), SUM(wl.sdrconversionpoints), SUM(wl.paromspoints), 0) AS capacitypercentagereports, dbo.capacitypointsperc(SUM(dbo.availablepoints(wl.nominaltarget, om.offendermanagertypeid, wl.contractedhoursperweek, wl.hoursreduction, wp.defaultcontractedhours, wp.defaultcontractedhourspso)), 0, 0, 0, 0, COALESCE(r.requirementspoints, 0)) AS capacitypercentagerequirements
    FROM orgunits
    INNER JOIN dbo.workload AS wl
        ON orgunits.teamid = wl.teamid
    INNER JOIN dbo.offendermanager AS om
        ON om.id = wl.offendermanagerid
    INNER JOIN dbo.organisationalunitworkloadreport AS owr
        ON owr.id = wl.organisationalunitworkloadreportid
    INNER JOIN dbo.workloadreport AS wr
        ON wr.id = owr.workloadreportid
    LEFT OUTER JOIN (SELECT
        defaultcontractedhours, defaultcontractedhourspso
        FROM dbo.workloadpoints
        WHERE isdeleted = FALSE
        ORDER BY createddatetime DESC NULLS FIRST
        LIMIT 1) AS wp
        ON 1 = 1
    LEFT OUTER JOIN requirements AS r
        ON r.workloadreportid = wr.id AND r.directorateid = orgunits.directorateid
    GROUP BY r.requirementspoints, r.requirementscount, wr.id, wr.date, orgunits.trustid, orgunits.directorateid, orgunits.directoratename;


CREATE OR REPLACE VIEW dbo.directoratecapacityhistory (workloadreportdate, directorateid, capacityorgunit, capacitypoints, capacitypercentage, capacitypercentagecases, capacitypercentagereports, capacitypercentagerequirements) AS
SELECT
    wr.date AS workloadreportdate, wld.directorateid, wld.capacityorgunit, wld.capacitypoints, wld.capacitypercentage, wld.capacitypercentagecases, wld.capacitypercentagereports, wld.capacitypercentagerequirements
    FROM dbo.workloadreportdirectorate AS wld
    INNER JOIN dbo.workloadreport AS wr
        ON wr.id = wld.workloadreportid
    WHERE wr.isdeleted = FALSE;
/* dbo.DirectorateCasesHistory source */;



CREATE OR REPLACE VIEW dbo.directoratecaseshistory (workloadreportid, workloadreportdate, directorateid, directoratename, activewarrants, upws, overdueterminations, totalcommcases, totalcustcases, totalcases, activecases, ordercount, capacitypercentage) AS
SELECT
    wr.id AS workloadreportid, wr.date AS workloadreportdate, directorateid, directoratename, SUM(activewarrants) AS activewarrants, SUM(upw) AS upws, SUM(overdueterminations) AS overdueterminations, SUM(comtier0count) + SUM(comtier1count) + SUM(CASE WHEN wp.comtier1cpenabled THEN comtier1cpcount ELSE 0 END) + SUM(comtier2count) + SUM(comtier3count) + SUM(comtier3dcount) + SUM(comtier4count) AS totalcommcases, SUM(custier0count) + SUM(custier1count) + SUM(custier2count) + SUM(custier3count) + SUM(custier4count) AS totalcustcases, SUM(totalcases) AS totalcases, SUM(totalcases) - SUM(totalcasesinactive) AS activecases, SUM(ordercount) AS ordercount, wlo.capacitypercentage
    FROM dbo.workloadreportdirectorate AS wlo
    INNER JOIN dbo.workloadreport AS wr
        ON wr.id = wlo.workloadreportid
    LEFT OUTER JOIN (SELECT
        comtier1cpenabled
        FROM dbo.workloadpoints
        WHERE isdeleted = FALSE
        ORDER BY createddatetime DESC NULLS FIRST
        LIMIT 1) AS wp
        ON 1 = 1
    WHERE wr.isdeleted = FALSE
    GROUP BY wr.id, wr.date, wlo.directorateid, wlo.directoratename, wlo.capacitypercentage, wp.comtier1cpenabled;
/* dbo.WorkloadReportLDU source */;


CREATE OR REPLACE VIEW dbo.workloadreportldu (workloadreportid, workloadreportdate, trustid, directorateid, lduid, lduname, lducode, ompot1casescount, ompot2casescount, ompot3casescount, ompot4casescount, ompsot1casescount, ompsot2casescount, ompsot3casescount, ompsot4casescount, comtier0count, comtier0ocount, comtier0wcount, comtier0ucount, comtier1count, comtier1cpcount, comtier1ocount, comtier1wcount, comtier1ucount, comtier2count, comtier2ocount, comtier2wcount, comtier2ucount, comtier3count, comtier3ocount, comtier3wcount, comtier3ucount, comtier3dcount, comtier3docount, comtier3dwcount, comtier3ducount, comtier4count, comtier4ocount, comtier4wcount, comtier4ucount, custier0count, custier0ocount, custier0wcount, custier0ucount, custier1count, custier1ocount, custier1wcount, custier1ucount, custier2count, custier2ocount, custier2wcount, custier2ucount, custier3count, custier3ocount, custier3wcount, custier3ucount, custier4count, custier4ocount, custier4wcount, custier4ucount, activewarrants, overdueterminations, upw, ordercount, totalcases, totalcasesinactive, totalcasesppo, commappal1, commappal2, commappal3, cusmappal1, cusmappal2, cusmappal3, monthlysdrs, sdrduenext30days, sdrconversionslast30days, paromsduenext30days, paromscompletedlast30days, contractedhours, reducedhours, workloadpoints, sdrpoints, paromspoints, sdrconversionpoints, requirementspoints, requirementscount, availablepoints, capacityorgunit, capacitypoints, capacitypercentage, capacitypercentagecases, capacitypercentagereports, capacitypercentagerequirements) AS
WITH requirements
AS (SELECT
    wr.id AS workloadreportid, ldu.id AS lduid, SUM(COALESCE(rw.points, 0)) AS requirementspoints, SUM(COALESCE(rw.count, 0)) AS requirementscount
    FROM dbo.requirementworkload AS rw
    INNER JOIN dbo.workload AS wl
        ON rw.workloadid = wl.id
    INNER JOIN dbo.organisationalunitworkloadreport AS owr
        ON owr.id = wl.organisationalunitworkloadreportid
    INNER JOIN dbo.workloadreport AS wr
        ON wr.id = owr.workloadreportid
    INNER JOIN dbo.organisationalunit AS team
        ON team.id = wl.teamid
    INNER JOIN dbo.organisationalunit AS ldu
        ON ldu.id = team.parentorganisationalunitid
    GROUP BY wr.id, ldu.id), orgunits
AS (SELECT
    team.id AS teamid, trust.id AS trustid, directorate.id AS directorateid, directorate.name AS directoratename, ldu.id AS lduid, ldu.name AS lduname, ldu.uniqueidentifier AS lducode
    FROM dbo.organisationalunit AS team
    INNER JOIN dbo.organisationalunit AS ldu
        ON ldu.id = team.parentorganisationalunitid
    INNER JOIN dbo.organisationalunit AS directorate
        ON directorate.id = ldu.parentorganisationalunitid
    INNER JOIN dbo.organisationalunit AS trust
        ON trust.id = directorate.parentorganisationalunitid
    /* left join [Users] as U on U.Id = Directorate.LastUpdateUserId */
    WHERE team.isdeleted = FALSE AND ldu.isdeleted = FALSE AND directorate.isdeleted = FALSE AND trust.isdeleted = FALSE)
SELECT
    wr.id AS workloadreportid, wr.date AS workloadreportdate, orgunits.trustid, orgunits.directorateid, orgunits.lduid, orgunits.lduname, orgunits.lducode,
    /* Ldu.Notes as LduNotes, */
    /* U.Username as LduUpdateUsername, */
    /* Ldu.LastUpdateDateTime as LduUpdateDateTime, */
    SUM(dbo.getomtcasescount(1::NUMERIC(10, 0), om.offendermanagertypeid, wl.comtier1, wl.comtier1o, wl.comtier1w, wl.comtier1u, wl.custier1, wl.custier1o, wl.custier1w, wl.custier1u)) AS ompot1casescount, SUM(dbo.getomtcasescount(1::NUMERIC(10, 0), om.offendermanagertypeid, wl.comtier2, wl.comtier2o, wl.comtier2w, wl.comtier2u, wl.custier2, wl.custier2o, wl.custier2w, wl.custier2u)) AS ompot2casescount, SUM(dbo.getomt3casescount(1::NUMERIC(10, 0), om.offendermanagertypeid, wl.comtier3n, wl.comtier3no, wl.comtier3nw, wl.comtier3nu, wl.comtier3d, wl.comtier3do, wl.comtier3dw, wl.comtier3du, wl.custier3, wl.custier3o, wl.custier3w, wl.custier3u)) AS ompot3casescount, SUM(dbo.getomtcasescount(1::NUMERIC(10, 0), om.offendermanagertypeid, wl.comtier4, wl.comtier4o, wl.comtier4w, wl.comtier4u, wl.custier4, wl.custier4o, wl.custier4w, wl.custier4u)) AS ompot4casescount, SUM(dbo.getomtcasescount(2::NUMERIC(10, 0), om.offendermanagertypeid, wl.comtier1, wl.comtier1o, wl.comtier1w, wl.comtier1u, wl.custier1, wl.custier1o, wl.custier1w, wl.custier1u)) AS ompsot1casescount, SUM(dbo.getomtcasescount(2::NUMERIC(10, 0), om.offendermanagertypeid, wl.comtier2, wl.comtier2o, wl.comtier2w, wl.comtier2u, wl.custier2, wl.custier2o, wl.custier2w, wl.custier2u)) AS ompsot2casescount, SUM(dbo.getomt3casescount(2::NUMERIC(10, 0), om.offendermanagertypeid, wl.comtier3n, wl.comtier3no, wl.comtier3nw, wl.comtier3nu, wl.comtier3d, wl.comtier3do, wl.comtier3dw, wl.comtier3du, wl.custier3, wl.custier3o, wl.custier3w, wl.custier3u)) AS ompsot3casescount, SUM(dbo.getomtcasescount(2::NUMERIC(10, 0), om.offendermanagertypeid, wl.comtier4, wl.comtier4o, wl.comtier4w, wl.comtier4u, wl.custier4, wl.custier4o, wl.custier4w, wl.custier4u)) AS ompsot4casescount, SUM(wl.comtier0) AS comtier0count, SUM(wl.comtier0o) AS comtier0ocount, SUM(wl.comtier0w) AS comtier0wcount, SUM(wl.comtier0u) AS comtier0ucount, SUM(wl.comtier1) AS comtier1count, SUM(wl.comtier1cp) AS comtier1cpcount, SUM(wl.comtier1o) AS comtier1ocount, SUM(wl.comtier1w) AS comtier1wcount, SUM(wl.comtier1u) AS comtier1ucount, SUM(wl.comtier2) AS comtier2count, SUM(wl.comtier2o) AS comtier2ocount, SUM(wl.comtier2w) AS comtier2wcount, SUM(wl.comtier2u) AS comtier2ucount, SUM(wl.comtier3n) AS comtier3count, SUM(wl.comtier3no) AS comtier3ocount, SUM(wl.comtier3nw) AS comtier3wcount, SUM(wl.comtier3nu) AS comtier3ucount, SUM(wl.comtier3d) AS comtier3dcount, SUM(wl.comtier3do) AS comtier3docount, SUM(wl.comtier3dw) AS comtier3dwcount, SUM(wl.comtier3du) AS comtier3ducount, SUM(wl.comtier4) AS comtier4count, SUM(wl.comtier4o) AS comtier4ocount, SUM(wl.comtier4w) AS comtier4wcount, SUM(wl.comtier4u) AS comtier4ucount, SUM(wl.custier0) AS custier0count, SUM(wl.custier0o) AS custier0ocount, SUM(wl.custier0w) AS custier0wcount, SUM(wl.custier0u) AS custier0ucount, SUM(wl.custier1) AS custier1count, SUM(wl.custier1o) AS custier1ocount, SUM(wl.custier1w) AS custier1wcount, SUM(wl.custier1u) AS custier1ucount, SUM(wl.custier2) AS custier2count, SUM(wl.custier2o) AS custier2ocount, SUM(wl.custier2w) AS custier2wcount, SUM(wl.custier2u) AS custier2ucount, SUM(wl.custier3) AS custier3count, SUM(wl.custier3o) AS custier3ocount, SUM(wl.custier3w) AS custier3wcount, SUM(wl.custier3u) AS custier3ucount, SUM(wl.custier4) AS custier4count, SUM(wl.custier4o) AS custier4ocount, SUM(wl.custier4w) AS custier4wcount, SUM(wl.custier4u) AS custier4ucount, SUM(wl.activewarrants) AS activewarrants, SUM(wl.overdueterminations) AS overdueterminations, SUM(wl.upw) AS upw, SUM(wl.ordercount) AS ordercount, SUM(wl.totalcases) AS totalcases, SUM(wl.totalcasesinactive) AS totalcasesinactive, SUM(wl.totalcasesppo) AS totalcasesppo, SUM(wl.commappal1) AS commappal1, SUM(wl.commappal2) AS commappal2, SUM(wl.commappal3) AS commappal3, SUM(wl.cusmappal1) AS cusmappal1, SUM(wl.cusmappal2) AS cusmappal2, SUM(wl.cusmappal3) AS cusmappal3, SUM(wl.monthlysdrs) AS monthlysdrs, SUM(wl.sdrduenext30days) AS sdrduenext30days, SUM(wl.sdrconversionslast30days) AS sdrconversionslast30days, SUM(wl.paromsduenext30days) AS paromsduenext30days, SUM(wl.paromscompletedlast30days) AS paromscompletedlast30days, SUM(wl.contractedhoursperweek) AS contractedhours, SUM(wl.hoursreduction) AS reducedhours, SUM(wl.totalpoints) AS workloadpoints, SUM(wl.sdrpoints) AS sdrpoints, SUM(wl.paromspoints) AS paromspoints, SUM(wl.sdrconversionpoints) AS sdrconversionpoints, COALESCE(r.requirementspoints, 0) AS requirementspoints, COALESCE(r.requirementscount, 0) AS requirementscount, SUM(dbo.availablepoints(wl.nominaltarget, om.offendermanagertypeid, wl.contractedhoursperweek, wl.hoursreduction, wp.defaultcontractedhours, wp.defaultcontractedhourspso)) AS availablepoints, dbo.capacityom(SUM(wl.contractedhoursperweek), SUM(wl.hoursreduction)) AS capacityorgunit, dbo.capacitypoints(SUM(dbo.availablepoints(wl.nominaltarget, om.offendermanagertypeid, wl.contractedhoursperweek, wl.hoursreduction, wp.defaultcontractedhours, wp.defaultcontractedhourspso)), SUM(wl.totalpoints), SUM(wl.sdrpoints), SUM(wl.sdrconversionpoints), SUM(wl.paromspoints), COALESCE(r.requirementspoints, 0)) AS capacitypoints, dbo.capacitypointsperc(SUM(dbo.availablepoints(wl.nominaltarget, om.offendermanagertypeid, wl.contractedhoursperweek, wl.hoursreduction, wp.defaultcontractedhours, wp.defaultcontractedhourspso)), SUM(wl.totalpoints), SUM(wl.sdrpoints), SUM(wl.sdrconversionpoints), SUM(wl.paromspoints), COALESCE(r.requirementspoints, 0)) AS capacitypercentage, dbo.capacitypointsperc(SUM(dbo.availablepoints(wl.nominaltarget, om.offendermanagertypeid, wl.contractedhoursperweek, wl.hoursreduction, wp.defaultcontractedhours, wp.defaultcontractedhourspso)), SUM(wl.totalpoints), 0, 0, 0, 0) AS capacitypercentagecases, dbo.capacitypointsperc(SUM(dbo.availablepoints(wl.nominaltarget, om.offendermanagertypeid, wl.contractedhoursperweek, wl.hoursreduction, wp.defaultcontractedhours, wp.defaultcontractedhourspso)), 0, SUM(wl.sdrpoints), SUM(wl.sdrconversionpoints), SUM(wl.paromspoints), 0) AS capacitypercentagereports, dbo.capacitypointsperc(SUM(dbo.availablepoints(wl.nominaltarget, om.offendermanagertypeid, wl.contractedhoursperweek, wl.hoursreduction, wp.defaultcontractedhours, wp.defaultcontractedhourspso)), 0, 0, 0, 0, COALESCE(r.requirementspoints, 0)) AS capacitypercentagerequirements
    FROM orgunits
    INNER JOIN dbo.workload AS wl
        ON wl.teamid = orgunits.teamid
    INNER JOIN
    /* inner join dbo.OrganisationalUnit as Team on Team.Id = WL.TeamId */
    /* inner join dbo.OrganisationalUnit as Ldu on Ldu.Id = Team.ParentOrganisationalUnitId */
    /* inner join dbo.OrganisationalUnit as Directorate on Directorate.Id = Ldu.ParentOrganisationalUnitId */
    /* inner join dbo.OrganisationalUnit as Trust on Trust.Id = Directorate.ParentOrganisationalUnitId */
    /* left join [Users] as U on U.Id = Ldu.LastUpdateUserId */
    dbo.offendermanager AS om
        ON om.id = wl.offendermanagerid
    INNER JOIN dbo.organisationalunitworkloadreport AS owr
        ON owr.id = wl.organisationalunitworkloadreportid
    INNER JOIN dbo.workloadreport AS wr
        ON wr.id = owr.workloadreportid
    LEFT OUTER JOIN (SELECT
        defaultcontractedhours, defaultcontractedhourspso
        FROM dbo.workloadpoints
        WHERE isdeleted = FALSE
        ORDER BY createddatetime DESC NULLS FIRST
        LIMIT 1) AS wp
        ON 1 = 1
    LEFT OUTER JOIN requirements AS r
        ON r.workloadreportid = wr.id AND r.lduid = orgunits.lduid
    GROUP BY r.requirementspoints, r.requirementscount, wr.id, wr.date, orgunits.trustid, orgunits.directorateid, orgunits.lduid, orgunits.lduname, orgunits.lducode;


CREATE OR REPLACE VIEW dbo.fortnightly_aggregate_offender_managers_view (unique_identifier, om_id, om_type_id, workload_ldu_id, team_name, fortnight, start_date, end_date, om_name, average_cases, average_points, average_sdr_points, average_sdr_conversion_points, average_paroms_points, average_nominal_target, average_contracted_hours, average_hours_reduction) AS
SELECT
    unique_identifier AS unique_identifier, om_id AS om_id, om_type_id AS om_type_id, workload_ldu_id, team_name AS team_name, ((workload_date::DATE - '2016-08-25'::DATE) / 14) AS fortnight, '2016-08-25'::DATE + (interval '1' day * (- 14 * (((workload_date::DATE - '2016-08-25'::DATE) / 14) + 1))) AS start_date, '2016-08-25'::DATE + (interval '1' day * (- 14 * (((workload_date::DATE - '2016-08-25'::DATE) / 14)))) AS end_date,
    /* , MONTH(workload_date) */
    CONCAT(om_forename, ' ', om_surname) AS om_name, AVG(total_cases) AS average_cases, AVG(total_points) AS average_points, AVG(sdr_points) AS average_sdr_points, AVG(sdr_conversion_points) AS average_sdr_conversion_points, AVG(paroms_points) AS average_paroms_points, AVG(nominal_target) AS average_nominal_target, AVG(contracted_hours) AS average_contracted_hours, AVG(hours_reduction) AS average_hours_reduction
    FROM dbo.offender_managers_archive_view
    GROUP BY unique_identifier, om_id, om_type_id, workload_ldu_id, team_name, om_forename, om_surname, ((workload_date::DATE - '2016-08-25'::DATE) / 14);
/* dbo.fortnightly_archive_data_view source */;



CREATE OR REPLACE VIEW dbo.fortnightly_archive_data_view (unique_identifier, om_id, om_type_id, start_date, end_date, ldu_name, team_name, om_name, average_cases, average_points, average_sdr_points, average_sdr_conversion_points, average_paroms_points, average_nominal_target, average_contracted_hours, average_hours_reduction) AS
SELECT
    om.unique_identifier AS unique_identifier, om.om_id AS om_id, om.om_type_id AS om_type_id, om.start_date AS start_date, om.end_date AS end_date, ouldu.name AS ldu_name, om.team_name AS team_name, om.om_name AS om_name, om.average_cases AS average_cases, om.average_points AS average_points, om.average_sdr_points AS average_sdr_points, om.average_sdr_conversion_points AS average_sdr_conversion_points, om.average_paroms_points AS average_paroms_points, om.average_nominal_target AS average_nominal_target, om.average_contracted_hours AS average_contracted_hours, om.average_hours_reduction AS average_hours_reduction
    FROM dbo.fortnightly_aggregate_offender_managers_view AS om
    JOIN dbo.organisationalunit AS ouldu
        ON om.workload_ldu_id = ouldu.id;
/* dbo.reductions_archive_view source */;



CREATE OR REPLACE VIEW dbo.lducapacityhistory (workloadreportdate, lduid, capacityorgunit, capacitypoints, capacitypercentage, capacitypercentagecases, capacitypercentagereports, capacitypercentagerequirements) AS
SELECT
    wr.date AS workloadreportdate, wll.lduid, wll.capacityorgunit, wll.capacitypoints, wll.capacitypercentage, wll.capacitypercentagecases, wll.capacitypercentagereports, wll.capacitypercentagerequirements
    FROM dbo.workloadreportldu AS wll
    INNER JOIN dbo.workloadreport AS wr
        ON wr.id = wll.workloadreportid
    WHERE wr.isdeleted = FALSE;
/* dbo.LDUCasesHistory source */;



CREATE OR REPLACE VIEW dbo.lducaseshistory (workloadreportid, workloadreportdate, lduid, lduname, activewarrants, upws, overdueterminations, totalcommcases, totalcustcases, totalcases, activecases, ordercount, capacitypercentage, directorateid) AS
SELECT
    wr.id AS workloadreportid, wr.date AS workloadreportdate, lduid, lduname, SUM(activewarrants) AS activewarrants, SUM(upw) AS upws, SUM(overdueterminations) AS overdueterminations, SUM(comtier0count) + SUM(comtier1count) + SUM(CASE WHEN wp.comtier1cpenabled THEN comtier1cpcount ELSE 0 END) + SUM(comtier2count) + SUM(comtier3count) + SUM(comtier3dcount) + SUM(comtier4count) AS totalcommcases, SUM(custier0count) + SUM(custier1count) + SUM(custier2count) + SUM(custier3count) + SUM(custier4count) AS totalcustcases, SUM(totalcases) AS totalcases, SUM(totalcases) - SUM(totalcasesinactive) AS activecases, SUM(ordercount) AS ordercount, wlo.capacitypercentage, wlo.directorateid
    FROM dbo.workloadreportldu AS wlo
    INNER JOIN dbo.workloadreport AS wr
        ON wr.id = wlo.workloadreportid
    LEFT OUTER JOIN (SELECT
        comtier1cpenabled
        FROM dbo.workloadpoints
        WHERE isdeleted = FALSE
        ORDER BY createddatetime DESC NULLS FIRST
        LIMIT 1) AS wp
        ON 1 = 1
    WHERE wr.isdeleted = FALSE
    GROUP BY wr.id, wr.date, wlo.lduid, wlo.lduname, wlo.capacitypercentage, wlo.directorateid, wp.comtier1cpenabled;
/* dbo.MgtReportDirCapacity source */;



CREATE OR REPLACE VIEW dbo.mgtreportdircapacity (id, name, current, currentsdr, mth1, mth1sdr, mth2, mth2sdr, mth3, mth3sdr, mth4, mth4sdr, mth5, mth5sdr, mth6, mth6sdr, mth7, mth7sdr, mth8, mth8sdr, mth9, mth9sdr, mth10, mth10sdr, mth11, mth11sdr, mth12, mth12sdr) AS
SELECT
    dir.id, dir.name, mth0.capacity AS current, mth0.sdr AS currentsdr, mth1.capacity AS mth1, mth1.sdr AS mth1sdr, mth2.capacity AS mth2, mth2.sdr AS mth2sdr, mth3.capacity AS mth3, mth3.sdr AS mth3sdr, mth4.capacity AS mth4, mth4.sdr AS mth4sdr, mth5.capacity AS mth5, mth5.sdr AS mth5sdr, mth6.capacity AS mth6, mth6.sdr AS mth6sdr, mth7.capacity AS mth7, mth7.sdr AS mth7sdr, mth8.capacity AS mth8, mth8.sdr AS mth8sdr, mth9.capacity AS mth9, mth9.sdr AS mth9sdr, mth10.capacity AS mth10, mth10.sdr AS mth10sdr, mth11.capacity AS mth11, mth11.sdr AS mth11sdr, mth12.capacity AS mth12, mth12.sdr AS mth12sdr
    FROM dbo.organisationalunit AS dir
    LEFT OUTER JOIN dbo.capacityaveragedirectorate AS mth0
        ON mth0.directorateid = dir.id AND mth0.reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (0::NUMERIC || ' MONTH')::INTERVAL)
    LEFT OUTER JOIN dbo.capacityaveragedirectorate AS mth1
        ON mth1.directorateid = dir.id AND mth1.reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 1::NUMERIC || ' MONTH')::INTERVAL)
    LEFT OUTER JOIN dbo.capacityaveragedirectorate AS mth2
        ON mth2.directorateid = dir.id AND mth2.reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 2::NUMERIC || ' MONTH')::INTERVAL)
    LEFT OUTER JOIN dbo.capacityaveragedirectorate AS mth3
        ON mth3.directorateid = dir.id AND mth3.reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 3::NUMERIC || ' MONTH')::INTERVAL)
    LEFT OUTER JOIN dbo.capacityaveragedirectorate AS mth4
        ON mth4.directorateid = dir.id AND mth4.reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 4::NUMERIC || ' MONTH')::INTERVAL)
    LEFT OUTER JOIN dbo.capacityaveragedirectorate AS mth5
        ON mth5.directorateid = dir.id AND mth5.reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 5::NUMERIC || ' MONTH')::INTERVAL)
    LEFT OUTER JOIN dbo.capacityaveragedirectorate AS mth6
        ON mth6.directorateid = dir.id AND mth6.reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 6::NUMERIC || ' MONTH')::INTERVAL)
    LEFT OUTER JOIN dbo.capacityaveragedirectorate AS mth7
        ON mth7.directorateid = dir.id AND mth7.reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 7::NUMERIC || ' MONTH')::INTERVAL)
    LEFT OUTER JOIN dbo.capacityaveragedirectorate AS mth8
        ON mth8.directorateid = dir.id AND mth8.reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 8::NUMERIC || ' MONTH')::INTERVAL)
    LEFT OUTER JOIN dbo.capacityaveragedirectorate AS mth9
        ON mth9.directorateid = dir.id AND mth9.reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 9::NUMERIC || ' MONTH')::INTERVAL)
    LEFT OUTER JOIN dbo.capacityaveragedirectorate AS mth10
        ON mth10.directorateid = dir.id AND mth10.reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 10::NUMERIC || ' MONTH')::INTERVAL)
    LEFT OUTER JOIN dbo.capacityaveragedirectorate AS mth11
        ON mth11.directorateid = dir.id AND mth11.reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 11::NUMERIC || ' MONTH')::INTERVAL)
    LEFT OUTER JOIN dbo.capacityaveragedirectorate AS mth12
        ON mth12.directorateid = dir.id AND mth12.reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 12::NUMERIC || ' MONTH')::INTERVAL)
    WHERE dir.organisationalunittypeid = 2 AND dir.isdeleted = FALSE;
/* dbo.MgtReportLduCapacity source */;



CREATE OR REPLACE VIEW dbo.mgtreportlducapacity (dirid, dirname, lduid, lducode, lduname, current, currentsdr, mth3, mth3sdr, mth6, mth6sdr, mth9, mth9sdr, mth12, mth12sdr) AS
SELECT
    dir.id AS dirid, dir.name AS dirname, ldu.id AS lduid, ldu.uniqueidentifier AS lducode, ldu.name AS lduname, mth0.capacity AS current, mth0.sdr AS currentsdr, mth3.capacity AS mth3, mth3.sdr AS mth3sdr, mth6.capacity AS mth6, mth6.sdr AS mth6sdr, mth9.capacity AS mth9, mth9.sdr AS mth9sdr, mth12.capacity AS mth12, mth12.sdr AS mth12sdr
    FROM dbo.organisationalunit AS ldu
    INNER JOIN dbo.organisationalunit AS dir
        ON dir.id = ldu.parentorganisationalunitid
    LEFT OUTER JOIN dbo.capacityaverageldu AS mth0
        ON mth0.lduid = ldu.id AND mth0.reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (0::NUMERIC || ' MONTH')::INTERVAL)
    LEFT OUTER JOIN dbo.capacityaverageldu AS mth3
        ON mth3.lduid = ldu.id AND mth3.reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 3::NUMERIC || ' MONTH')::INTERVAL)
    LEFT OUTER JOIN dbo.capacityaverageldu AS mth6
        ON mth6.lduid = ldu.id AND mth6.reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 6::NUMERIC || ' MONTH')::INTERVAL)
    LEFT OUTER JOIN dbo.capacityaverageldu AS mth9
        ON mth9.lduid = ldu.id AND mth9.reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 9::NUMERIC || ' MONTH')::INTERVAL)
    LEFT OUTER JOIN dbo.capacityaverageldu AS mth12
        ON mth12.lduid = ldu.id AND mth12.reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 12::NUMERIC || ' MONTH')::INTERVAL)
    WHERE ldu.organisationalunittypeid = 3 AND ldu.isdeleted = FALSE;
/* dbo.MgtReportPOCapacity source */;



CREATE OR REPLACE VIEW dbo.mgtreportpocapacity (displayorder, capacity, omcount, current, mth1, mth2, mth3, mth4, mth5, mth6, mth7, mth8, mth9, mth10, mth11, mth12) AS
SELECT
    1 AS displayorder, '110% +'::TEXT AS "Capacity", (SELECT
        gt110
        FROM dbo.capacityaverageom
        WHERE directorateid = 0 AND offendermanagertypeid = 1 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (0::NUMERIC || ' MONTH')::INTERVAL)) AS "OMCount", (SELECT
        ((gt110 / total) * 100.0) / 100
        FROM dbo.capacityaverageom
        WHERE directorateid = 0 AND offendermanagertypeid = 1 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (0::NUMERIC || ' MONTH')::INTERVAL)) AS current, (SELECT
        ((gt110 / total) * 100.0) / 100
        FROM dbo.capacityaverageom
        WHERE directorateid = 0 AND offendermanagertypeid = 1 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 1::NUMERIC || ' MONTH')::INTERVAL)) AS mth1, (SELECT
        ((gt110 / total) * 100.0) / 100
        FROM dbo.capacityaverageom
        WHERE directorateid = 0 AND offendermanagertypeid = 1 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 2::NUMERIC || ' MONTH')::INTERVAL)) AS mth2, (SELECT
        ((gt110 / total) * 100.0) / 100
        FROM dbo.capacityaverageom
        WHERE directorateid = 0 AND offendermanagertypeid = 1 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 3::NUMERIC || ' MONTH')::INTERVAL)) AS mth3, (SELECT
        ((gt110 / total) * 100.0) / 100
        FROM dbo.capacityaverageom
        WHERE directorateid = 0 AND offendermanagertypeid = 1 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 4::NUMERIC || ' MONTH')::INTERVAL)) AS mth4, (SELECT
        ((gt110 / total) * 100.0) / 100
        FROM dbo.capacityaverageom
        WHERE directorateid = 0 AND offendermanagertypeid = 1 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 5::NUMERIC || ' MONTH')::INTERVAL)) AS mth5, (SELECT
        ((gt110 / total) * 100.0) / 100
        FROM dbo.capacityaverageom
        WHERE directorateid = 0 AND offendermanagertypeid = 1 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 6::NUMERIC || ' MONTH')::INTERVAL)) AS mth6, (SELECT
        ((gt110 / total) * 100.0) / 100
        FROM dbo.capacityaverageom
        WHERE directorateid = 0 AND offendermanagertypeid = 1 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 7::NUMERIC || ' MONTH')::INTERVAL)) AS mth7, (SELECT
        ((gt110 / total) * 100.0) / 100
        FROM dbo.capacityaverageom
        WHERE directorateid = 0 AND offendermanagertypeid = 1 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 8::NUMERIC || ' MONTH')::INTERVAL)) AS mth8, (SELECT
        ((gt110 / total) * 100.0) / 100
        FROM dbo.capacityaverageom
        WHERE directorateid = 0 AND offendermanagertypeid = 1 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 9::NUMERIC || ' MONTH')::INTERVAL)) AS mth9, (SELECT
        ((gt110 / total) * 100.0) / 100
        FROM dbo.capacityaverageom
        WHERE directorateid = 0 AND offendermanagertypeid = 1 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 10::NUMERIC || ' MONTH')::INTERVAL)) AS mth10, (SELECT
        ((gt110 / total) * 100.0) / 100
        FROM dbo.capacityaverageom
        WHERE directorateid = 0 AND offendermanagertypeid = 1 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 11::NUMERIC || ' MONTH')::INTERVAL)) AS mth11, (SELECT
        ((gt110 / total) * 100.0) / 100
        FROM dbo.capacityaverageom
        WHERE directorateid = 0 AND offendermanagertypeid = 1 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 12::NUMERIC || ' MONTH')::INTERVAL)) AS mth12
UNION
SELECT
    2 AS displayorder, '100% +'::TEXT, (SELECT
        gt100
        FROM dbo.capacityaverageom
        WHERE directorateid = 0 AND offendermanagertypeid = 1 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (0::NUMERIC || ' MONTH')::INTERVAL)), (SELECT
        ((gt100 / total) * 100.0) / 100
        FROM dbo.capacityaverageom
        WHERE directorateid = 0 AND offendermanagertypeid = 1 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (0::NUMERIC || ' MONTH')::INTERVAL)) AS current, (SELECT
        ((gt100 / total) * 100.0) / 100
        FROM dbo.capacityaverageom
        WHERE directorateid = 0 AND offendermanagertypeid = 1 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 1::NUMERIC || ' MONTH')::INTERVAL)) AS mth1, (SELECT
        ((gt100 / total) * 100.0) / 100
        FROM dbo.capacityaverageom
        WHERE directorateid = 0 AND offendermanagertypeid = 1 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 2::NUMERIC || ' MONTH')::INTERVAL)) AS mth2, (SELECT
        ((gt100 / total) * 100.0) / 100
        FROM dbo.capacityaverageom
        WHERE directorateid = 0 AND offendermanagertypeid = 1 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 3::NUMERIC || ' MONTH')::INTERVAL)) AS mth3, (SELECT
        ((gt100 / total) * 100.0) / 100
        FROM dbo.capacityaverageom
        WHERE directorateid = 0 AND offendermanagertypeid = 1 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 4::NUMERIC || ' MONTH')::INTERVAL)) AS mth4, (SELECT
        ((gt100 / total) * 100.0) / 100
        FROM dbo.capacityaverageom
        WHERE directorateid = 0 AND offendermanagertypeid = 1 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 5::NUMERIC || ' MONTH')::INTERVAL)) AS mth5, (SELECT
        ((gt100 / total) * 100.0) / 100
        FROM dbo.capacityaverageom
        WHERE directorateid = 0 AND offendermanagertypeid = 1 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 6::NUMERIC || ' MONTH')::INTERVAL)) AS mth6, (SELECT
        ((gt100 / total) * 100.0) / 100
        FROM dbo.capacityaverageom
        WHERE directorateid = 0 AND offendermanagertypeid = 1 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 7::NUMERIC || ' MONTH')::INTERVAL)) AS mth7, (SELECT
        ((gt100 / total) * 100.0) / 100
        FROM dbo.capacityaverageom
        WHERE directorateid = 0 AND offendermanagertypeid = 1 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 8::NUMERIC || ' MONTH')::INTERVAL)) AS mth8, (SELECT
        ((gt100 / total) * 100.0) / 100
        FROM dbo.capacityaverageom
        WHERE directorateid = 0 AND offendermanagertypeid = 1 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 9::NUMERIC || ' MONTH')::INTERVAL)) AS mth9, (SELECT
        ((gt100 / total) * 100.0) / 100
        FROM dbo.capacityaverageom
        WHERE directorateid = 0 AND offendermanagertypeid = 1 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 10::NUMERIC || ' MONTH')::INTERVAL)) AS mth10, (SELECT
        ((gt100 / total) * 100.0) / 100
        FROM dbo.capacityaverageom
        WHERE directorateid = 0 AND offendermanagertypeid = 1 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 11::NUMERIC || ' MONTH')::INTERVAL)) AS mth11, (SELECT
        ((gt100 / total) * 100.0) / 100
        FROM dbo.capacityaverageom
        WHERE directorateid = 0 AND offendermanagertypeid = 1 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 12::NUMERIC || ' MONTH')::INTERVAL)) AS mth12
UNION
SELECT
    3 AS displayorder, '< 100%'::TEXT, (SELECT
        lt100
        FROM dbo.capacityaverageom
        WHERE directorateid = 0 AND offendermanagertypeid = 1 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (0::NUMERIC || ' MONTH')::INTERVAL)), (SELECT
        ((lt100 / total) * 100.0) / 100
        FROM dbo.capacityaverageom
        WHERE directorateid = 0 AND offendermanagertypeid = 1 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (0::NUMERIC || ' MONTH')::INTERVAL)) AS current, (SELECT
        ((lt100 / total) * 100.0) / 100
        FROM dbo.capacityaverageom
        WHERE directorateid = 0 AND offendermanagertypeid = 1 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 1::NUMERIC || ' MONTH')::INTERVAL)) AS mth1, (SELECT
        ((lt100 / total) * 100.0) / 100
        FROM dbo.capacityaverageom
        WHERE directorateid = 0 AND offendermanagertypeid = 1 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 2::NUMERIC || ' MONTH')::INTERVAL)) AS mth2, (SELECT
        ((lt100 / total) * 100.0) / 100
        FROM dbo.capacityaverageom
        WHERE directorateid = 0 AND offendermanagertypeid = 1 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 3::NUMERIC || ' MONTH')::INTERVAL)) AS mth3, (SELECT
        ((lt100 / total) * 100.0) / 100
        FROM dbo.capacityaverageom
        WHERE directorateid = 0 AND offendermanagertypeid = 1 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 4::NUMERIC || ' MONTH')::INTERVAL)) AS mth4, (SELECT
        ((lt100 / total) * 100.0) / 100
        FROM dbo.capacityaverageom
        WHERE directorateid = 0 AND offendermanagertypeid = 1 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 5::NUMERIC || ' MONTH')::INTERVAL)) AS mth5, (SELECT
        ((lt100 / total) * 100.0) / 100
        FROM dbo.capacityaverageom
        WHERE directorateid = 0 AND offendermanagertypeid = 1 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 6::NUMERIC || ' MONTH')::INTERVAL)) AS mth6, (SELECT
        ((lt100 / total) * 100.0) / 100
        FROM dbo.capacityaverageom
        WHERE directorateid = 0 AND offendermanagertypeid = 1 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 7::NUMERIC || ' MONTH')::INTERVAL)) AS mth7, (SELECT
        ((lt100 / total) * 100.0) / 100
        FROM dbo.capacityaverageom
        WHERE directorateid = 0 AND offendermanagertypeid = 1 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 8::NUMERIC || ' MONTH')::INTERVAL)) AS mth8, (SELECT
        ((lt100 / total) * 100.0) / 100
        FROM dbo.capacityaverageom
        WHERE directorateid = 0 AND offendermanagertypeid = 1 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 9::NUMERIC || ' MONTH')::INTERVAL)) AS mth9, (SELECT
        ((lt100 / total) * 100.0) / 100
        FROM dbo.capacityaverageom
        WHERE directorateid = 0 AND offendermanagertypeid = 1 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 10::NUMERIC || ' MONTH')::INTERVAL)) AS mth10, (SELECT
        ((lt100 / total) * 100.0) / 100
        FROM dbo.capacityaverageom
        WHERE directorateid = 0 AND offendermanagertypeid = 1 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 11::NUMERIC || ' MONTH')::INTERVAL)) AS mth11, (SELECT
        ((lt100 / total) * 100.0) / 100
        FROM dbo.capacityaverageom
        WHERE directorateid = 0 AND offendermanagertypeid = 1 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 12::NUMERIC || ' MONTH')::INTERVAL)) AS mth12;
/* union */
/* select	4 as DisplayOrder, */
/* 'Total', */
/* (select Total from dbo.CapacityAverageOM where OffenderManagerTypeId = 1 and ReportPeriod = dbo.[GetFirstDayOfMonth](dateadd(m, -1, getdate()))), */
/* (select ((Total/Total) * 100.0) / 100 from dbo.CapacityAverageOM where OffenderManagerTypeId = 1 and ReportPeriod = dbo.[GetFirstDayOfMonth](dateadd(m, -1, getdate()))) as [Current], */
/* (select ((Total/Total) * 100.0) / 100 from dbo.CapacityAverageOM where OffenderManagerTypeId = 1 and ReportPeriod = dbo.[GetFirstDayOfMonth](dateadd(m, -2, getdate()))) as [Mth1], */
/* (select ((Total/Total) * 100.0) / 100 from dbo.CapacityAverageOM where OffenderManagerTypeId = 1 and ReportPeriod = dbo.[GetFirstDayOfMonth](dateadd(m, -3, getdate()))) as [Mth2], */
/* (select ((Total/Total) * 100.0) / 100 from dbo.CapacityAverageOM where OffenderManagerTypeId = 1 and ReportPeriod = dbo.[GetFirstDayOfMonth](dateadd(m, -4, getdate()))) as [Mth3], */
/* (select ((Total/Total) * 100.0) / 100 from dbo.CapacityAverageOM where OffenderManagerTypeId = 1 and ReportPeriod = dbo.[GetFirstDayOfMonth](dateadd(m, -5, getdate()))) as [Mth4], */
/* (select ((Total/Total) * 100.0) / 100 from dbo.CapacityAverageOM where OffenderManagerTypeId = 1 and ReportPeriod = dbo.[GetFirstDayOfMonth](dateadd(m, -7, getdate()))) as [Mth5], */
/* (select ((Total/Total) * 100.0) / 100 from dbo.CapacityAverageOM where OffenderManagerTypeId = 1 and ReportPeriod = dbo.[GetFirstDayOfMonth](dateadd(m, -7, getdate()))) as [Mth6], */
/* (select ((Total/Total) * 100.0) / 100 from dbo.CapacityAverageOM where OffenderManagerTypeId = 1 and ReportPeriod = dbo.[GetFirstDayOfMonth](dateadd(m, -8, getdate()))) as [Mth7], */
/* (select ((Total/Total) * 100.0) / 100 from dbo.CapacityAverageOM where OffenderManagerTypeId = 1 and ReportPeriod = dbo.[GetFirstDayOfMonth](dateadd(m, -9, getdate()))) as [Mth8], */
/* (select ((Total/Total) * 100.0) / 100 from dbo.CapacityAverageOM where OffenderManagerTypeId = 1 and ReportPeriod = dbo.[GetFirstDayOfMonth](dateadd(m, -10, getdate()))) as [Mth9], */
/* (select ((Total/Total) * 100.0) / 100 from dbo.CapacityAverageOM where OffenderManagerTypeId = 1 and ReportPeriod = dbo.[GetFirstDayOfMonth](dateadd(m, -11, getdate()))) as [Mth10], */
/* (select ((Total/Total) * 100.0) / 100 from dbo.CapacityAverageOM where OffenderManagerTypeId = 1 and ReportPeriod = dbo.[GetFirstDayOfMonth](dateadd(m, -12, getdate()))) as [Mth11], */
/* (select ((Total/Total) * 100.0) / 100 from dbo.CapacityAverageOM where OffenderManagerTypeId = 1 and ReportPeriod = dbo.[GetFirstDayOfMonth](dateadd(m, -13, getdate()))) as [Mth12] */
/* GO; */
/* dbo.MgtReportPOCapacityAvgPeriodList source */;



CREATE OR REPLACE VIEW dbo.mgtreportpocapacityavgperiodlist (workloadid, lduname, teamname, offendermanagername, contractedhoursperweek, hoursreduction, currentcapacitypercentage, capacitypercentage, offendermanagerupdateusername, offendermanagerupdatedatetime) AS
SELECT
    wro.workloadid, wro.lduname, wro.teamname, wro.offendermanagername, wro.contractedhoursperweek, wro.hoursreduction, wro.capacitypercentage AS currentcapacitypercentage, cpo.avgcapacity AS capacitypercentage, wro.offendermanagerupdateusername, wro.offendermanagerupdatedatetime
    FROM dbo.workloadreportofficer AS wro
    INNER JOIN dbo.capacityperiodofficer AS cpo
        ON cpo.offendermanagerid = wro.offendermanagerid AND (avgcapacity > 110) AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (0::NUMERIC || ' MONTH')::INTERVAL)
    INNER JOIN dbo.workloadreport AS wr
        ON wr.id = wro.workloadreportid
    WHERE wr.id = (SELECT
        id
        FROM dbo.workloadreport
        ORDER BY date DESC NULLS FIRST
        LIMIT 1);
/* dbo.MgtReportPOCapacityPeriod source */;



CREATE OR REPLACE VIEW dbo.mgtreportpocapacityperiod (displayorder, capacity, omcount, omperc) AS
SELECT
    1 AS displayorder, '110% +'::TEXT AS "Capacity", COUNT('x') AS "OMCount", ROUND(((COUNT('x') / (SELECT
        CASE
            WHEN COUNT('x') < 1 THEN 1
            ELSE COUNT('x')
        END * 1.0
        FROM dbo.capacityperiodofficer
        WHERE reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (0::NUMERIC || ' MONTH')::INTERVAL))) * 100.0), 1) AS "OMPerc"
    FROM dbo.capacityperiodofficer
    WHERE avgcapacity > 110 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (0::NUMERIC || ' MONTH')::INTERVAL)
UNION
SELECT
    2, '100% +'::TEXT AS "Capacity", COUNT('x'), ROUND(((COUNT('x') / (SELECT
        CASE
            WHEN COUNT('x') < 1 THEN 1
            ELSE COUNT('x')
        END * 1.0
        FROM dbo.capacityperiodofficer
        WHERE reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (0::NUMERIC || ' MONTH')::INTERVAL))) * 100.0), 1)
    FROM dbo.capacityperiodofficer
    WHERE avgcapacity BETWEEN 100 AND 110 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (0::NUMERIC || ' MONTH')::INTERVAL)
UNION
SELECT
    3, '< 100%'::TEXT AS "Capacity", COUNT('x'), ROUND(((COUNT('x') / (SELECT
        CASE
            WHEN COUNT('x') < 1 THEN 1
            ELSE COUNT('x')
        END * 1.0
        FROM dbo.capacityperiodofficer
        WHERE reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (0::NUMERIC || ' MONTH')::INTERVAL))) * 100.0), 1)
    FROM dbo.capacityperiodofficer
    WHERE avgcapacity < 100 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (0::NUMERIC || ' MONTH')::INTERVAL);
/* dbo.MgtReportPOCapacityPeriodList source */;



CREATE OR REPLACE VIEW dbo.mgtreportpocapacityperiodlist (workloadid, lduname, teamname, offendermanagername, contractedhoursperweek, hoursreduction, currentcapacitypercentage, capacitypercentage, offendermanagernotes, offendermanagerupdateusername, offendermanagerupdatedatetime) AS
SELECT
    wro.workloadid, wro.lduname, wro.teamname, wro.offendermanagername, wro.contractedhoursperweek, wro.hoursreduction, wro.capacitypercentage AS currentcapacitypercentage, cpo.avgcapacity AS capacitypercentage, wro.offendermanagernotes, wro.offendermanagerupdateusername, wro.offendermanagerupdatedatetime
    FROM dbo.workloadreportofficer AS wro
    INNER JOIN dbo.capacityperiodofficer AS cpo
        ON cpo.offendermanagerid = wro.offendermanagerid AND (mincapacity > 110 AND maxcapacity > 110) AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (0::NUMERIC || ' MONTH')::INTERVAL)
    INNER JOIN dbo.workloadreport AS wr
        ON wr.id = wro.workloadreportid
    WHERE wr.id = (SELECT
        id
        FROM dbo.workloadreport
        ORDER BY date DESC NULLS FIRST
        LIMIT 1);
/* dbo.MgtReportPOCaseload source */;



CREATE OR REPLACE VIEW dbo.mgtreportpocaseload (displayorder, casecountdesc, omcount, omtotal, current, mth1, mth2, mth3, mth4, mth5, mth6, mth7, mth8, mth9, mth10, mth11, mth12) AS
SELECT
    1 AS displayorder, '60+'::TEXT AS casecountdesc, (SELECT
        omcountavg
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('60+') AND offendermanagertypeid = 1 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (0::NUMERIC || ' MONTH')::INTERVAL)) AS omcount, (SELECT
        totalomcountavg
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('60+') AND offendermanagertypeid = 1 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (0::NUMERIC || ' MONTH')::INTERVAL)) AS omtotal, (SELECT
        ROUND(CAST(((omcountavg / totalomcountavg) * 100) AS numeric), 1)
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('60+') AND offendermanagertypeid = 1 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (0::NUMERIC || ' MONTH')::INTERVAL)) AS current, (SELECT
        ROUND(CAST(((omcountavg / totalomcountavg) * 100) AS numeric),1)
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('60+') AND offendermanagertypeid = 1 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 1::NUMERIC || ' MONTH')::INTERVAL)) AS mth1, (SELECT
        ROUND(CAST(((omcountavg / totalomcountavg) * 100) AS numeric),1)
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('60+') AND offendermanagertypeid = 1 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 2::NUMERIC || ' MONTH')::INTERVAL)) AS mth2, (SELECT
        ROUND(CAST(((omcountavg / totalomcountavg) * 100) AS numeric),1)
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('60+') AND offendermanagertypeid = 1 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 3::NUMERIC || ' MONTH')::INTERVAL)) AS mth3, (SELECT
        ROUND(CAST(((omcountavg / totalomcountavg) * 100) AS numeric),1)
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('60+') AND offendermanagertypeid = 1 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 4::NUMERIC || ' MONTH')::INTERVAL)) AS mth4, (SELECT
        ROUND(CAST(((omcountavg / totalomcountavg) * 100) AS numeric),1)
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('60+') AND offendermanagertypeid = 1 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 5::NUMERIC || ' MONTH')::INTERVAL)) AS mth5, (SELECT
        ROUND(CAST(((omcountavg / totalomcountavg) * 100) AS numeric),1)
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('60+') AND offendermanagertypeid = 1 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 6::NUMERIC || ' MONTH')::INTERVAL)) AS mth6, (SELECT
        ROUND(CAST(((omcountavg / totalomcountavg) * 100) AS numeric),1)
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('60+') AND offendermanagertypeid = 1 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 7::NUMERIC || ' MONTH')::INTERVAL)) AS mth7, (SELECT
        ROUND(CAST(((omcountavg / totalomcountavg) * 100) AS numeric),1)
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('60+') AND offendermanagertypeid = 1 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 8::NUMERIC || ' MONTH')::INTERVAL)) AS mth8, (SELECT
        ROUND(CAST(((omcountavg / totalomcountavg) * 100) AS numeric),1)
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('60+') AND offendermanagertypeid = 1 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 9::NUMERIC || ' MONTH')::INTERVAL)) AS mth9, (SELECT
        ROUND(CAST(((omcountavg / totalomcountavg) * 100) AS numeric),1)
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('60+') AND offendermanagertypeid = 1 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 10::NUMERIC || ' MONTH')::INTERVAL)) AS mth10, (SELECT
        ROUND(CAST(((omcountavg / totalomcountavg) * 100) AS numeric),1)
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('60+') AND offendermanagertypeid = 1 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 11::NUMERIC || ' MONTH')::INTERVAL)) AS mth11, (SELECT
        ROUND(CAST(((omcountavg / totalomcountavg) * 100) AS numeric),1)
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('60+') AND offendermanagertypeid = 1 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 12::NUMERIC || ' MONTH')::INTERVAL)) AS mth12
UNION
SELECT
    2 AS displayorder, '50-59'::TEXT AS casecountdesc, (SELECT
        omcountavg
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('50-59') AND offendermanagertypeid = 1 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (0::NUMERIC || ' MONTH')::INTERVAL)), (SELECT
        totalomcountavg
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('50-59') AND offendermanagertypeid = 1 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (0::NUMERIC || ' MONTH')::INTERVAL)), (SELECT
        ROUND(CAST(((omcountavg / totalomcountavg) * 100) AS numeric),1)
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('50-59') AND offendermanagertypeid = 1 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (0::NUMERIC || ' MONTH')::INTERVAL)), (SELECT
        ROUND(CAST(((omcountavg / totalomcountavg) * 100) AS numeric),1)
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('50-59') AND offendermanagertypeid = 1 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 1::NUMERIC || ' MONTH')::INTERVAL)), (SELECT
        ROUND(CAST(((omcountavg / totalomcountavg) * 100) AS numeric),1)
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('50-59') AND offendermanagertypeid = 1 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 2::NUMERIC || ' MONTH')::INTERVAL)), (SELECT
        ROUND(CAST(((omcountavg / totalomcountavg) * 100) AS numeric),1)
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('50-59') AND offendermanagertypeid = 1 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 3::NUMERIC || ' MONTH')::INTERVAL)), (SELECT
        ROUND(CAST(((omcountavg / totalomcountavg) * 100) AS numeric),1)
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('50-59') AND offendermanagertypeid = 1 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 4::NUMERIC || ' MONTH')::INTERVAL)), (SELECT
        ROUND(CAST(((omcountavg / totalomcountavg) * 100) AS numeric),1)
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('50-59') AND offendermanagertypeid = 1 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 5::NUMERIC || ' MONTH')::INTERVAL)), (SELECT
        ROUND(CAST(((omcountavg / totalomcountavg) * 100) AS numeric),1)
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('50-59') AND offendermanagertypeid = 1 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 6::NUMERIC || ' MONTH')::INTERVAL)), (SELECT
        ROUND(CAST(((omcountavg / totalomcountavg) * 100) AS numeric),1)
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('50-59') AND offendermanagertypeid = 1 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 7::NUMERIC || ' MONTH')::INTERVAL)), (SELECT
        ROUND(CAST(((omcountavg / totalomcountavg) * 100) AS numeric),1)
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('50-59') AND offendermanagertypeid = 1 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 8::NUMERIC || ' MONTH')::INTERVAL)), (SELECT
        ROUND(CAST(((omcountavg / totalomcountavg) * 100) AS numeric),1)
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('50-59') AND offendermanagertypeid = 1 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 9::NUMERIC || ' MONTH')::INTERVAL)), (SELECT
        ROUND(CAST(((omcountavg / totalomcountavg) * 100) AS numeric),1)
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('50-59') AND offendermanagertypeid = 1 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 10::NUMERIC || ' MONTH')::INTERVAL)), (SELECT
        ROUND(CAST(((omcountavg / totalomcountavg) * 100) AS numeric),1)
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('50-59') AND offendermanagertypeid = 1 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 11::NUMERIC || ' MONTH')::INTERVAL)), (SELECT
        ROUND(CAST(((omcountavg / totalomcountavg) * 100) AS numeric),1)
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('50-59') AND offendermanagertypeid = 1 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 12::NUMERIC || ' MONTH')::INTERVAL))
UNION
SELECT
    3 AS displayorder, '40-49'::TEXT AS casecountdesc, (SELECT
        omcountavg
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('40-49') AND offendermanagertypeid = 1 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (0::NUMERIC || ' MONTH')::INTERVAL)), (SELECT
        totalomcountavg
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('40-49') AND offendermanagertypeid = 1 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (0::NUMERIC || ' MONTH')::INTERVAL)), (SELECT
        ROUND(CAST(((omcountavg / totalomcountavg) * 100) AS numeric),1)
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('40-49') AND offendermanagertypeid = 1 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (0::NUMERIC || ' MONTH')::INTERVAL)), (SELECT
        ROUND(CAST(((omcountavg / totalomcountavg) * 100) AS numeric),1)
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('40-49') AND offendermanagertypeid = 1 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 1::NUMERIC || ' MONTH')::INTERVAL)), (SELECT
        ROUND(CAST(((omcountavg / totalomcountavg) * 100) AS numeric),1)
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('40-49') AND offendermanagertypeid = 1 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 2::NUMERIC || ' MONTH')::INTERVAL)), (SELECT
        ROUND(CAST(((omcountavg / totalomcountavg) * 100) AS numeric),1)
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('40-49') AND offendermanagertypeid = 1 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 3::NUMERIC || ' MONTH')::INTERVAL)), (SELECT
        ROUND(CAST(((omcountavg / totalomcountavg) * 100) AS numeric),1)
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('40-49') AND offendermanagertypeid = 1 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 4::NUMERIC || ' MONTH')::INTERVAL)), (SELECT
        ROUND(CAST(((omcountavg / totalomcountavg) * 100) AS numeric),1)
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('40-49') AND offendermanagertypeid = 1 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 5::NUMERIC || ' MONTH')::INTERVAL)), (SELECT
        ROUND(CAST(((omcountavg / totalomcountavg) * 100) AS numeric),1)
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('40-49') AND offendermanagertypeid = 1 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 6::NUMERIC || ' MONTH')::INTERVAL)), (SELECT
        ROUND(CAST(((omcountavg / totalomcountavg) * 100) AS numeric),1)
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('40-49') AND offendermanagertypeid = 1 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 7::NUMERIC || ' MONTH')::INTERVAL)), (SELECT
        ROUND(CAST(((omcountavg / totalomcountavg) * 100) AS numeric),1)
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('40-49') AND offendermanagertypeid = 1 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 8::NUMERIC || ' MONTH')::INTERVAL)), (SELECT
        ROUND(CAST(((omcountavg / totalomcountavg) * 100) AS numeric),1)
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('40-49') AND offendermanagertypeid = 1 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 9::NUMERIC || ' MONTH')::INTERVAL)), (SELECT
        ROUND(CAST(((omcountavg / totalomcountavg) * 100) AS numeric),1)
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('40-49') AND offendermanagertypeid = 1 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 10::NUMERIC || ' MONTH')::INTERVAL)), (SELECT
        ROUND(CAST(((omcountavg / totalomcountavg) * 100) AS numeric),1)
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('40-49') AND offendermanagertypeid = 1 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 11::NUMERIC || ' MONTH')::INTERVAL)), (SELECT
        ROUND(CAST(((omcountavg / totalomcountavg) * 100) AS numeric),1)
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('40-49') AND offendermanagertypeid = 1 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 12::NUMERIC || ' MONTH')::INTERVAL))
UNION
SELECT
    4 AS displayorder, '30-39'::TEXT AS casecountdesc, (SELECT
        omcountavg
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('30-39') AND offendermanagertypeid = 1 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (0::NUMERIC || ' MONTH')::INTERVAL)), (SELECT
        totalomcountavg
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('30-39') AND offendermanagertypeid = 1 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (0::NUMERIC || ' MONTH')::INTERVAL)), (SELECT
        ROUND(CAST(((omcountavg / totalomcountavg) * 100) AS numeric),1)
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('30-39') AND offendermanagertypeid = 1 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (0::NUMERIC || ' MONTH')::INTERVAL)), (SELECT
        ROUND(CAST(((omcountavg / totalomcountavg) * 100) AS numeric),1)
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('30-39') AND offendermanagertypeid = 1 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 1::NUMERIC || ' MONTH')::INTERVAL)), (SELECT
        ROUND(CAST(((omcountavg / totalomcountavg) * 100) AS numeric),1)
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('30-39') AND offendermanagertypeid = 1 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 2::NUMERIC || ' MONTH')::INTERVAL)), (SELECT
        ROUND(CAST(((omcountavg / totalomcountavg) * 100) AS numeric),1)
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('30-39') AND offendermanagertypeid = 1 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 3::NUMERIC || ' MONTH')::INTERVAL)), (SELECT
        ROUND(CAST(((omcountavg / totalomcountavg) * 100) AS numeric),1)
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('30-39') AND offendermanagertypeid = 1 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 4::NUMERIC || ' MONTH')::INTERVAL)), (SELECT
        ROUND(CAST(((omcountavg / totalomcountavg) * 100) AS numeric),1)
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('30-39') AND offendermanagertypeid = 1 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 5::NUMERIC || ' MONTH')::INTERVAL)), (SELECT
        ROUND(CAST(((omcountavg / totalomcountavg) * 100) AS numeric),1)
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('30-39') AND offendermanagertypeid = 1 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 6::NUMERIC || ' MONTH')::INTERVAL)), (SELECT
        ROUND(CAST(((omcountavg / totalomcountavg) * 100) AS numeric),1)
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('30-39') AND offendermanagertypeid = 1 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 7::NUMERIC || ' MONTH')::INTERVAL)), (SELECT
        ROUND(CAST(((omcountavg / totalomcountavg) * 100) AS numeric),1)
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('30-39') AND offendermanagertypeid = 1 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 8::NUMERIC || ' MONTH')::INTERVAL)), (SELECT
        ROUND(CAST(((omcountavg / totalomcountavg) * 100) AS numeric),1)
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('30-39') AND offendermanagertypeid = 1 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 9::NUMERIC || ' MONTH')::INTERVAL)), (SELECT
        ROUND(CAST(((omcountavg / totalomcountavg) * 100) AS numeric),1)
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('30-39') AND offendermanagertypeid = 1 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 10::NUMERIC || ' MONTH')::INTERVAL)), (SELECT
        ROUND(CAST(((omcountavg / totalomcountavg) * 100) AS numeric),1)
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('30-39') AND offendermanagertypeid = 1 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 11::NUMERIC || ' MONTH')::INTERVAL)), (SELECT
        ROUND(CAST(((omcountavg / totalomcountavg) * 100) AS numeric),1)
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('30-39') AND offendermanagertypeid = 1 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 12::NUMERIC || ' MONTH')::INTERVAL))
UNION
SELECT
    5 AS displayorder, '10-29'::TEXT AS casecountdesc, (SELECT
        omcountavg
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('10-29') AND offendermanagertypeid = 1 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (0::NUMERIC || ' MONTH')::INTERVAL)), (SELECT
        totalomcountavg
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('10-29') AND offendermanagertypeid = 1 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (0::NUMERIC || ' MONTH')::INTERVAL)), (SELECT
        ROUND(CAST(((omcountavg / totalomcountavg) * 100) AS numeric),1)
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('10-29') AND offendermanagertypeid = 1 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (0::NUMERIC || ' MONTH')::INTERVAL)), (SELECT
        ROUND(CAST(((omcountavg / totalomcountavg) * 100) AS numeric),1)
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('10-29') AND offendermanagertypeid = 1 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 1::NUMERIC || ' MONTH')::INTERVAL)), (SELECT
        ROUND(CAST(((omcountavg / totalomcountavg) * 100) AS numeric),1)
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('10-29') AND offendermanagertypeid = 1 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 2::NUMERIC || ' MONTH')::INTERVAL)), (SELECT
        ROUND(CAST(((omcountavg / totalomcountavg) * 100) AS numeric),1)
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('10-29') AND offendermanagertypeid = 1 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 3::NUMERIC || ' MONTH')::INTERVAL)), (SELECT
        ROUND(CAST(((omcountavg / totalomcountavg) * 100) AS numeric),1)
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('10-29') AND offendermanagertypeid = 1 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 4::NUMERIC || ' MONTH')::INTERVAL)), (SELECT
        ROUND(CAST(((omcountavg / totalomcountavg) * 100) AS numeric),1)
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('10-29') AND offendermanagertypeid = 1 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 5::NUMERIC || ' MONTH')::INTERVAL)), (SELECT
        ROUND(CAST(((omcountavg / totalomcountavg) * 100) AS numeric),1)
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('10-29') AND offendermanagertypeid = 1 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 6::NUMERIC || ' MONTH')::INTERVAL)), (SELECT
        ROUND(CAST(((omcountavg / totalomcountavg) * 100) AS numeric),1)
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('10-29') AND offendermanagertypeid = 1 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 7::NUMERIC || ' MONTH')::INTERVAL)), (SELECT
        ROUND(CAST(((omcountavg / totalomcountavg) * 100) AS numeric),1)
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('10-29') AND offendermanagertypeid = 1 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 8::NUMERIC || ' MONTH')::INTERVAL)), (SELECT
        ROUND(CAST(((omcountavg / totalomcountavg) * 100) AS numeric),1)
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('10-29') AND offendermanagertypeid = 1 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 9::NUMERIC || ' MONTH')::INTERVAL)), (SELECT
        ROUND(CAST(((omcountavg / totalomcountavg) * 100) AS numeric),1)
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('10-29') AND offendermanagertypeid = 1 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 10::NUMERIC || ' MONTH')::INTERVAL)), (SELECT
        ROUND(CAST(((omcountavg / totalomcountavg) * 100) AS numeric),1)
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('10-29') AND offendermanagertypeid = 1 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 11::NUMERIC || ' MONTH')::INTERVAL)), (SELECT
        ROUND(CAST(((omcountavg / totalomcountavg) * 100) AS numeric),1)
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('10-29') AND offendermanagertypeid = 1 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 12::NUMERIC || ' MONTH')::INTERVAL));
/* dbo.MgtReportPSOCapacity source */;



CREATE OR REPLACE VIEW dbo.mgtreportpsocapacity (displayorder, capacity, omcount, current, mth1, mth2, mth3, mth4, mth5, mth6, mth7, mth8, mth9, mth10, mth11, mth12) AS
SELECT
    1 AS displayorder, '110% +'::TEXT AS "Capacity", (SELECT
        gt110
        FROM dbo.capacityaverageom
        WHERE directorateid = 0 AND offendermanagertypeid = 2 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (0::NUMERIC || ' MONTH')::INTERVAL)) AS "OMCount", (SELECT
        ((gt110 / total) * 100.0) / 100
        FROM dbo.capacityaverageom
        WHERE directorateid = 0 AND offendermanagertypeid = 2 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (0::NUMERIC || ' MONTH')::INTERVAL)) AS current, (SELECT
        ((gt110 / total) * 100.0) / 100
        FROM dbo.capacityaverageom
        WHERE directorateid = 0 AND offendermanagertypeid = 2 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 1::NUMERIC || ' MONTH')::INTERVAL)) AS mth1, (SELECT
        ((gt110 / total) * 100.0) / 100
        FROM dbo.capacityaverageom
        WHERE directorateid = 0 AND offendermanagertypeid = 2 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 2::NUMERIC || ' MONTH')::INTERVAL)) AS mth2, (SELECT
        ((gt110 / total) * 100.0) / 100
        FROM dbo.capacityaverageom
        WHERE directorateid = 0 AND offendermanagertypeid = 2 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 3::NUMERIC || ' MONTH')::INTERVAL)) AS mth3, (SELECT
        ((gt110 / total) * 100.0) / 100
        FROM dbo.capacityaverageom
        WHERE directorateid = 0 AND offendermanagertypeid = 2 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 4::NUMERIC || ' MONTH')::INTERVAL)) AS mth4, (SELECT
        ((gt110 / total) * 100.0) / 100
        FROM dbo.capacityaverageom
        WHERE directorateid = 0 AND offendermanagertypeid = 2 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 5::NUMERIC || ' MONTH')::INTERVAL)) AS mth5, (SELECT
        ((gt110 / total) * 100.0) / 100
        FROM dbo.capacityaverageom
        WHERE directorateid = 0 AND offendermanagertypeid = 2 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 6::NUMERIC || ' MONTH')::INTERVAL)) AS mth6, (SELECT
        ((gt110 / total) * 100.0) / 100
        FROM dbo.capacityaverageom
        WHERE directorateid = 0 AND offendermanagertypeid = 2 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 7::NUMERIC || ' MONTH')::INTERVAL)) AS mth7, (SELECT
        ((gt110 / total) * 100.0) / 100
        FROM dbo.capacityaverageom
        WHERE directorateid = 0 AND offendermanagertypeid = 2 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 8::NUMERIC || ' MONTH')::INTERVAL)) AS mth8, (SELECT
        ((gt110 / total) * 100.0) / 100
        FROM dbo.capacityaverageom
        WHERE directorateid = 0 AND offendermanagertypeid = 2 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 9::NUMERIC || ' MONTH')::INTERVAL)) AS mth9, (SELECT
        ((gt110 / total) * 100.0) / 100
        FROM dbo.capacityaverageom
        WHERE directorateid = 0 AND offendermanagertypeid = 2 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 10::NUMERIC || ' MONTH')::INTERVAL)) AS mth10, (SELECT
        ((gt110 / total) * 100.0) / 100
        FROM dbo.capacityaverageom
        WHERE directorateid = 0 AND offendermanagertypeid = 2 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 11::NUMERIC || ' MONTH')::INTERVAL)) AS mth11, (SELECT
        ((gt110 / total) * 100.0) / 100
        FROM dbo.capacityaverageom
        WHERE directorateid = 0 AND offendermanagertypeid = 2 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 12::NUMERIC || ' MONTH')::INTERVAL)) AS mth12
UNION
SELECT
    2 AS displayorder, '100% +'::TEXT, (SELECT
        gt100
        FROM dbo.capacityaverageom
        WHERE directorateid = 0 AND offendermanagertypeid = 2 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (0::NUMERIC || ' MONTH')::INTERVAL)), (SELECT
        ((gt100 / total) * 100.0) / 100
        FROM dbo.capacityaverageom
        WHERE directorateid = 0 AND offendermanagertypeid = 2 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (0::NUMERIC || ' MONTH')::INTERVAL)) AS current, (SELECT
        ((gt100 / total) * 100.0) / 100
        FROM dbo.capacityaverageom
        WHERE directorateid = 0 AND offendermanagertypeid = 2 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 1::NUMERIC || ' MONTH')::INTERVAL)) AS mth1, (SELECT
        ((gt100 / total) * 100.0) / 100
        FROM dbo.capacityaverageom
        WHERE directorateid = 0 AND offendermanagertypeid = 2 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 2::NUMERIC || ' MONTH')::INTERVAL)) AS mth2, (SELECT
        ((gt100 / total) * 100.0) / 100
        FROM dbo.capacityaverageom
        WHERE directorateid = 0 AND offendermanagertypeid = 2 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 3::NUMERIC || ' MONTH')::INTERVAL)) AS mth3, (SELECT
        ((gt100 / total) * 100.0) / 100
        FROM dbo.capacityaverageom
        WHERE directorateid = 0 AND offendermanagertypeid = 2 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 4::NUMERIC || ' MONTH')::INTERVAL)) AS mth4, (SELECT
        ((gt100 / total) * 100.0) / 100
        FROM dbo.capacityaverageom
        WHERE directorateid = 0 AND offendermanagertypeid = 2 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 5::NUMERIC || ' MONTH')::INTERVAL)) AS mth5, (SELECT
        ((gt100 / total) * 100.0) / 100
        FROM dbo.capacityaverageom
        WHERE directorateid = 0 AND offendermanagertypeid = 2 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 6::NUMERIC || ' MONTH')::INTERVAL)) AS mth6, (SELECT
        ((gt100 / total) * 100.0) / 100
        FROM dbo.capacityaverageom
        WHERE directorateid = 0 AND offendermanagertypeid = 2 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 7::NUMERIC || ' MONTH')::INTERVAL)) AS mth7, (SELECT
        ((gt100 / total) * 100.0) / 100
        FROM dbo.capacityaverageom
        WHERE directorateid = 0 AND offendermanagertypeid = 2 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 8::NUMERIC || ' MONTH')::INTERVAL)) AS mth8, (SELECT
        ((gt100 / total) * 100.0) / 100
        FROM dbo.capacityaverageom
        WHERE directorateid = 0 AND offendermanagertypeid = 2 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 9::NUMERIC || ' MONTH')::INTERVAL)) AS mth9, (SELECT
        ((gt100 / total) * 100.0) / 100
        FROM dbo.capacityaverageom
        WHERE directorateid = 0 AND offendermanagertypeid = 2 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 10::NUMERIC || ' MONTH')::INTERVAL)) AS mth10, (SELECT
        ((gt100 / total) * 100.0) / 100
        FROM dbo.capacityaverageom
        WHERE directorateid = 0 AND offendermanagertypeid = 2 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 11::NUMERIC || ' MONTH')::INTERVAL)) AS mth11, (SELECT
        ((gt100 / total) * 100.0) / 100
        FROM dbo.capacityaverageom
        WHERE directorateid = 0 AND offendermanagertypeid = 2 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 12::NUMERIC || ' MONTH')::INTERVAL)) AS mth12
UNION
SELECT
    3 AS displayorder, '< 100%'::TEXT, (SELECT
        lt100
        FROM dbo.capacityaverageom
        WHERE directorateid = 0 AND offendermanagertypeid = 2 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (0::NUMERIC || ' MONTH')::INTERVAL)), (SELECT
        ((lt100 / total) * 100.0) / 100
        FROM dbo.capacityaverageom
        WHERE directorateid = 0 AND offendermanagertypeid = 2 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (0::NUMERIC || ' MONTH')::INTERVAL)) AS current, (SELECT
        ((lt100 / total) * 100.0) / 100
        FROM dbo.capacityaverageom
        WHERE directorateid = 0 AND offendermanagertypeid = 2 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 1::NUMERIC || ' MONTH')::INTERVAL)) AS mth1, (SELECT
        ((lt100 / total) * 100.0) / 100
        FROM dbo.capacityaverageom
        WHERE directorateid = 0 AND offendermanagertypeid = 2 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 2::NUMERIC || ' MONTH')::INTERVAL)) AS mth2, (SELECT
        ((lt100 / total) * 100.0) / 100
        FROM dbo.capacityaverageom
        WHERE directorateid = 0 AND offendermanagertypeid = 2 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 3::NUMERIC || ' MONTH')::INTERVAL)) AS mth3, (SELECT
        ((lt100 / total) * 100.0) / 100
        FROM dbo.capacityaverageom
        WHERE directorateid = 0 AND offendermanagertypeid = 2 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 4::NUMERIC || ' MONTH')::INTERVAL)) AS mth4, (SELECT
        ((lt100 / total) * 100.0) / 100
        FROM dbo.capacityaverageom
        WHERE directorateid = 0 AND offendermanagertypeid = 2 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 5::NUMERIC || ' MONTH')::INTERVAL)) AS mth5, (SELECT
        ((lt100 / total) * 100.0) / 100
        FROM dbo.capacityaverageom
        WHERE directorateid = 0 AND offendermanagertypeid = 2 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 6::NUMERIC || ' MONTH')::INTERVAL)) AS mth6, (SELECT
        ((lt100 / total) * 100.0) / 100
        FROM dbo.capacityaverageom
        WHERE directorateid = 0 AND offendermanagertypeid = 2 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 7::NUMERIC || ' MONTH')::INTERVAL)) AS mth7, (SELECT
        ((lt100 / total) * 100.0) / 100
        FROM dbo.capacityaverageom
        WHERE directorateid = 0 AND offendermanagertypeid = 2 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 8::NUMERIC || ' MONTH')::INTERVAL)) AS mth8, (SELECT
        ((lt100 / total) * 100.0) / 100
        FROM dbo.capacityaverageom
        WHERE directorateid = 0 AND offendermanagertypeid = 2 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 9::NUMERIC || ' MONTH')::INTERVAL)) AS mth9, (SELECT
        ((lt100 / total) * 100.0) / 100
        FROM dbo.capacityaverageom
        WHERE directorateid = 0 AND offendermanagertypeid = 2 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 10::NUMERIC || ' MONTH')::INTERVAL)) AS mth10, (SELECT
        ((lt100 / total) * 100.0) / 100
        FROM dbo.capacityaverageom
        WHERE directorateid = 0 AND offendermanagertypeid = 2 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 11::NUMERIC || ' MONTH')::INTERVAL)) AS mth11, (SELECT
        ((lt100 / total) * 100.0) / 100
        FROM dbo.capacityaverageom
        WHERE directorateid = 0 AND offendermanagertypeid = 2 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 12::NUMERIC || ' MONTH')::INTERVAL)) AS mth12;
/* union */
/* select	4 as DisplayOrder, */
/* 'Total', */
/* (select Total from dbo.CapacityAverageOM where OffenderManagerTypeId = 2 and ReportPeriod = dbo.[GetFirstDayOfMonth](dateadd(m, -1, getdate()))), */
/* (select ((Total/Total) * 100.0) / 100 from dbo.CapacityAverageOM where OffenderManagerTypeId = 2 and ReportPeriod = dbo.[GetFirstDayOfMonth](dateadd(m, -1, getdate()))) as [Current], */
/* (select ((Total/Total) * 100.0) / 100 from dbo.CapacityAverageOM where OffenderManagerTypeId = 2 and ReportPeriod = dbo.[GetFirstDayOfMonth](dateadd(m, -2, getdate()))) as [Mth1], */
/* (select ((Total/Total) * 100.0) / 100 from dbo.CapacityAverageOM where OffenderManagerTypeId = 2 and ReportPeriod = dbo.[GetFirstDayOfMonth](dateadd(m, -3, getdate()))) as [Mth2], */
/* (select ((Total/Total) * 100.0) / 100 from dbo.CapacityAverageOM where OffenderManagerTypeId = 2 and ReportPeriod = dbo.[GetFirstDayOfMonth](dateadd(m, -4, getdate()))) as [Mth3], */
/* (select ((Total/Total) * 100.0) / 100 from dbo.CapacityAverageOM where OffenderManagerTypeId = 2 and ReportPeriod = dbo.[GetFirstDayOfMonth](dateadd(m, -5, getdate()))) as [Mth4], */
/* (select ((Total/Total) * 100.0) / 100 from dbo.CapacityAverageOM where OffenderManagerTypeId = 2 and ReportPeriod = dbo.[GetFirstDayOfMonth](dateadd(m, -7, getdate()))) as [Mth5], */
/* (select ((Total/Total) * 100.0) / 100 from dbo.CapacityAverageOM where OffenderManagerTypeId = 2 and ReportPeriod = dbo.[GetFirstDayOfMonth](dateadd(m, -7, getdate()))) as [Mth6], */
/* (select ((Total/Total) * 100.0) / 100 from dbo.CapacityAverageOM where OffenderManagerTypeId = 2 and ReportPeriod = dbo.[GetFirstDayOfMonth](dateadd(m, -8, getdate()))) as [Mth7], */
/* (select ((Total/Total) * 100.0) / 100 from dbo.CapacityAverageOM where OffenderManagerTypeId = 2 and ReportPeriod = dbo.[GetFirstDayOfMonth](dateadd(m, -9, getdate()))) as [Mth8], */
/* (select ((Total/Total) * 100.0) / 100 from dbo.CapacityAverageOM where OffenderManagerTypeId = 2 and ReportPeriod = dbo.[GetFirstDayOfMonth](dateadd(m, -10, getdate()))) as [Mth9], */
/* (select ((Total/Total) * 100.0) / 100 from dbo.CapacityAverageOM where OffenderManagerTypeId = 2 and ReportPeriod = dbo.[GetFirstDayOfMonth](dateadd(m, -11, getdate()))) as [Mth10], */
/* (select ((Total/Total) * 100.0) / 100 from dbo.CapacityAverageOM where OffenderManagerTypeId = 2 and ReportPeriod = dbo.[GetFirstDayOfMonth](dateadd(m, -12, getdate()))) as [Mth11], */
/* (select ((Total/Total) * 100.0) / 100 from dbo.CapacityAverageOM where OffenderManagerTypeId = 2 and ReportPeriod = dbo.[GetFirstDayOfMonth](dateadd(m, -13, getdate()))) as [Mth12]; */
/* dbo.MgtReportPSOCaseload source */;



CREATE OR REPLACE VIEW dbo.mgtreportpsocaseload (displayorder, casecountdesc, omcount, omtotal, current, mth1, mth2, mth3, mth4, mth5, mth6, mth7, mth8, mth9, mth10, mth11, mth12) AS
SELECT
    1 AS displayorder, '70+'::TEXT AS casecountdesc, (SELECT
        omcountavg
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('70+') AND offendermanagertypeid = 2 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (0::NUMERIC || ' MONTH')::INTERVAL)) AS omcount, (SELECT
        totalomcountavg
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('70+') AND offendermanagertypeid = 2 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (0::NUMERIC || ' MONTH')::INTERVAL)) AS omtotal, (SELECT
        ROUND(CAST(((omcountavg / totalomcountavg) * 100) AS numeric),1)
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('70+') AND offendermanagertypeid = 2 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (0::NUMERIC || ' MONTH')::INTERVAL)) AS current, (SELECT
        ROUND(CAST(((omcountavg / totalomcountavg) * 100) AS numeric),1)
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('70+') AND offendermanagertypeid = 2 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 1::NUMERIC || ' MONTH')::INTERVAL)) AS mth1, (SELECT
        ROUND(CAST(((omcountavg / totalomcountavg) * 100) AS numeric),1)
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('70+') AND offendermanagertypeid = 2 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 2::NUMERIC || ' MONTH')::INTERVAL)) AS mth2, (SELECT
        ROUND(CAST(((omcountavg / totalomcountavg) * 100) AS numeric),1)
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('70+') AND offendermanagertypeid = 2 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 3::NUMERIC || ' MONTH')::INTERVAL)) AS mth3, (SELECT
        ROUND(CAST(((omcountavg / totalomcountavg) * 100) AS numeric),1)
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('70+') AND offendermanagertypeid = 2 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 4::NUMERIC || ' MONTH')::INTERVAL)) AS mth4, (SELECT
        ROUND(CAST(((omcountavg / totalomcountavg) * 100) AS numeric),1)
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('70+') AND offendermanagertypeid = 2 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 5::NUMERIC || ' MONTH')::INTERVAL)) AS mth5, (SELECT
        ROUND(CAST(((omcountavg / totalomcountavg) * 100) AS numeric),1)
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('70+') AND offendermanagertypeid = 2 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 6::NUMERIC || ' MONTH')::INTERVAL)) AS mth6, (SELECT
        ROUND(CAST(((omcountavg / totalomcountavg) * 100) AS numeric),1)
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('70+') AND offendermanagertypeid = 2 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 7::NUMERIC || ' MONTH')::INTERVAL)) AS mth7, (SELECT
        ROUND(CAST(((omcountavg / totalomcountavg) * 100) AS numeric),1)
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('70+') AND offendermanagertypeid = 2 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 8::NUMERIC || ' MONTH')::INTERVAL)) AS mth8, (SELECT
        ROUND(CAST(((omcountavg / totalomcountavg) * 100) AS numeric),1)
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('70+') AND offendermanagertypeid = 2 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 9::NUMERIC || ' MONTH')::INTERVAL)) AS mth9, (SELECT
        ROUND(CAST(((omcountavg / totalomcountavg) * 100) AS numeric),1)
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('70+') AND offendermanagertypeid = 2 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 10::NUMERIC || ' MONTH')::INTERVAL)) AS mth10, (SELECT
        ROUND(CAST(((omcountavg / totalomcountavg) * 100) AS numeric),1)
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('70+') AND offendermanagertypeid = 2 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 11::NUMERIC || ' MONTH')::INTERVAL)) AS mth11, (SELECT
        ROUND(CAST(((omcountavg / totalomcountavg) * 100) AS numeric),1)
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('70+') AND offendermanagertypeid = 2 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 12::NUMERIC || ' MONTH')::INTERVAL)) AS mth12
UNION
SELECT
    2 AS displayorder, '60-69'::TEXT AS casecountdesc, (SELECT
        omcountavg
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('60-69') AND offendermanagertypeid = 2 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (0::NUMERIC || ' MONTH')::INTERVAL)), (SELECT
        totalomcountavg
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('60-69') AND offendermanagertypeid = 2 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (0::NUMERIC || ' MONTH')::INTERVAL)), (SELECT
        ROUND(CAST(((omcountavg / totalomcountavg) * 100) AS numeric),1)
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('60-69') AND offendermanagertypeid = 2 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (0::NUMERIC || ' MONTH')::INTERVAL)), (SELECT
        ROUND(CAST(((omcountavg / totalomcountavg) * 100) AS numeric),1)
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('60-69') AND offendermanagertypeid = 2 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 1::NUMERIC || ' MONTH')::INTERVAL)), (SELECT
        ROUND(CAST(((omcountavg / totalomcountavg) * 100) AS numeric),1)
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('60-69') AND offendermanagertypeid = 2 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 2::NUMERIC || ' MONTH')::INTERVAL)), (SELECT
        ROUND(CAST(((omcountavg / totalomcountavg) * 100) AS numeric),1)
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('60-69') AND offendermanagertypeid = 2 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 3::NUMERIC || ' MONTH')::INTERVAL)), (SELECT
        ROUND(CAST(((omcountavg / totalomcountavg) * 100) AS numeric),1)
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('60-69') AND offendermanagertypeid = 2 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 4::NUMERIC || ' MONTH')::INTERVAL)), (SELECT
        ROUND(CAST(((omcountavg / totalomcountavg) * 100) AS numeric),1)
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('60-69') AND offendermanagertypeid = 2 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 5::NUMERIC || ' MONTH')::INTERVAL)), (SELECT
        ROUND(CAST(((omcountavg / totalomcountavg) * 100) AS numeric),1)
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('60-69') AND offendermanagertypeid = 2 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 6::NUMERIC || ' MONTH')::INTERVAL)), (SELECT
        ROUND(CAST(((omcountavg / totalomcountavg) * 100) AS numeric),1)
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('60-69') AND offendermanagertypeid = 2 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 7::NUMERIC || ' MONTH')::INTERVAL)), (SELECT
        ROUND(CAST(((omcountavg / totalomcountavg) * 100) AS numeric),1)
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('60-69') AND offendermanagertypeid = 2 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 8::NUMERIC || ' MONTH')::INTERVAL)), (SELECT
        ROUND(CAST(((omcountavg / totalomcountavg) * 100) AS numeric),1)
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('60-69') AND offendermanagertypeid = 2 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 9::NUMERIC || ' MONTH')::INTERVAL)), (SELECT
        ROUND(CAST(((omcountavg / totalomcountavg) * 100) AS numeric),1)
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('60-69') AND offendermanagertypeid = 2 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 10::NUMERIC || ' MONTH')::INTERVAL)), (SELECT
        ROUND(CAST(((omcountavg / totalomcountavg) * 100) AS numeric),1)
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('60-69') AND offendermanagertypeid = 2 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 11::NUMERIC || ' MONTH')::INTERVAL)), (SELECT
        ROUND(CAST(((omcountavg / totalomcountavg) * 100) AS numeric),1)
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('60-69') AND offendermanagertypeid = 2 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 12::NUMERIC || ' MONTH')::INTERVAL))
UNION
SELECT
    3 AS displayorder, '50-59'::TEXT AS casecountdesc, (SELECT
        omcountavg
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('50-59') AND offendermanagertypeid = 2 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (0::NUMERIC || ' MONTH')::INTERVAL)), (SELECT
        totalomcountavg
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('50-59') AND offendermanagertypeid = 2 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (0::NUMERIC || ' MONTH')::INTERVAL)), (SELECT
        ROUND(CAST(((omcountavg / totalomcountavg) * 100) AS numeric),1)
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('50-59') AND offendermanagertypeid = 2 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (0::NUMERIC || ' MONTH')::INTERVAL)), (SELECT
        ROUND(CAST(((omcountavg / totalomcountavg) * 100) AS numeric),1)
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('50-59') AND offendermanagertypeid = 2 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 1::NUMERIC || ' MONTH')::INTERVAL)), (SELECT
        ROUND(CAST(((omcountavg / totalomcountavg) * 100) AS numeric),1)
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('50-59') AND offendermanagertypeid = 2 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 2::NUMERIC || ' MONTH')::INTERVAL)), (SELECT
        ROUND(CAST(((omcountavg / totalomcountavg) * 100) AS numeric),1)
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('50-59') AND offendermanagertypeid = 2 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 3::NUMERIC || ' MONTH')::INTERVAL)), (SELECT
        ROUND(CAST(((omcountavg / totalomcountavg) * 100) AS numeric),1)
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('50-59') AND offendermanagertypeid = 2 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 4::NUMERIC || ' MONTH')::INTERVAL)), (SELECT
        ROUND(CAST(((omcountavg / totalomcountavg) * 100) AS numeric),1)
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('50-59') AND offendermanagertypeid = 2 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 5::NUMERIC || ' MONTH')::INTERVAL)), (SELECT
        ROUND(CAST(((omcountavg / totalomcountavg) * 100) AS numeric),1)
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('50-59') AND offendermanagertypeid = 2 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 6::NUMERIC || ' MONTH')::INTERVAL)), (SELECT
        ROUND(CAST(((omcountavg / totalomcountavg) * 100) AS numeric),1)
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('50-59') AND offendermanagertypeid = 2 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 7::NUMERIC || ' MONTH')::INTERVAL)), (SELECT
        ROUND(CAST(((omcountavg / totalomcountavg) * 100) AS numeric),1)
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('50-59') AND offendermanagertypeid = 2 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 8::NUMERIC || ' MONTH')::INTERVAL)), (SELECT
        ROUND(CAST(((omcountavg / totalomcountavg) * 100) AS numeric),1)
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('50-59') AND offendermanagertypeid = 2 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 9::NUMERIC || ' MONTH')::INTERVAL)), (SELECT
        ROUND(CAST(((omcountavg / totalomcountavg) * 100) AS numeric),1)
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('50-59') AND offendermanagertypeid = 2 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 10::NUMERIC || ' MONTH')::INTERVAL)), (SELECT
        ROUND(CAST(((omcountavg / totalomcountavg) * 100) AS numeric),1)
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('50-59') AND offendermanagertypeid = 2 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 11::NUMERIC || ' MONTH')::INTERVAL)), (SELECT
        ROUND(CAST(((omcountavg / totalomcountavg) * 100) AS numeric),1)
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('50-59') AND offendermanagertypeid = 2 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 12::NUMERIC || ' MONTH')::INTERVAL))
UNION
SELECT
    4 AS displayorder, '40-49'::TEXT AS casecountdesc, (SELECT
        omcountavg
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('40-49') AND offendermanagertypeid = 2 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (0::NUMERIC || ' MONTH')::INTERVAL)), (SELECT
        totalomcountavg
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('40-49') AND offendermanagertypeid = 2 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (0::NUMERIC || ' MONTH')::INTERVAL)), (SELECT
        ROUND(CAST(((omcountavg / totalomcountavg) * 100) AS numeric),1)
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('40-49') AND offendermanagertypeid = 2 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (0::NUMERIC || ' MONTH')::INTERVAL)), (SELECT
        ROUND(CAST(((omcountavg / totalomcountavg) * 100) AS numeric),1)
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('40-49') AND offendermanagertypeid = 2 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 1::NUMERIC || ' MONTH')::INTERVAL)), (SELECT
        ROUND(CAST(((omcountavg / totalomcountavg) * 100) AS numeric),1)
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('40-49') AND offendermanagertypeid = 2 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 2::NUMERIC || ' MONTH')::INTERVAL)), (SELECT
        ROUND(CAST(((omcountavg / totalomcountavg) * 100) AS numeric),1)
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('40-49') AND offendermanagertypeid = 2 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 3::NUMERIC || ' MONTH')::INTERVAL)), (SELECT
        ROUND(CAST(((omcountavg / totalomcountavg) * 100) AS numeric),1)
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('40-49') AND offendermanagertypeid = 2 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 4::NUMERIC || ' MONTH')::INTERVAL)), (SELECT
        ROUND(CAST(((omcountavg / totalomcountavg) * 100) AS numeric),1)
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('40-49') AND offendermanagertypeid = 2 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 5::NUMERIC || ' MONTH')::INTERVAL)), (SELECT
        ROUND(CAST(((omcountavg / totalomcountavg) * 100) AS numeric),1)
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('40-49') AND offendermanagertypeid = 2 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 6::NUMERIC || ' MONTH')::INTERVAL)), (SELECT
        ROUND(CAST(((omcountavg / totalomcountavg) * 100) AS numeric),1)
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('40-49') AND offendermanagertypeid = 2 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 7::NUMERIC || ' MONTH')::INTERVAL)), (SELECT
        ROUND(CAST(((omcountavg / totalomcountavg) * 100) AS numeric),1)
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('40-49') AND offendermanagertypeid = 2 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 8::NUMERIC || ' MONTH')::INTERVAL)), (SELECT
        ROUND(CAST(((omcountavg / totalomcountavg) * 100) AS numeric),1)
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('40-49') AND offendermanagertypeid = 2 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 9::NUMERIC || ' MONTH')::INTERVAL)), (SELECT
        ROUND(CAST(((omcountavg / totalomcountavg) * 100) AS numeric),1)
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('40-49') AND offendermanagertypeid = 2 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 10::NUMERIC || ' MONTH')::INTERVAL)), (SELECT
        ROUND(CAST(((omcountavg / totalomcountavg) * 100) AS numeric),1)
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('40-49') AND offendermanagertypeid = 2 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 11::NUMERIC || ' MONTH')::INTERVAL)), (SELECT
        ROUND(CAST(((omcountavg / totalomcountavg) * 100) AS numeric),1)
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('40-49') AND offendermanagertypeid = 2 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 12::NUMERIC || ' MONTH')::INTERVAL))
UNION
SELECT
    5 AS displayorder, '10-39'::TEXT AS casecountdesc, (SELECT
        omcountavg
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('10-39') AND offendermanagertypeid = 2 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (0::NUMERIC || ' MONTH')::INTERVAL)), (SELECT
        totalomcountavg
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('10-39') AND offendermanagertypeid = 2 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (0::NUMERIC || ' MONTH')::INTERVAL)), (SELECT
        ROUND(CAST(((omcountavg / totalomcountavg) * 100) AS numeric),1)
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('10-39') AND offendermanagertypeid = 2 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (0::NUMERIC || ' MONTH')::INTERVAL)), (SELECT
        ROUND(CAST(((omcountavg / totalomcountavg) * 100) AS numeric),1)
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('10-39') AND offendermanagertypeid = 2 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 1::NUMERIC || ' MONTH')::INTERVAL)), (SELECT
        ROUND(CAST(((omcountavg / totalomcountavg) * 100) AS numeric),1)
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('10-39') AND offendermanagertypeid = 2 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 2::NUMERIC || ' MONTH')::INTERVAL)), (SELECT
        ROUND(CAST(((omcountavg / totalomcountavg) * 100) AS numeric),1)
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('10-39') AND offendermanagertypeid = 2 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 3::NUMERIC || ' MONTH')::INTERVAL)), (SELECT
        ROUND(CAST(((omcountavg / totalomcountavg) * 100) AS numeric),1)
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('10-39') AND offendermanagertypeid = 2 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 4::NUMERIC || ' MONTH')::INTERVAL)), (SELECT
        ROUND(CAST(((omcountavg / totalomcountavg) * 100) AS numeric),1)
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('10-39') AND offendermanagertypeid = 2 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 5::NUMERIC || ' MONTH')::INTERVAL)), (SELECT
        ROUND(CAST(((omcountavg / totalomcountavg) * 100) AS numeric),1)
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('10-39') AND offendermanagertypeid = 2 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 6::NUMERIC || ' MONTH')::INTERVAL)), (SELECT
        ROUND(CAST(((omcountavg / totalomcountavg) * 100) AS numeric),1)
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('10-39') AND offendermanagertypeid = 2 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 7::NUMERIC || ' MONTH')::INTERVAL)), (SELECT
        ROUND(CAST(((omcountavg / totalomcountavg) * 100) AS numeric),1)
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('10-39') AND offendermanagertypeid = 2 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 8::NUMERIC || ' MONTH')::INTERVAL)), (SELECT
        ROUND(CAST(((omcountavg / totalomcountavg) * 100) AS numeric),1)
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('10-39') AND offendermanagertypeid = 2 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 9::NUMERIC || ' MONTH')::INTERVAL)), (SELECT
        ROUND(CAST(((omcountavg / totalomcountavg) * 100) AS numeric),1)
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('10-39') AND offendermanagertypeid = 2 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 10::NUMERIC || ' MONTH')::INTERVAL)), (SELECT
        ROUND(CAST(((omcountavg / totalomcountavg) * 100) AS numeric),1)
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('10-39') AND offendermanagertypeid = 2 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 11::NUMERIC || ' MONTH')::INTERVAL)), (SELECT
        ROUND(CAST(((omcountavg / totalomcountavg) * 100) AS numeric),1)
        FROM dbo.caseloadofficer
        WHERE LOWER(casecountdesc) = LOWER('10-39') AND offendermanagertypeid = 2 AND reportperiod = dbo.getfirstdayofmonth(timezone('utc', now()) + (- 12::NUMERIC || ' MONTH')::INTERVAL));
/* dbo.OffenderManagerNote source */
/* Update OffenderManagerNoteView to expose OffenderManagerParentID */;




/* dbo.aggregate_offender_managers_view source */;



CREATE OR REPLACE VIEW dbo.offendermanagernote (id, offendermanagerid, offendermanagerparentid, notes, hoursreduced, lastupdateuserid, lastupdatedusername, lastupdatedatetime) AS
SELECT
    dbo.note.id, dbo.note.offendermanagerid, dbo.note.offendermanagerparentid, dbo.note.notes, dbo.note.hoursreduced, dbo.note.lastupdateuserid, dbo.users.username AS lastupdatedusername, dbo.note.lastupdatedatetime
    FROM dbo.note
    LEFT OUTER JOIN dbo.users
        ON dbo.note.lastupdateuserid = dbo.users.id
    WHERE (dbo.note.offendermanagerid IS NOT NULL) AND (dbo.note.organisationalunitid IS NULL);
/* dbo.OffenderManagers source */;



CREATE OR REPLACE VIEW dbo.offendermanagers (workloadreportid, workloadreportdatetime, teamid, teamname, offendermanagerid, offendermanagername, offendermanagertype, offendermanagertypeid) AS
SELECT
    wr.id AS workloadreportid, wr.date AS workloadreportdatetime, team.id AS teamid, team.name AS teamname, om.id AS offendermanagerid, om.forename || ' ' || om.surname AS offendermanagername, omt.name AS offendermanagertype, om.offendermanagertypeid AS offendermanagertypeid
    FROM dbo.workloadreport AS wr
    INNER JOIN dbo.organisationalunitworkloadreport AS owr
        ON wr.id = owr.workloadreportid
    INNER JOIN dbo.workload AS wl
        ON owr.id = wl.organisationalunitworkloadreportid
    INNER JOIN dbo.organisationalunit AS team
        ON team.id = wl.teamid
    INNER JOIN dbo.offendermanager AS om
        ON om.id = wl.offendermanagerid
    INNER JOIN dbo.offendermanagertype AS omt
        ON omt.id = om.offendermanagertypeid;
/* dbo.OfficerCapacityHistory source */;



CREATE OR REPLACE VIEW dbo.officercapacityhistory (workloadreportdate, offendermanagerid, id, capacityorgunit, capacitypoints, capacitypercentage, capacitypercentagecases, capacitypercentagereports, capacitypercentagerequirements) AS
SELECT
    wr.date AS workloadreportdate, wlo.offendermanagerid, wr.id AS id, dbo.capacityom(SUM(wlo.contractedhoursperweek), SUM(wlo.hoursreduction)) AS capacityorgunit, SUM(wlo.capacitypoints) AS capacitypoints, dbo.capacitypointsperc(dbo.availablepoints(wlo.nominaltarget, wlo.offendermanagertypeid, SUM(wlo.contractedhoursperweek), SUM(wlo.hoursreduction), wlo.defaultcontractedhours, wlo.defaultcontractedhourspso), SUM(wlo.totalpoints), SUM(wlo.sdrpoints), SUM(wlo.sdrconversionpoints), SUM(wlo.paromspoints), SUM(wlo.requirementspoints)) AS capacitypercentage, dbo.capacitypointsperc(dbo.availablepoints(wlo.nominaltarget, wlo.offendermanagertypeid, SUM(wlo.contractedhoursperweek), SUM(wlo.hoursreduction), wlo.defaultcontractedhours, wlo.defaultcontractedhourspso), SUM(wlo.totalpoints), 0, 0, 0, 0) AS capacitypercentagecases, dbo.capacitypointsperc(dbo.availablepoints(wlo.nominaltarget, wlo.offendermanagertypeid, SUM(wlo.contractedhoursperweek), SUM(wlo.hoursreduction), wlo.defaultcontractedhours, wlo.defaultcontractedhourspso), 0, SUM(wlo.sdrpoints), SUM(wlo.sdrconversionpoints), SUM(wlo.paromspoints), 0) AS capacitypercentagereports, dbo.capacitypointsperc(dbo.availablepoints(wlo.nominaltarget, wlo.offendermanagertypeid, SUM(wlo.contractedhoursperweek), SUM(wlo.hoursreduction), wlo.defaultcontractedhours, wlo.defaultcontractedhourspso), 0, 0, 0, 0, SUM(wlo.requirementspoints)) AS capacitypercentagerequirements
    FROM dbo.workloadreportofficer AS wlo
    INNER JOIN dbo.workloadreport AS wr
        ON wr.id = wlo.workloadreportid
    WHERE wr.isdeleted = FALSE
    GROUP BY wr.id, wr.date, wlo.offendermanagerid, wlo.nominaltarget, wlo.offendermanagertypeid, wlo.defaultcontractedhours, wlo.defaultcontractedhourspso;
/* dbo.OfficerCasesHistory source */;



CREATE OR REPLACE VIEW dbo.officercaseshistory (workloadreportid, workloadreportdate, offendermanagerid, offendermanagername, activewarrants, upws, overdueterminations, totalcommcases, totalcustcases, totalcases, activecases, ordercount, capacitypercentage, offendermanagertypeid) AS
SELECT
    wr.id AS workloadreportid, wr.date AS workloadreportdate, offendermanagerid, offendermanagername, SUM(activewarrants) AS activewarrants, SUM(upw) AS upws, SUM(overdueterminations) AS overdueterminations, SUM(comtier0) + SUM(comtier1) + (SUM(CASE WHEN wp.comtier1cpenabled THEN comtier1cp ELSE 0 END)) + SUM(comtier2) + SUM(comtier3n) + SUM(comtier3d) + SUM(comtier4) AS totalcommcases, SUM(custier0) + SUM(custier1) + SUM(custier2) + SUM(custier3) + SUM(custier4) AS totalcustcases, SUM(totalcases) AS totalcases, SUM(totalcases) - SUM(totalcasesinactive) AS activecases, SUM(ordercount) AS ordercount, dbo.capacitypointsperc(dbo.availablepoints(wlo.nominaltarget, wlo.offendermanagertypeid, SUM(wlo.contractedhoursperweek), SUM(wlo.hoursreduction), wlo.defaultcontractedhours, wlo.defaultcontractedhourspso), SUM(wlo.totalpoints), SUM(wlo.sdrpoints), SUM(wlo.sdrconversionpoints), SUM(wlo.paromspoints), SUM(wlo.requirementspoints)) AS capacitypercentage, wlo.offendermanagertypeid
    FROM dbo.workloadreportofficer AS wlo
    INNER JOIN dbo.workloadreport AS wr
        ON wr.id = wlo.workloadreportid
    LEFT OUTER JOIN (SELECT
        comtier1cpenabled
        FROM dbo.workloadpoints
        WHERE isdeleted = FALSE
        ORDER BY createddatetime DESC NULLS FIRST
        LIMIT 1) AS wp
        ON 1 = 1
    WHERE wr.isdeleted = FALSE
    GROUP BY wr.id, wr.date, wlo.offendermanagerid, wlo.offendermanagername, wlo.offendermanagertypeid, wlo.nominaltarget, wlo.offendermanagertypeid, wlo.defaultcontractedhours, wlo.defaultcontractedhourspso, wp.comtier1cpenabled;
/* dbo.OrganisationalUnitInactiveCase source */;



CREATE OR REPLACE VIEW dbo.organisationalunitinactivecase (id, workloadreportid, directorateid, directoratename, lduid, lduname, teamid, teamname, offendermanagerid, offendermanagername, flag, tier, crn) AS
SELECT
    ic.id, owr.workloadreportid, directorate.id AS directorateid, directorate.name AS directoratename, ldu.id AS lduid, ldu.name AS lduname, team.id AS teamid, team.name AS teamname, om.id AS offendermanagerid, om.forename || ' ' || om.surname AS offendermanagername, ic.flag, ic.tier, ic.crn
    FROM dbo.workload AS wl
    INNER JOIN dbo.organisationalunit AS trust
        ON trust.id = wl.trustid
    INNER JOIN dbo.organisationalunit AS directorate
        ON directorate.id = wl.directorateid
    INNER JOIN dbo.organisationalunit AS ldu
        ON ldu.id = wl.lduid
    INNER JOIN dbo.organisationalunit AS team
        ON team.id = wl.teamid
    INNER JOIN dbo.offendermanager AS om
        ON om.id = wl.offendermanagerid
    INNER JOIN dbo.organisationalunitworkloadreport AS owr
        ON owr.id = wl.organisationalunitworkloadreportid
    INNER JOIN dbo.workloadreport AS wr
        ON wr.id = owr.workloadreportid
    INNER JOIN dbo.inactivecase AS ic
        ON wl.id = ic.workloadid;
/* dbo.OrganisationalUnitNote source */
/* Updated Notes Views to also select where User is null */;



CREATE OR REPLACE VIEW dbo.organisationalunitnote (id, organisationalunitid, notes, hoursreduced, lastupdateuserid, lastupdatedusername, lastupdatedatetime, organisationalunittypeid) AS
SELECT
    dbo.note.id, dbo.note.organisationalunitid, dbo.note.notes, dbo.note.hoursreduced, dbo.note.lastupdateuserid, dbo.users.username AS lastupdatedusername, dbo.note.lastupdatedatetime, dbo.organisationalunit.organisationalunittypeid
    FROM dbo.note
    LEFT OUTER JOIN dbo.users
        ON dbo.note.lastupdateuserid = dbo.users.id
    INNER JOIN dbo.organisationalunit
        ON dbo.note.organisationalunitid = dbo.organisationalunit.id
    WHERE (dbo.note.offendermanagerid IS NULL) AND (dbo.note.organisationalunitid IS NOT NULL);
/* dbo.QAPCapacityPOStage1 source */;



CREATE OR REPLACE VIEW dbo.qapcapacitypostage1 (capacity, poscurrent, poscurrentall, pos1mth, pos1mthall, pos2mth, pos2mthall, pos3mth, pos3mthall, pos4mth, pos4mthall, pos5mth, pos5mthall, pos6mth, pos6mthall, pos7mth, pos7mthall, pos8mth, pos8mthall, pos9mth, pos9mthall, pos10mth, pos10mthall, pos11mth, pos11mthall, pos12mth, pos12mthall) AS
SELECT
    '110% +'::TEXT AS capacity, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (1, 6) AND capacitypercentage >= 110) AS poscurrent, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (1, 6)) AS poscurrentall, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 1::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (1, 6) AND capacitypercentage >= 110) AS pos1mth, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 1::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (1, 6)) AS pos1mthall, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 2::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (1, 6) AND capacitypercentage >= 110) AS pos2mth, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 2::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (1, 6)) AS pos2mthall, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 3::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (1, 6) AND capacitypercentage >= 110) AS pos3mth, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 3::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (1, 6)) AS pos3mthall, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 4::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (1, 6) AND capacitypercentage >= 110) AS pos4mth, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 4::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (1, 6)) AS pos4mthall, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 5::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (1, 6) AND capacitypercentage >= 110) AS pos5mth, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 5::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (1, 6)) AS pos5mthall, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 6::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (1, 6) AND capacitypercentage >= 110) AS pos6mth, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 6::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (1, 6)) AS pos6mthall, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 7::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (1, 6) AND capacitypercentage >= 110) AS pos7mth, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 7::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (1, 6)) AS pos7mthall, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 8::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (1, 6) AND capacitypercentage >= 110) AS pos8mth, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 8::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (1, 6)) AS pos8mthall, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 9::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (1, 6) AND capacitypercentage >= 110) AS pos9mth, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 9::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (1, 6)) AS pos9mthall, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 10::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (1, 6) AND capacitypercentage >= 110) AS pos10mth, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 10::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (1, 6)) AS pos10mthall, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 11::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (1, 6) AND capacitypercentage >= 110) AS pos11mth, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 11::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (1, 6)) AS pos11mthall, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 12::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (1, 6) AND capacitypercentage >= 110) AS pos12mth, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 12::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (1, 6)) AS pos12mthall
UNION
SELECT
    '100% +'::TEXT AS capacity, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (1, 6) AND capacitypercentage BETWEEN 100 AND 109) AS poscurrent, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (1, 6)) AS poscurrentall, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 1::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (1, 6) AND capacitypercentage BETWEEN 100 AND 109) AS pos1mth, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 1::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (1, 6)) AS pos1mthall, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 2::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (1, 6) AND capacitypercentage BETWEEN 100 AND 109) AS pos2mth, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 2::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (1, 6)) AS pos2mthall, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 3::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (1, 6) AND capacitypercentage BETWEEN 100 AND 109) AS pos3mth, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 3::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (1, 6)) AS pos3mthall, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 4::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (1, 6) AND capacitypercentage BETWEEN 100 AND 109) AS pos4mth, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 4::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (1, 6)) AS pos4mthall, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 5::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (1, 6) AND capacitypercentage BETWEEN 100 AND 109) AS pos5mth, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 5::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (1, 6)) AS pos5mthall, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 6::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (1, 6) AND capacitypercentage BETWEEN 100 AND 109) AS pos6mth, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 6::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (1, 6)) AS pos6mthall, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 7::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (1, 6) AND capacitypercentage BETWEEN 100 AND 109) AS pos7mth, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 7::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (1, 6)) AS pos7mthall, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 8::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (1, 6) AND capacitypercentage BETWEEN 100 AND 109) AS pos8mth, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 8::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (1, 6)) AS pos8mthall, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 9::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (1, 6) AND capacitypercentage BETWEEN 100 AND 109) AS pos9mth, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 9::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (1, 6)) AS pos9mthall, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 10::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (1, 6) AND capacitypercentage BETWEEN 100 AND 109) AS pos10mth, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 10::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (1, 6)) AS pos10mthall, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 11::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (1, 6) AND capacitypercentage BETWEEN 100 AND 109) AS pos11mth, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 11::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (1, 6)) AS pos11mthall, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 12::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (1, 6) AND capacitypercentage BETWEEN 100 AND 109) AS pos12mth, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 12::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (1, 6)) AS pos12mthall
UNION
SELECT
    '< 100%'::TEXT AS capacity, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (1, 6) AND capacitypercentage < 100) AS poscurrent, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (1, 6)) AS poscurrentall, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 1::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (1, 6) AND capacitypercentage < 100) AS pos1mth, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 1::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (1, 6)) AS pos1mthall, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 2::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (1, 6) AND capacitypercentage < 100) AS pos2mth, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 2::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (1, 6)) AS pos2mthall, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 3::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (1, 6) AND capacitypercentage < 100) AS pos3mth, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 3::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (1, 6)) AS pos3mthall, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 4::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (1, 6) AND capacitypercentage < 100) AS pos4mth, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 4::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (1, 6)) AS pos4mthall, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 5::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (1, 6) AND capacitypercentage < 100) AS pos5mth, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 5::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (1, 6)) AS pos5mthall, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 6::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (1, 6) AND capacitypercentage < 100) AS pos6mth, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 6::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (1, 6)) AS pos6mthall, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 7::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (1, 6) AND capacitypercentage < 100) AS pos7mth, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 7::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (1, 6)) AS pos7mthall, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 8::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (1, 6) AND capacitypercentage < 100) AS pos8mth, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 8::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (1, 6)) AS pos8mthall, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 9::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (1, 6) AND capacitypercentage < 100) AS pos9mth, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 9::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (1, 6)) AS pos9mthall, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 10::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (1, 6) AND capacitypercentage < 100) AS pos10mth, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 10::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (1, 6)) AS pos10mthall, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 11::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (1, 6) AND capacitypercentage < 100) AS pos11mth, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 11::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (1, 6)) AS pos11mthall, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 12::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (1, 6) AND capacitypercentage < 100) AS pos12mth, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 12::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (1, 6)) AS pos12mthall;
/* dbo.QAPCapacityPSOStage1 source */;



CREATE OR REPLACE VIEW dbo.qapcapacitypsostage1 (capacity, psoscurrent, psoscurrentall, psos1mth, psos1mthall, psos2mth, psos2mthall, psos3mth, psos3mthall, psos4mth, psos4mthall, psos5mth, psos5mthall, psos6mth, psos6mthall, psos7mth, psos7mthall, psos8mth, psos8mthall, psos9mth, psos9mthall, psos10mth, psos10mthall, psos11mth, psos11mthall, psos12mth, psos12mthall) AS
SELECT
    '110% +'::TEXT AS capacity, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (2, 7) AND capacitypercentage >= 110) AS psoscurrent, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (2, 7)) AS psoscurrentall, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 1::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (2, 7) AND capacitypercentage >= 110) AS psos1mth, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 1::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (2, 7)) AS psos1mthall, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 2::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (2, 7) AND capacitypercentage >= 110) AS psos2mth, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 2::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (2, 7)) AS psos2mthall, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 3::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (2, 7) AND capacitypercentage >= 110) AS psos3mth, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 3::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (2, 7)) AS psos3mthall, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 4::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (2, 7) AND capacitypercentage >= 110) AS psos4mth, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 4::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (2, 7)) AS psos4mthall, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 5::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (2, 7) AND capacitypercentage >= 110) AS psos5mth, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 5::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (2, 7)) AS psos5mthall, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 6::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (2, 7) AND capacitypercentage >= 110) AS psos6mth, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 6::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (2, 7)) AS psos6mthall, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 7::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (2, 7) AND capacitypercentage >= 110) AS psos7mth, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 7::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (2, 7)) AS psos7mthall, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 8::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (2, 7) AND capacitypercentage >= 110) AS psos8mth, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 8::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (2, 7)) AS psos8mthall, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 9::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (2, 7) AND capacitypercentage >= 110) AS psos9mth, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 9::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (2, 7)) AS psos9mthall, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 10::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (2, 7) AND capacitypercentage >= 110) AS psos10mth, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 10::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (2, 7)) AS psos10mthall, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 11::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (2, 7) AND capacitypercentage >= 110) AS psos11mth, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 11::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (2, 7)) AS psos11mthall, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 12::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (2, 7) AND capacitypercentage >= 110) AS psos12mth, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 12::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (2, 7)) AS psos12mthall
UNION
SELECT
    '100% +'::TEXT AS capacity, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (2, 7) AND capacitypercentage BETWEEN 100 AND 109), (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (2, 7)), (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 1::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (2, 7) AND capacitypercentage BETWEEN 100 AND 109) AS psos1mth, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 1::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (2, 7)) AS psos1mthall, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 2::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (2, 7) AND capacitypercentage BETWEEN 100 AND 109) AS psos2mth, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 2::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (2, 7)) AS psos2mthall, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 3::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (2, 7) AND capacitypercentage BETWEEN 100 AND 109) AS psos3mth, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 3::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (2, 7)) AS psos3mthall, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 4::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (2, 7) AND capacitypercentage BETWEEN 100 AND 109) AS psos4mth, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 4::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (2, 7)) AS psos4mthall, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 5::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (2, 7) AND capacitypercentage BETWEEN 100 AND 109) AS psos5mth, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 5::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (2, 7)) AS psos5mthall, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 6::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (2, 7) AND capacitypercentage BETWEEN 100 AND 109) AS psos6mth, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 6::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (2, 7)) AS psos6mthall, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 7::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (2, 7) AND capacitypercentage BETWEEN 100 AND 109) AS psos7mth, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 7::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (2, 7)) AS psos7mthall, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 8::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (2, 7) AND capacitypercentage BETWEEN 100 AND 109) AS psos8mth, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 8::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (2, 7)) AS psos8mthall, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 9::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (2, 7) AND capacitypercentage BETWEEN 100 AND 109) AS psos9mth, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 9::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (2, 7)) AS psos9mthall, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 10::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (2, 7) AND capacitypercentage BETWEEN 100 AND 109) AS psos10mth, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 10::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (2, 7)) AS psos10mthall, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 11::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (2, 7) AND capacitypercentage BETWEEN 100 AND 109) AS psos11mth, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 11::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (2, 7)) AS psos11mthall, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 12::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (2, 7) AND capacitypercentage BETWEEN 100 AND 109) AS psos12mth, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 12::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (2, 7)) AS psos12mthall
UNION
SELECT
    '< 100%'::TEXT AS capacity, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (2, 7) AND capacitypercentage < 100), (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (2, 7)), (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 1::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (2, 7) AND capacitypercentage < 100) AS psos1mth, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 1::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (2, 7)) AS psos1mthall, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 2::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (2, 7) AND capacitypercentage < 100) AS psos2mth, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 2::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (2, 7)) AS psos2mthall, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 3::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (2, 7) AND capacitypercentage < 100) AS psos3mth, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 3::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (2, 7)) AS psos3mthall, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 4::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (2, 7) AND capacitypercentage < 100) AS psos4mth, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 4::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (2, 7)) AS psos4mthall, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 5::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (2, 7) AND capacitypercentage < 100) AS psos5mth, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 5::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (2, 7)) AS psos5mthall, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 6::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (2, 7) AND capacitypercentage < 100) AS psos6mth, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 6::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (2, 7)) AS psos6mthall, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 7::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (2, 7) AND capacitypercentage < 100) AS psos7mth, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 7::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (2, 7)) AS psos7mthall, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 8::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (2, 7) AND capacitypercentage < 100) AS psos8mth, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 8::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (2, 7)) AS psos8mthall, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 9::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (2, 7) AND capacitypercentage < 100) AS psos9mth, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 9::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (2, 7)) AS psos9mthall, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 10::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (2, 7) AND capacitypercentage < 100) AS psos10mth, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 10::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (2, 7)) AS psos10mthall, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 11::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (2, 7) AND capacitypercentage < 100) AS psos11mth, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 11::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (2, 7)) AS psos11mthall, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 12::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (2, 7) AND capacitypercentage < 100) AS psos12mth, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 12::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (2, 7)) AS psos12mthall;
/* dbo.QAPCaseloadPOStage1 source */;



CREATE OR REPLACE VIEW dbo.qapcaseloadpostage1 (cases, poscurrent, poscurrentall, pos3mths, pos3mthsall, pos12mths, pos12mthsall) AS
SELECT
    '60+'::TEXT AS cases, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (1, 6) AND (totalcases - totalcasesinactive) > 60) AS poscurrent, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (1, 6)) AS poscurrentall, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 3::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (1, 6) AND (totalcases - totalcasesinactive) > 60) AS pos3mths, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 3::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (1, 6)) AS pos3mthsall, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 12::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (1, 6) AND (totalcases - totalcasesinactive) > 60) AS pos12mths, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 12::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (1, 6)) AS pos12mthsall
UNION
SELECT
    '50-60'::TEXT, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (1, 6) AND (totalcases - totalcasesinactive) BETWEEN 50 AND 60), (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (1, 6)), (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 3::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (1, 6) AND (totalcases - totalcasesinactive) BETWEEN 50 AND 60), (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 3::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (1, 6)), (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 12::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (1, 6) AND (totalcases - totalcasesinactive) BETWEEN 50 AND 60), (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 12::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (1, 6))
UNION
SELECT
    '40-50'::TEXT, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (1, 6) AND (totalcases - totalcasesinactive) BETWEEN 40 AND 50), (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (1, 6)), (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 3::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (1, 6) AND (totalcases - totalcasesinactive) BETWEEN 40 AND 50), (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 3::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (1, 6)), (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 12::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (1, 6) AND (totalcases - totalcasesinactive) BETWEEN 40 AND 50), (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 12::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (1, 6))
UNION
SELECT
    '30-40'::TEXT, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (1, 6) AND (totalcases - totalcasesinactive) BETWEEN 30 AND 40), (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (1, 6)), (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 3::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (1, 6) AND (totalcases - totalcasesinactive) BETWEEN 30 AND 40), (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 3::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (1, 6)), (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 12::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (1, 6) AND (totalcases - totalcasesinactive) BETWEEN 30 AND 40), (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 12::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (1, 6))
UNION
SELECT
    '< 30'::TEXT, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (1, 6) AND (totalcases - totalcasesinactive) < 30), (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (1, 6)), (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 3::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (1, 6) AND (totalcases - totalcasesinactive) < 30), (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 3::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (1, 6)), (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 12::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (1, 6) AND (totalcases - totalcasesinactive) < 30), (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 12::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (1, 6));
/* dbo.QAPCaseloadPOStageAvg1 source */;



CREATE OR REPLACE VIEW dbo.qapcaseloadpostageavg1 (cases, current, currentall, "1Mth", "1MthAll", "2Mth", "2MthAll", "3Mth", "3MthAll", "4Mth", "4MthAll", "5Mth", "5MthAll", "6Mth", "6MthAll", "7Mth", "7MthAll", "8Mth", "8MthAll", "9Mth", "9MthAll", "10Mth", "10MthAll", "11Mth", "11MthAll", "12Mth", "12MthAll") AS
SELECT
    '60+'::TEXT AS "Cases", dbo.calccaseloadpoavg(60::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 1::NUMERIC(10, 0)) AS "Current", dbo.calccaseloadpoavg(0::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 1::NUMERIC(10, 0)) AS "CurrentAll", dbo.calccaseloadpoavg(60::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 2::NUMERIC(10, 0)) AS "1Mth", dbo.calccaseloadpoavg(0::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 2::NUMERIC(10, 0)) AS "1MthAll", dbo.calccaseloadpoavg(60::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 3::NUMERIC(10, 0)) AS "2Mth", dbo.calccaseloadpoavg(0::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 3::NUMERIC(10, 0)) AS "2MthAll", dbo.calccaseloadpoavg(60::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 4::NUMERIC(10, 0)) AS "3Mth", dbo.calccaseloadpoavg(0::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 4::NUMERIC(10, 0)) AS "3MthAll", dbo.calccaseloadpoavg(60::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 5::NUMERIC(10, 0)) AS "4Mth", dbo.calccaseloadpoavg(0::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 5::NUMERIC(10, 0)) AS "4MthAll", dbo.calccaseloadpoavg(60::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 6::NUMERIC(10, 0)) AS "5Mth", dbo.calccaseloadpoavg(0::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 6::NUMERIC(10, 0)) AS "5MthAll", dbo.calccaseloadpoavg(60::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 7::NUMERIC(10, 0)) AS "6Mth", dbo.calccaseloadpoavg(0::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 7::NUMERIC(10, 0)) AS "6MthAll", dbo.calccaseloadpoavg(60::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 8::NUMERIC(10, 0)) AS "7Mth", dbo.calccaseloadpoavg(0::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 8::NUMERIC(10, 0)) AS "7MthAll", dbo.calccaseloadpoavg(60::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 9::NUMERIC(10, 0)) AS "8Mth", dbo.calccaseloadpoavg(0::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 9::NUMERIC(10, 0)) AS "8MthAll", dbo.calccaseloadpoavg(60::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 10::NUMERIC(10, 0)) AS "9Mth", dbo.calccaseloadpoavg(0::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 10::NUMERIC(10, 0)) AS "9MthAll", dbo.calccaseloadpoavg(60::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 11::NUMERIC(10, 0)) AS "10Mth", dbo.calccaseloadpoavg(0::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 11::NUMERIC(10, 0)) AS "10MthAll", dbo.calccaseloadpoavg(60::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 12::NUMERIC(10, 0)) AS "11Mth", dbo.calccaseloadpoavg(0::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 12::NUMERIC(10, 0)) AS "11MthAll", dbo.calccaseloadpoavg(60::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 13::NUMERIC(10, 0)) AS "12Mth", dbo.calccaseloadpoavg(0::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 13::NUMERIC(10, 0)) AS "12MthAll"
UNION
SELECT
    '50-59'::TEXT AS "Cases", dbo.calccaseloadpoavg(50::NUMERIC(10, 0), 59::NUMERIC(10, 0), - 1::NUMERIC(10, 0)) AS "Current", dbo.calccaseloadpoavg(0::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 1::NUMERIC(10, 0)) AS "CurrentAll", dbo.calccaseloadpoavg(50::NUMERIC(10, 0), 59::NUMERIC(10, 0), - 2::NUMERIC(10, 0)) AS "1Mth", dbo.calccaseloadpoavg(0::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 2::NUMERIC(10, 0)) AS "1MthAll", dbo.calccaseloadpoavg(50::NUMERIC(10, 0), 59::NUMERIC(10, 0), - 3::NUMERIC(10, 0)) AS "2Mth", dbo.calccaseloadpoavg(0::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 3::NUMERIC(10, 0)) AS "2MthAll", dbo.calccaseloadpoavg(50::NUMERIC(10, 0), 59::NUMERIC(10, 0), - 4::NUMERIC(10, 0)) AS "3Mth", dbo.calccaseloadpoavg(0::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 4::NUMERIC(10, 0)) AS "3MthAll", dbo.calccaseloadpoavg(50::NUMERIC(10, 0), 59::NUMERIC(10, 0), - 5::NUMERIC(10, 0)) AS "4Mth", dbo.calccaseloadpoavg(0::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 5::NUMERIC(10, 0)) AS "4MthAll", dbo.calccaseloadpoavg(50::NUMERIC(10, 0), 59::NUMERIC(10, 0), - 6::NUMERIC(10, 0)) AS "5Mth", dbo.calccaseloadpoavg(0::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 6::NUMERIC(10, 0)) AS "5MthAll", dbo.calccaseloadpoavg(50::NUMERIC(10, 0), 59::NUMERIC(10, 0), - 7::NUMERIC(10, 0)) AS "6Mth", dbo.calccaseloadpoavg(0::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 7::NUMERIC(10, 0)) AS "6MthAll", dbo.calccaseloadpoavg(50::NUMERIC(10, 0), 59::NUMERIC(10, 0), - 8::NUMERIC(10, 0)) AS "7Mth", dbo.calccaseloadpoavg(0::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 8::NUMERIC(10, 0)) AS "7MthAll", dbo.calccaseloadpoavg(50::NUMERIC(10, 0), 59::NUMERIC(10, 0), - 9::NUMERIC(10, 0)) AS "8Mth", dbo.calccaseloadpoavg(0::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 9::NUMERIC(10, 0)) AS "8MthAll", dbo.calccaseloadpoavg(50::NUMERIC(10, 0), 59::NUMERIC(10, 0), - 10::NUMERIC(10, 0)) AS "9Mth", dbo.calccaseloadpoavg(0::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 10::NUMERIC(10, 0)) AS "9MthAll", dbo.calccaseloadpoavg(50::NUMERIC(10, 0), 59::NUMERIC(10, 0), - 11::NUMERIC(10, 0)) AS "10Mth", dbo.calccaseloadpoavg(0::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 11::NUMERIC(10, 0)) AS "10MthAll", dbo.calccaseloadpoavg(50::NUMERIC(10, 0), 59::NUMERIC(10, 0), - 12::NUMERIC(10, 0)) AS "11Mth", dbo.calccaseloadpoavg(0::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 12::NUMERIC(10, 0)) AS "11MthAll", dbo.calccaseloadpoavg(50::NUMERIC(10, 0), 59::NUMERIC(10, 0), - 13::NUMERIC(10, 0)) AS "12Mth", dbo.calccaseloadpoavg(0::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 13::NUMERIC(10, 0)) AS "12MthAll"
UNION
SELECT
    '40-49'::TEXT AS "Cases", dbo.calccaseloadpoavg(40::NUMERIC(10, 0), 49::NUMERIC(10, 0), - 1::NUMERIC(10, 0)) AS "Current", dbo.calccaseloadpoavg(0::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 1::NUMERIC(10, 0)) AS "CurrentAll", dbo.calccaseloadpoavg(40::NUMERIC(10, 0), 49::NUMERIC(10, 0), - 2::NUMERIC(10, 0)) AS "1Mth", dbo.calccaseloadpoavg(0::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 2::NUMERIC(10, 0)) AS "1MthAll", dbo.calccaseloadpoavg(40::NUMERIC(10, 0), 49::NUMERIC(10, 0), - 3::NUMERIC(10, 0)) AS "2Mth", dbo.calccaseloadpoavg(0::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 3::NUMERIC(10, 0)) AS "2MthAll", dbo.calccaseloadpoavg(40::NUMERIC(10, 0), 49::NUMERIC(10, 0), - 4::NUMERIC(10, 0)) AS "3Mth", dbo.calccaseloadpoavg(0::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 4::NUMERIC(10, 0)) AS "3MthAll", dbo.calccaseloadpoavg(40::NUMERIC(10, 0), 49::NUMERIC(10, 0), - 5::NUMERIC(10, 0)) AS "4Mth", dbo.calccaseloadpoavg(0::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 5::NUMERIC(10, 0)) AS "4MthAll", dbo.calccaseloadpoavg(40::NUMERIC(10, 0), 49::NUMERIC(10, 0), - 6::NUMERIC(10, 0)) AS "5Mth", dbo.calccaseloadpoavg(0::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 6::NUMERIC(10, 0)) AS "5MthAll", dbo.calccaseloadpoavg(40::NUMERIC(10, 0), 49::NUMERIC(10, 0), - 7::NUMERIC(10, 0)) AS "6Mth", dbo.calccaseloadpoavg(0::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 7::NUMERIC(10, 0)) AS "6MthAll", dbo.calccaseloadpoavg(40::NUMERIC(10, 0), 49::NUMERIC(10, 0), - 8::NUMERIC(10, 0)) AS "7Mth", dbo.calccaseloadpoavg(0::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 8::NUMERIC(10, 0)) AS "7MthAll", dbo.calccaseloadpoavg(40::NUMERIC(10, 0), 49::NUMERIC(10, 0), - 9::NUMERIC(10, 0)) AS "8Mth", dbo.calccaseloadpoavg(0::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 9::NUMERIC(10, 0)) AS "8MthAll", dbo.calccaseloadpoavg(40::NUMERIC(10, 0), 49::NUMERIC(10, 0), - 10::NUMERIC(10, 0)) AS "9Mth", dbo.calccaseloadpoavg(0::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 10::NUMERIC(10, 0)) AS "9MthAll", dbo.calccaseloadpoavg(40::NUMERIC(10, 0), 49::NUMERIC(10, 0), - 11::NUMERIC(10, 0)) AS "10Mth", dbo.calccaseloadpoavg(0::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 11::NUMERIC(10, 0)) AS "10MthAll", dbo.calccaseloadpoavg(40::NUMERIC(10, 0), 49::NUMERIC(10, 0), - 12::NUMERIC(10, 0)) AS "11Mth", dbo.calccaseloadpoavg(0::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 12::NUMERIC(10, 0)) AS "11MthAll", dbo.calccaseloadpoavg(40::NUMERIC(10, 0), 49::NUMERIC(10, 0), - 13::NUMERIC(10, 0)) AS "12Mth", dbo.calccaseloadpoavg(0::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 13::NUMERIC(10, 0)) AS "12MthAll"
UNION
SELECT
    '30-39'::TEXT AS "Cases", dbo.calccaseloadpoavg(30::NUMERIC(10, 0), 39::NUMERIC(10, 0), - 1::NUMERIC(10, 0)) AS "Current", dbo.calccaseloadpoavg(0::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 1::NUMERIC(10, 0)) AS "CurrentAll", dbo.calccaseloadpoavg(30::NUMERIC(10, 0), 39::NUMERIC(10, 0), - 2::NUMERIC(10, 0)) AS "1Mth", dbo.calccaseloadpoavg(0::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 2::NUMERIC(10, 0)) AS "1MthAll", dbo.calccaseloadpoavg(30::NUMERIC(10, 0), 39::NUMERIC(10, 0), - 3::NUMERIC(10, 0)) AS "2Mth", dbo.calccaseloadpoavg(0::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 3::NUMERIC(10, 0)) AS "2MthAll", dbo.calccaseloadpoavg(30::NUMERIC(10, 0), 39::NUMERIC(10, 0), - 4::NUMERIC(10, 0)) AS "3Mth", dbo.calccaseloadpoavg(0::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 4::NUMERIC(10, 0)) AS "3MthAll", dbo.calccaseloadpoavg(30::NUMERIC(10, 0), 39::NUMERIC(10, 0), - 5::NUMERIC(10, 0)) AS "4Mth", dbo.calccaseloadpoavg(0::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 5::NUMERIC(10, 0)) AS "4MthAll", dbo.calccaseloadpoavg(30::NUMERIC(10, 0), 39::NUMERIC(10, 0), - 6::NUMERIC(10, 0)) AS "5Mth", dbo.calccaseloadpoavg(0::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 6::NUMERIC(10, 0)) AS "5MthAll", dbo.calccaseloadpoavg(30::NUMERIC(10, 0), 39::NUMERIC(10, 0), - 7::NUMERIC(10, 0)) AS "6Mth", dbo.calccaseloadpoavg(0::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 7::NUMERIC(10, 0)) AS "6MthAll", dbo.calccaseloadpoavg(30::NUMERIC(10, 0), 39::NUMERIC(10, 0), - 8::NUMERIC(10, 0)) AS "7Mth", dbo.calccaseloadpoavg(0::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 8::NUMERIC(10, 0)) AS "7MthAll", dbo.calccaseloadpoavg(30::NUMERIC(10, 0), 39::NUMERIC(10, 0), - 9::NUMERIC(10, 0)) AS "8Mth", dbo.calccaseloadpoavg(0::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 9::NUMERIC(10, 0)) AS "8MthAll", dbo.calccaseloadpoavg(30::NUMERIC(10, 0), 39::NUMERIC(10, 0), - 10::NUMERIC(10, 0)) AS "9Mth", dbo.calccaseloadpoavg(0::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 10::NUMERIC(10, 0)) AS "9MthAll", dbo.calccaseloadpoavg(30::NUMERIC(10, 0), 39::NUMERIC(10, 0), - 11::NUMERIC(10, 0)) AS "10Mth", dbo.calccaseloadpoavg(0::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 11::NUMERIC(10, 0)) AS "10MthAll", dbo.calccaseloadpoavg(30::NUMERIC(10, 0), 39::NUMERIC(10, 0), - 12::NUMERIC(10, 0)) AS "11Mth", dbo.calccaseloadpoavg(0::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 12::NUMERIC(10, 0)) AS "11MthAll", dbo.calccaseloadpoavg(30::NUMERIC(10, 0), 39::NUMERIC(10, 0), - 13::NUMERIC(10, 0)) AS "12Mth", dbo.calccaseloadpoavg(0::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 13::NUMERIC(10, 0)) AS "12MthAll"
UNION
SELECT
    '10-29'::TEXT AS "Cases", dbo.calccaseloadpoavg(10::NUMERIC(10, 0), 29::NUMERIC(10, 0), - 1::NUMERIC(10, 0)) AS "Current", dbo.calccaseloadpoavg(0::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 1::NUMERIC(10, 0)) AS "CurrentAll", dbo.calccaseloadpoavg(10::NUMERIC(10, 0), 29::NUMERIC(10, 0), - 2::NUMERIC(10, 0)) AS "1Mth", dbo.calccaseloadpoavg(0::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 2::NUMERIC(10, 0)) AS "1MthAll", dbo.calccaseloadpoavg(10::NUMERIC(10, 0), 29::NUMERIC(10, 0), - 3::NUMERIC(10, 0)) AS "2Mth", dbo.calccaseloadpoavg(0::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 3::NUMERIC(10, 0)) AS "2MthAll", dbo.calccaseloadpoavg(10::NUMERIC(10, 0), 29::NUMERIC(10, 0), - 4::NUMERIC(10, 0)) AS "3Mth", dbo.calccaseloadpoavg(0::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 4::NUMERIC(10, 0)) AS "3MthAll", dbo.calccaseloadpoavg(10::NUMERIC(10, 0), 29::NUMERIC(10, 0), - 5::NUMERIC(10, 0)) AS "4Mth", dbo.calccaseloadpoavg(0::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 5::NUMERIC(10, 0)) AS "4MthAll", dbo.calccaseloadpoavg(10::NUMERIC(10, 0), 29::NUMERIC(10, 0), - 6::NUMERIC(10, 0)) AS "5Mth", dbo.calccaseloadpoavg(0::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 6::NUMERIC(10, 0)) AS "5MthAll", dbo.calccaseloadpoavg(10::NUMERIC(10, 0), 29::NUMERIC(10, 0), - 7::NUMERIC(10, 0)) AS "6Mth", dbo.calccaseloadpoavg(0::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 7::NUMERIC(10, 0)) AS "6MthAll", dbo.calccaseloadpoavg(10::NUMERIC(10, 0), 29::NUMERIC(10, 0), - 8::NUMERIC(10, 0)) AS "7Mth", dbo.calccaseloadpoavg(0::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 8::NUMERIC(10, 0)) AS "7MthAll", dbo.calccaseloadpoavg(10::NUMERIC(10, 0), 29::NUMERIC(10, 0), - 9::NUMERIC(10, 0)) AS "8Mth", dbo.calccaseloadpoavg(0::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 9::NUMERIC(10, 0)) AS "8MthAll", dbo.calccaseloadpoavg(10::NUMERIC(10, 0), 29::NUMERIC(10, 0), - 10::NUMERIC(10, 0)) AS "9Mth", dbo.calccaseloadpoavg(0::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 10::NUMERIC(10, 0)) AS "9MthAll", dbo.calccaseloadpoavg(10::NUMERIC(10, 0), 29::NUMERIC(10, 0), - 11::NUMERIC(10, 0)) AS "10Mth", dbo.calccaseloadpoavg(0::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 11::NUMERIC(10, 0)) AS "10MthAll", dbo.calccaseloadpoavg(10::NUMERIC(10, 0), 29::NUMERIC(10, 0), - 12::NUMERIC(10, 0)) AS "11Mth", dbo.calccaseloadpoavg(0::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 12::NUMERIC(10, 0)) AS "11MthAll", dbo.calccaseloadpoavg(10::NUMERIC(10, 0), 29::NUMERIC(10, 0), - 13::NUMERIC(10, 0)) AS "12Mth", dbo.calccaseloadpoavg(0::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 13::NUMERIC(10, 0)) AS "12MthAll";
/* dbo.QAPCaseloadPSOStage1 source */;



CREATE OR REPLACE VIEW dbo.qapcaseloadpsostage1 (cases, psoscurrent, psoscurrentall, psos3mths, psos3mthsall, psos12mths, psos12mthsall) AS
SELECT
    '80+'::TEXT AS cases, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (2, 7) AND (totalcases - totalcasesinactive) > 80) AS psoscurrent, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (2, 7)) AS psoscurrentall, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 3::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (2, 7) AND (totalcases - totalcasesinactive) > 80) AS psos3mths, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 3::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (2, 7)) AS psos3mthsall, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 12::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (2, 7) AND (totalcases - totalcasesinactive) > 80) AS psos12mths, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 12::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (2, 7)) AS psos12mthsall
UNION
SELECT
    '70-80'::TEXT, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (2, 7) AND (totalcases - totalcasesinactive) BETWEEN 70 AND 80), (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (2, 7)), (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 3::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (2, 7) AND (totalcases - totalcasesinactive) BETWEEN 70 AND 80), (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 3::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (2, 7)), (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 12::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (2, 7) AND (totalcases - totalcasesinactive) BETWEEN 70 AND 80), (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 12::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (2, 7))
UNION
SELECT
    '60-70'::TEXT, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (2, 7) AND (totalcases - totalcasesinactive) BETWEEN 60 AND 70), (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (2, 7)), (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 3::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (2, 7) AND (totalcases - totalcasesinactive) BETWEEN 60 AND 70), (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 3::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (2, 7)), (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 12::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (2, 7) AND (totalcases - totalcasesinactive) BETWEEN 60 AND 70), (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 12::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (2, 7))
UNION
SELECT
    '50-60'::TEXT, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (2, 7) AND (totalcases - totalcasesinactive) BETWEEN 50 AND 60), (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (2, 7)), (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 3::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (2, 7) AND (totalcases - totalcasesinactive) BETWEEN 50 AND 60), (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 3::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (2, 7)), (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 12::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (2, 7) AND (totalcases - totalcasesinactive) BETWEEN 50 AND 60), (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 12::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (2, 7))
UNION
SELECT
    '< 50'::TEXT, (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (2, 7) AND (totalcases - totalcasesinactive) < 50), (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (2, 7)), (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 3::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (2, 7) AND (totalcases - totalcasesinactive) < 50), (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 3::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (2, 7)), (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 12::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (2, 7) AND (totalcases - totalcasesinactive) < 50), (SELECT
        COUNT('x')
        FROM dbo.workloadreportofficer
        WHERE workloadreportid IN (SELECT
            id
            FROM dbo.workloadreport
            WHERE timezone('utc', now()) + (- 12::NUMERIC || ' MONTH')::INTERVAL > date
            ORDER BY date DESC NULLS FIRST
            LIMIT 1) AND offendermanagertypeid IN (2, 7));
/* [dbo].[CalcCaseloadPSOAvg] source */;



CREATE OR REPLACE VIEW dbo.qapcaseloadpsostageavg1 (cases, current, currentall, "1Mth", "1MthAll", "2Mth", "2MthAll", "3Mth", "3MthAll", "4Mth", "4MthAll", "5Mth", "5MthAll", "6Mth", "6MthAll", "7Mth", "7MthAll", "8Mth", "8MthAll", "9Mth", "9MthAll", "10Mth", "10MthAll", "11Mth", "11MthAll", "12Mth", "12MthAll") AS
SELECT
    '70+'::TEXT AS "Cases", dbo.calccaseloadpsoavg(70::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 1::NUMERIC(10, 0)) AS "Current", dbo.calccaseloadpsoavg(0::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 1::NUMERIC(10, 0)) AS "CurrentAll", dbo.calccaseloadpsoavg(70::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 2::NUMERIC(10, 0)) AS "1Mth", dbo.calccaseloadpsoavg(0::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 2::NUMERIC(10, 0)) AS "1MthAll", dbo.calccaseloadpsoavg(70::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 3::NUMERIC(10, 0)) AS "2Mth", dbo.calccaseloadpsoavg(0::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 3::NUMERIC(10, 0)) AS "2MthAll", dbo.calccaseloadpsoavg(70::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 4::NUMERIC(10, 0)) AS "3Mth", dbo.calccaseloadpsoavg(0::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 4::NUMERIC(10, 0)) AS "3MthAll", dbo.calccaseloadpsoavg(70::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 5::NUMERIC(10, 0)) AS "4Mth", dbo.calccaseloadpsoavg(0::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 5::NUMERIC(10, 0)) AS "4MthAll", dbo.calccaseloadpsoavg(70::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 6::NUMERIC(10, 0)) AS "5Mth", dbo.calccaseloadpsoavg(0::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 6::NUMERIC(10, 0)) AS "5MthAll", dbo.calccaseloadpsoavg(70::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 7::NUMERIC(10, 0)) AS "6Mth", dbo.calccaseloadpsoavg(0::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 7::NUMERIC(10, 0)) AS "6MthAll", dbo.calccaseloadpsoavg(70::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 8::NUMERIC(10, 0)) AS "7Mth", dbo.calccaseloadpsoavg(0::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 8::NUMERIC(10, 0)) AS "7MthAll", dbo.calccaseloadpsoavg(70::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 9::NUMERIC(10, 0)) AS "8Mth", dbo.calccaseloadpsoavg(0::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 9::NUMERIC(10, 0)) AS "8MthAll", dbo.calccaseloadpsoavg(70::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 10::NUMERIC(10, 0)) AS "9Mth", dbo.calccaseloadpsoavg(0::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 10::NUMERIC(10, 0)) AS "9MthAll", dbo.calccaseloadpsoavg(70::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 11::NUMERIC(10, 0)) AS "10Mth", dbo.calccaseloadpsoavg(0::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 11::NUMERIC(10, 0)) AS "10MthAll", dbo.calccaseloadpsoavg(70::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 12::NUMERIC(10, 0)) AS "11Mth", dbo.calccaseloadpsoavg(0::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 12::NUMERIC(10, 0)) AS "11MthAll", dbo.calccaseloadpsoavg(70::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 13::NUMERIC(10, 0)) AS "12Mth", dbo.calccaseloadpsoavg(0::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 13::NUMERIC(10, 0)) AS "12MthAll"
UNION
SELECT
    '60-69'::TEXT AS "Cases", dbo.calccaseloadpsoavg(60::NUMERIC(10, 0), 69::NUMERIC(10, 0), - 1::NUMERIC(10, 0)) AS "Current", dbo.calccaseloadpsoavg(0::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 1::NUMERIC(10, 0)) AS "CurrentAll", dbo.calccaseloadpsoavg(60::NUMERIC(10, 0), 69::NUMERIC(10, 0), - 2::NUMERIC(10, 0)) AS "1Mth", dbo.calccaseloadpsoavg(0::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 2::NUMERIC(10, 0)) AS "1MthAll", dbo.calccaseloadpsoavg(60::NUMERIC(10, 0), 69::NUMERIC(10, 0), - 3::NUMERIC(10, 0)) AS "2Mth", dbo.calccaseloadpsoavg(0::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 3::NUMERIC(10, 0)) AS "2MthAll", dbo.calccaseloadpsoavg(60::NUMERIC(10, 0), 69::NUMERIC(10, 0), - 4::NUMERIC(10, 0)) AS "3Mth", dbo.calccaseloadpsoavg(0::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 4::NUMERIC(10, 0)) AS "3MthAll", dbo.calccaseloadpsoavg(60::NUMERIC(10, 0), 69::NUMERIC(10, 0), - 5::NUMERIC(10, 0)) AS "4Mth", dbo.calccaseloadpsoavg(0::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 5::NUMERIC(10, 0)) AS "4MthAll", dbo.calccaseloadpsoavg(60::NUMERIC(10, 0), 69::NUMERIC(10, 0), - 6::NUMERIC(10, 0)) AS "5Mth", dbo.calccaseloadpsoavg(0::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 6::NUMERIC(10, 0)) AS "5MthAll", dbo.calccaseloadpsoavg(60::NUMERIC(10, 0), 69::NUMERIC(10, 0), - 7::NUMERIC(10, 0)) AS "6Mth", dbo.calccaseloadpsoavg(0::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 7::NUMERIC(10, 0)) AS "6MthAll", dbo.calccaseloadpsoavg(60::NUMERIC(10, 0), 69::NUMERIC(10, 0), - 8::NUMERIC(10, 0)) AS "7Mth", dbo.calccaseloadpsoavg(0::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 8::NUMERIC(10, 0)) AS "7MthAll", dbo.calccaseloadpsoavg(60::NUMERIC(10, 0), 69::NUMERIC(10, 0), - 9::NUMERIC(10, 0)) AS "8Mth", dbo.calccaseloadpsoavg(0::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 9::NUMERIC(10, 0)) AS "8MthAll", dbo.calccaseloadpsoavg(60::NUMERIC(10, 0), 69::NUMERIC(10, 0), - 10::NUMERIC(10, 0)) AS "9Mth", dbo.calccaseloadpsoavg(0::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 10::NUMERIC(10, 0)) AS "9MthAll", dbo.calccaseloadpsoavg(60::NUMERIC(10, 0), 69::NUMERIC(10, 0), - 11::NUMERIC(10, 0)) AS "10Mth", dbo.calccaseloadpsoavg(0::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 11::NUMERIC(10, 0)) AS "10MthAll", dbo.calccaseloadpsoavg(60::NUMERIC(10, 0), 69::NUMERIC(10, 0), - 12::NUMERIC(10, 0)) AS "11Mth", dbo.calccaseloadpsoavg(0::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 12::NUMERIC(10, 0)) AS "11MthAll", dbo.calccaseloadpsoavg(60::NUMERIC(10, 0), 69::NUMERIC(10, 0), - 13::NUMERIC(10, 0)) AS "12Mth", dbo.calccaseloadpsoavg(0::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 13::NUMERIC(10, 0)) AS "12MthAll"
UNION
SELECT
    '50-59'::TEXT AS "Cases", dbo.calccaseloadpsoavg(50::NUMERIC(10, 0), 59::NUMERIC(10, 0), - 1::NUMERIC(10, 0)) AS "Current", dbo.calccaseloadpsoavg(0::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 1::NUMERIC(10, 0)) AS "CurrentAll", dbo.calccaseloadpsoavg(50::NUMERIC(10, 0), 59::NUMERIC(10, 0), - 2::NUMERIC(10, 0)) AS "1Mth", dbo.calccaseloadpsoavg(0::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 2::NUMERIC(10, 0)) AS "1MthAll", dbo.calccaseloadpsoavg(50::NUMERIC(10, 0), 59::NUMERIC(10, 0), - 3::NUMERIC(10, 0)) AS "2Mth", dbo.calccaseloadpsoavg(0::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 3::NUMERIC(10, 0)) AS "2MthAll", dbo.calccaseloadpsoavg(50::NUMERIC(10, 0), 59::NUMERIC(10, 0), - 4::NUMERIC(10, 0)) AS "3Mth", dbo.calccaseloadpsoavg(0::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 4::NUMERIC(10, 0)) AS "3MthAll", dbo.calccaseloadpsoavg(50::NUMERIC(10, 0), 59::NUMERIC(10, 0), - 5::NUMERIC(10, 0)) AS "4Mth", dbo.calccaseloadpsoavg(0::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 5::NUMERIC(10, 0)) AS "4MthAll", dbo.calccaseloadpsoavg(50::NUMERIC(10, 0), 59::NUMERIC(10, 0), - 6::NUMERIC(10, 0)) AS "5Mth", dbo.calccaseloadpsoavg(0::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 6::NUMERIC(10, 0)) AS "5MthAll", dbo.calccaseloadpsoavg(50::NUMERIC(10, 0), 59::NUMERIC(10, 0), - 7::NUMERIC(10, 0)) AS "6Mth", dbo.calccaseloadpsoavg(0::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 7::NUMERIC(10, 0)) AS "6MthAll", dbo.calccaseloadpsoavg(50::NUMERIC(10, 0), 59::NUMERIC(10, 0), - 8::NUMERIC(10, 0)) AS "7Mth", dbo.calccaseloadpsoavg(0::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 8::NUMERIC(10, 0)) AS "7MthAll", dbo.calccaseloadpsoavg(50::NUMERIC(10, 0), 59::NUMERIC(10, 0), - 9::NUMERIC(10, 0)) AS "8Mth", dbo.calccaseloadpsoavg(0::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 9::NUMERIC(10, 0)) AS "8MthAll", dbo.calccaseloadpsoavg(50::NUMERIC(10, 0), 59::NUMERIC(10, 0), - 10::NUMERIC(10, 0)) AS "9Mth", dbo.calccaseloadpsoavg(0::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 10::NUMERIC(10, 0)) AS "9MthAll", dbo.calccaseloadpsoavg(50::NUMERIC(10, 0), 59::NUMERIC(10, 0), - 11::NUMERIC(10, 0)) AS "10Mth", dbo.calccaseloadpsoavg(0::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 11::NUMERIC(10, 0)) AS "10MthAll", dbo.calccaseloadpsoavg(50::NUMERIC(10, 0), 59::NUMERIC(10, 0), - 12::NUMERIC(10, 0)) AS "11Mth", dbo.calccaseloadpsoavg(0::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 12::NUMERIC(10, 0)) AS "11MthAll", dbo.calccaseloadpsoavg(50::NUMERIC(10, 0), 59::NUMERIC(10, 0), - 13::NUMERIC(10, 0)) AS "12Mth", dbo.calccaseloadpsoavg(0::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 13::NUMERIC(10, 0)) AS "12MthAll"
UNION
SELECT
    '40-49'::TEXT AS "Cases", dbo.calccaseloadpsoavg(40::NUMERIC(10, 0), 49::NUMERIC(10, 0), - 1::NUMERIC(10, 0)) AS "Current", dbo.calccaseloadpsoavg(0::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 1::NUMERIC(10, 0)) AS "CurrentAll", dbo.calccaseloadpsoavg(40::NUMERIC(10, 0), 49::NUMERIC(10, 0), - 2::NUMERIC(10, 0)) AS "1Mth", dbo.calccaseloadpsoavg(0::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 2::NUMERIC(10, 0)) AS "1MthAll", dbo.calccaseloadpsoavg(40::NUMERIC(10, 0), 49::NUMERIC(10, 0), - 3::NUMERIC(10, 0)) AS "2Mth", dbo.calccaseloadpsoavg(0::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 3::NUMERIC(10, 0)) AS "2MthAll", dbo.calccaseloadpsoavg(40::NUMERIC(10, 0), 49::NUMERIC(10, 0), - 4::NUMERIC(10, 0)) AS "3Mth", dbo.calccaseloadpsoavg(0::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 4::NUMERIC(10, 0)) AS "3MthAll", dbo.calccaseloadpsoavg(40::NUMERIC(10, 0), 49::NUMERIC(10, 0), - 5::NUMERIC(10, 0)) AS "4Mth", dbo.calccaseloadpsoavg(0::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 5::NUMERIC(10, 0)) AS "4MthAll", dbo.calccaseloadpsoavg(40::NUMERIC(10, 0), 49::NUMERIC(10, 0), - 6::NUMERIC(10, 0)) AS "5Mth", dbo.calccaseloadpsoavg(0::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 6::NUMERIC(10, 0)) AS "5MthAll", dbo.calccaseloadpsoavg(40::NUMERIC(10, 0), 49::NUMERIC(10, 0), - 7::NUMERIC(10, 0)) AS "6Mth", dbo.calccaseloadpsoavg(0::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 7::NUMERIC(10, 0)) AS "6MthAll", dbo.calccaseloadpsoavg(40::NUMERIC(10, 0), 49::NUMERIC(10, 0), - 8::NUMERIC(10, 0)) AS "7Mth", dbo.calccaseloadpsoavg(0::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 8::NUMERIC(10, 0)) AS "7MthAll", dbo.calccaseloadpsoavg(40::NUMERIC(10, 0), 49::NUMERIC(10, 0), - 9::NUMERIC(10, 0)) AS "8Mth", dbo.calccaseloadpsoavg(0::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 9::NUMERIC(10, 0)) AS "8MthAll", dbo.calccaseloadpsoavg(40::NUMERIC(10, 0), 49::NUMERIC(10, 0), - 10::NUMERIC(10, 0)) AS "9Mth", dbo.calccaseloadpsoavg(0::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 10::NUMERIC(10, 0)) AS "9MthAll", dbo.calccaseloadpsoavg(40::NUMERIC(10, 0), 49::NUMERIC(10, 0), - 11::NUMERIC(10, 0)) AS "10Mth", dbo.calccaseloadpsoavg(0::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 11::NUMERIC(10, 0)) AS "10MthAll", dbo.calccaseloadpsoavg(40::NUMERIC(10, 0), 49::NUMERIC(10, 0), - 12::NUMERIC(10, 0)) AS "11Mth", dbo.calccaseloadpsoavg(0::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 12::NUMERIC(10, 0)) AS "11MthAll", dbo.calccaseloadpsoavg(40::NUMERIC(10, 0), 49::NUMERIC(10, 0), - 13::NUMERIC(10, 0)) AS "12Mth", dbo.calccaseloadpsoavg(0::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 13::NUMERIC(10, 0)) AS "12MthAll"
UNION
SELECT
    '10-39'::TEXT AS "Cases", dbo.calccaseloadpsoavg(10::NUMERIC(10, 0), 39::NUMERIC(10, 0), - 1::NUMERIC(10, 0)) AS "Current", dbo.calccaseloadpsoavg(0::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 1::NUMERIC(10, 0)) AS "CurrentAll", dbo.calccaseloadpsoavg(10::NUMERIC(10, 0), 39::NUMERIC(10, 0), - 2::NUMERIC(10, 0)) AS "1Mth", dbo.calccaseloadpsoavg(0::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 2::NUMERIC(10, 0)) AS "1MthAll", dbo.calccaseloadpsoavg(10::NUMERIC(10, 0), 39::NUMERIC(10, 0), - 3::NUMERIC(10, 0)) AS "2Mth", dbo.calccaseloadpsoavg(0::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 3::NUMERIC(10, 0)) AS "2MthAll", dbo.calccaseloadpsoavg(10::NUMERIC(10, 0), 39::NUMERIC(10, 0), - 4::NUMERIC(10, 0)) AS "3Mth", dbo.calccaseloadpsoavg(0::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 4::NUMERIC(10, 0)) AS "3MthAll", dbo.calccaseloadpsoavg(10::NUMERIC(10, 0), 39::NUMERIC(10, 0), - 5::NUMERIC(10, 0)) AS "4Mth", dbo.calccaseloadpsoavg(0::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 5::NUMERIC(10, 0)) AS "4MthAll", dbo.calccaseloadpsoavg(10::NUMERIC(10, 0), 39::NUMERIC(10, 0), - 6::NUMERIC(10, 0)) AS "5Mth", dbo.calccaseloadpsoavg(0::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 6::NUMERIC(10, 0)) AS "5MthAll", dbo.calccaseloadpsoavg(10::NUMERIC(10, 0), 39::NUMERIC(10, 0), - 7::NUMERIC(10, 0)) AS "6Mth", dbo.calccaseloadpsoavg(0::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 7::NUMERIC(10, 0)) AS "6MthAll", dbo.calccaseloadpsoavg(10::NUMERIC(10, 0), 39::NUMERIC(10, 0), - 8::NUMERIC(10, 0)) AS "7Mth", dbo.calccaseloadpsoavg(0::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 8::NUMERIC(10, 0)) AS "7MthAll", dbo.calccaseloadpsoavg(10::NUMERIC(10, 0), 39::NUMERIC(10, 0), - 9::NUMERIC(10, 0)) AS "8Mth", dbo.calccaseloadpsoavg(0::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 9::NUMERIC(10, 0)) AS "8MthAll", dbo.calccaseloadpsoavg(10::NUMERIC(10, 0), 39::NUMERIC(10, 0), - 10::NUMERIC(10, 0)) AS "9Mth", dbo.calccaseloadpsoavg(0::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 10::NUMERIC(10, 0)) AS "9MthAll", dbo.calccaseloadpsoavg(10::NUMERIC(10, 0), 39::NUMERIC(10, 0), - 11::NUMERIC(10, 0)) AS "10Mth", dbo.calccaseloadpsoavg(0::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 11::NUMERIC(10, 0)) AS "10MthAll", dbo.calccaseloadpsoavg(10::NUMERIC(10, 0), 39::NUMERIC(10, 0), - 12::NUMERIC(10, 0)) AS "11Mth", dbo.calccaseloadpsoavg(0::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 12::NUMERIC(10, 0)) AS "11MthAll", dbo.calccaseloadpsoavg(10::NUMERIC(10, 0), 39::NUMERIC(10, 0), - 13::NUMERIC(10, 0)) AS "12Mth", dbo.calccaseloadpsoavg(0::NUMERIC(10, 0), 999::NUMERIC(10, 0), - 13::NUMERIC(10, 0)) AS "12MthAll";
/* dbo.ReportingDirectorateCapacityHistory source */;



CREATE OR REPLACE VIEW dbo.reductions_archive_view (reduction_id, ou_ldu_id, offender_manager_id, reduction, comments, reduction_date, reduction_added_by) AS
SELECT
    n.id AS reduction_id, n.organisationalunitid AS ou_ldu_id, n.offendermanagerid AS offender_manager_id, n.hoursreduced AS reduction, n.notes AS comments, n.lastupdatedatetime AS reduction_date, n.lastupdateuserid AS reduction_added_by
    FROM dbo.note AS n;



CREATE OR REPLACE VIEW dbo.reportingdirectoratecapacityhistory (workloadreportdate, trustid, directorateid, capacitypoints, contractedhours, reducedhours, capacitypercentage, capacitypercentagecases, capacitypercentagereports, availablepoints, workloadpoints, sdrpoints, sdrconversionpoints, paromspoints, requirementspoints) AS
SELECT
    wr.date AS workloadreportdate, wl.trustid, wl.directorateid, wl.capacitypoints, wl.contractedhours, wl.reducedhours, wl.capacitypercentage, wl.capacitypercentagecases, wl.capacitypercentagereports, wl.availablepoints, wl.workloadpoints, wl.sdrpoints, wl.sdrconversionpoints, wl.paromspoints, wl.requirementspoints
    FROM dbo.workloadreportdirectorate AS wl
    INNER JOIN dbo.workloadreport AS wr
        ON wr.id = wl.workloadreportid
    WHERE wr.isdeleted = FALSE;
/* dbo.ReportingLDUCapacityHistory source */;



CREATE OR REPLACE VIEW dbo.reportinglducapacityhistory (workloadreportdate, trustid, directorateid, lduid, capacitypoints, contractedhours, reducedhours, capacitypercentage, capacitypercentagecases, capacitypercentagereports, availablepoints, workloadpoints, sdrpoints, sdrconversionpoints, paromspoints, requirementspoints) AS
SELECT
    wr.date AS workloadreportdate, wl.trustid, wl.directorateid, wl.lduid, wl.capacitypoints, wl.contractedhours, wl.reducedhours, wl.capacitypercentage, wl.capacitypercentagecases, wl.capacitypercentagereports, wl.availablepoints, wl.workloadpoints, wl.sdrpoints, wl.sdrconversionpoints, wl.paromspoints, wl.requirementspoints
    FROM dbo.workloadreportldu AS wl
    INNER JOIN dbo.workloadreport AS wr
        ON wr.id = wl.workloadreportid
    WHERE wr.isdeleted = FALSE;
/* dbo.ReportingOffenderManagerCapacityHistory source */;



CREATE OR REPLACE VIEW dbo.reportingoffendermanagercapacityhistory (workloadreportdate, directorateid, lduid, teamid, teamname, offendermanagerid, offendermanagertypeid, capacitypoints, contractedhoursperweek, hoursreduction, capacitypercentage, capacitypercentagecases, capacitypercentagereports, availablepoints, workloadpoints, sdrpoints, sdrconversionpoints, paromspoints, requirementspoints) AS
SELECT
    wr.date AS workloadreportdate,
    /* , WL.TrustId */
    wl.directorateid, wl.lduid, wl.teamid, wl.teamname, wl.offendermanagerid, wl.offendermanagertypeid, wl.capacitypoints, wl.contractedhoursperweek, wl.hoursreduction, wl.capacitypercentage, wl.capacitypercentagecases, wl.capacitypercentagereports, wl.availablepoints, wl.totalpoints AS workloadpoints, wl.sdrpoints, wl.sdrconversionpoints, wl.paromspoints, wl.requirementspoints
    FROM dbo.workloadreportofficer AS wl
    INNER JOIN dbo.workloadreport AS wr
        ON wr.id = wl.workloadreportid
    WHERE wr.isdeleted = FALSE;
/* dbo.ReportingOfficerCasesHistory source */;



CREATE OR REPLACE VIEW dbo.reportingofficercaseshistory (workloadreportid, workloadreportdate, offendermanagerid, offendermanagername, activewarrants, upws, overdueterminations, totalcommcases, totalcustcases, totalcases, activecases, ordercount, capacitypercentage, directorateid, lduid, offendermanagertypeid) AS
SELECT
    wr.id AS workloadreportid, wr.date AS workloadreportdate, offendermanagerid, offendermanagername, SUM(activewarrants) AS activewarrants, SUM(upw) AS upws, SUM(overdueterminations) AS overdueterminations, SUM(comtier0) + SUM(comtier1) + (SUM(CASE WHEN wp.comtier1cpenabled THEN comtier1cp ELSE 0 END)) + SUM(comtier2) + SUM(comtier3n) + SUM(comtier3d) + SUM(comtier4) AS totalcommcases, SUM(custier0) + SUM(custier1) + SUM(custier2) + SUM(custier3) + SUM(custier4) AS totalcustcases, SUM(totalcases) AS totalcases, SUM(totalcases) - SUM(totalcasesinactive) AS activecases, SUM(ordercount) AS ordercount, dbo.capacitypointsperc(dbo.availablepoints(wlo.nominaltarget, wlo.offendermanagertypeid, SUM(wlo.contractedhoursperweek), SUM(wlo.hoursreduction), wlo.defaultcontractedhours, wlo.defaultcontractedhourspso), SUM(wlo.totalpoints), SUM(wlo.sdrpoints), SUM(wlo.sdrconversionpoints), SUM(wlo.paromspoints), SUM(wlo.requirementspoints)) AS capacitypercentage, wlo.directorateid, wlo.lduid, wlo.offendermanagertypeid
    FROM dbo.workloadreportofficer AS wlo
    INNER JOIN dbo.workloadreport AS wr
        ON wr.id = wlo.workloadreportid
    LEFT OUTER JOIN (SELECT
        comtier1cpenabled
        FROM dbo.workloadpoints
        WHERE isdeleted = FALSE
        ORDER BY createddatetime DESC NULLS FIRST
        LIMIT 1) AS wp
        ON 1 = 1
    WHERE wr.isdeleted = FALSE
    GROUP BY wr.id, wr.date, wlo.offendermanagerid, wlo.offendermanagername, wlo.directorateid, wlo.lduid, wlo.offendermanagertypeid, wlo.nominaltarget, wlo.offendermanagertypeid, wlo.defaultcontractedhours, wlo.defaultcontractedhourspso, wp.comtier1cpenabled;
/* dbo.WorkloadReportTeam source */;


CREATE OR REPLACE VIEW dbo.workloadreportteam (workloadreportid, workloadreportdate, directorateid, lduid, teamid, teamname, teamupdatedatetime, deliverytypeid, ompot1casescount, ompot2casescount, ompot3casescount, ompot4casescount, ompsot1casescount, ompsot2casescount, ompsot3casescount, ompsot4casescount, comtier0count, comtier0ocount, comtier0wcount, comtier0ucount, comtier1count, comtier1cpcount, comtier1ocount, comtier1wcount, comtier1ucount, comtier2count, comtier2ocount, comtier2wcount, comtier2ucount, comtier3count, comtier3ocount, comtier3wcount, comtier3ucount, comtier3dcount, comtier3docount, comtier3dwcount, comtier3ducount, comtier4count, comtier4ocount, comtier4wcount, comtier4ucount, custier0count, custier0ocount, custier0wcount, custier0ucount, custier1count, custier1ocount, custier1wcount, custier1ucount, custier2count, custier2ocount, custier2wcount, custier2ucount, custier3count, custier3ocount, custier3wcount, custier3ucount, custier4count, custier4ocount, custier4wcount, custier4ucount, activewarrants, overdueterminations, upw, ordercount, totalcases, totalcasesinactive, totalcasesppo, commappal1, commappal2, commappal3, cusmappal1, cusmappal2, cusmappal3, monthlysdrs, sdrduenext30days, sdrconversionslast30days, paromsduenext30days, paromscompletedlast30days, contractedhours, reducedhours, workloadpoints, sdrpoints, paromspoints, sdrconversionpoints, requirementspoints, requirementscount, availablepoints, capacityorgunit, capacitypoints, capacitypercentage, capacitypercentagecases, capacitypercentagereports, capacitypercentagerequirements) AS
WITH requirements
AS (SELECT
    wr.id AS workloadreportid, wl.teamid, SUM(COALESCE(rw.points, 0)) AS requirementspoints, SUM(COALESCE(rw.count, 0)) AS requirementscount
    FROM dbo.requirementworkload AS rw
    INNER JOIN dbo.workload AS wl
        ON rw.workloadid = wl.id
    INNER JOIN dbo.organisationalunitworkloadreport AS owr
        ON owr.id = wl.organisationalunitworkloadreportid
    INNER JOIN dbo.workloadreport AS wr
        ON wr.id = owr.workloadreportid
    GROUP BY wr.id, wl.teamid), orgunits
AS (SELECT
    team.id AS teamid, directorate.id AS directorateid, directorate.name AS directoratename, ldu.id AS lduid, team.name AS teamname, team.deliverytypeid AS teamdeliverytypeid, team.lastupdatedatetime AS teamupdatedatetime
    FROM dbo.organisationalunit AS team
    INNER JOIN dbo.organisationalunit AS ldu
        ON ldu.id = team.parentorganisationalunitid
    INNER JOIN dbo.organisationalunit AS directorate
        ON directorate.id = ldu.parentorganisationalunitid
    WHERE team.isdeleted = FALSE AND ldu.isdeleted = FALSE AND directorate.isdeleted = FALSE)
SELECT
    wr.id AS workloadreportid, wr.date AS workloadreportdate,
    /* Trust.Id as TrustId, */
    orgunits.DirectorateId, orgunits.LduId, orgunits.TeamId, orgunits.teamname,
    /* Team.Notes as TeamNotes, */
    /* U.Username as TeamUpdateUsername, */
    orgunits.teamupdatedatetime, orgunits.teamdeliverytypeid AS deliverytypeid, SUM(dbo.getomtcasescount(1::NUMERIC(10, 0), om.offendermanagertypeid, wl.comtier1, wl.comtier1o, wl.comtier1w, wl.comtier1u, wl.custier1, wl.custier1o, wl.custier1w, wl.custier1u)) AS ompot1casescount, SUM(dbo.getomtcasescount(1::NUMERIC(10, 0), om.offendermanagertypeid, wl.comtier2, wl.comtier2o, wl.comtier2w, wl.comtier2u, wl.custier2, wl.custier2o, wl.custier2w, wl.custier2u)) AS ompot2casescount, SUM(dbo.getomt3casescount(1::NUMERIC(10, 0), om.offendermanagertypeid, wl.comtier3n, wl.comtier3no, wl.comtier3nw, wl.comtier3nu, wl.comtier3d, wl.comtier3do, wl.comtier3dw, wl.comtier3du, wl.custier3, wl.custier3o, wl.custier3w, wl.custier3u)) AS ompot3casescount, SUM(dbo.getomtcasescount(1::NUMERIC(10, 0), om.offendermanagertypeid, wl.comtier4, wl.comtier4o, wl.comtier4w, wl.comtier4u, wl.custier4, wl.custier4o, wl.custier4w, wl.custier4u)) AS ompot4casescount, SUM(dbo.getomtcasescount(2::NUMERIC(10, 0), om.offendermanagertypeid, wl.comtier1, wl.comtier1o, wl.comtier1w, wl.comtier1u, wl.custier1, wl.custier1o, wl.custier1w, wl.custier1u)) AS ompsot1casescount, SUM(dbo.getomtcasescount(2::NUMERIC(10, 0), om.offendermanagertypeid, wl.comtier2, wl.comtier2o, wl.comtier2w, wl.comtier2u, wl.custier2, wl.custier2o, wl.custier2w, wl.custier2u)) AS ompsot2casescount, SUM(dbo.getomt3casescount(2::NUMERIC(10, 0), om.offendermanagertypeid, wl.comtier3n, wl.comtier3no, wl.comtier3nw, wl.comtier3nu, wl.comtier3d, wl.comtier3do, wl.comtier3dw, wl.comtier3du, wl.custier3, wl.custier3o, wl.custier3w, wl.custier3u)) AS ompsot3casescount, SUM(dbo.getomtcasescount(2::NUMERIC(10, 0), om.offendermanagertypeid, wl.comtier4, wl.comtier4o, wl.comtier4w, wl.comtier4u, wl.custier4, wl.custier4o, wl.custier4w, wl.custier4u)) AS ompsot4casescount, SUM(wl.comtier0) AS comtier0count, SUM(wl.comtier0o) AS comtier0ocount, SUM(wl.comtier0w) AS comtier0wcount, SUM(wl.comtier0u) AS comtier0ucount, SUM(wl.comtier1) AS comtier1count, SUM(wl.comtier1cp) AS comtier1cpcount, SUM(wl.comtier1o) AS comtier1ocount, SUM(wl.comtier1w) AS comtier1wcount, SUM(wl.comtier1u) AS comtier1ucount, SUM(wl.comtier2) AS comtier2count, SUM(wl.comtier2o) AS comtier2ocount, SUM(wl.comtier2w) AS comtier2wcount, SUM(wl.comtier2u) AS comtier2ucount, SUM(wl.comtier3n) AS comtier3count, SUM(wl.comtier3no) AS comtier3ocount, SUM(wl.comtier3nw) AS comtier3wcount, SUM(wl.comtier3nu) AS comtier3ucount, SUM(wl.comtier3d) AS comtier3dcount, SUM(wl.comtier3do) AS comtier3docount, SUM(wl.comtier3dw) AS comtier3dwcount, SUM(wl.comtier3du) AS comtier3ducount, SUM(wl.comtier4) AS comtier4count, SUM(wl.comtier4o) AS comtier4ocount, SUM(wl.comtier4w) AS comtier4wcount, SUM(wl.comtier4u) AS comtier4ucount, SUM(wl.custier0) AS custier0count, SUM(wl.custier0o) AS custier0ocount, SUM(wl.custier0w) AS custier0wcount, SUM(wl.custier0u) AS custier0ucount, SUM(wl.custier1) AS custier1count, SUM(wl.custier1o) AS custier1ocount, SUM(wl.custier1w) AS custier1wcount, SUM(wl.custier1u) AS custier1ucount, SUM(wl.custier2) AS custier2count, SUM(wl.custier2o) AS custier2ocount, SUM(wl.custier2w) AS custier2wcount, SUM(wl.custier2u) AS custier2ucount, SUM(wl.custier3) AS custier3count, SUM(wl.custier3o) AS custier3ocount, SUM(wl.custier3w) AS custier3wcount, SUM(wl.custier3u) AS custier3ucount, SUM(wl.custier4) AS custier4count, SUM(wl.custier4o) AS custier4ocount, SUM(wl.custier4w) AS custier4wcount, SUM(wl.custier4u) AS custier4ucount, SUM(wl.activewarrants) AS activewarrants, SUM(wl.overdueterminations) AS overdueterminations, SUM(wl.upw) AS upw, SUM(wl.ordercount) AS ordercount, SUM(wl.totalcases) AS totalcases, SUM(wl.totalcasesinactive) AS totalcasesinactive, SUM(wl.totalcasesppo) AS totalcasesppo, SUM(wl.commappal1) AS commappal1, SUM(wl.commappal2) AS commappal2, SUM(wl.commappal3) AS commappal3, SUM(wl.cusmappal1) AS cusmappal1, SUM(wl.cusmappal2) AS cusmappal2, SUM(wl.cusmappal3) AS cusmappal3, SUM(wl.monthlysdrs) AS monthlysdrs, SUM(wl.sdrduenext30days) AS sdrduenext30days, SUM(wl.sdrconversionslast30days) AS sdrconversionslast30days, SUM(wl.paromsduenext30days) AS paromsduenext30days, SUM(wl.paromscompletedlast30days) AS paromscompletedlast30days, SUM(wl.contractedhoursperweek) AS contractedhours, SUM(wl.hoursreduction) AS reducedhours, SUM(wl.totalpoints) AS workloadpoints, SUM(wl.sdrpoints) AS sdrpoints, SUM(wl.paromspoints) AS paromspoints, SUM(wl.sdrconversionpoints) AS sdrconversionpoints, COALESCE(r.requirementspoints, 0) AS requirementspoints, COALESCE(r.requirementscount, 0) AS requirementscount, SUM(dbo.availablepoints(wl.nominaltarget, om.offendermanagertypeid, wl.contractedhoursperweek, wl.hoursreduction, wp.defaultcontractedhours, wp.defaultcontractedhourspso)) AS availablepoints, dbo.capacityom(SUM(wl.contractedhoursperweek), SUM(wl.hoursreduction)) AS capacityorgunit, dbo.capacitypoints(SUM(dbo.availablepoints(wl.nominaltarget, om.offendermanagertypeid, wl.contractedhoursperweek, wl.hoursreduction, wp.defaultcontractedhours, wp.defaultcontractedhourspso)), SUM(wl.totalpoints), SUM(wl.sdrpoints), SUM(wl.sdrconversionpoints), SUM(wl.paromspoints), COALESCE(r.requirementspoints, 0)) AS capacitypoints, dbo.capacitypointsperc(SUM(dbo.availablepoints(wl.nominaltarget, om.offendermanagertypeid, wl.contractedhoursperweek, wl.hoursreduction, wp.defaultcontractedhours, wp.defaultcontractedhourspso)), SUM(wl.totalpoints), SUM(wl.sdrpoints), SUM(wl.sdrconversionpoints), SUM(wl.paromspoints), COALESCE(r.requirementspoints, 0)) AS capacitypercentage, dbo.capacitypointsperc(SUM(dbo.availablepoints(wl.nominaltarget, om.offendermanagertypeid, wl.contractedhoursperweek, wl.hoursreduction, wp.defaultcontractedhours, wp.defaultcontractedhourspso)), SUM(wl.totalpoints), 0, 0, 0, 0) AS capacitypercentagecases, dbo.capacitypointsperc(SUM(dbo.availablepoints(wl.nominaltarget, om.offendermanagertypeid, wl.contractedhoursperweek, wl.hoursreduction, wp.defaultcontractedhours, wp.defaultcontractedhourspso)), 0, SUM(wl.sdrpoints), SUM(wl.sdrconversionpoints), SUM(wl.paromspoints), 0) AS capacitypercentagereports, dbo.capacitypointsperc(SUM(dbo.availablepoints(wl.nominaltarget, om.offendermanagertypeid, wl.contractedhoursperweek, wl.hoursreduction, wp.defaultcontractedhours, wp.defaultcontractedhourspso)), 0, 0, 0, 0, COALESCE(r.requirementspoints, 0)) AS capacitypercentagerequirements
    FROM orgunits
    INNER JOIN dbo.workload AS wl
        ON orgunits.teamid = wl.teamid
    INNER JOIN
    /* inner join dbo.OrganisationalUnit as Team on Team.Id = WL.TeamId */
    /* inner join dbo.OrganisationalUnit as Ldu on Ldu.Id = Team.ParentOrganisationalUnitId */
    /* inner join dbo.OrganisationalUnit as Directorate on Directorate.Id = Ldu.ParentOrganisationalUnitId */
    /* --inner join dbo.OrganisationalUnit as Trust on Trust.Id = Directorate.ParentOrganisationalUnitId */
    /* left join [Users] as U on U.Id = Team.LastUpdateUserId */
    dbo.offendermanager AS om
        ON om.id = wl.offendermanagerid
    INNER JOIN dbo.organisationalunitworkloadreport AS owr
        ON owr.id = wl.organisationalunitworkloadreportid
    INNER JOIN dbo.workloadreport AS wr
        ON wr.id = owr.workloadreportid
    LEFT OUTER JOIN (SELECT
        defaultcontractedhours, defaultcontractedhourspso
        FROM dbo.workloadpoints
        WHERE isdeleted = FALSE
        ORDER BY createddatetime DESC NULLS FIRST
        LIMIT 1) AS wp
        ON 1 = 1
    LEFT OUTER JOIN requirements AS r
        ON r.workloadreportid = wr.id AND r.teamid = orgunits.teamid
    GROUP BY r.requirementspoints, r.requirementscount, wr.id, wr.date, /* Trust.Id, */ orgunits.directorateid, orgunits.lduid, orgunits.teamid, orgunits.teamname, orgunits.teamdeliverytypeid, orgunits.teamupdatedatetime;


CREATE OR REPLACE VIEW dbo.reportingteamcapacityhistory (workloadreportdate, directorateid, lduid, teamid, teamname, capacitypoints, contractedhours, reducedhours, capacitypercentage, capacitypercentagecases, capacitypercentagereports, availablepoints, workloadpoints, sdrpoints, sdrconversionpoints, paromspoints, requirementspoints, deliverytypeid) AS
SELECT
    wr.date AS workloadreportdate,
    /* WL.TrustId, */
    wl.directorateid, wl.lduid, wl.teamid, wl.teamname, wl.capacitypoints, wl.contractedhours, wl.reducedhours, wl.capacitypercentage, wl.capacitypercentagecases, wl.capacitypercentagereports, wl.availablepoints, wl.workloadpoints, wl.sdrpoints, wl.sdrconversionpoints, wl.paromspoints, wl.requirementspoints, wl.deliverytypeid
    FROM dbo.workloadreportteam AS wl
    INNER JOIN dbo.workloadreport AS wr
        ON wr.id = wl.workloadreportid
    WHERE wr.isdeleted = FALSE;
/* dbo.TeamCapacityHistory source */;



CREATE OR REPLACE VIEW dbo.teamcapacityhistory (workloadreportdate, teamid, capacityorgunit, capacitypoints, capacitypercentage, capacitypercentagecases, capacitypercentagereports, capacitypercentagerequirements) AS
SELECT
    wr.date AS workloadreportdate, wlt.teamid, wlt.capacityorgunit, wlt.capacitypoints, wlt.capacitypercentage, wlt.capacitypercentagecases, wlt.capacitypercentagereports, wlt.capacitypercentagerequirements
    FROM dbo.workloadreportteam AS wlt
    INNER JOIN dbo.workloadreport AS wr
        ON wr.id = wlt.workloadreportid
    WHERE wr.isdeleted = FALSE;
/* dbo.TeamCasesHistory source */;



CREATE OR REPLACE VIEW dbo.teamcaseshistory (workloadreportid, workloadreportdate, teamid, teamname, activewarrants, upws, overdueterminations, totalcommcases, totalcustcases, totalcases, activecases, ordercount, capacitypercentage, directorateid, lduid, deliverytypeid) AS
SELECT
    wr.id AS workloadreportid, wr.date AS workloadreportdate, teamid, teamname, SUM(activewarrants) AS activewarrants, SUM(upw) AS upws, SUM(overdueterminations) AS overdueterminations, SUM(comtier0count) + SUM(comtier1count) + SUM(CASE WHEN wp.comtier1cpenabled THEN comtier1cpcount ELSE 0 END) + SUM(comtier2count) + SUM(comtier3count) + SUM(comtier3dcount) + SUM(comtier4count) AS totalcommcases, SUM(custier0count) + SUM(custier1count) + SUM(custier2count) + SUM(custier3count) + SUM(custier4count) AS totalcustcases, SUM(totalcases) AS totalcases, SUM(totalcases) - SUM(totalcasesinactive) AS activecases, SUM(ordercount) AS ordercount, wlo.capacitypercentage, wlo.directorateid, wlo.lduid, wlo.deliverytypeid
    FROM dbo.workloadreportteam AS wlo
    INNER JOIN dbo.workloadreport AS wr
        ON wr.id = wlo.workloadreportid
    LEFT OUTER JOIN (SELECT
        comtier1cpenabled
        FROM dbo.workloadpoints
        WHERE isdeleted = FALSE
        ORDER BY createddatetime DESC NULLS FIRST
        LIMIT 1) AS wp
        ON 1 = 1
    WHERE wr.isdeleted = FALSE
    GROUP BY wr.id, wr.date, wlo.teamid, wlo.teamname, wlo.capacitypercentage, wlo.directorateid, wlo.lduid, wp.comtier1cpenabled, wlo.deliverytypeid;
/* dbo.TeamNote source */;



CREATE OR REPLACE VIEW dbo.teamnote (id, organisationalunitid, notes, hoursreduced, lastupdateuserid, lastupdatedusername, lastupdatedatetime) AS
SELECT
    dbo.note.id, dbo.note.organisationalunitid, dbo.note.notes, dbo.note.hoursreduced, dbo.note.lastupdateuserid, dbo.users.username AS lastupdatedusername, dbo.note.lastupdatedatetime
    FROM dbo.note
    INNER JOIN dbo.users
        ON dbo.note.lastupdateuserid = dbo.users.id
    WHERE (dbo.note.offendermanagerid IS NULL) AND (dbo.note.organisationalunitid IS NOT NULL);



CREATE OR REPLACE VIEW dbo.workloadreporttrust (workloadreportid, workloadreportdate, trustid, trustname, ompot1casescount, ompot2casescount, ompot3casescount, ompot4casescount, ompsot1casescount, ompsot2casescount, ompsot3casescount, ompsot4casescount, comtier0count, comtier0ocount, comtier0wcount, comtier0ucount, comtier1count, comtier1cpcount, comtier1ocount, comtier1wcount, comtier1ucount, comtier2count, comtier2ocount, comtier2wcount, comtier2ucount, comtier3count, comtier3ocount, comtier3wcount, comtier3ucount, comtier3dcount, comtier3docount, comtier3dwcount, comtier3ducount, comtier4count, comtier4ocount, comtier4wcount, comtier4ucount, custier0count, custier0ocount, custier0wcount, custier0ucount, custier1count, custier1ocount, custier1wcount, custier1ucount, custier2count, custier2ocount, custier2wcount, custier2ucount, custier3count, custier3ocount, custier3wcount, custier3ucount, custier4count, custier4ocount, custier4wcount, custier4ucount, activewarrants, overdueterminations, upw, ordercount, monthlysdrs, sdrduenext30days, sdrconversionslast30days, paromsduenext30days, paromscompletedlast30days, totalcases, totalcasesinactive, totalcasesppo, commappal1, commappal2, commappal3, cusmappal1, cusmappal2, cusmappal3, contractedhours, reducedhours, workloadpoints, sdrpoints, paromspoints, sdrconversionpoints, requirementspoints, requirementscount, availablepoints, capacityorgunit, capacitypoints, capacitypercentage, capacitypercentagecases, capacitypercentagereports, capacitypercentagerequirements) AS
WITH requirements
AS (SELECT
    wr.id AS workloadreportid, trust.id AS trustid, SUM(COALESCE(rw.points, 0)) AS requirementspoints, SUM(COALESCE(rw.count, 0)) AS requirementscount
    FROM dbo.requirementworkload AS rw
    INNER JOIN dbo.workload AS wl
        ON rw.workloadid = wl.id
    INNER JOIN dbo.organisationalunitworkloadreport AS owr
        ON owr.id = wl.organisationalunitworkloadreportid
    INNER JOIN dbo.workloadreport AS wr
        ON wr.id = owr.workloadreportid
    INNER JOIN dbo.organisationalunit AS team
        ON team.id = wl.teamid
    INNER JOIN dbo.organisationalunit AS ldu
        ON ldu.id = team.parentorganisationalunitid
    INNER JOIN dbo.organisationalunit AS directorate
        ON directorate.id = ldu.parentorganisationalunitid
    INNER JOIN dbo.organisationalunit AS trust
        ON trust.id = directorate.parentorganisationalunitid
    GROUP BY wr.id, trust.id), orgunits
AS (SELECT
    team.id AS teamid, trust.id AS trustid, trust.name AS trustname
    FROM dbo.organisationalunit AS team
    INNER JOIN dbo.organisationalunit AS ldu
        ON ldu.id = team.parentorganisationalunitid
    INNER JOIN dbo.organisationalunit AS directorate
        ON directorate.id = ldu.parentorganisationalunitid
    INNER JOIN dbo.organisationalunit AS trust
        ON trust.id = directorate.parentorganisationalunitid
    WHERE team.isdeleted = FALSE AND ldu.isdeleted = FALSE AND directorate.isdeleted = FALSE AND trust.isdeleted = FALSE)
SELECT
    wr.id AS workloadreportid, wr.date AS workloadreportdate, orgunits.trustid, orgunits.trustname, SUM(dbo.getomtcasescount(1::NUMERIC(10, 0), om.offendermanagertypeid, wl.comtier1, wl.comtier1o, wl.comtier1w, wl.comtier1u, wl.custier1, wl.custier1o, wl.custier1w, wl.custier1u)) AS ompot1casescount, SUM(dbo.getomtcasescount(1::NUMERIC(10, 0), om.offendermanagertypeid, wl.comtier2, wl.comtier2o, wl.comtier2w, wl.comtier2u, wl.custier2, wl.custier2o, wl.custier2w, wl.custier2u)) AS ompot2casescount, SUM(dbo.getomt3casescount(1::NUMERIC(10, 0), om.offendermanagertypeid, wl.comtier3n, wl.comtier3no, wl.comtier3nw, wl.comtier3nu, wl.comtier3d, wl.comtier3do, wl.comtier3dw, wl.comtier3du, wl.custier3, wl.custier3o, wl.custier3w, wl.custier3u)) AS ompot3casescount, SUM(dbo.getomtcasescount(1::NUMERIC(10, 0), om.offendermanagertypeid, wl.comtier4, wl.comtier4o, wl.comtier4w, wl.comtier4u, wl.custier4, wl.custier4o, wl.custier4w, wl.custier4u)) AS ompot4casescount, SUM(dbo.getomtcasescount(2::NUMERIC(10, 0), om.offendermanagertypeid, wl.comtier1, wl.comtier1o, wl.comtier1w, wl.comtier1u, wl.custier1, wl.custier1o, wl.custier1w, wl.custier1u)) AS ompsot1casescount, SUM(dbo.getomtcasescount(2::NUMERIC(10, 0), om.offendermanagertypeid, wl.comtier2, wl.comtier2o, wl.comtier2w, wl.comtier2u, wl.custier2, wl.custier2o, wl.custier2w, wl.custier2u)) AS ompsot2casescount, SUM(dbo.getomt3casescount(2::NUMERIC(10, 0), om.offendermanagertypeid, wl.comtier3n, wl.comtier3no, wl.comtier3nw, wl.comtier3nu, wl.comtier3d, wl.comtier3do, wl.comtier3dw, wl.comtier3du, wl.custier3, wl.custier3o, wl.custier3w, wl.custier3u)) AS ompsot3casescount, SUM(dbo.getomtcasescount(2::NUMERIC(10, 0), om.offendermanagertypeid, wl.comtier4, wl.comtier4o, wl.comtier4w, wl.comtier4u, wl.custier4, wl.custier4o, wl.custier4w, wl.custier4u)) AS ompsot4casescount, SUM(wl.comtier0) AS comtier0count, SUM(wl.comtier0o) AS comtier0ocount, SUM(wl.comtier0w) AS comtier0wcount, SUM(wl.comtier0u) AS comtier0ucount, SUM(wl.comtier1) AS comtier1count, SUM(wl.comtier1cp) AS comtier1cpcount, SUM(wl.comtier1o) AS comtier1ocount, SUM(wl.comtier1w) AS comtier1wcount, SUM(wl.comtier1u) AS comtier1ucount, SUM(wl.comtier2) AS comtier2count, SUM(wl.comtier2o) AS comtier2ocount, SUM(wl.comtier2w) AS comtier2wcount, SUM(wl.comtier2u) AS comtier2ucount, SUM(wl.comtier3n) AS comtier3count, SUM(wl.comtier3no) AS comtier3ocount, SUM(wl.comtier3nw) AS comtier3wcount, SUM(wl.comtier3nu) AS comtier3ucount, SUM(wl.comtier3d) AS comtier3dcount, SUM(wl.comtier3do) AS comtier3docount, SUM(wl.comtier3dw) AS comtier3dwcount, SUM(wl.comtier3du) AS comtier3ducount, SUM(wl.comtier4) AS comtier4count, SUM(wl.comtier4o) AS comtier4ocount, SUM(wl.comtier4w) AS comtier4wcount, SUM(wl.comtier4u) AS comtier4ucount, SUM(wl.custier0) AS custier0count, SUM(wl.custier0o) AS custier0ocount, SUM(wl.custier0w) AS custier0wcount, SUM(wl.custier0u) AS custier0ucount, SUM(wl.custier1) AS custier1count, SUM(wl.custier1o) AS custier1ocount, SUM(wl.custier1w) AS custier1wcount, SUM(wl.custier1u) AS custier1ucount, SUM(wl.custier2) AS custier2count, SUM(wl.custier2o) AS custier2ocount, SUM(wl.custier2w) AS custier2wcount, SUM(wl.custier2u) AS custier2ucount, SUM(wl.custier3) AS custier3count, SUM(wl.custier3o) AS custier3ocount, SUM(wl.custier3w) AS custier3wcount, SUM(wl.custier3u) AS custier3ucount, SUM(wl.custier4) AS custier4count, SUM(wl.custier4o) AS custier4ocount, SUM(wl.custier4w) AS custier4wcount, SUM(wl.custier4u) AS custier4ucount, SUM(wl.activewarrants) AS activewarrants, SUM(wl.overdueterminations) AS overdueterminations, SUM(wl.upw) AS upw, SUM(wl.ordercount) AS ordercount, SUM(wl.monthlysdrs) AS monthlysdrs, SUM(wl.sdrduenext30days) AS sdrduenext30days, SUM(wl.sdrconversionslast30days) AS sdrconversionslast30days, SUM(wl.paromsduenext30days) AS paromsduenext30days, SUM(wl.paromscompletedlast30days) AS paromscompletedlast30days, SUM(wl.totalcases) AS totalcases, SUM(wl.totalcasesinactive) AS totalcasesinactive, SUM(wl.totalcasesppo) AS totalcasesppo, SUM(wl.commappal1) AS commappal1, SUM(wl.commappal2) AS commappal2, SUM(wl.commappal3) AS commappal3, SUM(wl.cusmappal1) AS cusmappal1, SUM(wl.cusmappal2) AS cusmappal2, SUM(wl.cusmappal3) AS cusmappal3, SUM(wl.contractedhoursperweek) AS contractedhours, SUM(wl.hoursreduction) AS reducedhours, SUM(wl.totalpoints) AS workloadpoints, SUM(wl.sdrpoints) AS sdrpoints, SUM(wl.paromspoints) AS paromspoints, SUM(wl.sdrconversionpoints) AS sdrconversionpoints, COALESCE(r.requirementspoints, 0) AS requirementspoints, COALESCE(r.requirementscount, 0) AS requirementscount, SUM(dbo.availablepoints(wl.nominaltarget, om.offendermanagertypeid, wl.contractedhoursperweek, wl.hoursreduction, wp.defaultcontractedhours, wp.defaultcontractedhourspso)) AS availablepoints, dbo.capacityom(SUM(wl.contractedhoursperweek), SUM(wl.hoursreduction)) AS capacityorgunit, dbo.capacitypoints(SUM(dbo.availablepoints(wl.nominaltarget, om.offendermanagertypeid, wl.contractedhoursperweek, wl.hoursreduction, wp.defaultcontractedhours, wp.defaultcontractedhourspso)), SUM(wl.totalpoints), SUM(wl.sdrpoints), SUM(wl.sdrconversionpoints), SUM(wl.paromspoints), COALESCE(r.requirementspoints, 0)) AS capacitypoints, dbo.capacitypointsperc(SUM(dbo.availablepoints(wl.nominaltarget, om.offendermanagertypeid, wl.contractedhoursperweek, wl.hoursreduction, wp.defaultcontractedhours, wp.defaultcontractedhourspso)), SUM(wl.totalpoints), SUM(wl.sdrpoints), SUM(wl.sdrconversionpoints), SUM(wl.paromspoints), COALESCE(r.requirementspoints, 0)) AS capacitypercentage, dbo.capacitypointsperc(SUM(dbo.availablepoints(wl.nominaltarget, om.offendermanagertypeid, wl.contractedhoursperweek, wl.hoursreduction, wp.defaultcontractedhours, wp.defaultcontractedhourspso)), SUM(wl.totalpoints), 0, 0, 0, 0) AS capacitypercentagecases, dbo.capacitypointsperc(SUM(dbo.availablepoints(wl.nominaltarget, om.offendermanagertypeid, wl.contractedhoursperweek, wl.hoursreduction, wp.defaultcontractedhours, wp.defaultcontractedhourspso)), 0, SUM(wl.sdrpoints), SUM(wl.sdrconversionpoints), SUM(wl.paromspoints), 0) AS capacitypercentagereports, dbo.capacitypointsperc(SUM(dbo.availablepoints(wl.nominaltarget, om.offendermanagertypeid, wl.contractedhoursperweek, wl.hoursreduction, wp.defaultcontractedhours, wp.defaultcontractedhourspso)), 0, 0, 0, 0, COALESCE(r.requirementspoints, 0)) AS capacitypercentagerequirements
    FROM orgunits
    INNER JOIN dbo.workload AS wl
        ON orgunits.teamid = wl.teamid
    INNER JOIN dbo.offendermanager AS om
        ON om.id = wl.offendermanagerid
    INNER JOIN dbo.organisationalunitworkloadreport AS owr
        ON owr.id = wl.organisationalunitworkloadreportid
    INNER JOIN dbo.workloadreport AS wr
        ON wr.id = owr.workloadreportid
    LEFT OUTER JOIN (SELECT
        defaultcontractedhours, defaultcontractedhourspso
        FROM dbo.workloadpoints
        WHERE isdeleted = FALSE
        ORDER BY createddatetime DESC NULLS FIRST
        LIMIT 1) AS wp
        ON 1 = 1
    LEFT OUTER JOIN requirements AS r
        ON r.workloadreportid = wr.id AND r.trustid = orgunits.trustid
    GROUP BY r.requirementspoints, r.requirementscount, wr.id, wr.date, orgunits.trustid, orgunits.trustname;


CREATE OR REPLACE VIEW dbo.trustcapacityhistory (workloadreportdate, trustid, capacityorgunit, capacitypoints, capacitypercentage, capacitypercentagecases, capacitypercentagereports, capacitypercentagerequirements) AS
SELECT
    wr.date AS workloadreportdate, wlt.trustid, wlt.capacityorgunit, wlt.capacitypoints, wlt.capacitypercentage, wlt.capacitypercentagecases, wlt.capacitypercentagereports, wlt.capacitypercentagerequirements
    FROM dbo.workloadreporttrust AS wlt
    INNER JOIN dbo.workloadreport AS wr
        ON wr.id = wlt.workloadreportid
    WHERE wr.isdeleted = FALSE;
/* dbo.TrustCasesHistory source */;



CREATE OR REPLACE VIEW dbo.trustcaseshistory (workloadreportid, workloadreportdate, trustid, trustname, activewarrants, upws, overdueterminations, totalcommcases, totalcustcases, totalcases, activecases, ordercount) AS
SELECT
    wr.id AS workloadreportid, wr.date AS workloadreportdate, trustid, trustname, SUM(activewarrants) AS activewarrants, SUM(upw) AS upws, SUM(overdueterminations) AS overdueterminations, SUM(comtier0count) + SUM(comtier1count) + SUM(CASE WHEN wp.comtier1cpenabled THEN comtier1cpcount ELSE 0 END) + SUM(comtier2count) + SUM(comtier3count) + SUM(comtier3dcount) + SUM(comtier4count) AS totalcommcases, SUM(custier0count) + SUM(custier1count) + SUM(custier2count) + SUM(custier3count) + SUM(custier4count) AS totalcustcases, SUM(totalcases) AS totalcases, SUM(totalcases) - SUM(totalcasesinactive) AS activecases, SUM(ordercount) AS ordercount
    FROM dbo.workloadreporttrust AS wlo
    INNER JOIN dbo.workloadreport AS wr
        ON wr.id = wlo.workloadreportid
    LEFT OUTER JOIN (SELECT
        comtier1cpenabled
        FROM dbo.workloadpoints
        WHERE isdeleted = FALSE
        ORDER BY createddatetime DESC NULLS FIRST
        LIMIT 1) AS wp
        ON 1 = 1
    WHERE wr.isdeleted = FALSE
    GROUP BY wr.id, wr.date, wlo.trustid, wlo.trustname, wp.comtier1cpenabled;





/* OrgUnits.DirectorateNotes, */
/* OrgUnits.DirectorateUpdateUsername, */
/* OrgUnits.DirectorateUpdateDateTime; */
/* dbo.DirectorateCapacityHistory source */;



/* dbo.LDUCapacityHistory source */;


/* dbo.ReportingTeamCapacityHistory source */;



/* dbo.TrustCapacityHistory source */;



-- ------------ Write CREATE-INDEX-stage scripts -----------



CREATE UNIQUE INDEX ix_organisationalunittype_uc_parent_1
ON dbo.organisationalunittype
USING BTREE (parentorganisationalunittypeid ASC);



CREATE UNIQUE INDEX ix_users_uq__users__536c85e4744fe1f11
ON dbo.users
USING BTREE (username ASC);



-- ------------ Write CREATE-CONSTRAINT-stage scripts -----------

ALTER TABLE dbo.archive_reduction_data
ADD CONSTRAINT pk__archive___3213e83fe15271e5_951674438 PRIMARY KEY (id);



ALTER TABLE dbo.capacityabsoluteom
ADD CONSTRAINT pk_capacityabsoluteom_343672272 PRIMARY KEY (id);



ALTER TABLE dbo.capacityaveragedirectorate
ADD CONSTRAINT pk_capacityaveragedirectorate_375672386 PRIMARY KEY (id);



ALTER TABLE dbo.capacityaverageldu
ADD CONSTRAINT pk_capacityavgldu_423672557 PRIMARY KEY (id);



ALTER TABLE dbo.capacityaverageom
ADD CONSTRAINT pk_capacityaverageom_471672728 PRIMARY KEY (id);



ALTER TABLE dbo.capacityperiodofficer
ADD CONSTRAINT pk_capacityperiodofficer_535672956 PRIMARY KEY (id);



ALTER TABLE dbo.caseloadofficer
ADD CONSTRAINT pk_caseloadaverageom_567673070 PRIMARY KEY (id);



ALTER TABLE dbo.daily_archive_data
ADD CONSTRAINT pk__daily_ar__3213e83f0179f3b0_983674552 PRIMARY KEY (id);



ALTER TABLE dbo.databaseupdateresulttype
ADD CONSTRAINT pk_databaseupdateresulttype_599673184 PRIMARY KEY (id);



ALTER TABLE dbo.databaseupdatescriptlog
ADD CONSTRAINT pk_databaseupdatescriptlog_1847677630 PRIMARY KEY (id);



ALTER TABLE dbo.databaseupdateversionlog
ADD CONSTRAINT pk_databaseupdateversionlog_1047674780 PRIMARY KEY (id);



ALTER TABLE dbo.deliverytype
ADD CONSTRAINT pk_deliverytype_631673298 PRIMARY KEY (id);



ALTER TABLE dbo.displaysettings
ADD CONSTRAINT pk_displaysettings_663673412 PRIMARY KEY (id);

ALTER TABLE dbo.fortnightly_archive_data
ADD CONSTRAINT pk__fortnigh__3213e83ff7f3ce38_1015674666 PRIMARY KEY (id);



ALTER TABLE dbo.inactivecase
ADD CONSTRAINT pk_inactivecase_1428200138 PRIMARY KEY (id);



ALTER TABLE dbo.logging
ADD CONSTRAINT pk_logging_727673640 PRIMARY KEY (id);



ALTER TABLE dbo.messages
ADD CONSTRAINT "PK_Messages_775673811" PRIMARY KEY (id);



ALTER TABLE dbo.note
ADD CONSTRAINT pk_offendermanagernote_1492200366 PRIMARY KEY (id);



ALTER TABLE dbo.offendermanager
ADD CONSTRAINT pk_offendermanager_1911677858 PRIMARY KEY (id);



ALTER TABLE dbo.offendermanagertype
ADD CONSTRAINT pk_offendermanagertype_855674096 PRIMARY KEY (id);



ALTER TABLE dbo.organisationalunit
ADD CONSTRAINT pk_organisationalunit_1991678143 PRIMARY KEY (id);



ALTER TABLE dbo.organisationalunittype
ADD CONSTRAINT pk_organisationalunittype_1095674951 PRIMARY KEY (id);



ALTER TABLE dbo.organisationalunittype
ADD CONSTRAINT uc_parent_1111675008 UNIQUE (parentorganisationalunittypeid);



ALTER TABLE dbo.organisationalunitworkloadreport
ADD CONSTRAINT pk_organisationalunitworkloadreport_2103678542 PRIMARY KEY (id);



ALTER TABLE dbo.requirementdetails
ADD CONSTRAINT pk_requirementdetails_1652200936 PRIMARY KEY (id);



ALTER TABLE dbo.requirementtype
ADD CONSTRAINT pk_requirementtype_36195179 PRIMARY KEY (id);



ALTER TABLE dbo.requirementworkload
ADD CONSTRAINT pk_requirementworkload_1588200708 PRIMARY KEY (id);



ALTER TABLE dbo.requirementworkloadpoints
ADD CONSTRAINT pk_requirementworkloadpoints_164195635 PRIMARY KEY (id);



ALTER TABLE dbo.roles
ADD CONSTRAINT pk_roles_903674267 PRIMARY KEY (id);



ALTER TABLE dbo.userroles
ADD CONSTRAINT pk_userroles_228195863 PRIMARY KEY (usersid, rolesid);



ALTER TABLE dbo.users
ADD CONSTRAINT pk_users_1175675236 PRIMARY KEY (id);



ALTER TABLE dbo.users
ADD CONSTRAINT uq__users__536c85e4744fe1f1_1191675293 UNIQUE (username);



ALTER TABLE dbo.workload
ADD CONSTRAINT pk_workload_292196091 PRIMARY KEY (id);



ALTER TABLE dbo.workloadpoints
ADD CONSTRAINT pk_workloadpoints_1287675635 PRIMARY KEY (id);



ALTER TABLE dbo.workloadreport
ADD CONSTRAINT pk_organisationalunitworkload_1783677402 PRIMARY KEY (id);



-- ------------ Write CREATE-FOREIGN-KEY-CONSTRAINT-stage scripts -----------

ALTER TABLE dbo.databaseupdatescriptlog
ADD CONSTRAINT fk_databaseupdatescriptlog_databaseupdateresulttype_1863677687 FOREIGN KEY (resulttypeid)
REFERENCES dbo.databaseupdateresulttype (id)
ON UPDATE NO ACTION
ON DELETE NO ACTION;



ALTER TABLE dbo.databaseupdatescriptlog
ADD CONSTRAINT fk_databaseupdatescriptlog_databaseupdateversionlog_1879677744 FOREIGN KEY (databaseupdateversionlogid)
REFERENCES dbo.databaseupdateversionlog (id)
ON UPDATE NO ACTION
ON DELETE NO ACTION;



ALTER TABLE dbo.databaseupdateversionlog
ADD CONSTRAINT fk_databaseupdateversionlog_databaseupdateresulttype_1063674837 FOREIGN KEY (resulttypeid)
REFERENCES dbo.databaseupdateresulttype (id)
ON UPDATE NO ACTION
ON DELETE NO ACTION;



ALTER TABLE dbo.inactivecase
ADD CONSTRAINT fk_inactivecase_workload_1460200252 FOREIGN KEY (workloadid)
REFERENCES dbo.workload (id)
ON UPDATE NO ACTION
ON DELETE NO ACTION;



ALTER TABLE dbo.note
ADD CONSTRAINT fk_note_organisationalunit_1524200480 FOREIGN KEY (organisationalunitid)
REFERENCES dbo.organisationalunit (id)
ON UPDATE NO ACTION
ON DELETE NO ACTION;



ALTER TABLE dbo.note
ADD CONSTRAINT fk_note_users_1540200537 FOREIGN KEY (lastupdateuserid)
REFERENCES dbo.users (id)
ON UPDATE NO ACTION
ON DELETE NO ACTION;



ALTER TABLE dbo.note
ADD CONSTRAINT fk_offendermanagernote_offendermanager_1556200594 FOREIGN KEY (offendermanagerid)
REFERENCES dbo.offendermanager (id)
ON UPDATE NO ACTION
ON DELETE NO ACTION;



ALTER TABLE dbo.offendermanager
ADD CONSTRAINT fk_offendermanager_offendermanagertype_1943677972 FOREIGN KEY (offendermanagertypeid)
REFERENCES dbo.offendermanagertype (id)
ON UPDATE NO ACTION
ON DELETE NO ACTION;



ALTER TABLE dbo.offendermanager
ADD CONSTRAINT fk_offendermanager_users_1959678029 FOREIGN KEY (lastupdateuserid)
REFERENCES dbo.users (id)
ON UPDATE NO ACTION
ON DELETE NO ACTION;



ALTER TABLE dbo.organisationalunit
ADD CONSTRAINT fk_organisationalunit_deliverytype_2023678257 FOREIGN KEY (deliverytypeid)
REFERENCES dbo.deliverytype (id)
ON UPDATE NO ACTION
ON DELETE NO ACTION;



ALTER TABLE dbo.organisationalunit
ADD CONSTRAINT fk_organisationalunit_organisationalunit_2039678314 FOREIGN KEY (parentorganisationalunitid)
REFERENCES dbo.organisationalunit (id)
ON UPDATE NO ACTION
ON DELETE NO ACTION;



ALTER TABLE dbo.organisationalunit
ADD CONSTRAINT fk_organisationalunit_organisationalunittype_2055678371 FOREIGN KEY (organisationalunittypeid)
REFERENCES dbo.organisationalunittype (id)
ON UPDATE NO ACTION
ON DELETE NO ACTION;



ALTER TABLE dbo.organisationalunit
ADD CONSTRAINT fk_organisationalunit_users_2071678428 FOREIGN KEY (lastupdateuserid)
REFERENCES dbo.users (id)
ON UPDATE NO ACTION
ON DELETE NO ACTION;



ALTER TABLE dbo.organisationalunittype
ADD CONSTRAINT fk_organisationalunittype_parentorganisationalunittype_1143675122 FOREIGN KEY (parentorganisationalunittypeid)
REFERENCES dbo.organisationalunittype (id)
ON UPDATE NO ACTION
ON DELETE NO ACTION;



ALTER TABLE dbo.organisationalunitworkloadreport
ADD CONSTRAINT fk_organisationalunitworkloadreport_organisationalunit_2135678656 FOREIGN KEY (organisationalunitid)
REFERENCES dbo.organisationalunit (id)
ON UPDATE NO ACTION
ON DELETE NO ACTION;



ALTER TABLE dbo.organisationalunitworkloadreport
ADD CONSTRAINT fk_organisationalunitworkloadreport_workloadreport_4195065 FOREIGN KEY (workloadreportid)
REFERENCES dbo.workloadreport (id)
ON UPDATE NO ACTION
ON DELETE NO ACTION;



ALTER TABLE dbo.requirementdetails
ADD CONSTRAINT fk_requirementdetails_requirementworkload_1668200993 FOREIGN KEY (requirementworkloadid)
REFERENCES dbo.requirementworkload (id)
ON UPDATE NO ACTION
ON DELETE NO ACTION;



ALTER TABLE dbo.requirementtype
ADD CONSTRAINT fk_requirementtype_createdbyuser_68195293 FOREIGN KEY (createdbyuserid)
REFERENCES dbo.users (id)
ON UPDATE NO ACTION
ON DELETE NO ACTION;



ALTER TABLE dbo.requirementtype
ADD CONSTRAINT fk_requirementtype_defaultchildrequirementtype_84195350 FOREIGN KEY (defaultchildrequirementtypeid)
REFERENCES dbo.requirementtype (id)
ON UPDATE NO ACTION
ON DELETE NO ACTION;



ALTER TABLE dbo.requirementtype
ADD CONSTRAINT fk_requirementtype_modifiedbyuser_100195407 FOREIGN KEY (modifiedbyuserid)
REFERENCES dbo.users (id)
ON UPDATE NO ACTION
ON DELETE NO ACTION;



ALTER TABLE dbo.requirementtype
ADD CONSTRAINT fk_requirementtype_originalparentrequirementtype_116195464 FOREIGN KEY (originalparentrequirementtypeid)
REFERENCES dbo.requirementtype (id)
ON UPDATE NO ACTION
ON DELETE NO ACTION;



ALTER TABLE dbo.requirementtype
ADD CONSTRAINT fk_requirementtype_parentrequirementtype_132195521 FOREIGN KEY (parentrequirementtypeid)
REFERENCES dbo.requirementtype (id)
ON UPDATE NO ACTION
ON DELETE NO ACTION;



ALTER TABLE dbo.requirementworkload
ADD CONSTRAINT fk_requirementworkload_requirementtype_1604200765 FOREIGN KEY (requirementtypeid)
REFERENCES dbo.requirementtype (id)
ON UPDATE NO ACTION
ON DELETE NO ACTION;



ALTER TABLE dbo.requirementworkload
ADD CONSTRAINT fk_requirementworkload_workload_1620200822 FOREIGN KEY (workloadid)
REFERENCES dbo.workload (id)
ON UPDATE NO ACTION
ON DELETE NO ACTION;



ALTER TABLE dbo.requirementworkloadpoints
ADD CONSTRAINT fk_requirementworkloadpoints_requirementtype_180195692 FOREIGN KEY (requirementtypeid)
REFERENCES dbo.requirementtype (id)
ON UPDATE NO ACTION
ON DELETE NO ACTION;



ALTER TABLE dbo.requirementworkloadpoints
ADD CONSTRAINT fk_requirementworkloadpoints_workloadpoints_196195749 FOREIGN KEY (workloadpointsid)
REFERENCES dbo.workloadpoints (id)
ON UPDATE NO ACTION
ON DELETE NO ACTION;



ALTER TABLE dbo.userroles
ADD CONSTRAINT fk_userroles_roles_244195920 FOREIGN KEY (rolesid)
REFERENCES dbo.roles (id)
ON UPDATE NO ACTION
ON DELETE NO ACTION;



ALTER TABLE dbo.userroles
ADD CONSTRAINT fk_userroles_users_260195977 FOREIGN KEY (usersid)
REFERENCES dbo.users (id)
ON UPDATE NO ACTION
ON DELETE NO ACTION;



ALTER TABLE dbo.users
ADD CONSTRAINT fk_users_modifiedbyuser_1255675521 FOREIGN KEY (modifiedbyuserid)
REFERENCES dbo.users (id)
ON UPDATE NO ACTION
ON DELETE NO ACTION;



ALTER TABLE dbo.workload
ADD CONSTRAINT fk_workload_offendermanager_1380199967 FOREIGN KEY (offendermanagerid)
REFERENCES dbo.offendermanager (id)
ON UPDATE NO ACTION
ON DELETE NO ACTION;



ALTER TABLE dbo.workload
ADD CONSTRAINT fk_workload_organisationalunitworkloadreport_1396200024 FOREIGN KEY (organisationalunitworkloadreportid)
REFERENCES dbo.organisationalunitworkloadreport (id)
ON UPDATE NO ACTION
ON DELETE NO ACTION;



ALTER TABLE dbo.workloadpoints
ADD CONSTRAINT fk_workloadpoints_users_1751677288 FOREIGN KEY (createdbyuserid)
REFERENCES dbo.users (id)
ON UPDATE NO ACTION
ON DELETE NO ACTION;



ALTER TABLE dbo.workloadreport
ADD CONSTRAINT fk_workloadreport_workloadpoints_1815677516 FOREIGN KEY (workloadpointsid)
REFERENCES dbo.workloadpoints (id)
ON UPDATE NO ACTION
ON DELETE NO ACTION;






-- ------------ Write CREATE-DATABASE-stage scripts -----------

CREATE SCHEMA IF NOT EXISTS staging;



-- ------------ Write CREATE-TABLE-stage scripts -----------

CREATE TABLE staging.arms(
    id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY,
    assessment_date VARCHAR(255),
    assessment_code VARCHAR(255),
    assessment_desc VARCHAR(255),
    assessment_staff_name VARCHAR(255),
    assessment_staff_key VARCHAR(255),
    assessment_staff_grade VARCHAR(255),
    assessment_team_key VARCHAR(255),
    assessment_provider_code VARCHAR(255),
    crn VARCHAR(255),
    disposal_or_release_date VARCHAR(255),
    sentence_type VARCHAR(255),
    so_registration_date VARCHAR(255),
    asmnt_outcome_cd VARCHAR(255),
    asmnt_outcome_desc VARCHAR(255),
    last_saved_dt_referral_doc VARCHAR(255),
    last_saved_dt_assessment_doc VARCHAR(255),
    offender_manager_staff_name VARCHAR(255),
    offender_manager_team_cd VARCHAR(255),
    offender_manager_cluster_cd VARCHAR(255),
    offender_manager_provider_cd VARCHAR(255),
    completed_date VARCHAR(255),
    offender_manager_pdu_cd VARCHAR(255)
)
        WITH (
        OIDS=FALSE
        );



CREATE TABLE staging.cms(
    id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY,
    contact_id VARCHAR(255),
    contact_date VARCHAR(255),
    contact_type_code VARCHAR(255),
    contact_type_desc VARCHAR(255),
    contact_staff_name VARCHAR(255),
    contact_staff_key VARCHAR(255),
    contact_staff_grade VARCHAR(255),
    contact_team_key VARCHAR(255),
    contact_provider_code VARCHAR(255),
    om_name VARCHAR(255),
    om_key VARCHAR(255),
    om_grade VARCHAR(255),
    om_team_key VARCHAR(255),
    om_provider_code VARCHAR(255),
    crn VARCHAR(255)
)
        WITH (
        OIDS=FALSE
        );



CREATE TABLE staging.court_reporters(
    id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY,
    trust VARCHAR(255),
    region_desc VARCHAR(255),
    region_code VARCHAR(255),
    ldu_desc VARCHAR(255),
    ldu_code VARCHAR(255),
    team_desc VARCHAR(255),
    team_code VARCHAR(255),
    om_surname VARCHAR(255),
    om_forename VARCHAR(255),
    om_grade_code VARCHAR(255),
    om_key VARCHAR(255),
    sdr_last_30 VARCHAR(255),
    sdr_due_next_30 VARCHAR(255),
    sdr_conv_last_30 VARCHAR(255),
    oral_reports VARCHAR(255),
    datestamp VARCHAR(255),
    pdu_desc VARCHAR(255),
    pdu_code VARCHAR(255)
)
        WITH (
        OIDS=FALSE
        );



CREATE TABLE staging.court_reports(
    id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY,
    team_desc VARCHAR(255),
    team_code VARCHAR(255),
    om_key VARCHAR(255),
    om_team_staff_grade VARCHAR(255),
    sdr_last_30 VARCHAR(255),
    sdr_due_next_30 VARCHAR(255),
    sdr_conv_last_30 VARCHAR(255),
    datestamp VARCHAR(255),
    oral_reports VARCHAR(255),
    trust VARCHAR(255),
    region_desc VARCHAR(255),
    region_code VARCHAR(255),
    ldu_desc VARCHAR(255),
    ldu_code VARCHAR(255),
    om_surname VARCHAR(255),
    om_forename VARCHAR(255),
    om_grade_code VARCHAR(255),
    pdu_code VARCHAR(255),
    pdu_desc VARCHAR(255)
)
        WITH (
        OIDS=FALSE
        );



CREATE TABLE staging.flag_o_due(
    id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY,
    row_type VARCHAR(255),
    case_ref_no VARCHAR(255),
    tier_code VARCHAR(255),
    team_code VARCHAR(255),
    om_grade_code VARCHAR(255),
    om_key VARCHAR(255),
    location VARCHAR(255)
)
        WITH (
        OIDS=FALSE
        );



CREATE TABLE staging.flag_priority(
    id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY,
    row_type VARCHAR(255),
    case_ref_no VARCHAR(255),
    tier_code VARCHAR(255),
    team_code VARCHAR(255),
    om_grade_code VARCHAR(255),
    om_key VARCHAR(255),
    location VARCHAR(255)
)
        WITH (
        OIDS=FALSE
        );



CREATE TABLE staging.flag_upw(
    id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY,
    row_type VARCHAR(255),
    case_ref_no VARCHAR(255),
    tier_code VARCHAR(255),
    team_code VARCHAR(255),
    om_grade_code VARCHAR(255),
    om_key VARCHAR(255),
    location VARCHAR(255)
)
        WITH (
        OIDS=FALSE
        );



CREATE TABLE staging.flag_warr_4_n(
    id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY,
    row_type VARCHAR(255),
    case_ref_no VARCHAR(255),
    tier_code VARCHAR(255),
    team_code VARCHAR(255),
    om_grade_code VARCHAR(255),
    om_key VARCHAR(255),
    location VARCHAR(255)
)
        WITH (
        OIDS=FALSE
        );



CREATE TABLE staging.gs(
    id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY,
    contact_id VARCHAR(255),
    contact_date VARCHAR(255),
    contact_type_code VARCHAR(255),
    contact_type_desc VARCHAR(255),
    om_name VARCHAR(255),
    om_key VARCHAR(255),
    om_grade VARCHAR(255),
    om_team_key VARCHAR(255),
    om_provider_code VARCHAR(255),
    crn VARCHAR(255)
)
        WITH (
        OIDS=FALSE
        );



CREATE TABLE staging.included_excluded(
    id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY,
    team_cd_ofm VARCHAR(255),
    staff_cd_ofm VARCHAR(255),
    crn VARCHAR(255),
    event_id VARCHAR(255),
    event_expected_term_dt VARCHAR(255),
    event_sentence_type VARCHAR(255),
    event_ident VARCHAR(255),
    tier VARCHAR(255),
    in_custody_yn VARCHAR(255),
    max_eligible_event_ident_in_crn VARCHAR(255),
    count_this_event VARCHAR(255),
    max_eligible_event_type VARCHAR(255),
    max_eligible_event_location VARCHAR(255),
    warrant VARCHAR(255),
    suspended_lifer VARCHAR(255),
    event_standalone_sso VARCHAR(255),
    event_overdue VARCHAR(255),
    event_filtered_out VARCHAR(255)
)
        WITH (
        OIDS=FALSE
        );



CREATE TABLE staging.inst_reports(
    id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY,
    team_desc VARCHAR(255),
    team_code VARCHAR(255),
    om_name VARCHAR(255),
    om_key VARCHAR(255),
    om_team_staff_grade VARCHAR(255),
    parom_due_next_30 VARCHAR(255),
    parom_comp_last_30 VARCHAR(255),
    datestamp VARCHAR(255)
)
        WITH (
        OIDS=FALSE
        );



CREATE TABLE staging.knex_migrations(
    id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY,
    name VARCHAR(255),
    batch BIGINT,
    migration_time TIMESTAMP WITH TIME ZONE
)
        WITH (
        OIDS=FALSE
        );



CREATE TABLE staging.knex_migrations_lock(
    is_locked BIGINT
)
        WITH (
        OIDS=FALSE
        );



CREATE TABLE staging.omic(
    id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY,
    team_ofm VARCHAR(255),
    team_cd_ofm VARCHAR(255),
    omic_cases VARCHAR(255),
    release_due_within_7_months VARCHAR(255),
    release_due_within_7_months_po VARCHAR(255),
    release_due_within_7_months_pso VARCHAR(255)
)
        WITH (
        OIDS=FALSE
        );



CREATE TABLE staging.omic_teams(
    id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY,
    trust VARCHAR(255),
    region_desc VARCHAR(255),
    region_code VARCHAR(255),
    ldu_desc VARCHAR(255),
    ldu_code VARCHAR(255),
    team_desc VARCHAR(255),
    team_code VARCHAR(255),
    om_surname VARCHAR(255),
    om_forename VARCHAR(255),
    om_grade_code VARCHAR(255),
    om_key VARCHAR(255),
    commtier0 VARCHAR(255),
    licencetier0 VARCHAR(255),
    custtier0 VARCHAR(255),
    comIn1st16Weeks VARCHAR(255),
    licin1st16weeks VARCHAR(255),
    datestamp VARCHAR(255),
    vcrn_count VARCHAR(255),
    team_type_ofm VARCHAR(255),
    pdu_code VARCHAR(255),
    pdu_desc VARCHAR(255),
    commtierd0 VARCHAR(255),
    commtierd1 VARCHAR(255),
    commtierd2 VARCHAR(255),
    commtierd3 VARCHAR(255),
    commtierc0 VARCHAR(255),
    commtierc1 VARCHAR(255),
    commtierc2 VARCHAR(255),
    commtierc3 VARCHAR(255),
    commtierb0 VARCHAR(255),
    commtierb1 VARCHAR(255),
    commtierb2 VARCHAR(255),
    commtierb3 VARCHAR(255),
    commtiera0 VARCHAR(255),
    commtiera1 VARCHAR(255),
    commtiera2 VARCHAR(255),
    commtiera3 VARCHAR(255),
    licencetierd0 VARCHAR(255),
    licencetierd1 VARCHAR(255),
    licencetierd2 VARCHAR(255),
    licencetierd3 VARCHAR(255),
    licencetierc0 VARCHAR(255),
    licencetierc1 VARCHAR(255),
    licencetierc2 VARCHAR(255),
    licencetierc3 VARCHAR(255),
    licencetierb0 VARCHAR(255),
    licencetierb1 VARCHAR(255),
    licencetierb2 VARCHAR(255),
    licencetierb3 VARCHAR(255),
    licencetiera0 VARCHAR(255),
    licencetiera1 VARCHAR(255),
    licencetiera2 VARCHAR(255),
    licencetiera3 VARCHAR(255),
    custtierd0 VARCHAR(255),
    custtierd1 VARCHAR(255),
    custtierd2 VARCHAR(255),
    custtierd3 VARCHAR(255),
    custtierc0 VARCHAR(255),
    custtierc1 VARCHAR(255),
    custtierc2 VARCHAR(255),
    custtierc3 VARCHAR(255),
    custtierb0 VARCHAR(255),
    custtierb1 VARCHAR(255),
    custtierb2 VARCHAR(255),
    custtierb3 VARCHAR(255),
    custtiera0 VARCHAR(255),
    custtiera1 VARCHAR(255),
    custtiera2 VARCHAR(255),
    custtiera3 VARCHAR(255),
    commtierc1a VARCHAR(255),
    commtiere VARCHAR(255),
    licencetierf VARCHAR(255),
    commtierg VARCHAR(255),
    commtierf VARCHAR(255),
    commtiera VARCHAR(255),
    licencetiera VARCHAR(255),
    custtierg VARCHAR(255),
    licencetierg VARCHAR(255),
    commtierd1a VARCHAR(255),
    custtiera VARCHAR(255),
    custtierf VARCHAR(255),
    licencetiere VARCHAR(255),
    custtiere VARCHAR(255)
)
        WITH (
        OIDS=FALSE
        );



CREATE TABLE staging.suspended_lifers(
    id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY,
    location VARCHAR(255),
    row_type VARCHAR(255),
    case_ref_no VARCHAR(255),
    tier_code VARCHAR(255),
    team_code VARCHAR(255),
    om_grade_code VARCHAR(255),
    om_key VARCHAR(255),
    in_custody VARCHAR(255),
    register_code VARCHAR(255),
    register_description VARCHAR(255),
    register_level VARCHAR(255),
    register_level_description VARCHAR(255),
    register_category VARCHAR(255),
    register_category_description VARCHAR(255),
    registration_date VARCHAR(255),
    next_review_date VARCHAR(255),
    deregistration_date VARCHAR(255)
)
        WITH (
        OIDS=FALSE
        );



CREATE TABLE staging.t2a(
    id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY,
    trust VARCHAR(255),
    region_desc VARCHAR(255),
    region_code VARCHAR(255),
    ldu_desc VARCHAR(255),
    ldu_code VARCHAR(255),
    team_desc VARCHAR(255),
    team_code VARCHAR(255),
    om_surname VARCHAR(255),
    om_forename VARCHAR(255),
    om_grade_code VARCHAR(255),
    om_key VARCHAR(255),
    commtier0 VARCHAR(255),
    licencetier0 VARCHAR(255),
    custtier0 VARCHAR(255),
    comIn1st16Weeks VARCHAR(255),
    licin1st16weeks VARCHAR(255),
    datestamp VARCHAR(255),
    vcrn_count VARCHAR(255),
    pdu_code VARCHAR(255),
    pdu_desc VARCHAR(255),
    commtierd0 VARCHAR(255),
    commtierd1 VARCHAR(255),
    commtierd2 VARCHAR(255),
    commtierd3 VARCHAR(255),
    commtierc0 VARCHAR(255),
    commtierc1 VARCHAR(255),
    commtierc2 VARCHAR(255),
    commtierc3 VARCHAR(255),
    commtierb0 VARCHAR(255),
    commtierb1 VARCHAR(255),
    commtierb2 VARCHAR(255),
    commtierb3 VARCHAR(255),
    commtiera0 VARCHAR(255),
    commtiera1 VARCHAR(255),
    commtiera2 VARCHAR(255),
    commtiera3 VARCHAR(255),
    licencetierd0 VARCHAR(255),
    licencetierd1 VARCHAR(255),
    licencetierd2 VARCHAR(255),
    licencetierd3 VARCHAR(255),
    licencetierc0 VARCHAR(255),
    licencetierc1 VARCHAR(255),
    licencetierc2 VARCHAR(255),
    licencetierc3 VARCHAR(255),
    licencetierb0 VARCHAR(255),
    licencetierb1 VARCHAR(255),
    licencetierb2 VARCHAR(255),
    licencetierb3 VARCHAR(255),
    licencetiera0 VARCHAR(255),
    licencetiera1 VARCHAR(255),
    licencetiera2 VARCHAR(255),
    licencetiera3 VARCHAR(255),
    custtierd0 VARCHAR(255),
    custtierd1 VARCHAR(255),
    custtierd2 VARCHAR(255),
    custtierd3 VARCHAR(255),
    custtierc0 VARCHAR(255),
    custtierc1 VARCHAR(255),
    custtierc2 VARCHAR(255),
    custtierc3 VARCHAR(255),
    custtierb0 VARCHAR(255),
    custtierb1 VARCHAR(255),
    custtierb2 VARCHAR(255),
    custtierb3 VARCHAR(255),
    custtiera0 VARCHAR(255),
    custtiera1 VARCHAR(255),
    custtiera2 VARCHAR(255),
    custtiera3 VARCHAR(255),
    commtierc1a VARCHAR(255),
    commtiere VARCHAR(255),
    licencetierf VARCHAR(255),
    commtierg VARCHAR(255),
    commtierf VARCHAR(255),
    commtiera VARCHAR(255),
    licencetiera VARCHAR(255),
    custtierg VARCHAR(255),
    licencetierg VARCHAR(255),
    commtierd1a VARCHAR(255),
    custtiera VARCHAR(255),
    custtierf VARCHAR(255),
    licencetiere VARCHAR(255),
    custtiere VARCHAR(255)
)
        WITH (
        OIDS=FALSE
        );



CREATE TABLE staging.t2a_detail(
    id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY,
    crn VARCHAR(255),
    event_no VARCHAR(255),
    allocation_date VARCHAR(255),
    allocation_reason VARCHAR(255),
    allocation_cd VARCHAR(255),
    provider_code_order_manager VARCHAR(255),
    cluster_order_manager VARCHAR(255),
    cluster_cd_order_manager VARCHAR(255),
    team_order_manager VARCHAR(255),
    team_cd_order_manager VARCHAR(255),
    staff_name_order_manager VARCHAR(255),
    staff_cd_order_manager VARCHAR(255),
    nsi_cd VARCHAR(255),
    nsi_desc VARCHAR(255),
    birth_date VARCHAR(255),
    age VARCHAR(255),
    nsi_status_cd VARCHAR(255),
    nsi_status_desc VARCHAR(255),
    nsi_outcome_cd VARCHAR(255),
    nsi_outcome_desc VARCHAR(255),
    staff_name_offender_manager VARCHAR(255),
    staff_cd_offender_manager VARCHAR(255),
    staff_grade_cd_offender_manager VARCHAR(255),
    provider_cd_offender_manager VARCHAR(255),
    cluster_cd_offender_manager VARCHAR(255),
    team_cd_offender_manager VARCHAR(255),
    pdu_order_manager VARCHAR(255),
    pdu_cd_order_manager VARCHAR(255),
    allocation_desc VARCHAR(255),
    pdu_cd_offender_manager VARCHAR(255)
)
        WITH (
        OIDS=FALSE
        );



CREATE TABLE staging.wmt_extract(
    id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY,
    trust VARCHAR(255),
    region_desc VARCHAR(255),
    region_code VARCHAR(255),
    ldu_desc VARCHAR(255),
    ldu_code VARCHAR(255),
    team_desc VARCHAR(255),
    team_code VARCHAR(255),
    om_surname VARCHAR(255),
    om_forename VARCHAR(255),
    om_grade_code VARCHAR(255),
    om_key VARCHAR(255),
    commtier0 VARCHAR(255),
    licencetier0 VARCHAR(255),
    custtier0 VARCHAR(255),
    comIn1st16Weeks VARCHAR(255),
    licin1st16weeks VARCHAR(255),
    datestamp VARCHAR(255),
    vcrn_count VARCHAR(255),
    pdu_code VARCHAR(255),
    pdu_desc VARCHAR(255),
    commtierd0 VARCHAR(255),
    commtierd1 VARCHAR(255),
    commtierd2 VARCHAR(255),
    commtierd3 VARCHAR(255),
    commtierc0 VARCHAR(255),
    commtierc1 VARCHAR(255),
    commtierc2 VARCHAR(255),
    commtierc3 VARCHAR(255),
    commtierb0 VARCHAR(255),
    commtierb1 VARCHAR(255),
    commtierb2 VARCHAR(255),
    commtierb3 VARCHAR(255),
    commtiera0 VARCHAR(255),
    commtiera1 VARCHAR(255),
    commtiera2 VARCHAR(255),
    commtiera3 VARCHAR(255),
    licencetierd0 VARCHAR(255),
    licencetierd1 VARCHAR(255),
    licencetierd2 VARCHAR(255),
    licencetierd3 VARCHAR(255),
    licencetierc0 VARCHAR(255),
    licencetierc1 VARCHAR(255),
    licencetierc2 VARCHAR(255),
    licencetierc3 VARCHAR(255),
    licencetierb0 VARCHAR(255),
    licencetierb1 VARCHAR(255),
    licencetierb2 VARCHAR(255),
    licencetierb3 VARCHAR(255),
    licencetiera0 VARCHAR(255),
    licencetiera1 VARCHAR(255),
    licencetiera2 VARCHAR(255),
    licencetiera3 VARCHAR(255),
    custtierd0 VARCHAR(255),
    custtierd1 VARCHAR(255),
    custtierd2 VARCHAR(255),
    custtierd3 VARCHAR(255),
    custtierc0 VARCHAR(255),
    custtierc1 VARCHAR(255),
    custtierc2 VARCHAR(255),
    custtierc3 VARCHAR(255),
    custtierb0 VARCHAR(255),
    custtierb1 VARCHAR(255),
    custtierb2 VARCHAR(255),
    custtierb3 VARCHAR(255),
    custtiera0 VARCHAR(255),
    custtiera1 VARCHAR(255),
    custtiera2 VARCHAR(255),
    custtiera3 VARCHAR(255),
    commtierc1a VARCHAR(255),
    commtiere VARCHAR(255),
    licencetierf VARCHAR(255),
    commtierg VARCHAR(255),
    commtierf VARCHAR(255),
    commtiera VARCHAR(255),
    licencetiera VARCHAR(255),
    custtierg VARCHAR(255),
    licencetierg VARCHAR(255),
    commtierd1a VARCHAR(255),
    custtiera VARCHAR(255),
    custtierf VARCHAR(255),
    licencetiere VARCHAR(255),
    custtiere VARCHAR(255)
)
        WITH (
        OIDS=FALSE
        );



CREATE TABLE staging.wmt_extract_filtered(
    id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY,
    trust VARCHAR(255),
    region_desc VARCHAR(255),
    region_code VARCHAR(255),
    ldu_desc VARCHAR(255),
    ldu_code VARCHAR(255),
    team_desc VARCHAR(255),
    team_code VARCHAR(255),
    om_surname VARCHAR(255),
    om_forename VARCHAR(255),
    om_grade_code VARCHAR(255),
    om_key VARCHAR(255),
    commtier0 VARCHAR(255),
    licencetier0 VARCHAR(255),
    custtier0 VARCHAR(255),
    comIn1st16Weeks VARCHAR(255),
    licin1st16weeks VARCHAR(255),
    datestamp VARCHAR(255),
    vcrn_count VARCHAR(255),
    pdu_code VARCHAR(255),
    pdu_desc VARCHAR(255),
    commtierd0 VARCHAR(255),
    commtierd1 VARCHAR(255),
    commtierd2 VARCHAR(255),
    commtierd3 VARCHAR(255),
    commtierc0 VARCHAR(255),
    commtierc1 VARCHAR(255),
    commtierc2 VARCHAR(255),
    commtierc3 VARCHAR(255),
    commtierb0 VARCHAR(255),
    commtierb1 VARCHAR(255),
    commtierb2 VARCHAR(255),
    commtierb3 VARCHAR(255),
    commtiera0 VARCHAR(255),
    commtiera1 VARCHAR(255),
    commtiera2 VARCHAR(255),
    commtiera3 VARCHAR(255),
    licencetierd0 VARCHAR(255),
    licencetierd1 VARCHAR(255),
    licencetierd2 VARCHAR(255),
    licencetierd3 VARCHAR(255),
    licencetierc0 VARCHAR(255),
    licencetierc1 VARCHAR(255),
    licencetierc2 VARCHAR(255),
    licencetierc3 VARCHAR(255),
    licencetierb0 VARCHAR(255),
    licencetierb1 VARCHAR(255),
    licencetierb2 VARCHAR(255),
    licencetierb3 VARCHAR(255),
    licencetiera0 VARCHAR(255),
    licencetiera1 VARCHAR(255),
    licencetiera2 VARCHAR(255),
    licencetiera3 VARCHAR(255),
    custtierd0 VARCHAR(255),
    custtierd1 VARCHAR(255),
    custtierd2 VARCHAR(255),
    custtierd3 VARCHAR(255),
    custtierc0 VARCHAR(255),
    custtierc1 VARCHAR(255),
    custtierc2 VARCHAR(255),
    custtierc3 VARCHAR(255),
    custtierb0 VARCHAR(255),
    custtierb1 VARCHAR(255),
    custtierb2 VARCHAR(255),
    custtierb3 VARCHAR(255),
    custtiera0 VARCHAR(255),
    custtiera1 VARCHAR(255),
    custtiera2 VARCHAR(255),
    custtiera3 VARCHAR(255),
    commtierc1a VARCHAR(255),
    commtiere VARCHAR(255),
    licencetierf VARCHAR(255),
    commtierg VARCHAR(255),
    commtierf VARCHAR(255),
    commtiera VARCHAR(255),
    licencetiera VARCHAR(255),
    custtierg VARCHAR(255),
    licencetierg VARCHAR(255),
    commtierd1a VARCHAR(255),
    custtiera VARCHAR(255),
    custtierf VARCHAR(255),
    licencetiere VARCHAR(255),
    custtiere VARCHAR(255)
)
        WITH (
        OIDS=FALSE
        );



CREATE TABLE staging.wmt_extract_sa(
    id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY,
    case_ref_no VARCHAR(255),
    tier_code VARCHAR(255),
    team_code VARCHAR(255),
    om_grade_code VARCHAR(255),
    om_key VARCHAR(255),
    location VARCHAR(255),
    disposal_type_desc VARCHAR(255),
    disposal_type_code VARCHAR(255),
    standalone_order VARCHAR(255),
    row_type VARCHAR(8) NOT NULL DEFAULT 'S'
)
        WITH (
        OIDS=FALSE
        );



-- ------------ Write CREATE-CONSTRAINT-stage scripts -----------

ALTER TABLE staging.arms
ADD CONSTRAINT pk__arms__3213e83f94fa5f35_592721164 PRIMARY KEY (id);



ALTER TABLE staging.cms
ADD CONSTRAINT pk__cms__3213e83f08d59c6a_624721278 PRIMARY KEY (id);



ALTER TABLE staging.court_reporters
ADD CONSTRAINT pk__court_re__3213e83f81dcb7b5_656721392 PRIMARY KEY (id);



ALTER TABLE staging.court_reports
ADD CONSTRAINT pk__court_re__3213e83ff00e15b4_688721506 PRIMARY KEY (id);



ALTER TABLE staging.flag_o_due
ADD CONSTRAINT pk__flag_o_d__3213e83fcd9cf8a4_720721620 PRIMARY KEY (id);



ALTER TABLE staging.flag_priority
ADD CONSTRAINT pk__flag_pri__3213e83f37a8595f_752721734 PRIMARY KEY (id);



ALTER TABLE staging.flag_upw
ADD CONSTRAINT pk__flag_upw__3213e83f8b30b3cc_784721848 PRIMARY KEY (id);



ALTER TABLE staging.flag_warr_4_n
ADD CONSTRAINT pk__flag_war__3213e83fcafe4daf_816721962 PRIMARY KEY (id);



ALTER TABLE staging.gs
ADD CONSTRAINT pk__gs__3213e83f6b287dd1_848722076 PRIMARY KEY (id);



ALTER TABLE staging.included_excluded
ADD CONSTRAINT pk__included__3213e83fb4989d2c_528720936 PRIMARY KEY (id);



ALTER TABLE staging.inst_reports
ADD CONSTRAINT pk__inst_rep__3213e83f889d84d1_880722190 PRIMARY KEY (id);



ALTER TABLE staging.knex_migrations
ADD CONSTRAINT pk__knex_mig__3213e83fb204299d_912722304 PRIMARY KEY (id);



ALTER TABLE staging.omic
ADD CONSTRAINT pk__omic__3213e83fa476416b_560721050 PRIMARY KEY (id);



ALTER TABLE staging.omic_teams
ADD CONSTRAINT pk__omic_wmt__3213e83f807ef08f_960722475 PRIMARY KEY (id);



ALTER TABLE staging.suspended_lifers
ADD CONSTRAINT pk__suspende__3213e83fb6072b82_992722589 PRIMARY KEY (id);



ALTER TABLE staging.t2a
ADD CONSTRAINT pk__t2a__3213e83f384899c3_1024722703 PRIMARY KEY (id);



ALTER TABLE staging.t2a_detail
ADD CONSTRAINT pk__t2a_deta__3213e83f72351239_1056722817 PRIMARY KEY (id);



ALTER TABLE staging.wmt_extract
ADD CONSTRAINT pk__wmt_extr__3213e83f7a6d5e31_1088722931 PRIMARY KEY (id);



ALTER TABLE staging.wmt_extract_filtered
ADD CONSTRAINT pk__wmt_extr__3213e83fbbc7f69c_1120723045 PRIMARY KEY (id);



ALTER TABLE staging.wmt_extract_sa
ADD CONSTRAINT pk__wmt_extr__3213e83fc4c06ba8_1152723159 PRIMARY KEY (id);


-- ------------ Write CREATE-DATABASE-stage scripts -----------

CREATE SCHEMA IF NOT EXISTS app;



-- ------------ Write CREATE-TABLE-stage scripts -----------

CREATE TABLE app.adjustment_category(
    id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY,
    category VARCHAR(255)
)
        WITH (
        OIDS=FALSE
        );



CREATE TABLE app.adjustment_reason(
    id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY,
    contact_code VARCHAR(255),
    contact_description VARCHAR(255),
    category_id BIGINT NOT NULL,
    points BIGINT
)
        WITH (
        OIDS=FALSE
        );



CREATE TABLE app.adjustments(
    id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY,
    adjustment_reason_id BIGINT NOT NULL,
    workload_owner_id BIGINT NOT NULL,
    points BIGINT NOT NULL,
    contact_id BIGINT,
    effective_from TIMESTAMP WITH TIME ZONE NOT NULL,
    effective_to TIMESTAMP WITH TIME ZONE,
    status VARCHAR(255),
    case_ref_no VARCHAR(255)
)
        WITH (
        OIDS=FALSE
        );



CREATE TABLE app.case_category(
    id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY,
    category_id BIGINT,
    category_name VARCHAR(20)
)
        WITH (
        OIDS=FALSE
        );



CREATE TABLE app.case_details(
    id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY,
    workload_id BIGINT NOT NULL,
    row_type VARCHAR(255),
    case_ref_no VARCHAR(255) NOT NULL,
    tier_code BIGINT NOT NULL,
    team_code VARCHAR(255) NOT NULL,
    grade_code VARCHAR(255),
    location VARCHAR(20)
)
        WITH (
        OIDS=FALSE
        );



CREATE TABLE app.court_reports(
    id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY,
    workload_owner_id BIGINT,
    staging_id BIGINT NOT NULL,
    total_sdrs BIGINT NOT NULL,
    total_fdrs BIGINT NOT NULL,
    total_oral_reports BIGINT NOT NULL,
    workload_report_id BIGINT
)
        WITH (
        OIDS=FALSE
        );



CREATE TABLE app.court_reports_calculations(
    id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY,
    workload_report_id BIGINT NOT NULL,
    court_reports_id BIGINT NOT NULL,
    workload_points_id BIGINT NOT NULL,
    reduction_hours NUMERIC(8,2) NOT NULL DEFAULT '0',
    contracted_hours NUMERIC(8,2) NOT NULL
)
        WITH (
        OIDS=FALSE
        );



CREATE TABLE app.export_file(
    id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY,
    file_type VARCHAR(20) NOT NULL,
    date_created TIMESTAMP WITH TIME ZONE NOT NULL,
    filepath VARCHAR(250) NOT NULL,
    is_enabled boolean DEFAULT TRUE
)
        WITH (
        OIDS=FALSE
        );



CREATE TABLE app.knex_migrations(
    id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY,
    name VARCHAR(255),
    batch BIGINT,
    migration_time TIMESTAMP WITH TIME ZONE
)
        WITH (
        OIDS=FALSE
        );



CREATE TABLE app.knex_migrations_lock(
    is_locked BIGINT
)
        WITH (
        OIDS=FALSE
        );



CREATE TABLE app.ldu(
    id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY,
    code VARCHAR(255),
    description VARCHAR(255),
    region_id BIGINT NOT NULL,
    effective_from TIMESTAMP WITH TIME ZONE DEFAULT timezone('utc', now()),
    effective_to TIMESTAMP WITH TIME ZONE
)
        WITH (
        OIDS=FALSE
        );



CREATE TABLE app.offender_manager(
    id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY,
    key VARCHAR(255),
    forename VARCHAR(255),
    surname VARCHAR(255),
    type_id BIGINT NOT NULL,
    effective_from TIMESTAMP WITH TIME ZONE DEFAULT timezone('utc', now()),
    effective_to TIMESTAMP WITH TIME ZONE
)
        WITH (
        OIDS=FALSE
        );



CREATE TABLE app.offender_manager_type(
    id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY,
    grade_code VARCHAR(255),
    description VARCHAR(255)
)
        WITH (
        OIDS=FALSE
        );



CREATE TABLE app.omic_case_details(
    id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY,
    omic_workload_id BIGINT NOT NULL,
    row_type VARCHAR(255),
    case_ref_no VARCHAR(255) NOT NULL,
    tier_code BIGINT NOT NULL,
    team_code VARCHAR(255) NOT NULL,
    grade_code VARCHAR(255),
    location VARCHAR(20)
)
        WITH (
        OIDS=FALSE
        );



CREATE TABLE app.omic_tiers(
    id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY,
    omic_workload_id BIGINT NOT NULL,
    location VARCHAR(255) NOT NULL,
    tier_number NUMERIC(5,0) NOT NULL,
    overdue_terminations_total NUMERIC(5,0) NOT NULL,
    warrants_total NUMERIC(5,0) NOT NULL,
    unpaid_work_total NUMERIC(5,0) NOT NULL,
    total_cases NUMERIC(5,0) NOT NULL,
    t2a_overdue_terminations_total BIGINT NOT NULL DEFAULT (0),
    t2a_warrants_total BIGINT NOT NULL DEFAULT (0),
    t2a_unpaid_work_total BIGINT NOT NULL DEFAULT (0),
    t2a_total_cases BIGINT NOT NULL DEFAULT (0),
    suspended_total BIGINT NOT NULL DEFAULT (0),
    suspended_lifer_total BIGINT NOT NULL DEFAULT (0),
    total_filtered_cases NUMERIC(5,0) NOT NULL DEFAULT (0)
)
        WITH (
        OIDS=FALSE
        );



CREATE TABLE app.omic_workload(
    id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY,
    workload_owner_id BIGINT,
    total_cases BIGINT NOT NULL,
    total_community_cases BIGINT NOT NULL,
    total_custody_cases BIGINT NOT NULL,
    total_license_cases BIGINT NOT NULL,
    monthly_sdrs BIGINT NOT NULL,
    sdr_due_next_30_days BIGINT NOT NULL,
    sdr_conversions_last_30_days BIGINT NOT NULL,
    paroms_completed_last_30_days BIGINT NOT NULL,
    paroms_due_next_30_days BIGINT NOT NULL,
    license_last_16_weeks BIGINT NOT NULL,
    community_last_16_weeks BIGINT NOT NULL,
    arms_community_cases BIGINT NOT NULL DEFAULT (0),
    arms_license_cases BIGINT NOT NULL DEFAULT (0),
    staging_id BIGINT,
    workload_report_id BIGINT,
    total_t2a_cases BIGINT NOT NULL DEFAULT (0),
    total_t2a_community_cases BIGINT NOT NULL DEFAULT (0),
    total_t2a_custody_cases BIGINT NOT NULL DEFAULT (0),
    total_t2a_license_cases BIGINT NOT NULL DEFAULT (0),
    total_filtered_cases NUMERIC(5,0) NOT NULL DEFAULT (0),
    total_filtered_community_cases NUMERIC(5,0) NOT NULL DEFAULT (0),
    total_filtered_custody_cases NUMERIC(5,0) NOT NULL DEFAULT (0),
    total_filtered_license_cases NUMERIC(5,0) NOT NULL DEFAULT (0)
)
        WITH (
        OIDS=FALSE
        );



CREATE TABLE app.omic_workload_points_calculations(
    id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY,
    workload_report_id BIGINT NOT NULL,
    workload_points_id BIGINT NOT NULL,
    omic_workload_id BIGINT NOT NULL,
    custody_points BIGINT NOT NULL,
    licence_points BIGINT NOT NULL,
    sdr_points BIGINT NOT NULL,
    sdr_conversion_points BIGINT NOT NULL,
    paroms_points BIGINT NOT NULL,
    nominal_target BIGINT NOT NULL,
    available_points BIGINT NOT NULL,
    contracted_hours NUMERIC(8,2) NOT NULL,
    arms_total_cases BIGINT NOT NULL DEFAULT (0),
    t2a_workload_points_id BIGINT,
    arms_points BIGINT NOT NULL DEFAULT (0)
)
        WITH (
        OIDS=FALSE
        );



CREATE TABLE app.reduction_category(
    id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY,
    category VARCHAR(255)
)
        WITH (
        OIDS=FALSE
        );



CREATE TABLE app.reduction_reason(
    id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY,
    reason VARCHAR(255),
    reason_short_name VARCHAR(255),
    category_id BIGINT NOT NULL,
    allowance_percentage NUMERIC(5,2),
    max_allowance_percentage NUMERIC(5,2),
    months_to_expiry BIGINT,
    is_enabled boolean NOT NULL DEFAULT TRUE
)
        WITH (
        OIDS=FALSE
        );



CREATE TABLE app.reductions(
    id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY,
    reduction_reason_id BIGINT NOT NULL DEFAULT '11',
    workload_owner_id BIGINT NOT NULL,
    hours NUMERIC(8,1) NOT NULL,
    effective_from TIMESTAMP WITH TIME ZONE NOT NULL,
    effective_to TIMESTAMP WITH TIME ZONE,
    status VARCHAR(255),
    notes TEXT,
    updated_date TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT timezone('utc', now()),
    user_id BIGINT
)
        WITH (
        OIDS=FALSE
        );



CREATE TABLE app.reductions_history(
    id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY,
    reduction_id BIGINT NOT NULL,
    reduction_reason_id BIGINT NOT NULL DEFAULT '11',
    hours NUMERIC(8,2) NOT NULL,
    effective_from TIMESTAMP WITH TIME ZONE NOT NULL,
    effective_to TIMESTAMP WITH TIME ZONE,
    status VARCHAR(255),
    notes TEXT,
    updated_date TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT timezone('utc', now()),
    user_id BIGINT
)
        WITH (
        OIDS=FALSE
        );



CREATE TABLE app.region(
    id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY,
    code VARCHAR(255),
    description VARCHAR(255)
)
        WITH (
        OIDS=FALSE
        );



CREATE TABLE app.roles(
    id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY,
    role VARCHAR(255) NOT NULL
)
        WITH (
        OIDS=FALSE
        );



CREATE TABLE app.row_type_definitions(
    id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY,
    row_type VARCHAR(255) NOT NULL,
    row_type_full_name VARCHAR(255) NOT NULL
)
        WITH (
        OIDS=FALSE
        );



CREATE TABLE app.tasks(
    id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY,
    submitting_agent VARCHAR(255),
    type VARCHAR(255),
    additional_data TEXT,
    workload_report_id BIGINT,
    date_created TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT timezone('utc', now()),
    date_processed TIMESTAMP WITH TIME ZONE,
    status VARCHAR(30),
    date_started TIMESTAMP WITH TIME ZONE
)
        WITH (
        OIDS=FALSE
        );



CREATE TABLE app.team(
    id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY,
    code VARCHAR(255),
    ldu_id BIGINT NOT NULL,
    description VARCHAR(255),
    effective_from TIMESTAMP WITH TIME ZONE DEFAULT timezone('utc', now()),
    effective_to TIMESTAMP WITH TIME ZONE
)
        WITH (
        OIDS=FALSE
        );



CREATE TABLE app.tiers(
    id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY,
    workload_id BIGINT NOT NULL,
    location VARCHAR(255) NOT NULL,
    tier_number NUMERIC(5,0) NOT NULL,
    overdue_terminations_total NUMERIC(5,0) NOT NULL,
    warrants_total NUMERIC(5,0) NOT NULL,
    unpaid_work_total NUMERIC(5,0) NOT NULL,
    total_cases NUMERIC(5,0) NOT NULL,
    t2a_overdue_terminations_total BIGINT NOT NULL DEFAULT (0),
    t2a_warrants_total BIGINT NOT NULL DEFAULT (0),
    t2a_unpaid_work_total BIGINT NOT NULL DEFAULT (0),
    t2a_total_cases BIGINT NOT NULL DEFAULT (0),
    suspended_total BIGINT NOT NULL DEFAULT (0),
    suspended_lifer_total BIGINT NOT NULL DEFAULT (0),
    total_filtered_cases NUMERIC(5,0) NOT NULL DEFAULT (0)
)
        WITH (
        OIDS=FALSE
        );



CREATE TABLE app.user_role(
    id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY,
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    last_updated TIMESTAMP WITH TIME ZONE,
    last_updated_by VARCHAR(255)
)
        WITH (
        OIDS=FALSE
        );



CREATE TABLE app.users(
    id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY,
    username VARCHAR(255) NOT NULL,
    name VARCHAR(255)
)
        WITH (
        OIDS=FALSE
        );



CREATE TABLE app.workload(
    id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY,
    workload_owner_id BIGINT,
    total_cases BIGINT NOT NULL,
    total_community_cases BIGINT NOT NULL,
    total_custody_cases BIGINT NOT NULL,
    total_license_cases BIGINT NOT NULL,
    monthly_sdrs BIGINT NOT NULL,
    sdr_due_next_30_days BIGINT NOT NULL,
    sdr_conversions_last_30_days BIGINT NOT NULL,
    paroms_completed_last_30_days BIGINT NOT NULL,
    paroms_due_next_30_days BIGINT NOT NULL,
    license_last_16_weeks BIGINT NOT NULL,
    community_last_16_weeks BIGINT NOT NULL,
    arms_community_cases BIGINT NOT NULL DEFAULT (0),
    arms_license_cases BIGINT NOT NULL DEFAULT (0),
    staging_id BIGINT,
    workload_report_id BIGINT,
    total_t2a_cases BIGINT NOT NULL DEFAULT (0),
    total_t2a_community_cases BIGINT NOT NULL DEFAULT (0),
    total_t2a_custody_cases BIGINT NOT NULL DEFAULT (0),
    total_t2a_license_cases BIGINT NOT NULL DEFAULT (0),
    total_filtered_cases NUMERIC(5,0) NOT NULL DEFAULT (0),
    total_filtered_community_cases NUMERIC(5,0) NOT NULL DEFAULT (0),
    total_filtered_custody_cases NUMERIC(5,0) NOT NULL DEFAULT (0),
    total_filtered_license_cases NUMERIC(5,0) NOT NULL DEFAULT (0)
)
        WITH (
        OIDS=FALSE
        );



CREATE TABLE app.workload_owner(
    id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY,
    offender_manager_id BIGINT NOT NULL,
    contracted_hours NUMERIC(8,2),
    team_id BIGINT NOT NULL
)
        WITH (
        OIDS=FALSE
        );



CREATE TABLE app.workload_points(
    id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY,
    comm_tier_1 BIGINT NOT NULL,
    comm_tier_2 BIGINT NOT NULL,
    comm_tier_3 BIGINT NOT NULL,
    comm_tier_4 BIGINT NOT NULL,
    comm_tier_5 BIGINT NOT NULL,
    comm_tier_6 BIGINT NOT NULL,
    comm_tier_7 BIGINT NOT NULL,
    cust_tier_1 BIGINT NOT NULL,
    cust_tier_2 BIGINT NOT NULL,
    cust_tier_3 BIGINT NOT NULL,
    cust_tier_4 BIGINT NOT NULL,
    cust_tier_5 BIGINT NOT NULL,
    cust_tier_6 BIGINT NOT NULL,
    cust_tier_7 BIGINT NOT NULL,
    lic_tier_1 BIGINT NOT NULL,
    lic_tier_2 BIGINT NOT NULL,
    lic_tier_3 BIGINT NOT NULL,
    lic_tier_4 BIGINT NOT NULL,
    lic_tier_5 BIGINT NOT NULL,
    lic_tier_6 BIGINT NOT NULL,
    lic_tier_7 BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    sdr BIGINT NOT NULL,
    sdr_conversion BIGINT NOT NULL,
    nominal_target_spo BIGINT NOT NULL,
    nominal_target_po BIGINT NOT NULL,
    default_contracted_hours_po NUMERIC(18,0) NOT NULL,
    default_contracted_hours_pso NUMERIC(18,0) NOT NULL,
    weighting_o BIGINT NOT NULL,
    weighting_w BIGINT NOT NULL,
    weighting_u BIGINT NOT NULL,
    paroms_enabled boolean NOT NULL DEFAULT FALSE,
    parom BIGINT NOT NULL,
    effective_from TIMESTAMP WITH TIME ZONE DEFAULT timezone('utc', now()),
    effective_to TIMESTAMP WITH TIME ZONE,
    weighting_arms_lic BIGINT DEFAULT (0),
    weighting_arms_comm BIGINT DEFAULT (0),
    is_t2a boolean NOT NULL DEFAULT FALSE,
    default_contracted_hours_spo NUMERIC(18,0),
    comm_tier_8 BIGINT NOT NULL DEFAULT (0),
    comm_tier_9 BIGINT NOT NULL DEFAULT (0),
    comm_tier_10 BIGINT NOT NULL DEFAULT (0),
    cust_tier_8 BIGINT NOT NULL DEFAULT (0),
    cust_tier_9 BIGINT NOT NULL DEFAULT (0),
    cust_tier_10 BIGINT NOT NULL DEFAULT (0),
    lic_tier_8 BIGINT NOT NULL DEFAULT (0),
    lic_tier_9 BIGINT NOT NULL DEFAULT (0),
    lic_tier_10 BIGINT NOT NULL DEFAULT (0),
    comm_tier_11 BIGINT NOT NULL DEFAULT (0),
    comm_tier_12 BIGINT NOT NULL DEFAULT (0),
    comm_tier_13 BIGINT NOT NULL DEFAULT (0),
    comm_tier_14 BIGINT NOT NULL DEFAULT (0),
    comm_tier_15 BIGINT NOT NULL DEFAULT (0),
    comm_tier_16 BIGINT NOT NULL DEFAULT (0),
    cust_tier_11 BIGINT NOT NULL DEFAULT (0),
    cust_tier_12 BIGINT NOT NULL DEFAULT (0),
    cust_tier_13 BIGINT NOT NULL DEFAULT (0),
    cust_tier_14 BIGINT NOT NULL DEFAULT (0),
    cust_tier_15 BIGINT NOT NULL DEFAULT (0),
    cust_tier_16 BIGINT NOT NULL DEFAULT (0),
    lic_tier_11 BIGINT NOT NULL DEFAULT (0),
    lic_tier_12 BIGINT NOT NULL DEFAULT (0),
    lic_tier_13 BIGINT NOT NULL DEFAULT (0),
    lic_tier_14 BIGINT NOT NULL DEFAULT (0),
    lic_tier_15 BIGINT NOT NULL DEFAULT (0),
    lic_tier_16 BIGINT NOT NULL DEFAULT (0)
)
        WITH (
        OIDS=FALSE
        );



CREATE TABLE app.workload_points_calculations(
    id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY,
    workload_report_id BIGINT NOT NULL,
    workload_points_id BIGINT NOT NULL,
    workload_id BIGINT NOT NULL,
    total_points BIGINT NOT NULL,
    sdr_points BIGINT NOT NULL,
    sdr_conversion_points BIGINT NOT NULL,
    paroms_points BIGINT NOT NULL,
    nominal_target BIGINT NOT NULL,
    available_points BIGINT NOT NULL,
    reduction_hours NUMERIC(8,2) NOT NULL DEFAULT '0',
    contracted_hours NUMERIC(8,2) NOT NULL,
    cms_adjustment_points BIGINT NOT NULL DEFAULT (0),
    gs_adjustment_points BIGINT NOT NULL DEFAULT (0),
    arms_total_cases BIGINT NOT NULL DEFAULT (0),
    t2a_workload_points_id BIGINT,
    arms_points BIGINT NOT NULL DEFAULT (0)
)
        WITH (
        OIDS=FALSE
        );



CREATE TABLE app.workload_report(
    id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY,
    effective_from TIMESTAMP WITH TIME ZONE DEFAULT timezone('utc', now()),
    effective_to TIMESTAMP WITH TIME ZONE,
    records_total BIGINT NOT NULL DEFAULT '0',
    status VARCHAR(255),
    status_description VARCHAR(255)
)
        WITH (
        OIDS=FALSE
        );



-- ------------ Write CREATE-VIEW-stage scripts -----------

CREATE OR REPLACE VIEW app.arms_export_view (regionname, regionid, lduname, lduid, teamname, teamid, assessmentdate, crn, workload_owner_id, omname, grade_code, sentencetype, releasedate, completeddate) AS
SELECT
    r.description AS regionname, r.id AS regionid, l.description AS lduname, l.id AS lduid, t.description AS teamname, t.id AS teamid, CAST (a.assessment_date AS TIMESTAMP(6) WITHOUT TIME ZONE) AS assessmentdate, a.crn AS crn, wo.id AS workload_owner_id, CONCAT(om.forename, ' ', om.surname) AS omname, omt.grade_code, a.sentence_type AS sentencetype, a.disposal_or_release_date AS releasedate, a.completed_date AS completeddate
    FROM staging.arms AS a
    JOIN app.team AS t
        ON LOWER(a.assessment_team_key) = LOWER(t.code)
    JOIN app.ldu AS l
        ON t.ldu_id = l.id
    JOIN app.region AS r
        ON r.id = l.region_id
    JOIN app.offender_manager AS om
        ON LOWER(a.assessment_staff_key) = LOWER(om.key)
    JOIN app.offender_manager_type AS omt
        ON om.type_id = omt.id
    JOIN app.workload_owner AS wo
        ON t.id = wo.team_id AND om.id = wo.offender_manager_id
    JOIN app.workload AS w
        ON wo.id = w.workload_owner_id
    JOIN app.workload_report AS wr
        ON wr.id = w.workload_report_id
    WHERE wr.effective_from IS NOT NULL AND wr.effective_to IS NULL;
/* app.case_details_export_view source */
/* case_details_export_view */;



CREATE OR REPLACE VIEW app.case_details_export_view (regionname, regionid, lduname, lduid, teamname, teamid, workloadid, workloadownerid, tiercode, rowtype, casereferenceno, casetype, offendermanagername, gradecode) AS
SELECT
    r.description AS regionname, r.id AS regionid, l.description AS lduname, l.id AS lduid, t.description AS teamname, t.id AS teamid, w.id AS workloadid, w.workload_owner_id AS workloadownerid, cc.category_name AS tiercode, rtd.row_type_full_name AS rowtype, case_ref_no AS casereferenceno, location AS casetype, CONCAT(om.forename, ' ', om.surname) AS offendermanagername, omt.grade_code AS gradecode
    FROM app.case_details AS c
    JOIN app.row_type_definitions AS rtd
        ON LOWER(c.row_type) = LOWER(rtd.row_type)
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
    WHERE wr.effective_from IS NOT NULL AND wr.effective_to IS NULL AND LOWER(c.row_type) != LOWER('L');
/* app.case_details_view source */
/* case_details_view */;



CREATE OR REPLACE VIEW app.case_details_view (workloadownerid, tiercode, rowtype, casereferenceno, casetype, incustody, registerlevel, registercategory, registercategorydescription, registrationdate) AS
SELECT
    w.workload_owner_id AS workloadownerid, c.tier_code AS tiercode, rtd.row_type_full_name AS rowtype, c.case_ref_no AS casereferenceno, c.location AS casetype, in_custody AS incustody, register_level AS registerlevel, register_category AS registercategory, register_category_description AS registercategorydescription, registration_date AS registrationdate
    FROM app.case_details AS c
    JOIN app.row_type_definitions AS rtd
        ON LOWER(c.row_type) = LOWER(rtd.row_type)
    JOIN app.workload AS w
        ON c.workload_id = w.id
    JOIN app.workload_report AS wr
        ON w.workload_report_id = wr.id
    LEFT OUTER JOIN staging.suspended_lifers AS sl
        ON LOWER(c.case_ref_no) = LOWER(sl.case_ref_no)
    WHERE wr.effective_from IS NOT NULL AND wr.effective_to IS NULL;
/* app.cms_export_view source */;



CREATE OR REPLACE VIEW app.cms_export_view (regionname, regionid, lduname, lduid, teamname, teamid, contactdate, workload_owner_id, omname, omgradecode, contact_description) AS
SELECT
    r.description AS regionname, r.id AS regionid, l.description AS lduname, l.id AS lduid, t.description AS teamname, t.id AS teamid, a.effective_from AS contactdate, wo.id AS workload_owner_id, CONCAT(om.forename, ' ', om.surname) AS omname, omt.grade_code AS omgradecode, ar.contact_description AS contact_description
    FROM app.adjustments AS a
    JOIN app.adjustment_reason AS ar
        ON a.adjustment_reason_id = ar.id
    JOIN app.adjustment_category AS ac
        ON ar.category_id = ac.id
    JOIN app.workload_owner AS wo
        ON a.workload_owner_id = wo.id
    JOIN app.workload AS w
        ON w.workload_owner_id = wo.id
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
    JOIN app.workload_report AS wr
        ON w.workload_report_id = wr.id
    WHERE wr.effective_from IS NOT NULL AND wr.effective_to IS NULL AND LOWER(ac.category) = LOWER('Case Management Support') AND LOWER(a.status) = LOWER('ACTIVE');
/* app.contact_cms_export_view source */
/* contact_cms_export_view */;



CREATE OR REPLACE VIEW app.contact_cms_export_view (contactregionname, contactregionid, contactlduname, contactlduid, contactteamname, contactteamid, contactdate, contactworkloadownerid, contactname, contactgradecode, contactid, contactdescription, contactcode, contactpoints, caserefno) AS
SELECT
    r.description AS contactregionname, r.id AS contactregionid, l.description AS contactlduname, l.id AS contactlduid, t.description AS contactteamname, t.id AS contactteamid, a.effective_from AS contactdate, wo.id AS contactworkloadownerid, CONCAT(om.forename, ' ', om.surname) AS contactname, omt.grade_code AS contactgradecode, a.contact_id AS contactid, ar.contact_description AS contactdescription, ar.contact_code AS contactcode, a.points AS contactpoints, a.case_ref_no AS caserefno
    FROM app.adjustments AS a
    JOIN app.adjustment_reason AS ar
        ON a.adjustment_reason_id = ar.id
    JOIN app.adjustment_category AS ac
        ON ar.category_id = ac.id
    JOIN app.workload_owner AS wo
        ON a.workload_owner_id = wo.id
    JOIN app.workload AS w
        ON w.workload_owner_id = wo.id
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
    JOIN app.workload_report AS wr
        ON w.workload_report_id = wr.id
    WHERE wr.effective_from IS NOT NULL AND wr.effective_to IS NULL AND LOWER(ac.category) = LOWER('Case Management Support') AND LOWER(a.status) = LOWER('ACTIVE') AND a.points > 0;
/* app.expiring_reductions_export_view source */;



CREATE OR REPLACE VIEW app.expiring_reductions_export_view (workload_owner_id, team_id, ldu_id, region_id, region_name, ldu_name, team_name, name, contracted_hours, reduction_reason, reduction_id, amount, start_date, end_date, reduction_status, additional_notes, grade_code, user_id, manager_responsible, workload_type) AS
SELECT
    wo.id AS workload_owner_id, team.id AS team_id, ldu.id AS ldu_id, region.id AS region_id, region.description AS region_name, ldu.description AS ldu_name, team.description AS team_name, CONCAT(om.forename, ' ', om.surname) AS name, wo.contracted_hours AS contracted_hours, rr.reason_short_name AS reduction_reason, r.id AS reduction_id, r.hours AS amount, r.effective_from AS start_date, r.effective_to AS end_date, r.status AS reduction_status, r.notes AS additional_notes, omt.grade_code AS grade_code, u.id AS user_id, u.name AS manager_responsible, 'probation'::TEXT AS workload_type
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
    JOIN app.users AS u
        ON r.user_id = u.id
    WHERE wr.effective_from IS NOT NULL AND wr.effective_to IS NULL AND LOWER(r.status) = LOWER('ACTIVE') AND r.effective_to BETWEEN timezone('utc', now()) AND timezone('utc', now()) + (30::NUMERIC || ' days')::INTERVAL;
/* app.expiring_reductions_view source */;



CREATE OR REPLACE VIEW app.expiring_reductions_view (workload_owner_id, team_id, ldu_id, region_id, region_name, ldu_name, team_name, name, contracted_hours, reduction_reason, reduction_id, amount, start_date, end_date, reduction_status, additional_notes, grade_code, user_id, manager_responsible, workload_type) AS
SELECT
    wo.id AS workload_owner_id, team.id AS team_id, ldu.id AS ldu_id, region.id AS region_id, region.description AS region_name, ldu.description AS ldu_name, team.description AS team_name, CONCAT(om.forename, ' ', om.surname) AS name, wo.contracted_hours AS contracted_hours, rr.reason_short_name AS reduction_reason, r.id AS reduction_id, r.hours AS amount, r.effective_from AS start_date, r.effective_to AS end_date, r.status AS reduction_status, r.notes AS additional_notes, omt.grade_code AS grade_code, u.id AS user_id, u.name AS manager_responsible, 'probation'::TEXT AS workload_type
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
    JOIN app.users AS u
        ON r.user_id = u.id
    WHERE wr.effective_from IS NOT NULL AND wr.effective_to IS NULL AND LOWER(r.status) = LOWER('ACTIVE') AND r.effective_to BETWEEN timezone('utc', now()) AND timezone('utc', now()) + (30::NUMERIC || ' days')::INTERVAL
UNION
SELECT
    wo.id AS workload_owner_id, team.id AS team_id, ldu.id AS ldu_id, region.id AS region_id, region.description AS region_name, ldu.description AS ldu_name, team.description AS team_name, CONCAT(om.forename, ' ', om.surname) AS name, wo.contracted_hours AS contracted_hours, rr.reason_short_name AS reduction_reason, r.id AS reduction_id, r.hours AS amount, r.effective_from AS start_date, r.effective_to AS end_date, r.status AS reduction_status, r.notes AS additional_notes, omt.grade_code AS grade_code, u.id AS user_id, u.name AS manager_responsible, 'court-reports'::TEXT AS workload_type
    FROM app.workload_owner AS wo
    JOIN app.team AS team
        ON wo.team_id = team.id
    JOIN app.ldu AS ldu
        ON team.ldu_id = ldu.id
    JOIN app.region AS region
        ON region.id = ldu.region_id
    JOIN app.court_reports AS cr
        ON wo.id = cr.workload_owner_id
    JOIN app.court_reports_calculations AS crc
        ON crc.court_reports_id = cr.id
    JOIN app.workload_report AS wr
        ON wr.id = crc.workload_report_id
    JOIN app.offender_manager AS om
        ON om.id = wo.offender_manager_id
    JOIN app.offender_manager_type AS omt
        ON om.type_id = omt.id
    JOIN app.reductions AS r
        ON r.workload_owner_id = wo.id
    JOIN app.reduction_reason AS rr
        ON r.reduction_reason_id = rr.id
    JOIN app.users AS u
        ON r.user_id = u.id
    WHERE wr.effective_from IS NOT NULL AND wr.effective_to IS NULL AND LOWER(r.status) = LOWER('ACTIVE') AND r.effective_to BETWEEN timezone('utc', now()) AND timezone('utc', now()) + (30::NUMERIC || ' days')::INTERVAL;
/* app.gs_export_view source */
/* gs_export_view */;



CREATE OR REPLACE VIEW app.gs_export_view (regionname, regionid, lduname, lduid, teamname, teamid, contactid, contactdate, workload_owner_id, omname, omgradecode, contact_description, contactcode, points, caserefno) AS
SELECT
    r.description AS regionname, r.id AS regionid, l.description AS lduname, l.id AS lduid, t.description AS teamname, t.id AS teamid, a.contact_id AS contactid, a.effective_from AS contactdate, wo.id AS workload_owner_id, CONCAT(om.forename, ' ', om.surname) AS omname, omt.grade_code AS omgradecode, ar.contact_description AS contact_description, ar.contact_code AS contactcode, a.points AS points, a.case_ref_no AS caserefno
    FROM app.adjustments AS a
    JOIN app.adjustment_reason AS ar
        ON a.adjustment_reason_id = ar.id
    JOIN app.adjustment_category AS ac
        ON ar.category_id = ac.id
    JOIN app.workload_owner AS wo
        ON a.workload_owner_id = wo.id
    JOIN app.workload AS w
        ON w.workload_owner_id = wo.id
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
    JOIN app.workload_report AS wr
        ON w.workload_report_id = wr.id
    WHERE wr.effective_from IS NOT NULL AND wr.effective_to IS NULL AND LOWER(ac.category) = LOWER('Group Supervision') AND LOWER(a.status) = LOWER('ACTIVE');
/* app.individual_capacity_view source */;



CREATE OR REPLACE VIEW app.individual_capacity_view (effective_from, total_points, available_points, reduction_hours, id, workload_report_id, contracted_hours) AS
SELECT
    wr.effective_from, wpc.total_points, wpc.available_points, wpc.reduction_hours, w.workload_owner_id AS id, wr.id AS workload_report_id, wpc.contracted_hours
    FROM app.workload_points_calculations AS wpc
    JOIN app.workload AS w
        ON wpc.workload_id = w.id
    JOIN app.workload_report AS wr
        ON wpc.workload_report_id = wr.id;



CREATE OR REPLACE VIEW app.individual_case_overview (workload_owner_id, team_id, ldu_id, region_id, grade_code, of_name, team_name, ldu_name, region_name, available_points, total_points, total_cases, contracted_hours, reduction_hours, cms_adjustment_points) AS
SELECT
    wo.id AS workload_owner_id, t.id AS team_id, l.id AS ldu_id, r.id AS region_id, om_type.grade_code AS grade_code, CONCAT(om.forename, ' ', om.surname) AS of_name, t.description AS team_name, l.description AS ldu_name, r.description AS region_name, wpc.available_points AS available_points, wpc.total_points AS total_points, (w.total_filtered_cases + w.total_t2a_cases) AS total_cases, wpc.contracted_hours AS contracted_hours, wpc.reduction_hours AS reduction_hours, wpc.cms_adjustment_points AS cms_adjustment_points
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



CREATE OR REPLACE VIEW app.individual_case_progress_view (forename, surname, community_last_16_weeks, license_last_16_weeks, total_cases, warrants_total, overdue_terminations_total, unpaid_work_total, id, count) AS
SELECT
    om.forename, om.surname, w.community_last_16_weeks AS community_last_16_weeks, w.license_last_16_weeks AS license_last_16_weeks, w.total_filtered_cases AS total_cases, SUM(tr.warrants_total) AS warrants_total, SUM(tr.overdue_terminations_total) AS overdue_terminations_total, SUM(tr.unpaid_work_total) AS unpaid_work_total, wo.id AS id, COUNT(*) AS count
    FROM app.workload AS w
    JOIN app.workload_points_calculations AS wpc
        ON wpc.workload_id = w.id
    JOIN app.workload_report AS wr
        ON wr.id = wpc.workload_report_id
    JOIN app.tiers AS tr
        ON tr.workload_id = w.id
    JOIN app.workload_owner AS wo
        ON wo.id = w.workload_owner_id
    JOIN app.offender_manager AS om
        ON om.id = wo.offender_manager_id
    WHERE wr.effective_from IS NOT NULL AND wr.effective_to IS NULL
    GROUP BY om.forename, om.surname, wo.id, w.community_last_16_weeks, w.license_last_16_weeks, w.total_filtered_cases;



CREATE OR REPLACE VIEW app.individual_court_reporter_overview (id, grade_code, link_id, name, contracted_hours, reduction_hours, total_sdrs, total_fdrs, total_oral_reports) AS
SELECT
    wo.id AS id, om_type.grade_code AS grade_code, t.id AS link_id, t.description AS name, crc.contracted_hours AS contracted_hours, crc.reduction_hours AS reduction_hours, cr.total_sdrs AS total_sdrs, cr.total_fdrs AS total_fdrs, cr.total_oral_reports AS total_oral_reports
    FROM app.workload_owner AS wo
    JOIN app.team AS t
        ON wo.team_id = t.id
    JOIN app.offender_manager AS om
        ON om.id = wo.offender_manager_id
    JOIN app.offender_manager_type AS om_type
        ON om_type.id = om.type_id
    JOIN app.court_reports AS cr
        ON wo.id = cr.workload_owner_id
    JOIN app.court_reports_calculations AS crc
        ON crc.court_reports_id = cr.id
    JOIN app.workload_report AS wr
        ON wr.id = crc.workload_report_id
    WHERE wr.effective_from IS NOT NULL AND wr.effective_to IS NULL;



CREATE OR REPLACE VIEW app.ldu_capacity_breakdown_view (id, link_id, name, grade_code, total_cases, total_t2a_cases, monthly_sdrs, sdr_conversions_last_30_days, paroms_completed_last_30_days, total_points, available_points, reduction_hours, cms_adjustment_points, gs_adjustment_points, contracted_hours, arms_total_cases, count) AS
SELECT
    l.id AS id, t.id AS link_id, t.description AS name, omt.grade_code, SUM(w.total_filtered_cases) AS total_cases, SUM(w.total_t2a_cases) AS total_t2a_cases, SUM(w.monthly_sdrs) AS monthly_sdrs, SUM(w.sdr_conversions_last_30_days) AS sdr_conversions_last_30_days, SUM(w.paroms_completed_last_30_days) AS paroms_completed_last_30_days, SUM(wpc.total_points) AS total_points, SUM(wpc.available_points) AS available_points, SUM(wpc.reduction_hours) AS reduction_hours, SUM(wpc.cms_adjustment_points) AS cms_adjustment_points, SUM(wpc.gs_adjustment_points) AS gs_adjustment_points, SUM(wpc.contracted_hours) AS contracted_hours, SUM(wpc.arms_total_cases) AS arms_total_cases, COUNT(*) AS count
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



CREATE OR REPLACE VIEW app.ldu_capacity_view (total_points, available_points, reduction_hours, contracted_hours, effective_from, workload_report_id, id, count) AS
SELECT
    SUM(total_points) AS total_points, SUM(available_points) AS available_points, SUM(reduction_hours) AS reduction_hours, SUM(wpc.contracted_hours) AS contracted_hours, wr.effective_from AS effective_from, wr.id AS workload_report_id, ldu.id AS id, COUNT(*) AS count
    FROM app.workload_points_calculations AS wpc
    JOIN app.workload AS w
        ON wpc.workload_id = w.id
    JOIN app.workload_owner AS wo
        ON w.workload_owner_id = wo.id
    JOIN app.team AS t
        ON wo.team_id = t.id
    JOIN app.ldu AS ldu
        ON t.ldu_id = ldu.id
    JOIN app.workload_report AS wr
        ON wpc.workload_report_id = wr.id
    GROUP BY ldu.id, wr.effective_from, wr.id;



CREATE OR REPLACE VIEW app.ldu_case_overview (name, total_cases, available_points, total_points, contracted_hours, reduction_hours, cms_adjustment_points, id, link_id, count) AS
SELECT
    t.description AS name, SUM(w.total_filtered_cases + w.total_t2a_cases) AS total_cases, SUM(wpc.available_points) AS available_points, SUM(wpc.total_points) AS total_points, SUM(wpc.contracted_hours) AS contracted_hours, SUM(wpc.reduction_hours) AS reduction_hours, SUM(wpc.cms_adjustment_points) AS cms_adjustment_points, l.id AS id, t.id AS link_id, COUNT(*) AS count
    FROM app.workload_owner AS wo
    JOIN app.team AS t
        ON wo.team_id = t.id
    JOIN app.ldu AS l
        ON t.ldu_id = l.id
    JOIN app.workload AS w
        ON wo.id = w.workload_owner_id
    JOIN app.workload_points_calculations AS wpc
        ON wpc.workload_id = w.id
    JOIN app.workload_report AS wr
        ON wr.id = wpc.workload_report_id
    WHERE wr.effective_from IS NOT NULL AND wr.effective_to IS NULL
    GROUP BY t.id, t.description, l.id;

CREATE OR REPLACE VIEW app.team_case_progress_view (forename, surname, workload_owner_id, community_last_16_weeks, license_last_16_weeks, total_cases, warrants_total, overdue_terminations_total, unpaid_work_total, id, count) AS
SELECT
    om.forename, om.surname, wo.id AS workload_owner_id, w.community_last_16_weeks AS community_last_16_weeks, w.license_last_16_weeks AS license_last_16_weeks, w.total_filtered_cases AS total_cases, SUM(tr.warrants_total) AS warrants_total, SUM(tr.overdue_terminations_total) AS overdue_terminations_total, SUM(tr.unpaid_work_total) AS unpaid_work_total, t.id AS id, COUNT(*) AS count
    FROM app.workload AS w
    JOIN app.workload_points_calculations AS wpc
        ON wpc.workload_id = w.id
    JOIN app.workload_report AS wr
        ON wr.id = wpc.workload_report_id
    JOIN app.tiers AS tr
        ON tr.workload_id = w.id
    JOIN app.workload_owner AS wo
        ON wo.id = w.workload_owner_id
    JOIN app.offender_manager AS om
        ON om.id = wo.offender_manager_id
    JOIN app.team AS t
        ON wo.team_id = t.id
    WHERE wr.effective_from IS NOT NULL AND wr.effective_to IS NULL
    GROUP BY om.forename, om.surname, wo.id, t.id, w.community_last_16_weeks, w.license_last_16_weeks, w.total_filtered_cases;

CREATE OR REPLACE VIEW app.ldu_case_progress_view (name, community_last_16_weeks, license_last_16_weeks, total_cases, warrants_total, overdue_terminations_total, unpaid_work_total, id) AS
SELECT
    MAX(t.description) AS name, SUM(tv.community_last_16_weeks) AS community_last_16_weeks, SUM(tv.license_last_16_weeks) AS license_last_16_weeks, SUM(tv.total_cases) AS total_cases, SUM(tv.warrants_total) AS warrants_total, SUM(tv.overdue_terminations_total) AS overdue_terminations_total, SUM(tv.unpaid_work_total) AS unpaid_work_total, MAX(l.id) AS id
    FROM app.team_case_progress_view AS tv
    JOIN app.team AS t
        ON t.id = tv.id
    JOIN app.ldu AS l
        ON t.ldu_id = l.id
    GROUP BY t.id;
/* app.ldu_caseload_view source */;



CREATE OR REPLACE VIEW app.ldu_caseload_view (id, link_id, name, grade_code, location, region_name, untiered, a3, a2, a1, a0, b3, b2, b1, b0, c3, c2, c1, c0, d3, d2, d1, d0, total_cases, count) AS
SELECT
    t.ldu_id AS id, t.id AS link_id, t.description AS name, omt.grade_code, tr.location, r.description AS region_name, SUM((CASE
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
    GROUP BY t.ldu_id, t.id, t.description, r.description, omt.grade_code, tr.location;



CREATE OR REPLACE VIEW app.ldu_court_reporter_overview (id, name, link_id, contracted_hours, reduction_hours, total_sdrs, total_fdrs, total_oral_reports, count) AS
SELECT
    l.id, t.description AS name, t.id AS link_id, SUM(crc.contracted_hours) AS contracted_hours, SUM(crc.reduction_hours) AS reduction_hours, SUM(cr.total_sdrs) AS total_sdrs, SUM(cr.total_fdrs) AS total_fdrs, SUM(cr.total_oral_reports) AS total_oral_reports, COUNT(*) AS count
    FROM app.workload_owner AS wo
    JOIN app.team AS t
        ON wo.team_id = t.id
    JOIN app.offender_manager AS om
        ON om.id = wo.offender_manager_id
    JOIN app.offender_manager_type AS om_type
        ON om_type.id = om.type_id
    JOIN app.court_reports AS cr
        ON wo.id = cr.workload_owner_id
    JOIN app.court_reports_calculations AS crc
        ON crc.court_reports_id = cr.id
    JOIN app.workload_report AS wr
        ON wr.id = crc.workload_report_id
    JOIN app.ldu AS l
        ON l.id = t.ldu_id
    WHERE wr.effective_from IS NOT NULL AND wr.effective_to IS NULL
    GROUP BY l.id, t.id, t.description;



CREATE OR REPLACE VIEW app.ldu_outstanding_reports_view (id, link_id, name, grade_code, ow, ot, upw, t2a_ow, t2a_ot, t2a_upw, sso, sl, count) AS
SELECT
    t.ldu_id AS id, t.id AS link_id, t.description AS name, omt.grade_code, SUM(tr.warrants_total) AS ow, SUM(tr.overdue_terminations_total) AS ot, SUM(tr.unpaid_work_total) AS upw, SUM(tr.t2a_warrants_total) AS t2a_ow, SUM(tr.t2a_overdue_terminations_total) AS t2a_ot, SUM(tr.t2a_unpaid_work_total) AS t2a_upw, SUM(tr.suspended_total) AS sso, SUM(tr.suspended_lifer_total) AS sl, COUNT(*) AS count
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
    JOIN app.offender_manager AS om
        ON om.id = wo.offender_manager_id
    JOIN app.offender_manager_type AS omt
        ON omt.id = om.type_id
    WHERE wr.effective_from IS NOT NULL AND wr.effective_to IS NULL
    GROUP BY t.ldu_id, t.id, t.description, omt.grade_code;

CREATE OR REPLACE VIEW app.reductions_notes_export_view (workload_owner_id, team_id, ldu_id, region_id, region_name, ldu_name, team_name, name, contracted_hours, reduction_reason, amount, start_date, end_date, reduction_status, additional_notes, grade_code) AS
SELECT
    wo.id AS workload_owner_id, team.id AS team_id, ldu.id AS ldu_id, region.id AS region_id, region.description AS region_name, ldu.description AS ldu_name, team.description AS team_name, CONCAT(om.forename, ' ', om.surname) AS name, wo.contracted_hours AS contracted_hours, rr.reason_short_name AS reduction_reason, r.hours AS amount, r.effective_from AS start_date, r.effective_to AS end_date, r.status AS reduction_status, r.notes AS additional_notes, omt.grade_code AS grade_code
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

CREATE OR REPLACE VIEW app.ldu_reductions_statistics (reduction_reason, ldu_id, count) AS
SELECT
    reduction_reason, ldu_id, COUNT(reduction_reason) AS count
    FROM app.reductions_notes_export_view
    GROUP BY reduction_reason, ldu_id;
/* app.national_capacity_breakdown_view source */;



CREATE OR REPLACE VIEW app.national_capacity_breakdown_view (link_id, name, grade_code, total_cases, total_t2a_cases, monthly_sdrs, sdr_conversions_last_30_days, paroms_completed_last_30_days, total_points, available_points, reduction_hours, cms_adjustment_points, gs_adjustment_points, contracted_hours, arms_total_cases, count) AS
SELECT
    r.id AS link_id, r.description AS name, omt.grade_code, SUM(w.total_filtered_cases) AS total_cases, SUM(w.total_t2a_cases) AS total_t2a_cases, SUM(w.monthly_sdrs) AS monthly_sdrs, SUM(w.sdr_conversions_last_30_days) AS sdr_conversions_last_30_days, SUM(w.paroms_completed_last_30_days) AS paroms_completed_last_30_days, SUM(wpc.total_points) AS total_points, SUM(wpc.available_points) AS available_points, SUM(wpc.reduction_hours) AS reduction_hours, SUM(wpc.cms_adjustment_points) AS cms_adjustment_points, SUM(wpc.gs_adjustment_points) AS gs_adjustment_points, SUM(wpc.contracted_hours) AS contracted_hours, SUM(wpc.arms_total_cases) AS arms_total_cases, COUNT(*) AS count
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



CREATE OR REPLACE VIEW app.national_capacity_view (total_points, available_points, reduction_hours, contracted_hours, effective_from, workload_report_id, count) AS
SELECT
    SUM(total_points) AS total_points, SUM(available_points) AS available_points, SUM(reduction_hours) AS reduction_hours, SUM(wpc.contracted_hours) AS contracted_hours, wr.effective_from AS effective_from, wr.id AS workload_report_id, COUNT(*) AS count
    FROM app.workload_points_calculations AS wpc
    JOIN app.workload AS w
        ON wpc.workload_id = w.id
    JOIN app.workload_report AS wr
        ON wpc.workload_report_id = wr.id
    GROUP BY wr.effective_from, wr.id;



CREATE OR REPLACE VIEW app.national_case_overview (name, total_cases, available_points, total_points, contracted_hours, reduction_hours, cms_adjustment_points, link_id, count) AS
SELECT
    r.description AS name, SUM(w.total_filtered_cases + w.total_t2a_cases) AS total_cases, SUM(wpc.available_points) AS available_points, SUM(wpc.total_points) AS total_points, SUM(wpc.contracted_hours) AS contracted_hours, SUM(wpc.reduction_hours) AS reduction_hours, SUM(wpc.cms_adjustment_points) AS cms_adjustment_points, r.id AS link_id, COUNT(*) AS count
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
    WHERE wr.effective_from IS NOT NULL AND wr.effective_to IS NULL
    GROUP BY r.id, r.description;



CREATE OR REPLACE VIEW app.national_case_progress_view (name, community_last_16_weeks, license_last_16_weeks, total_cases, warrants_total, overdue_terminations_total, unpaid_work_total) AS
SELECT
    MAX(r.description) AS name, SUM(tv.community_last_16_weeks) AS community_last_16_weeks, SUM(tv.license_last_16_weeks) AS license_last_16_weeks, SUM(tv.total_cases) AS total_cases, SUM(tv.warrants_total) AS warrants_total, SUM(tv.overdue_terminations_total) AS overdue_terminations_total, SUM(tv.unpaid_work_total) AS unpaid_work_total
    FROM app.team_case_progress_view AS tv
    JOIN app.team AS t
        ON t.id = tv.id
    JOIN app.ldu AS l
        ON l.id = t.ldu_id
    JOIN app.region AS r
        ON r.id = l.region_id
    GROUP BY r.id;
/* app.national_caseload_view source */;



CREATE OR REPLACE VIEW app.national_caseload_view (link_id, name, region_name, grade_code, location, untiered, a3, a2, a1, a0, b3, b2, b1, b0, c3, c2, c1, c0, d3, d2, d1, d0, total_cases, count) AS
SELECT
    r.id AS link_id, r.description AS name, r.description AS region_name, omt.grade_code, tr.location, SUM((CASE
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



CREATE OR REPLACE VIEW app.national_court_reporter_overview (name, link_id, contracted_hours, reduction_hours, total_sdrs, total_fdrs, total_oral_reports, count) AS
SELECT
    r.description AS name, r.id AS link_id, SUM(crc.contracted_hours) AS contracted_hours, SUM(crc.reduction_hours) AS reduction_hours, SUM(cr.total_sdrs) AS total_sdrs, SUM(cr.total_fdrs) AS total_fdrs, SUM(cr.total_oral_reports) AS total_oral_reports, COUNT(*) AS count
    FROM app.workload_owner AS wo
    JOIN app.team AS t
        ON wo.team_id = t.id
    JOIN app.offender_manager AS om
        ON om.id = wo.offender_manager_id
    JOIN app.offender_manager_type AS om_type
        ON om_type.id = om.type_id
    JOIN app.court_reports AS cr
        ON wo.id = cr.workload_owner_id
    JOIN app.court_reports_calculations AS crc
        ON crc.court_reports_id = cr.id
    JOIN app.workload_report AS wr
        ON wr.id = crc.workload_report_id
    JOIN app.ldu AS l
        ON l.id = t.ldu_id
    JOIN app.region AS r
        ON r.id = l.region_id
    WHERE wr.effective_from IS NOT NULL AND wr.effective_to IS NULL
    GROUP BY r.id, r.description;



CREATE OR REPLACE VIEW app.national_outstanding_reports_view (link_id, name, grade_code, ow, ot, upw, t2a_ow, t2a_ot, t2a_upw, sso, sl, count) AS
SELECT
    r.id AS link_id, r.description AS name, omt.grade_code, SUM(tr.warrants_total) AS ow, SUM(tr.overdue_terminations_total) AS ot, SUM(tr.unpaid_work_total) AS upw, SUM(tr.t2a_warrants_total) AS t2a_ow, SUM(tr.t2a_overdue_terminations_total) AS t2a_ot, SUM(tr.t2a_unpaid_work_total) AS t2a_upw, SUM(tr.suspended_total) AS sso, SUM(tr.suspended_lifer_total) AS sl, COUNT(*) AS count
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
    GROUP BY r.id, r.description, omt.grade_code;



CREATE OR REPLACE VIEW app.offender_manager_search_view (workload_owner_id, forename, surname, team, teamid, ldu, lduid, region, regionid) AS
SELECT
    wo.id AS workload_owner_id, om.forename, om.surname, t.description AS team, t.id AS teamid, l.description AS ldu, l.id AS lduid, r.description AS region, r.id AS regionid
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
/* app.om_cms_export_view source */
/* om_cms_export_view */;



CREATE OR REPLACE VIEW app.om_cms_export_view (omregionname, omregionid, omlduname, omlduid, omteamname, omteamid, omcontactdate, omworkloadownerid, omname, omgradecode, contactid, contactdescription, contactcode, ompoints, caserefno) AS
SELECT
    r.description AS omregionname, r.id AS omregionid, l.description AS omlduname, l.id AS omlduid, t.description AS omteamname, t.id AS omteamid, a.effective_from AS omcontactdate, wo.id AS omworkloadownerid, CONCAT(om.forename, ' ', om.surname) AS omname, omt.grade_code AS omgradecode, a.contact_id AS contactid, ar.contact_description AS contactdescription, ar.contact_code AS contactcode, a.points AS ompoints, a.case_ref_no AS caserefno
    FROM app.adjustments AS a
    JOIN app.adjustment_reason AS ar
        ON a.adjustment_reason_id = ar.id
    JOIN app.adjustment_category AS ac
        ON ar.category_id = ac.id
    JOIN app.workload_owner AS wo
        ON a.workload_owner_id = wo.id
    JOIN app.workload AS w
        ON w.workload_owner_id = wo.id
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
    JOIN app.workload_report AS wr
        ON w.workload_report_id = wr.id
    WHERE wr.effective_from IS NOT NULL AND wr.effective_to IS NULL AND LOWER(ac.category) = LOWER('Case Management Support') AND LOWER(a.status) = LOWER('ACTIVE') AND a.points < 0;
/* app.omic_ldu_case_overview source */;



CREATE OR REPLACE VIEW app.omic_ldu_case_overview (name, total_cases, total_licence_points, total_custody_points, id, link_id, count) AS
SELECT
    t.description AS name, SUM(w.total_custody_cases) AS total_cases, SUM(wpc.licence_points) AS total_licence_points, SUM(wpc.custody_points) AS total_custody_points, l.id AS id, t.id AS link_id, COUNT(*) AS count
    FROM app.workload_owner AS wo
    JOIN app.team AS t
        ON wo.team_id = t.id
    JOIN app.ldu AS l
        ON t.ldu_id = l.id
    JOIN app.omic_workload AS w
        ON wo.id = w.workload_owner_id
    JOIN app.omic_workload_points_calculations AS wpc
        ON wpc.omic_workload_id = w.id
    JOIN app.workload_report AS wr
        ON wr.id = wpc.workload_report_id
    WHERE wr.effective_from IS NOT NULL AND wr.effective_to IS NULL
    GROUP BY t.id, t.description, l.id;



CREATE OR REPLACE VIEW app.omic_national_case_overview (name, total_cases, total_licence_points, total_custody_points, link_id, count) AS
SELECT
    r.description AS name, SUM(w.total_custody_cases) AS total_cases, SUM(wpc.licence_points) AS total_licence_points, SUM(wpc.custody_points) AS total_custody_points, r.id AS link_id, COUNT(*) AS count
    FROM app.workload_owner AS wo
    JOIN app.team AS t
        ON wo.team_id = t.id
    JOIN app.ldu AS l
        ON t.ldu_id = l.id
    JOIN app.region AS r
        ON r.id = l.region_id
    JOIN app.omic_workload AS w
        ON wo.id = w.workload_owner_id
    JOIN app.omic_workload_points_calculations AS wpc
        ON wpc.omic_workload_id = w.id
    JOIN app.workload_report AS wr
        ON wr.id = wpc.workload_report_id
    WHERE wr.effective_from IS NOT NULL AND wr.effective_to IS NULL
    GROUP BY r.id, r.description;



CREATE OR REPLACE VIEW app.omic_region_case_overview (name, total_cases, total_licence_points, total_custody_points, id, link_id, count) AS
SELECT
    l.description AS name, SUM(w.total_custody_cases) AS total_cases, SUM(wpc.licence_points) AS total_licence_points, SUM(wpc.custody_points) AS total_custody_points, r.id AS id, l.id AS link_id, COUNT(*) AS count
    FROM app.workload_owner AS wo
    JOIN app.team AS t
        ON wo.team_id = t.id
    JOIN app.ldu AS l
        ON t.ldu_id = l.id
    JOIN app.region AS r
        ON r.id = l.region_id
    JOIN app.omic_workload AS w
        ON wo.id = w.workload_owner_id
    JOIN app.omic_workload_points_calculations AS wpc
        ON wpc.omic_workload_id = w.id
    JOIN app.workload_report AS wr
        ON wr.id = wpc.workload_report_id
    WHERE wr.effective_from IS NOT NULL AND wr.effective_to IS NULL
    GROUP BY l.id, l.description, r.id;



CREATE OR REPLACE VIEW app.omic_scenario_view (region_id, region_name, ldu_id, ldu_name, team_id, team_name, om_name, workload_owner_id, grade_code, location, total_cases, tier_number, t2a_total_cases, warrants_total, t2a_warrants_total, overdue_terminations_total, t2a_overdue_terminations_total, unpaid_work_total, t2a_unpaid_work_total, workload_id, arms_community_cases, arms_license_cases, nominal_target, contracted_hours, default_contracted_hours_po, default_contracted_hours_pso, default_contracted_hours_spo, sdr_total, sdr_conversions_total, paroms_total) AS
SELECT
    r.id AS region_id, r.description AS region_name, l.id AS ldu_id, l.description AS ldu_name, t.id AS team_id, t.description AS team_name, CONCAT(om.forename, ' ', om.surname) AS om_name, wo.id AS workload_owner_id, omt.grade_code, tr.location, tr.total_filtered_cases AS total_cases, tr.tier_number, tr.t2a_total_cases, tr.warrants_total, tr.t2a_warrants_total, tr.overdue_terminations_total, tr.t2a_overdue_terminations_total, tr.unpaid_work_total, tr.t2a_unpaid_work_total, w.id AS workload_id, w.arms_community_cases AS arms_community_cases, w.arms_license_cases AS arms_license_cases, wpc.nominal_target AS nominal_target, wpc.contracted_hours AS contracted_hours, wp.default_contracted_hours_po AS default_contracted_hours_po, wp.default_contracted_hours_pso AS default_contracted_hours_pso, wp.default_contracted_hours_spo AS default_contracted_hours_spo, w.monthly_sdrs AS sdr_total, w.sdr_conversions_last_30_days AS sdr_conversions_total, w.paroms_completed_last_30_days AS paroms_total
    FROM app.omic_tiers AS tr
    JOIN app.omic_workload AS w
        ON tr.omic_workload_id = w.id
    JOIN app.omic_workload_points_calculations AS wpc
        ON wpc.omic_workload_id = w.id
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
    JOIN app.workload_points AS wp
        ON wpc.workload_points_id = wp.id
    WHERE wr.effective_from IS NOT NULL AND wr.effective_to IS NULL;
/* app.reductions_archive_view source */;



CREATE OR REPLACE VIEW app.reductions_archive_view (om_name, hours_reduced, reduction_id, comments, last_updated_date, reduction_added_by, reduction_reason, start_date, end_date, reduction_status) AS
SELECT
    CONCAT(om.forename, ' ', om.surname) AS om_name, r.hours AS hours_reduced, r.id AS reduction_id, r.notes AS comments, r.updated_date AS last_updated_date, u.name AS reduction_added_by, rr.reason_short_name AS reduction_reason, r.effective_from AS start_date, r.effective_to AS end_date, r.status AS reduction_status
    FROM app.workload_owner AS wo
    JOIN app.offender_manager AS om
        ON om.id = wo.offender_manager_id
    JOIN app.reductions AS r
        ON r.workload_owner_id = wo.id
    JOIN app.reduction_reason AS rr
        ON r.reduction_reason_id = rr.id
    JOIN app.users AS u
        ON r.user_id = u.id;
/* app.reductions_notes_dashboard source */;



CREATE OR REPLACE VIEW app.reductions_notes_dashboard (workload_owner_id, team_id, ldu_id, region_id, region_name, ldu_name, team_name, name, contracted_hours, reduction_reason, amount, start_date, end_date, reduction_status, additional_notes, grade_code) AS
SELECT
    wo.id AS workload_owner_id, team.id AS team_id, ldu.id AS ldu_id, region.id AS region_id, region.description AS region_name, ldu.description AS ldu_name, team.description AS team_name, CONCAT(om.forename, ' ', om.surname) AS name, wo.contracted_hours AS contracted_hours, rr.reason_short_name AS reduction_reason, r.hours AS amount, r.effective_from AS start_date, r.effective_to AS end_date, r.status AS reduction_status, regexp_replace(r.notes, '[\n\r]+', ' ', 'g') AS additional_notes, omt.grade_code AS grade_code
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
/* app.region_capacity_breakdown_view source */;




/* app.ldu_reductions_statistics source */;



CREATE OR REPLACE VIEW app.region_capacity_breakdown_view (id, link_id, name, grade_code, total_cases, total_t2a_cases, monthly_sdrs, sdr_conversions_last_30_days, paroms_completed_last_30_days, total_points, available_points, reduction_hours, cms_adjustment_points, gs_adjustment_points, contracted_hours, arms_total_cases, count) AS
SELECT
    r.id AS id, l.id AS link_id, l.description AS name, omt.grade_code, SUM(w.total_filtered_cases) AS total_cases, SUM(w.total_t2a_cases) AS total_t2a_cases, SUM(w.monthly_sdrs) AS monthly_sdrs, SUM(w.sdr_conversions_last_30_days) AS sdr_conversions_last_30_days, SUM(w.paroms_completed_last_30_days) AS paroms_completed_last_30_days, SUM(wpc.total_points) AS total_points, SUM(wpc.available_points) AS available_points, SUM(wpc.reduction_hours) AS reduction_hours, SUM(wpc.cms_adjustment_points) AS cms_adjustment_points, SUM(wpc.gs_adjustment_points) AS gs_adjustment_points, SUM(wpc.contracted_hours) AS contracted_hours, SUM(wpc.arms_total_cases) AS arms_total_cases, COUNT(*) AS count
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



CREATE OR REPLACE VIEW app.region_capacity_view (total_points, available_points, reduction_hours, contracted_hours, effective_from, id, count, workload_report_id) AS
SELECT
    SUM(total_points) AS total_points, SUM(available_points) AS available_points, SUM(reduction_hours) AS reduction_hours, SUM(wpc.contracted_hours) AS contracted_hours, wr.effective_from AS effective_from, region.id AS id, COUNT(*) AS count, wr.id AS workload_report_id
    FROM app.workload_points_calculations AS wpc
    JOIN app.workload AS w
        ON wpc.workload_id = w.id
    JOIN app.workload_owner AS wo
        ON w.workload_owner_id = wo.id
    JOIN app.team AS t
        ON wo.team_id = t.id
    JOIN app.ldu AS ldu
        ON t.ldu_id = ldu.id
    JOIN app.region AS region
        ON ldu.region_id = region.id
    JOIN app.workload_report AS wr
        ON wpc.workload_report_id = wr.id
    GROUP BY region.id, wr.effective_from, wr.id;



CREATE OR REPLACE VIEW app.region_case_overview (name, total_cases, available_points, total_points, contracted_hours, reduction_hours, cms_adjustment_points, id, link_id, count) AS
SELECT
    l.description AS name, SUM(w.total_filtered_cases + w.total_t2a_cases) AS total_cases, SUM(wpc.available_points) AS available_points, SUM(wpc.total_points) AS total_points, SUM(wpc.contracted_hours) AS contracted_hours, SUM(wpc.reduction_hours) AS reduction_hours, SUM(wpc.cms_adjustment_points) AS cms_adjustment_points, r.id AS id, l.id AS link_id, COUNT(*) AS count
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
    WHERE wr.effective_from IS NOT NULL AND wr.effective_to IS NULL
    GROUP BY l.id, l.description, r.id;



CREATE OR REPLACE VIEW app.region_case_progress_view (name, community_last_16_weeks, license_last_16_weeks, total_cases, warrants_total, overdue_terminations_total, unpaid_work_total, id) AS
SELECT
    MAX(l.description) AS name, SUM(tv.community_last_16_weeks) AS community_last_16_weeks, SUM(tv.license_last_16_weeks) AS license_last_16_weeks, SUM(tv.total_cases) AS total_cases, SUM(tv.warrants_total) AS warrants_total, SUM(tv.overdue_terminations_total) AS overdue_terminations_total, SUM(tv.unpaid_work_total) AS unpaid_work_total, MAX(r.id) AS id
    FROM app.team_case_progress_view AS tv
    JOIN app.team AS t
        ON t.id = tv.id
    JOIN app.ldu AS l
        ON l.id = t.ldu_id
    JOIN app.region AS r
        ON r.id = l.region_id
    GROUP BY l.id;
/* app.region_caseload_view source */;



CREATE OR REPLACE VIEW app.region_caseload_view (id, link_id, name, region_name, grade_code, location, untiered, a3, a2, a1, a0, b3, b2, b1, b0, c3, c2, c1, c0, d3, d2, d1, d0, total_cases, count) AS
SELECT
    l.region_id AS id, l.id AS link_id, l.description AS name, r.description AS region_name, omt.grade_code, tr.location, SUM((CASE
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
    GROUP BY l.region_id, l.id, r.description, l.description, omt.grade_code, tr.location;



CREATE OR REPLACE VIEW app.region_court_reporter_overview (id, name, link_id, contracted_hours, reduction_hours, total_sdrs, total_fdrs, total_oral_reports, count) AS
SELECT
    r.id, l.description AS name, l.id AS link_id, SUM(crc.contracted_hours) AS contracted_hours, SUM(crc.reduction_hours) AS reduction_hours, SUM(cr.total_sdrs) AS total_sdrs, SUM(cr.total_fdrs) AS total_fdrs, SUM(cr.total_oral_reports) AS total_oral_reports, COUNT(*) AS count
    FROM app.workload_owner AS wo
    JOIN app.team AS t
        ON wo.team_id = t.id
    JOIN app.offender_manager AS om
        ON om.id = wo.offender_manager_id
    JOIN app.offender_manager_type AS om_type
        ON om_type.id = om.type_id
    JOIN app.court_reports AS cr
        ON wo.id = cr.workload_owner_id
    JOIN app.court_reports_calculations AS crc
        ON crc.court_reports_id = cr.id
    JOIN app.workload_report AS wr
        ON wr.id = crc.workload_report_id
    JOIN app.ldu AS l
        ON l.id = t.ldu_id
    JOIN app.region AS r
        ON r.id = l.region_id
    WHERE wr.effective_from IS NOT NULL AND wr.effective_to IS NULL
    GROUP BY r.id, l.id, l.description;



CREATE OR REPLACE VIEW app.region_outstanding_reports_view (id, link_id, name, grade_code, ow, ot, upw, t2a_ow, t2a_ot, t2a_upw, sso, sl, count) AS
SELECT
    l.region_id AS id, l.id AS link_id, l.description AS name, omt.grade_code, SUM(tr.warrants_total) AS ow, SUM(tr.overdue_terminations_total) AS ot, SUM(tr.unpaid_work_total) AS upw, SUM(tr.t2a_warrants_total) AS t2a_ow, SUM(tr.t2a_overdue_terminations_total) AS t2a_ot, SUM(tr.t2a_unpaid_work_total) AS t2a_upw, SUM(tr.suspended_total) AS sso, SUM(tr.suspended_lifer_total) AS sl, COUNT(*) AS count
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
    JOIN app.offender_manager AS om
        ON om.id = wo.offender_manager_id
    JOIN app.offender_manager_type AS omt
        ON omt.id = om.type_id
    WHERE wr.effective_from IS NOT NULL AND wr.effective_to IS NULL
    GROUP BY l.region_id, l.id, l.description, omt.grade_code;



CREATE OR REPLACE VIEW app.scenario_view (region_id, region_name, ldu_id, ldu_name, team_id, team_name, om_name, workload_owner_id, grade_code, location, total_cases, tier_number, t2a_total_cases, warrants_total, t2a_warrants_total, overdue_terminations_total, t2a_overdue_terminations_total, unpaid_work_total, t2a_unpaid_work_total, workload_id, arms_community_cases, arms_license_cases, nominal_target, contracted_hours, reduction_hours, default_contracted_hours_po, default_contracted_hours_pso, default_contracted_hours_spo, cms_points, gs_points, sdr_total, sdr_conversions_total, paroms_total) AS
SELECT
    r.id AS region_id, r.description AS region_name, l.id AS ldu_id, l.description AS ldu_name, t.id AS team_id, t.description AS team_name, CONCAT(om.forename, ' ', om.surname) AS om_name, wo.id AS workload_owner_id, omt.grade_code, tr.location, tr.total_filtered_cases AS total_cases, tr.tier_number, tr.t2a_total_cases, tr.warrants_total, tr.t2a_warrants_total, tr.overdue_terminations_total, tr.t2a_overdue_terminations_total, tr.unpaid_work_total, tr.t2a_unpaid_work_total, w.id AS workload_id, w.arms_community_cases AS arms_community_cases, w.arms_license_cases AS arms_license_cases, wpc.nominal_target AS nominal_target, wpc.contracted_hours AS contracted_hours, wpc.reduction_hours AS reduction_hours, wp.default_contracted_hours_po AS default_contracted_hours_po, wp.default_contracted_hours_pso AS default_contracted_hours_pso, wp.default_contracted_hours_spo AS default_contracted_hours_spo, wpc.cms_adjustment_points AS cms_points, wpc.gs_adjustment_points AS gs_points, w.monthly_sdrs AS sdr_total, w.sdr_conversions_last_30_days AS sdr_conversions_total, w.paroms_completed_last_30_days AS paroms_total
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
    JOIN app.workload_points AS wp
        ON wpc.workload_points_id = wp.id
    WHERE wr.effective_from IS NOT NULL AND wr.effective_to IS NULL;
/* app.suspended_lifers_export_view source */
/* suspended_lifers_export_view */;



CREATE OR REPLACE VIEW app.suspended_lifers_export_view (regionname, regionid, lduname, lduid, teamname, teamid, workloadid, workloadownerid, tiercode, rowtype, casereferenceno, casetype, offendermanagername, gradecode, incustody, registerlevel, registercategory, registercategorydescription, registrationdate) AS
SELECT
    r.description AS regionname, r.id AS regionid, l.description AS lduname, l.id AS lduid, t.description AS teamname, t.id AS teamid, w.id AS workloadid, w.workload_owner_id AS workloadownerid, cc.category_name AS tiercode, rtd.row_type_full_name AS rowtype, c.case_ref_no AS casereferenceno, c.location AS casetype, CONCAT(om.forename, ' ', om.surname) AS offendermanagername, omt.grade_code AS gradecode, in_custody AS incustody, register_level AS registerlevel, register_category AS registercategory, register_category_description AS registercategorydescription, registration_date AS registrationdate
    FROM app.case_details AS c
    JOIN app.row_type_definitions AS rtd
        ON LOWER(c.row_type) = LOWER(rtd.row_type)
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
    LEFT OUTER JOIN staging.suspended_lifers AS sl
        ON LOWER(c.case_ref_no) = LOWER(sl.case_ref_no)
    WHERE wr.effective_from IS NOT NULL AND wr.effective_to IS NULL AND LOWER(c.row_type) = LOWER('L');
/* app.t2a_detail_export_view source */;



CREATE OR REPLACE VIEW app.t2a_detail_export_view (regionname, regionid, lduname, lduid, teamname, teamid, crn, workload_owner_id, omname, omcode, event_no, allocation_date, nsi_outcome_cd, nsi_outcome_desc) AS
SELECT
    r.description AS regionname, r.id AS regionid, l.description AS lduname, l.id AS lduid, t.description AS teamname, t.id AS teamid, t2a.crn AS crn, wo.id AS workload_owner_id, CONCAT(om.forename, ' ', om.surname) AS omname, omt.grade_code AS omcode, t2a.event_no, t2a.allocation_date, t2a.nsi_outcome_cd, t2a.nsi_outcome_desc
    FROM staging.t2a_detail AS t2a
    JOIN app.team AS t
        ON LOWER(t2a.team_cd_order_manager) = LOWER(t.code)
    JOIN app.ldu AS l
        ON t.ldu_id = l.id
    JOIN app.region AS r
        ON r.id = l.region_id
    JOIN app.offender_manager AS om
        ON LOWER(t2a.staff_cd_order_manager) = LOWER(om.key)
    JOIN app.offender_manager_type AS omt
        ON om.type_id = omt.id
    JOIN app.workload_owner AS wo
        ON t.id = wo.team_id AND om.id = wo.offender_manager_id
    JOIN app.workload AS w
        ON wo.id = w.workload_owner_id
    JOIN app.workload_report AS wr
        ON wr.id = w.workload_report_id
    WHERE wr.effective_from IS NOT NULL AND wr.effective_to IS NULL;
/* app.team_archive_data source */;



CREATE OR REPLACE VIEW app.team_archive_data (workload_date, workload_id, ldu_id, region_name, ldu_name, team_name, team_id, link_id, om_name, grade_code, total_cases, total_filtered_cases, total_t2a_cases, monthly_sdrs, sdr_conversions_last_30_days, paroms_completed_last_30_days, total_points, available_points, hours_reduction, cms_adjustment_points, gs_adjustment_points, contracted_hours, arms_total_cases, paroms_points, sdr_points, sdr_conversion_points, nominal_target) AS
SELECT
    wr.effective_from AS workload_date, w.id AS workload_id, l.id AS ldu_id, r.description AS region_name, l.description AS ldu_name, t.description AS team_name, t.id AS team_id, wo.id AS link_id, CONCAT(om.forename, ' ', om.surname) AS om_name, omt.grade_code, w.total_cases AS total_cases, w.total_filtered_cases AS total_filtered_cases, w.total_t2a_cases, w.monthly_sdrs, w.sdr_conversions_last_30_days, w.paroms_completed_last_30_days, wpc.total_points AS total_points, wpc.available_points AS available_points, wpc.reduction_hours AS hours_reduction, wpc.cms_adjustment_points AS cms_adjustment_points, wpc.gs_adjustment_points AS gs_adjustment_points, wpc.contracted_hours AS contracted_hours, wpc.arms_total_cases AS arms_total_cases, wpc.paroms_points AS paroms_points, wpc.sdr_points AS sdr_points, wpc.sdr_conversion_points AS sdr_conversion_points, wpc.nominal_target AS nominal_target
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
/* app.team_capacity_breakdown_view source */;



CREATE OR REPLACE VIEW app.team_capacity_breakdown_view (id, link_id, forename, surname, grade_code, total_cases, total_t2a_cases, monthly_sdrs, sdr_conversions_last_30_days, paroms_completed_last_30_days, total_points, available_points, reduction_hours, cms_adjustment_points, gs_adjustment_points, contracted_hours, arms_total_cases) AS
SELECT
    t.id AS id, wo.id AS link_id, om.forename, om.surname, omt.grade_code, w.total_filtered_cases AS total_cases, w.total_t2a_cases, w.monthly_sdrs, w.sdr_conversions_last_30_days, w.paroms_completed_last_30_days, wpc.total_points, wpc.available_points, wpc.reduction_hours, wpc.cms_adjustment_points, wpc.gs_adjustment_points, wpc.contracted_hours, wpc.arms_total_cases
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



CREATE OR REPLACE VIEW app.team_capacity_view (total_points, available_points, reduction_hours, contracted_hours, effective_from, workload_report_id, id, count) AS
SELECT
    SUM(total_points) AS total_points, SUM(available_points) AS available_points, SUM(reduction_hours) AS reduction_hours, SUM(wpc.contracted_hours) AS contracted_hours, wr.effective_from AS effective_from, wr.id AS workload_report_id, t.id AS id, COUNT(*) AS count
    FROM app.workload_points_calculations AS wpc
    JOIN app.workload AS w
        ON wpc.workload_id = w.id
    JOIN app.workload_owner AS wo
        ON w.workload_owner_id = wo.id
    JOIN app.workload_report AS wr
        ON wpc.workload_report_id = wr.id
    JOIN app.team AS t
        ON wo.team_id = t.id
    GROUP BY t.id, wr.effective_from, wr.id;



CREATE OR REPLACE VIEW app.team_case_details_view (id, link_id, ldu_description, team_description, forename, surname, grade_code, flag, case_ref_no, location, tier_code, count) AS
SELECT
    wo.team_id AS id, wo.id AS link_id, l.description AS ldu_description, t.description AS team_description, om.forename, om.surname, omt.grade_code, cd.row_type AS flag, cd.case_ref_no, cd.location, cd.tier_code, COUNT(*) AS count
    FROM app.case_details AS cd
    JOIN app.workload AS w
        ON w.id = cd.workload_id
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
    JOIN app.offender_manager AS om
        ON om.id = wo.offender_manager_id
    JOIN app.offender_manager_type AS omt
        ON omt.id = om.type_id
    WHERE wr.effective_from IS NOT NULL AND wr.effective_to IS NULL
    GROUP BY wo.team_id, wo.id, om.forename, om.surname, omt.grade_code, cd.row_type, cd.case_ref_no, cd.location, cd.tier_code, l.description, t.description;



CREATE OR REPLACE VIEW app.team_case_overview (name, grade_code, total_cases, available_points, total_points, contracted_hours, reduction_hours, cms_adjustment_points, id, link_id) AS
SELECT
    CONCAT(om.forename, ' ', om.surname) AS name, om_type.grade_code AS grade_code, (w.total_filtered_cases + w.total_t2a_cases) AS total_cases, wpc.available_points AS available_points, wpc.total_points AS total_points, wpc.contracted_hours AS contracted_hours, wpc.reduction_hours AS reduction_hours, wpc.cms_adjustment_points AS cms_adjustment_points, t.id AS id, wo.id AS link_id
    FROM app.workload_owner AS wo
    JOIN app.team AS t
        ON wo.team_id = t.id
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







CREATE OR REPLACE VIEW app.team_caseload_view (id, link_id, forename, surname, grade_code, location, untiered, a3, a2, a1, a0, b3, b2, b1, b0, c3, c2, c1, c0, d3, d2, d1, d0, total_cases, count) AS
SELECT
    wo.team_id AS id, wo.id AS link_id, om.forename, om.surname, omt.grade_code, tr.location, SUM((CASE
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
    JOIN app.offender_manager AS om
        ON om.id = wo.offender_manager_id
    JOIN app.offender_manager_type AS omt
        ON omt.id = om.type_id
    WHERE wr.effective_from IS NOT NULL AND wr.effective_to IS NULL
    GROUP BY wo.team_id, wo.id, om.forename, om.surname, omt.grade_code, tr.location;



CREATE OR REPLACE VIEW app.team_court_reporter_overview (forename, surname, grade_code, contracted_hours, reduction_hours, id, link_id, total_sdrs, total_fdrs, total_oral_reports) AS
SELECT
    om.forename, om.surname, om_type.grade_code, crc.contracted_hours, crc.reduction_hours, t.id, wo.id AS link_id, cr.total_sdrs, cr.total_fdrs, cr.total_oral_reports
    FROM app.workload_owner AS wo
    JOIN app.team AS t
        ON wo.team_id = t.id
    JOIN app.offender_manager AS om
        ON om.id = wo.offender_manager_id
    JOIN app.offender_manager_type AS om_type
        ON om_type.id = om.type_id
    JOIN app.court_reports AS cr
        ON wo.id = cr.workload_owner_id
    JOIN app.court_reports_calculations AS crc
        ON crc.court_reports_id = cr.id
    JOIN app.workload_report AS wr
        ON wr.id = crc.workload_report_id
    WHERE wr.effective_from IS NOT NULL AND wr.effective_to IS NULL;



CREATE OR REPLACE VIEW app.team_outstanding_reports_view (id, link_id, forename, surname, grade_code, ow, ot, upw, t2a_ow, t2a_ot, t2a_upw, sso, sl, count) AS
SELECT
    wo.team_id AS id, wo.id AS link_id, om.forename, om.surname, omt.grade_code, SUM(tr.warrants_total) AS ow, SUM(tr.overdue_terminations_total) AS ot, SUM(tr.unpaid_work_total) AS upw, SUM(tr.t2a_warrants_total) AS t2a_ow, SUM(tr.t2a_overdue_terminations_total) AS t2a_ot, SUM(tr.t2a_unpaid_work_total) AS t2a_upw, SUM(tr.suspended_total) AS sso, SUM(tr.suspended_lifer_total) AS sl, COUNT(*) AS count
    FROM app.tiers AS tr
    JOIN app.workload AS w
        ON tr.workload_id = w.id
    JOIN app.workload_points_calculations AS wpc
        ON wpc.workload_id = w.id
    JOIN app.workload_report AS wr
        ON wr.id = wpc.workload_report_id
    JOIN app.workload_owner AS wo
        ON wo.id = w.workload_owner_id
    JOIN app.offender_manager AS om
        ON om.id = wo.offender_manager_id
    JOIN app.offender_manager_type AS omt
        ON omt.id = om.type_id
    WHERE wr.effective_from IS NOT NULL AND wr.effective_to IS NULL
    GROUP BY wo.team_id, wo.id, om.forename, om.surname, omt.grade_code;



CREATE OR REPLACE VIEW app.team_reductions_statistics (reduction_reason, team_id, count) AS
SELECT
    reduction_reason, team_id, COUNT(reduction_reason) AS count
    FROM app.reductions_notes_export_view
    GROUP BY reduction_reason, team_id;
/* app.workload_percentage_breakdown_view source */;



CREATE OR REPLACE VIEW app.workload_percentage_breakdown_view (region_name, ldu_name, team_name, region_id, ldu_id, team_id, workload_owner_id, om_name, grade_code, total_case_points, total_points_overall, non_case_points_total, arms_community_points, arms_community_cases, arms_licence_points, arms_license_cases, available_points, cms_adjustment_points, gs_adjustment_points, paroms_points, sdr_points, sdr_conversion_points, contracted_hours, reduction_hours) AS
SELECT
    r.description AS region_name, l.description AS ldu_name, t.description AS team_name, r.id AS region_id, l.id AS ldu_id, t.id AS team_id, wo.id AS workload_owner_id, CONCAT(om.forename, ' ', om.surname) AS om_name, omt.grade_code, (wpc.total_points - wpc.cms_adjustment_points - wpc.gs_adjustment_points - wpc.paroms_points - wpc.sdr_conversion_points - wpc.sdr_conversion_points - (w.arms_license_cases * wp.weighting_arms_lic) - (w.arms_community_cases * wp.weighting_arms_comm)) AS total_case_points, wpc.total_points AS total_points_overall, (wpc.cms_adjustment_points + wpc.gs_adjustment_points + wpc.paroms_points + wpc.sdr_conversion_points + wpc.sdr_conversion_points + (w.arms_license_cases * wp.weighting_arms_lic) + (w.arms_community_cases * wp.weighting_arms_comm)) AS non_case_points_total, w.arms_community_cases * wp.weighting_arms_comm AS arms_community_points, w.arms_community_cases AS arms_community_cases, w.arms_license_cases * wp.weighting_arms_lic AS arms_licence_points, w.arms_license_cases AS arms_license_cases, wpc.available_points AS available_points, wpc.cms_adjustment_points AS cms_adjustment_points, wpc.gs_adjustment_points AS gs_adjustment_points, wpc.paroms_points AS paroms_points, wpc.sdr_points AS sdr_points, wpc.sdr_conversion_points AS sdr_conversion_points, wpc.contracted_hours AS contracted_hours, wpc.reduction_hours AS reduction_hours
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
        ON om.type_id = omt.id
    JOIN app.workload_points AS wp
        ON wpc.workload_points_id = wp.id
    WHERE wr.effective_from IS NOT NULL AND wr.effective_to IS NULL;



-- ------------ Write CREATE-INDEX-stage scripts -----------

CREATE INDEX ix_ldu_idx_ldu_region_id
ON app.ldu
USING BTREE (region_id ASC) INCLUDE(description)
WITH (FILLFACTOR = 100);



CREATE UNIQUE INDEX ix_roles_roles_role_unique_1
ON app.roles
USING BTREE (role ASC);



CREATE INDEX ix_team_idx_team_ldu_id
ON app.team
USING BTREE (ldu_id ASC) INCLUDE(description)
WITH (FILLFACTOR = 100);



CREATE INDEX ix_tiers_idx_app_tiers_workload_id
ON app.tiers
USING BTREE (workload_id ASC) INCLUDE(overdue_terminations_total, unpaid_work_total, warrants_total)
WITH (FILLFACTOR = 100);



CREATE INDEX ix_tiers_ix_tiers_workload_id
ON app.tiers
USING BTREE (workload_id ASC) INCLUDE(location, overdue_terminations_total, tier_number, total_cases, unpaid_work_total, warrants_total)
WITH (FILLFACTOR = 100);



CREATE UNIQUE INDEX ix_user_role_user_role_user_id_unique_1
ON app.user_role
USING BTREE (user_id ASC);



CREATE UNIQUE INDEX ix_users_users_username_unique_1
ON app.users
USING BTREE (username ASC);



CREATE INDEX ix_workload_ix_workload_staging_id
ON app.workload
USING BTREE (staging_id ASC)
WITH (FILLFACTOR = 100);



CREATE INDEX ix_workload_ix_workload_workload_owner_id
ON app.workload
USING BTREE (workload_owner_id ASC) INCLUDE(total_cases)
WITH (FILLFACTOR = 100);



CREATE INDEX ix_workload_owner_idx_workload_owner_team_id
ON app.workload_owner
USING BTREE (team_id ASC)
WITH (FILLFACTOR = 100);



CREATE INDEX ix_workload_points_calculations_idx_app_workload_points_calculations_workload_id
ON app.workload_points_calculations
USING BTREE (workload_id ASC) INCLUDE(available_points, contracted_hours, reduction_hours, total_points)
WITH (FILLFACTOR = 100);



CREATE INDEX index_workload_points_calculations_id_available_points_reduction_hours_total_points
ON app.workload_points_calculations
USING BTREE (workload_id ASC) INCLUDE(available_points, reduction_hours, total_points)
WITH (FILLFACTOR = 100);



CREATE INDEX index_workload_points_calculations_report_id_workload_id
ON app.workload_points_calculations
USING BTREE (workload_report_id ASC) INCLUDE(workload_id)
WITH (FILLFACTOR = 100);



CREATE INDEX ix_workload_points_calculations_ix_workload_points_calculations_workload_report_id_l
ON app.workload_points_calculations
USING BTREE (workload_report_id ASC) INCLUDE(available_points, reduction_hours, total_points, workload_id, workload_points_id)
WITH (FILLFACTOR = 100);



CREATE INDEX ix_workload_report_ix_workload_report_effective_dates
ON app.workload_report
USING BTREE (id ASC) INCLUDE(effective_from, effective_to)
WITH (FILLFACTOR = 100);



-- ------------ Write CREATE-CONSTRAINT-stage scripts -----------

ALTER TABLE app.adjustment_category
ADD CONSTRAINT pk__adjustme__3213e83fab7e5d72_1200723330 PRIMARY KEY (id);



ALTER TABLE app.adjustment_reason
ADD CONSTRAINT pk__adjustme__3213e83faaad4773_1232723444 PRIMARY KEY (id);



ALTER TABLE app.adjustments
ADD CONSTRAINT pk__adjustme__3213e83fc8419911_1264723558 PRIMARY KEY (id);



ALTER TABLE app.case_details
ADD CONSTRAINT pk__case_det__3213e83f52a26133_1597248745 PRIMARY KEY (id);



ALTER TABLE app.court_reports
ADD CONSTRAINT pk__court_re__3213e83f7578916c_541244983 PRIMARY KEY (id);



ALTER TABLE app.court_reports_calculations
ADD CONSTRAINT pk__court_re__3213e83f461e5c19_589245154 PRIMARY KEY (id);



ALTER TABLE app.export_file
ADD CONSTRAINT pk__export_f__3213e83fb71d945d_1312723729 PRIMARY KEY (id);



ALTER TABLE app.knex_migrations
ADD CONSTRAINT pk__knex_mig__3213e83fae0e2878_1360723900 PRIMARY KEY (id);



ALTER TABLE app.ldu
ADD CONSTRAINT pk__ldu__3213e83f38ef5f02_77243330 PRIMARY KEY (id);



ALTER TABLE app.offender_manager
ADD CONSTRAINT pk__offender__3213e83fbffa39c9_141243558 PRIMARY KEY (id);



ALTER TABLE app.offender_manager_type
ADD CONSTRAINT pk__offender__3213e83fd50387bb_1408724071 PRIMARY KEY (id);



ALTER TABLE app.omic_case_details
ADD CONSTRAINT pk__omic_cas__3213e83f379b3b88_1645248916 PRIMARY KEY (id);



ALTER TABLE app.omic_tiers
ADD CONSTRAINT pk__omic_tie__3213e83fb9d8c3d7_1693249087 PRIMARY KEY (id);



ALTER TABLE app.omic_workload
ADD CONSTRAINT pk__omic_wor__3213e83f5e0a0c30_685245496 PRIMARY KEY (id);



ALTER TABLE app.omic_workload_points_calculations
ADD CONSTRAINT pk__omic_wor__3213e83fbeb87b92_893246237 PRIMARY KEY (id);



ALTER TABLE app.reduction_category
ADD CONSTRAINT pk__reductio__3213e83f982adeed_1440724185 PRIMARY KEY (id);



ALTER TABLE app.reduction_reason
ADD CONSTRAINT pk__reductio__3213e83f206bdfdd_205243786 PRIMARY KEY (id);



ALTER TABLE app.reductions
ADD CONSTRAINT pk__reductio__3213e83f5482a38a_1021246693 PRIMARY KEY (id);



ALTER TABLE app.reductions_history
ADD CONSTRAINT pk__reductio__3213e83fbfa421c7_1117247035 PRIMARY KEY (id);



ALTER TABLE app.region
ADD CONSTRAINT pk__region__3213e83fffc24aba_1472724299 PRIMARY KEY (id);



ALTER TABLE app.roles
ADD CONSTRAINT pk__roles__3213e83f137713d5_1504724413 PRIMARY KEY (id);



ALTER TABLE app.roles
ADD CONSTRAINT roles_role_unique_1520724470 UNIQUE (role);



ALTER TABLE app.row_type_definitions
ADD CONSTRAINT pk__row_type__3213e83f0ee27be3_1552724584 PRIMARY KEY (id);



ALTER TABLE app.tasks
ADD CONSTRAINT pk__tasks__3213e83fb84b9cd7_269244014 PRIMARY KEY (id);



ALTER TABLE app.team
ADD CONSTRAINT pk__team__3213e83f14d6a0f0_333244242 PRIMARY KEY (id);



ALTER TABLE app.tiers
ADD CONSTRAINT pk__tiers__3213e83ff9377d21_1853249657 PRIMARY KEY (id);



ALTER TABLE app.user_role
ADD CONSTRAINT pk__user_rol__3213e83f72d16c5f_397244470 PRIMARY KEY (id);



ALTER TABLE app.user_role
ADD CONSTRAINT user_role_user_id_unique_413244527 UNIQUE (user_id);



ALTER TABLE app.users
ADD CONSTRAINT pk__users__3213e83f57e9c76f_1584724698 PRIMARY KEY (id);



ALTER TABLE app.users
ADD CONSTRAINT users_username_unique_1600724755 UNIQUE (username);



ALTER TABLE app.workload
ADD CONSTRAINT pk__workload__3213e83f428ddef6_1213247377 PRIMARY KEY (id);



ALTER TABLE app.workload_owner
ADD CONSTRAINT pk__workload__3213e83fee6f2402_477244755 PRIMARY KEY (id);



ALTER TABLE app.workload_points
ADD CONSTRAINT pk__workload__3213e83f762715e2_1632724869 PRIMARY KEY (id);



ALTER TABLE app.workload_points_calculations
ADD CONSTRAINT pk__workload__3213e83f963a7f76_1421248118 PRIMARY KEY (id);



ALTER TABLE app.workload_report
ADD CONSTRAINT pk__workload__3213e83fe5f0fb29_13243102 PRIMARY KEY (id);



-- ------------ Write CREATE-FOREIGN-KEY-CONSTRAINT-stage scripts -----------

ALTER TABLE app.case_details
ADD CONSTRAINT case_details_workload_id_foreign_1613248802 FOREIGN KEY (workload_id)
REFERENCES app.workload (id)
ON UPDATE NO ACTION
ON DELETE NO ACTION;



ALTER TABLE app.court_reports
ADD CONSTRAINT court_reports_workload_owner_id_foreign_557245040 FOREIGN KEY (workload_owner_id)
REFERENCES app.workload_owner (id)
ON UPDATE NO ACTION
ON DELETE NO ACTION;



ALTER TABLE app.court_reports_calculations
ADD CONSTRAINT court_reports_calculations_court_reports_id_foreign_621245268 FOREIGN KEY (court_reports_id)
REFERENCES app.court_reports (id)
ON UPDATE NO ACTION
ON DELETE NO ACTION;



ALTER TABLE app.court_reports_calculations
ADD CONSTRAINT court_reports_calculations_workload_points_id_foreign_637245325 FOREIGN KEY (workload_points_id)
REFERENCES app.workload_points (id)
ON UPDATE NO ACTION
ON DELETE NO ACTION;



ALTER TABLE app.court_reports_calculations
ADD CONSTRAINT court_reports_calculations_workload_report_id_foreign_653245382 FOREIGN KEY (workload_report_id)
REFERENCES app.workload_report (id)
ON UPDATE NO ACTION
ON DELETE NO ACTION;



ALTER TABLE app.ldu
ADD CONSTRAINT ldu_region_id_foreign_109243444 FOREIGN KEY (region_id)
REFERENCES app.region (id)
ON UPDATE NO ACTION
ON DELETE NO ACTION;



ALTER TABLE app.offender_manager
ADD CONSTRAINT offender_manager_type_id_foreign_173243672 FOREIGN KEY (type_id)
REFERENCES app.offender_manager_type (id)
ON UPDATE NO ACTION
ON DELETE NO ACTION;



ALTER TABLE app.omic_case_details
ADD CONSTRAINT fk__omic_case__omic___58e7d564_1661248973 FOREIGN KEY (omic_workload_id)
REFERENCES app.omic_workload (id)
ON UPDATE NO ACTION
ON DELETE NO ACTION;



ALTER TABLE app.omic_tiers
ADD CONSTRAINT fk__omic_tier__omic___4f5e6b2a_1821249543 FOREIGN KEY (omic_workload_id)
REFERENCES app.omic_workload (id)
ON UPDATE NO ACTION
ON DELETE NO ACTION;



ALTER TABLE app.omic_workload
ADD CONSTRAINT fk__omic_work__workl__42f89445_861246123 FOREIGN KEY (workload_owner_id)
REFERENCES app.workload_owner (id)
ON UPDATE NO ACTION
ON DELETE NO ACTION;



ALTER TABLE app.omic_workload_points_calculations
ADD CONSTRAINT fk__omic_work__omic___5dac8a81_941246408 FOREIGN KEY (omic_workload_id)
REFERENCES app.omic_workload (id)
ON UPDATE NO ACTION
ON DELETE NO ACTION;



ALTER TABLE app.omic_workload_points_calculations
ADD CONSTRAINT fk__omic_work__t2a_w__5f94d2f3_957246465 FOREIGN KEY (t2a_workload_points_id)
REFERENCES app.workload_points (id)
ON UPDATE NO ACTION
ON DELETE NO ACTION;



ALTER TABLE app.omic_workload_points_calculations
ADD CONSTRAINT fk__omic_work__workl__5bc4420f_973246522 FOREIGN KEY (workload_report_id)
REFERENCES app.workload_report (id)
ON UPDATE NO ACTION
ON DELETE NO ACTION;



ALTER TABLE app.omic_workload_points_calculations
ADD CONSTRAINT fk__omic_work__workl__5cb86648_989246579 FOREIGN KEY (workload_points_id)
REFERENCES app.workload_points (id)
ON UPDATE NO ACTION
ON DELETE NO ACTION;



ALTER TABLE app.reduction_reason
ADD CONSTRAINT reduction_reason_category_id_foreign_237243900 FOREIGN KEY (category_id)
REFERENCES app.reduction_category (id)
ON UPDATE NO ACTION
ON DELETE NO ACTION;



ALTER TABLE app.reductions
ADD CONSTRAINT reductions_reduction_reason_id_foreign_1069246864 FOREIGN KEY (reduction_reason_id)
REFERENCES app.reduction_reason (id)
ON UPDATE NO ACTION
ON DELETE NO ACTION;



ALTER TABLE app.reductions
ADD CONSTRAINT reductions_workload_owner_id_foreign_1085246921 FOREIGN KEY (workload_owner_id)
REFERENCES app.workload_owner (id)
ON UPDATE NO ACTION
ON DELETE NO ACTION;



ALTER TABLE app.reductions_history
ADD CONSTRAINT fk__reduction__reduc__2acc04f9_1165247206 FOREIGN KEY (reduction_id)
REFERENCES app.reductions (id)
ON UPDATE NO ACTION
ON DELETE NO ACTION;



ALTER TABLE app.reductions_history
ADD CONSTRAINT fk__reduction__reduc__2cb44d6b_1181247263 FOREIGN KEY (reduction_reason_id)
REFERENCES app.reduction_reason (id)
ON UPDATE NO ACTION
ON DELETE NO ACTION;



ALTER TABLE app.tasks
ADD CONSTRAINT tasks_workload_report_id_foreign_301244128 FOREIGN KEY (workload_report_id)
REFERENCES app.workload_report (id)
ON UPDATE NO ACTION
ON DELETE NO ACTION;



ALTER TABLE app.team
ADD CONSTRAINT team_ldu_id_foreign_365244356 FOREIGN KEY (ldu_id)
REFERENCES app.ldu (id)
ON UPDATE NO ACTION
ON DELETE NO ACTION;



ALTER TABLE app.tiers
ADD CONSTRAINT tiers_workload_id_foreign_1981250113 FOREIGN KEY (workload_id)
REFERENCES app.workload (id)
ON UPDATE NO ACTION
ON DELETE NO ACTION;



ALTER TABLE app.user_role
ADD CONSTRAINT user_role_role_id_foreign_429244584 FOREIGN KEY (role_id)
REFERENCES app.roles (id)
ON UPDATE NO ACTION
ON DELETE NO ACTION;



ALTER TABLE app.user_role
ADD CONSTRAINT user_role_user_id_foreign_445244641 FOREIGN KEY (user_id)
REFERENCES app.users (id)
ON UPDATE NO ACTION
ON DELETE NO ACTION;



ALTER TABLE app.workload
ADD CONSTRAINT workload_workload_owner_id_foreign_1389248004 FOREIGN KEY (workload_owner_id)
REFERENCES app.workload_owner (id)
ON UPDATE NO ACTION
ON DELETE NO ACTION;



ALTER TABLE app.workload_owner
ADD CONSTRAINT workload_owner_offender_manager_id_foreign_493244812 FOREIGN KEY (offender_manager_id)
REFERENCES app.offender_manager (id)
ON UPDATE NO ACTION
ON DELETE NO ACTION;



ALTER TABLE app.workload_owner
ADD CONSTRAINT workload_owner_team_id_foreign_509244869 FOREIGN KEY (team_id)
REFERENCES app.team (id)
ON UPDATE NO ACTION
ON DELETE NO ACTION;



ALTER TABLE app.workload_points_calculations
ADD CONSTRAINT workload_points_calculations_t2a_workload_points_id_foreign_1517248460 FOREIGN KEY (t2a_workload_points_id)
REFERENCES app.workload_points (id)
ON UPDATE NO ACTION
ON DELETE NO ACTION;



ALTER TABLE app.workload_points_calculations
ADD CONSTRAINT workload_points_calculations_workload_id_foreign_1533248517 FOREIGN KEY (workload_id)
REFERENCES app.workload (id)
ON UPDATE NO ACTION
ON DELETE NO ACTION;



ALTER TABLE app.workload_points_calculations
ADD CONSTRAINT workload_points_calculations_workload_points_id_foreign_1549248574 FOREIGN KEY (workload_points_id)
REFERENCES app.workload_points (id)
ON UPDATE NO ACTION
ON DELETE NO ACTION;



ALTER TABLE app.workload_points_calculations
ADD CONSTRAINT workload_points_calculations_workload_report_id_foreign_1565248631 FOREIGN KEY (workload_report_id)
REFERENCES app.workload_report (id)
ON UPDATE NO ACTION
ON DELETE NO ACTION;
