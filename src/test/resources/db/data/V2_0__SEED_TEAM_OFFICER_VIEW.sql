
INSERT INTO app.region(code, description) VALUES ('R1', 'Test Region');
INSERT INTO app.ldu(code, description, region_id) VALUES ('L1', 'Test LDU', (select max(id) from app.region));
INSERT INTO app.team(code, description, ldu_id) VALUES ('T1', 'Test Team', (select max(id) from app.ldu));
INSERT INTO app.offender_manager("key", forename, surname, type_id) VALUES('OM1', 'Ben', 'Doe', 1);
INSERT INTO app.workload_owner(offender_manager_id,contracted_hours,team_id) VALUES ((select max(id) from app.offender_manager), 37, (select max(id) from app.team));
INSERT INTO app.workload_report (status, status_description) VALUES ('COMPLETE', 'COMPLETE');


INSERT INTO app.workload
(workload_owner_id, workload_report_id, total_filtered_community_cases, total_filtered_custody_cases, total_filtered_license_cases,  total_cases, total_community_cases, total_custody_cases, total_license_cases, monthly_sdrs, sdr_due_next_30_days, sdr_conversions_last_30_days, paroms_completed_last_30_days, paroms_due_next_30_days, license_last_16_weeks, community_last_16_weeks, arms_community_cases, arms_license_cases, staging_id, total_t2a_cases, total_t2a_community_cases, total_t2a_custody_cases, total_t2a_license_cases, total_filtered_cases)
VALUES((select max(id) from app.workload_owner), (select max(id) from app.workload_report), 10, 20, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);


INSERT INTO app.workload_points_calculations
(workload_report_id, workload_points_id, workload_id, total_points, sdr_points, sdr_conversion_points, paroms_points, nominal_target, available_points, contracted_hours, t2a_workload_points_id, reduction_hours,last_updated_on)
VALUES((select max(id) from app.workload_report), (select id from app.workload_points where is_t2a = false), (select max(id) from app.workload), 500, 0, 0, 0, 0, 1000,  37, (select id from app.workload_points where is_t2a = true), 10,'2013-11-03 09:00:00'::timestamp);
