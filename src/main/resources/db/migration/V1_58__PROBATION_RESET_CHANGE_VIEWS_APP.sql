
DROP VIEW app.ldu_caseload_view;
CREATE OR REPLACE VIEW app.ldu_caseload_view
AS SELECT t.ldu_id AS id,
          t.id AS link_id,
          t.description AS name,
          omt.grade_code,
          tr.location,
          r.description AS region_name,
          sum(
                  CASE
                      WHEN tr.tier_number = 0::numeric THEN tr.total_filtered_cases
                      ELSE 0::numeric
                      END +
                  CASE
                      WHEN tr.tier_number = 0::numeric THEN tr.t2a_total_cases
                      ELSE 0::bigint
                      END::numeric) AS untiered,
          sum(
                  CASE
                      WHEN tr.tier_number = 1::numeric THEN tr.total_filtered_cases
                      ELSE 0::numeric
                      END +
                  CASE
                      WHEN tr.tier_number = 1::numeric THEN tr.t2a_total_cases
                      ELSE 0::bigint
                      END::numeric) AS a3,
          sum(
                  CASE
                      WHEN tr.tier_number = 2::numeric THEN tr.total_filtered_cases
                      ELSE 0::numeric
                      END +
                  CASE
                      WHEN tr.tier_number = 2::numeric THEN tr.t2a_total_cases
                      ELSE 0::bigint
                      END::numeric) AS a2,
          sum(
                  CASE
                      WHEN tr.tier_number = 3::numeric THEN tr.total_filtered_cases
                      ELSE 0::numeric
                      END +
                  CASE
                      WHEN tr.tier_number = 3::numeric THEN tr.t2a_total_cases
                      ELSE 0::bigint
                      END::numeric) AS a1,
          sum(
                  CASE
                      WHEN tr.tier_number = 4::numeric THEN tr.total_filtered_cases
                      ELSE 0::numeric
                      END +
                  CASE
                      WHEN tr.tier_number = 4::numeric THEN tr.t2a_total_cases
                      ELSE 0::bigint
                      END::numeric) AS a0,
          sum(
                  CASE
                      WHEN tr.tier_number = 5::numeric THEN tr.total_filtered_cases
                      ELSE 0::numeric
                      END +
                  CASE
                      WHEN tr.tier_number = 5::numeric THEN tr.t2a_total_cases
                      ELSE 0::bigint
                      END::numeric) AS b3,
          sum(
                  CASE
                      WHEN tr.tier_number = 6::numeric THEN tr.total_filtered_cases
                      ELSE 0::numeric
                      END +
                  CASE
                      WHEN tr.tier_number = 6::numeric THEN tr.t2a_total_cases
                      ELSE 0::bigint
                      END::numeric) AS b2,
          sum(
                  CASE
                      WHEN tr.tier_number = 7::numeric THEN tr.total_filtered_cases
                      ELSE 0::numeric
                      END +
                  CASE
                      WHEN tr.tier_number = 7::numeric THEN tr.t2a_total_cases
                      ELSE 0::bigint
                      END::numeric) AS b1,
          sum(
                  CASE
                      WHEN tr.tier_number = 8::numeric THEN tr.total_filtered_cases
                      ELSE 0::numeric
                      END +
                  CASE
                      WHEN tr.tier_number = 8::numeric THEN tr.t2a_total_cases
                      ELSE 0::bigint
                      END::numeric) AS b0,
          sum(
                  CASE
                      WHEN tr.tier_number = 9::numeric THEN tr.total_filtered_cases
                      ELSE 0::numeric
                      END +
                  CASE
                      WHEN tr.tier_number = 9::numeric THEN tr.t2a_total_cases
                      ELSE 0::bigint
                      END::numeric) AS c3,
          sum(
                  CASE
                      WHEN tr.tier_number = 10::numeric THEN tr.total_filtered_cases
                      ELSE 0::numeric
                      END +
                  CASE
                      WHEN tr.tier_number = 10::numeric THEN tr.t2a_total_cases
                      ELSE 0::bigint
                      END::numeric) AS c2,
          sum(
                  CASE
                      WHEN tr.tier_number = 11::numeric THEN tr.total_filtered_cases
                      ELSE 0::numeric
                      END +
                  CASE
                      WHEN tr.tier_number = 11::numeric THEN tr.t2a_total_cases
                      ELSE 0::bigint
                      END::numeric) AS c1,
          sum(
                  CASE
                      WHEN tr.tier_number = 12::numeric THEN tr.total_filtered_cases
                      ELSE 0::numeric
                      END +
                  CASE
                      WHEN tr.tier_number = 12::numeric THEN tr.t2a_total_cases
                      ELSE 0::bigint
                      END::numeric) AS c0,
          sum(
                  CASE
                      WHEN tr.tier_number = 13::numeric THEN tr.total_filtered_cases
                      ELSE 0::numeric
                      END +
                  CASE
                      WHEN tr.tier_number = 13::numeric THEN tr.t2a_total_cases
                      ELSE 0::bigint
                      END::numeric) AS d3,
          sum(
                  CASE
                      WHEN tr.tier_number = 14::numeric THEN tr.total_filtered_cases
                      ELSE 0::numeric
                      END +
                  CASE
                      WHEN tr.tier_number = 14::numeric THEN tr.t2a_total_cases
                      ELSE 0::bigint
                      END::numeric) AS d2,
          sum(
                  CASE
                      WHEN tr.tier_number = 15::numeric THEN tr.total_filtered_cases
                      ELSE 0::numeric
                      END +
                  CASE
                      WHEN tr.tier_number = 15::numeric THEN tr.t2a_total_cases
                      ELSE 0::bigint
                      END::numeric) AS d1,
          sum(
                  CASE
                      WHEN tr.tier_number = 16::numeric THEN tr.total_filtered_cases
                      ELSE 0::numeric
                      END +
                  CASE
                      WHEN tr.tier_number = 16::numeric THEN tr.t2a_total_cases
                      ELSE 0::bigint
                      END::numeric) AS d0,
          sum(
                  CASE
                      WHEN tr.tier_number = 17::numeric THEN tr.total_filtered_cases
                      ELSE 0::numeric
                      END +
                  CASE
                      WHEN tr.tier_number = 17::numeric THEN tr.t2a_total_cases
                      ELSE 0::bigint
                      END::numeric) AS a3_s,
          sum(
                  CASE
                      WHEN tr.tier_number = 18::numeric THEN tr.total_filtered_cases
                      ELSE 0::numeric
                      END +
                  CASE
                      WHEN tr.tier_number = 18::numeric THEN tr.t2a_total_cases
                      ELSE 0::bigint
                      END::numeric) AS a2_s,
          sum(
                  CASE
                      WHEN tr.tier_number = 19::numeric THEN tr.total_filtered_cases
                      ELSE 0::numeric
                      END +
                  CASE
                      WHEN tr.tier_number = 19::numeric THEN tr.t2a_total_cases
                      ELSE 0::bigint
                      END::numeric) AS a1_s,
          sum(
                  CASE
                      WHEN tr.tier_number = 20::numeric THEN tr.total_filtered_cases
                      ELSE 0::numeric
                      END +
                  CASE
                      WHEN tr.tier_number = 20::numeric THEN tr.t2a_total_cases
                      ELSE 0::bigint
                      END::numeric) AS a0_s,
          sum(
                  CASE
                      WHEN tr.tier_number = 21::numeric THEN tr.total_filtered_cases
                      ELSE 0::numeric
                      END +
                  CASE
                      WHEN tr.tier_number = 21::numeric THEN tr.t2a_total_cases
                      ELSE 0::bigint
                      END::numeric) AS b3_s,
          sum(
                  CASE
                      WHEN tr.tier_number = 22::numeric THEN tr.total_filtered_cases
                      ELSE 0::numeric
                      END +
                  CASE
                      WHEN tr.tier_number = 22::numeric THEN tr.t2a_total_cases
                      ELSE 0::bigint
                      END::numeric) AS b2_s,
          sum(
                  CASE
                      WHEN tr.tier_number = 23::numeric THEN tr.total_filtered_cases
                      ELSE 0::numeric
                      END +
                  CASE
                      WHEN tr.tier_number = 23::numeric THEN tr.t2a_total_cases
                      ELSE 0::bigint
                      END::numeric) AS b1_s,
          sum(
                  CASE
                      WHEN tr.tier_number = 24::numeric THEN tr.total_filtered_cases
                      ELSE 0::numeric
                      END +
                  CASE
                      WHEN tr.tier_number = 24::numeric THEN tr.t2a_total_cases
                      ELSE 0::bigint
                      END::numeric) AS b0_s,
          sum(
                  CASE
                      WHEN tr.tier_number = 25::numeric THEN tr.total_filtered_cases
                      ELSE 0::numeric
                      END +
                  CASE
                      WHEN tr.tier_number = 25::numeric THEN tr.t2a_total_cases
                      ELSE 0::bigint
                      END::numeric) AS c3_s,
          sum(
                  CASE
                      WHEN tr.tier_number = 26::numeric THEN tr.total_filtered_cases
                      ELSE 0::numeric
                      END +
                  CASE
                      WHEN tr.tier_number = 26::numeric THEN tr.t2a_total_cases
                      ELSE 0::bigint
                      END::numeric) AS c2_s,
          sum(
                  CASE
                      WHEN tr.tier_number = 27::numeric THEN tr.total_filtered_cases
                      ELSE 0::numeric
                      END +
                  CASE
                      WHEN tr.tier_number = 27::numeric THEN tr.t2a_total_cases
                      ELSE 0::bigint
                      END::numeric) AS c1_s,
          sum(
                  CASE
                      WHEN tr.tier_number = 28::numeric THEN tr.total_filtered_cases
                      ELSE 0::numeric
                      END +
                  CASE
                      WHEN tr.tier_number = 28::numeric THEN tr.t2a_total_cases
                      ELSE 0::bigint
                      END::numeric) AS c0_s,
          sum(
                  CASE
                      WHEN tr.tier_number = 29::numeric THEN tr.total_filtered_cases
                      ELSE 0::numeric
                      END +
                  CASE
                      WHEN tr.tier_number = 29::numeric THEN tr.t2a_total_cases
                      ELSE 0::bigint
                      END::numeric) AS d3_s,
          sum(
                  CASE
                      WHEN tr.tier_number = 30::numeric THEN tr.total_filtered_cases
                      ELSE 0::numeric
                      END +
                  CASE
                      WHEN tr.tier_number = 30::numeric THEN tr.t2a_total_cases
                      ELSE 0::bigint
                      END::numeric) AS d2_s,
          sum(
                  CASE
                      WHEN tr.tier_number = 31::numeric THEN tr.total_filtered_cases
                      ELSE 0::numeric
                      END +
                  CASE
                      WHEN tr.tier_number = 31::numeric THEN tr.t2a_total_cases
                      ELSE 0::bigint
                      END::numeric) AS d1_s,
          sum(
                  CASE
                      WHEN tr.tier_number = 32::numeric THEN tr.total_filtered_cases
                      ELSE 0::numeric
                      END +
                  CASE
                      WHEN tr.tier_number = 32::numeric THEN tr.t2a_total_cases
                      ELSE 0::bigint
                      END::numeric) AS d0_s,
          sum(tr.total_filtered_cases + tr.t2a_total_cases::numeric) AS total_cases,
          count(*) AS count
   FROM app.tiers tr
            JOIN app.workload w ON tr.workload_id = w.id
            JOIN app.workload_points_calculations wpc ON wpc.workload_id = w.id
            JOIN app.workload_report wr ON wr.id = wpc.workload_report_id
            JOIN app.workload_owner wo ON wo.id = w.workload_owner_id
            JOIN app.team t ON t.id = wo.team_id
            JOIN app.ldu l ON l.id = t.ldu_id
            JOIN app.region r ON r.id = l.region_id
            JOIN app.offender_manager om ON om.id = wo.offender_manager_id
            JOIN app.offender_manager_type omt ON omt.id = om.type_id
   WHERE wr.effective_from IS NOT NULL AND wr.effective_to IS NULL
   GROUP BY t.ldu_id, t.id, t.description, r.description, omt.grade_code, tr.location;



-- app.national_caseload_view source
DROP VIEW app.national_caseload_view;
CREATE OR REPLACE VIEW app.national_caseload_view
AS SELECT r.id AS link_id,
          r.description AS name,
          omt.grade_code,
          tr.location,
          sum(
                  CASE
                      WHEN tr.tier_number = 0::numeric THEN tr.total_filtered_cases
                      ELSE 0::numeric
                      END +
                  CASE
                      WHEN tr.tier_number = 0::numeric THEN tr.t2a_total_cases
                      ELSE 0::bigint
                      END::numeric) AS untiered,
          sum(
                  CASE
                      WHEN tr.tier_number = 1::numeric THEN tr.total_filtered_cases
                      ELSE 0::numeric
                      END +
                  CASE
                      WHEN tr.tier_number = 1::numeric THEN tr.t2a_total_cases
                      ELSE 0::bigint
                      END::numeric) AS a3,
          sum(
                  CASE
                      WHEN tr.tier_number = 2::numeric THEN tr.total_filtered_cases
                      ELSE 0::numeric
                      END +
                  CASE
                      WHEN tr.tier_number = 2::numeric THEN tr.t2a_total_cases
                      ELSE 0::bigint
                      END::numeric) AS a2,
          sum(
                  CASE
                      WHEN tr.tier_number = 3::numeric THEN tr.total_filtered_cases
                      ELSE 0::numeric
                      END +
                  CASE
                      WHEN tr.tier_number = 3::numeric THEN tr.t2a_total_cases
                      ELSE 0::bigint
                      END::numeric) AS a1,
          sum(
                  CASE
                      WHEN tr.tier_number = 4::numeric THEN tr.total_filtered_cases
                      ELSE 0::numeric
                      END +
                  CASE
                      WHEN tr.tier_number = 4::numeric THEN tr.t2a_total_cases
                      ELSE 0::bigint
                      END::numeric) AS a0,
          sum(
                  CASE
                      WHEN tr.tier_number = 5::numeric THEN tr.total_filtered_cases
                      ELSE 0::numeric
                      END +
                  CASE
                      WHEN tr.tier_number = 5::numeric THEN tr.t2a_total_cases
                      ELSE 0::bigint
                      END::numeric) AS b3,
          sum(
                  CASE
                      WHEN tr.tier_number = 6::numeric THEN tr.total_filtered_cases
                      ELSE 0::numeric
                      END +
                  CASE
                      WHEN tr.tier_number = 6::numeric THEN tr.t2a_total_cases
                      ELSE 0::bigint
                      END::numeric) AS b2,
          sum(
                  CASE
                      WHEN tr.tier_number = 7::numeric THEN tr.total_filtered_cases
                      ELSE 0::numeric
                      END +
                  CASE
                      WHEN tr.tier_number = 7::numeric THEN tr.t2a_total_cases
                      ELSE 0::bigint
                      END::numeric) AS b1,
          sum(
                  CASE
                      WHEN tr.tier_number = 8::numeric THEN tr.total_filtered_cases
                      ELSE 0::numeric
                      END +
                  CASE
                      WHEN tr.tier_number = 8::numeric THEN tr.t2a_total_cases
                      ELSE 0::bigint
                      END::numeric) AS b0,
          sum(
                  CASE
                      WHEN tr.tier_number = 9::numeric THEN tr.total_filtered_cases
                      ELSE 0::numeric
                      END +
                  CASE
                      WHEN tr.tier_number = 9::numeric THEN tr.t2a_total_cases
                      ELSE 0::bigint
                      END::numeric) AS c3,
          sum(
                  CASE
                      WHEN tr.tier_number = 10::numeric THEN tr.total_filtered_cases
                      ELSE 0::numeric
                      END +
                  CASE
                      WHEN tr.tier_number = 10::numeric THEN tr.t2a_total_cases
                      ELSE 0::bigint
                      END::numeric) AS c2,
          sum(
                  CASE
                      WHEN tr.tier_number = 11::numeric THEN tr.total_filtered_cases
                      ELSE 0::numeric
                      END +
                  CASE
                      WHEN tr.tier_number = 11::numeric THEN tr.t2a_total_cases
                      ELSE 0::bigint
                      END::numeric) AS c1,
          sum(
                  CASE
                      WHEN tr.tier_number = 12::numeric THEN tr.total_filtered_cases
                      ELSE 0::numeric
                      END +
                  CASE
                      WHEN tr.tier_number = 12::numeric THEN tr.t2a_total_cases
                      ELSE 0::bigint
                      END::numeric) AS c0,
          sum(
                  CASE
                      WHEN tr.tier_number = 13::numeric THEN tr.total_filtered_cases
                      ELSE 0::numeric
                      END +
                  CASE
                      WHEN tr.tier_number = 13::numeric THEN tr.t2a_total_cases
                      ELSE 0::bigint
                      END::numeric) AS d3,
          sum(
                  CASE
                      WHEN tr.tier_number = 14::numeric THEN tr.total_filtered_cases
                      ELSE 0::numeric
                      END +
                  CASE
                      WHEN tr.tier_number = 14::numeric THEN tr.t2a_total_cases
                      ELSE 0::bigint
                      END::numeric) AS d2,
          sum(
                  CASE
                      WHEN tr.tier_number = 15::numeric THEN tr.total_filtered_cases
                      ELSE 0::numeric
                      END +
                  CASE
                      WHEN tr.tier_number = 15::numeric THEN tr.t2a_total_cases
                      ELSE 0::bigint
                      END::numeric) AS d1,
          sum(
                  CASE
                      WHEN tr.tier_number = 16::numeric THEN tr.total_filtered_cases
                      ELSE 0::numeric
                      END +
                  CASE
                      WHEN tr.tier_number = 16::numeric THEN tr.t2a_total_cases
                      ELSE 0::bigint
                      END::numeric) AS d0,
          sum(
                  CASE
                      WHEN tr.tier_number = 17::numeric THEN tr.total_filtered_cases
                      ELSE 0::numeric
                      END +
                  CASE
                      WHEN tr.tier_number = 17::numeric THEN tr.t2a_total_cases
                      ELSE 0::bigint
                      END::numeric) AS a3_s,
          sum(
                  CASE
                      WHEN tr.tier_number = 18::numeric THEN tr.total_filtered_cases
                      ELSE 0::numeric
                      END +
                  CASE
                      WHEN tr.tier_number = 18::numeric THEN tr.t2a_total_cases
                      ELSE 0::bigint
                      END::numeric) AS a2_s,
          sum(
                  CASE
                      WHEN tr.tier_number = 19::numeric THEN tr.total_filtered_cases
                      ELSE 0::numeric
                      END +
                  CASE
                      WHEN tr.tier_number = 19::numeric THEN tr.t2a_total_cases
                      ELSE 0::bigint
                      END::numeric) AS a1_s,
          sum(
                  CASE
                      WHEN tr.tier_number = 20::numeric THEN tr.total_filtered_cases
                      ELSE 0::numeric
                      END +
                  CASE
                      WHEN tr.tier_number = 20::numeric THEN tr.t2a_total_cases
                      ELSE 0::bigint
                      END::numeric) AS a0_s,
          sum(
                  CASE
                      WHEN tr.tier_number = 21::numeric THEN tr.total_filtered_cases
                      ELSE 0::numeric
                      END +
                  CASE
                      WHEN tr.tier_number = 21::numeric THEN tr.t2a_total_cases
                      ELSE 0::bigint
                      END::numeric) AS b3_s,
          sum(
                  CASE
                      WHEN tr.tier_number = 22::numeric THEN tr.total_filtered_cases
                      ELSE 0::numeric
                      END +
                  CASE
                      WHEN tr.tier_number = 22::numeric THEN tr.t2a_total_cases
                      ELSE 0::bigint
                      END::numeric) AS b2_s,
          sum(
                  CASE
                      WHEN tr.tier_number = 23::numeric THEN tr.total_filtered_cases
                      ELSE 0::numeric
                      END +
                  CASE
                      WHEN tr.tier_number = 23::numeric THEN tr.t2a_total_cases
                      ELSE 0::bigint
                      END::numeric) AS b1_s,
          sum(
                  CASE
                      WHEN tr.tier_number = 24::numeric THEN tr.total_filtered_cases
                      ELSE 0::numeric
                      END +
                  CASE
                      WHEN tr.tier_number = 24::numeric THEN tr.t2a_total_cases
                      ELSE 0::bigint
                      END::numeric) AS b0_s,
          sum(
                  CASE
                      WHEN tr.tier_number = 25::numeric THEN tr.total_filtered_cases
                      ELSE 0::numeric
                      END +
                  CASE
                      WHEN tr.tier_number = 25::numeric THEN tr.t2a_total_cases
                      ELSE 0::bigint
                      END::numeric) AS c3_s,
          sum(
                  CASE
                      WHEN tr.tier_number = 26::numeric THEN tr.total_filtered_cases
                      ELSE 0::numeric
                      END +
                  CASE
                      WHEN tr.tier_number = 26::numeric THEN tr.t2a_total_cases
                      ELSE 0::bigint
                      END::numeric) AS c2_s,
          sum(
                  CASE
                      WHEN tr.tier_number = 27::numeric THEN tr.total_filtered_cases
                      ELSE 0::numeric
                      END +
                  CASE
                      WHEN tr.tier_number = 27::numeric THEN tr.t2a_total_cases
                      ELSE 0::bigint
                      END::numeric) AS c1_s,
          sum(
                  CASE
                      WHEN tr.tier_number = 28::numeric THEN tr.total_filtered_cases
                      ELSE 0::numeric
                      END +
                  CASE
                      WHEN tr.tier_number = 28::numeric THEN tr.t2a_total_cases
                      ELSE 0::bigint
                      END::numeric) AS c0_s,
          sum(
                  CASE
                      WHEN tr.tier_number = 29::numeric THEN tr.total_filtered_cases
                      ELSE 0::numeric
                      END +
                  CASE
                      WHEN tr.tier_number = 29::numeric THEN tr.t2a_total_cases
                      ELSE 0::bigint
                      END::numeric) AS d3_s,
          sum(
                  CASE
                      WHEN tr.tier_number = 30::numeric THEN tr.total_filtered_cases
                      ELSE 0::numeric
                      END +
                  CASE
                      WHEN tr.tier_number = 30::numeric THEN tr.t2a_total_cases
                      ELSE 0::bigint
                      END::numeric) AS d2_s,
          sum(
                  CASE
                      WHEN tr.tier_number = 31::numeric THEN tr.total_filtered_cases
                      ELSE 0::numeric
                      END +
                  CASE
                      WHEN tr.tier_number = 31::numeric THEN tr.t2a_total_cases
                      ELSE 0::bigint
                      END::numeric) AS d1_s,
          sum(
                  CASE
                      WHEN tr.tier_number = 32::numeric THEN tr.total_filtered_cases
                      ELSE 0::numeric
                      END +
                  CASE
                      WHEN tr.tier_number = 32::numeric THEN tr.t2a_total_cases
                      ELSE 0::bigint
                      END::numeric) AS d0_s,
          sum(tr.total_filtered_cases + tr.t2a_total_cases::numeric) AS total_cases,
          count(*) AS count
   FROM app.tiers tr
            JOIN app.workload w ON tr.workload_id = w.id
            JOIN app.workload_points_calculations wpc ON wpc.workload_id = w.id
            JOIN app.workload_report wr ON wr.id = wpc.workload_report_id
            JOIN app.workload_owner wo ON wo.id = w.workload_owner_id
            JOIN app.team t ON t.id = wo.team_id
            JOIN app.ldu l ON l.id = t.ldu_id
            JOIN app.region r ON r.id = l.region_id
            JOIN app.offender_manager om ON om.id = wo.offender_manager_id
            JOIN app.offender_manager_type omt ON omt.id = om.type_id
   WHERE wr.effective_from IS NOT NULL AND wr.effective_to IS NULL
   GROUP BY r.id, r.description, omt.grade_code, tr.location;


-- app.region_caseload_view source
DROP VIEW app.region_caseload_view;
CREATE OR REPLACE VIEW app.region_caseload_view
AS SELECT l.region_id AS id,
          l.id AS link_id,
          l.description AS name,
          r.description AS region_name,
          r.code AS region_code,
          l.code AS ldu_code,
          omt.grade_code,
          tr.location,
          sum(
                  CASE
                      WHEN tr.tier_number = 0::numeric THEN tr.total_filtered_cases
                      ELSE 0::numeric
                      END +
                  CASE
                      WHEN tr.tier_number = 0::numeric THEN tr.t2a_total_cases
                      ELSE 0::bigint
                      END::numeric) AS untiered,
          sum(
                  CASE
                      WHEN tr.tier_number = 1::numeric THEN tr.total_filtered_cases
                      ELSE 0::numeric
                      END +
                  CASE
                      WHEN tr.tier_number = 1::numeric THEN tr.t2a_total_cases
                      ELSE 0::bigint
                      END::numeric) AS a3,
          sum(
                  CASE
                      WHEN tr.tier_number = 2::numeric THEN tr.total_filtered_cases
                      ELSE 0::numeric
                      END +
                  CASE
                      WHEN tr.tier_number = 2::numeric THEN tr.t2a_total_cases
                      ELSE 0::bigint
                      END::numeric) AS a2,
          sum(
                  CASE
                      WHEN tr.tier_number = 3::numeric THEN tr.total_filtered_cases
                      ELSE 0::numeric
                      END +
                  CASE
                      WHEN tr.tier_number = 3::numeric THEN tr.t2a_total_cases
                      ELSE 0::bigint
                      END::numeric) AS a1,
          sum(
                  CASE
                      WHEN tr.tier_number = 4::numeric THEN tr.total_filtered_cases
                      ELSE 0::numeric
                      END +
                  CASE
                      WHEN tr.tier_number = 4::numeric THEN tr.t2a_total_cases
                      ELSE 0::bigint
                      END::numeric) AS a0,
          sum(
                  CASE
                      WHEN tr.tier_number = 5::numeric THEN tr.total_filtered_cases
                      ELSE 0::numeric
                      END +
                  CASE
                      WHEN tr.tier_number = 5::numeric THEN tr.t2a_total_cases
                      ELSE 0::bigint
                      END::numeric) AS b3,
          sum(
                  CASE
                      WHEN tr.tier_number = 6::numeric THEN tr.total_filtered_cases
                      ELSE 0::numeric
                      END +
                  CASE
                      WHEN tr.tier_number = 6::numeric THEN tr.t2a_total_cases
                      ELSE 0::bigint
                      END::numeric) AS b2,
          sum(
                  CASE
                      WHEN tr.tier_number = 7::numeric THEN tr.total_filtered_cases
                      ELSE 0::numeric
                      END +
                  CASE
                      WHEN tr.tier_number = 7::numeric THEN tr.t2a_total_cases
                      ELSE 0::bigint
                      END::numeric) AS b1,
          sum(
                  CASE
                      WHEN tr.tier_number = 8::numeric THEN tr.total_filtered_cases
                      ELSE 0::numeric
                      END +
                  CASE
                      WHEN tr.tier_number = 8::numeric THEN tr.t2a_total_cases
                      ELSE 0::bigint
                      END::numeric) AS b0,
          sum(
                  CASE
                      WHEN tr.tier_number = 9::numeric THEN tr.total_filtered_cases
                      ELSE 0::numeric
                      END +
                  CASE
                      WHEN tr.tier_number = 9::numeric THEN tr.t2a_total_cases
                      ELSE 0::bigint
                      END::numeric) AS c3,
          sum(
                  CASE
                      WHEN tr.tier_number = 10::numeric THEN tr.total_filtered_cases
                      ELSE 0::numeric
                      END +
                  CASE
                      WHEN tr.tier_number = 10::numeric THEN tr.t2a_total_cases
                      ELSE 0::bigint
                      END::numeric) AS c2,
          sum(
                  CASE
                      WHEN tr.tier_number = 11::numeric THEN tr.total_filtered_cases
                      ELSE 0::numeric
                      END +
                  CASE
                      WHEN tr.tier_number = 11::numeric THEN tr.t2a_total_cases
                      ELSE 0::bigint
                      END::numeric) AS c1,
          sum(
                  CASE
                      WHEN tr.tier_number = 12::numeric THEN tr.total_filtered_cases
                      ELSE 0::numeric
                      END +
                  CASE
                      WHEN tr.tier_number = 12::numeric THEN tr.t2a_total_cases
                      ELSE 0::bigint
                      END::numeric) AS c0,
          sum(
                  CASE
                      WHEN tr.tier_number = 13::numeric THEN tr.total_filtered_cases
                      ELSE 0::numeric
                      END +
                  CASE
                      WHEN tr.tier_number = 13::numeric THEN tr.t2a_total_cases
                      ELSE 0::bigint
                      END::numeric) AS d3,
          sum(
                  CASE
                      WHEN tr.tier_number = 14::numeric THEN tr.total_filtered_cases
                      ELSE 0::numeric
                      END +
                  CASE
                      WHEN tr.tier_number = 14::numeric THEN tr.t2a_total_cases
                      ELSE 0::bigint
                      END::numeric) AS d2,
          sum(
                  CASE
                      WHEN tr.tier_number = 15::numeric THEN tr.total_filtered_cases
                      ELSE 0::numeric
                      END +
                  CASE
                      WHEN tr.tier_number = 15::numeric THEN tr.t2a_total_cases
                      ELSE 0::bigint
                      END::numeric) AS d1,
          sum(
                  CASE
                      WHEN tr.tier_number = 16::numeric THEN tr.total_filtered_cases
                      ELSE 0::numeric
                      END +
                  CASE
                      WHEN tr.tier_number = 16::numeric THEN tr.t2a_total_cases
                      ELSE 0::bigint
                      END::numeric) AS d0,
          sum(
                  CASE
                      WHEN tr.tier_number = 17::numeric THEN tr.total_filtered_cases
                      ELSE 0::numeric
                      END +
                  CASE
                      WHEN tr.tier_number = 17::numeric THEN tr.t2a_total_cases
                      ELSE 0::bigint
                      END::numeric) AS a3_s,
          sum(
                  CASE
                      WHEN tr.tier_number = 18::numeric THEN tr.total_filtered_cases
                      ELSE 0::numeric
                      END +
                  CASE
                      WHEN tr.tier_number = 18::numeric THEN tr.t2a_total_cases
                      ELSE 0::bigint
                      END::numeric) AS a2_s,
          sum(
                  CASE
                      WHEN tr.tier_number = 19::numeric THEN tr.total_filtered_cases
                      ELSE 0::numeric
                      END +
                  CASE
                      WHEN tr.tier_number = 19::numeric THEN tr.t2a_total_cases
                      ELSE 0::bigint
                      END::numeric) AS a1_s,
          sum(
                  CASE
                      WHEN tr.tier_number = 20::numeric THEN tr.total_filtered_cases
                      ELSE 0::numeric
                      END +
                  CASE
                      WHEN tr.tier_number = 20::numeric THEN tr.t2a_total_cases
                      ELSE 0::bigint
                      END::numeric) AS a0_s,
          sum(
                  CASE
                      WHEN tr.tier_number = 21::numeric THEN tr.total_filtered_cases
                      ELSE 0::numeric
                      END +
                  CASE
                      WHEN tr.tier_number = 21::numeric THEN tr.t2a_total_cases
                      ELSE 0::bigint
                      END::numeric) AS b3_s,
          sum(
                  CASE
                      WHEN tr.tier_number = 22::numeric THEN tr.total_filtered_cases
                      ELSE 0::numeric
                      END +
                  CASE
                      WHEN tr.tier_number = 22::numeric THEN tr.t2a_total_cases
                      ELSE 0::bigint
                      END::numeric) AS b2_s,
          sum(
                  CASE
                      WHEN tr.tier_number = 23::numeric THEN tr.total_filtered_cases
                      ELSE 0::numeric
                      END +
                  CASE
                      WHEN tr.tier_number = 23::numeric THEN tr.t2a_total_cases
                      ELSE 0::bigint
                      END::numeric) AS b1_s,
          sum(
                  CASE
                      WHEN tr.tier_number = 24::numeric THEN tr.total_filtered_cases
                      ELSE 0::numeric
                      END +
                  CASE
                      WHEN tr.tier_number = 24::numeric THEN tr.t2a_total_cases
                      ELSE 0::bigint
                      END::numeric) AS b0_s,
          sum(
                  CASE
                      WHEN tr.tier_number = 25::numeric THEN tr.total_filtered_cases
                      ELSE 0::numeric
                      END +
                  CASE
                      WHEN tr.tier_number = 25::numeric THEN tr.t2a_total_cases
                      ELSE 0::bigint
                      END::numeric) AS c3_s,
          sum(
                  CASE
                      WHEN tr.tier_number = 26::numeric THEN tr.total_filtered_cases
                      ELSE 0::numeric
                      END +
                  CASE
                      WHEN tr.tier_number = 26::numeric THEN tr.t2a_total_cases
                      ELSE 0::bigint
                      END::numeric) AS c2_s,
          sum(
                  CASE
                      WHEN tr.tier_number = 27::numeric THEN tr.total_filtered_cases
                      ELSE 0::numeric
                      END +
                  CASE
                      WHEN tr.tier_number = 27::numeric THEN tr.t2a_total_cases
                      ELSE 0::bigint
                      END::numeric) AS c1_s,
          sum(
                  CASE
                      WHEN tr.tier_number = 28::numeric THEN tr.total_filtered_cases
                      ELSE 0::numeric
                      END +
                  CASE
                      WHEN tr.tier_number = 28::numeric THEN tr.t2a_total_cases
                      ELSE 0::bigint
                      END::numeric) AS c0_s,
          sum(
                  CASE
                      WHEN tr.tier_number = 29::numeric THEN tr.total_filtered_cases
                      ELSE 0::numeric
                      END +
                  CASE
                      WHEN tr.tier_number = 29::numeric THEN tr.t2a_total_cases
                      ELSE 0::bigint
                      END::numeric) AS d3_s,
          sum(
                  CASE
                      WHEN tr.tier_number = 30::numeric THEN tr.total_filtered_cases
                      ELSE 0::numeric
                      END +
                  CASE
                      WHEN tr.tier_number = 30::numeric THEN tr.t2a_total_cases
                      ELSE 0::bigint
                      END::numeric) AS d2_s,
          sum(
                  CASE
                      WHEN tr.tier_number = 31::numeric THEN tr.total_filtered_cases
                      ELSE 0::numeric
                      END +
                  CASE
                      WHEN tr.tier_number = 31::numeric THEN tr.t2a_total_cases
                      ELSE 0::bigint
                      END::numeric) AS d1_s,
          sum(
                  CASE
                      WHEN tr.tier_number = 32::numeric THEN tr.total_filtered_cases
                      ELSE 0::numeric
                      END +
                  CASE
                      WHEN tr.tier_number = 32::numeric THEN tr.t2a_total_cases
                      ELSE 0::bigint
                      END::numeric) AS d0_s,
          sum(tr.total_filtered_cases + tr.t2a_total_cases::numeric) AS total_cases,
          count(*) AS count
   FROM app.tiers tr
            JOIN app.workload w ON tr.workload_id = w.id
            JOIN app.workload_points_calculations wpc ON wpc.workload_id = w.id
            JOIN app.workload_report wr ON wr.id = wpc.workload_report_id
            JOIN app.workload_owner wo ON wo.id = w.workload_owner_id
            JOIN app.team t ON t.id = wo.team_id
            JOIN app.ldu l ON l.id = t.ldu_id
            JOIN app.region r ON r.id = l.region_id
            JOIN app.offender_manager om ON om.id = wo.offender_manager_id
            JOIN app.offender_manager_type omt ON omt.id = om.type_id
   WHERE wr.effective_from IS NOT NULL AND wr.effective_to IS NULL
   GROUP BY l.region_id, l.id, r.description, l.description, omt.grade_code, tr.location, r.code, l.code;



-- app.team_caseload_view source

CREATE OR REPLACE VIEW app.team_caseload_view
AS SELECT wo.team_id AS id,
          wo.id AS link_id,
          om.forename,
          om.surname,
          omt.grade_code,
          tr.location,
          sum(
                  CASE
                      WHEN tr.tier_number = 0::numeric THEN tr.total_filtered_cases
                      ELSE 0::numeric
                      END +
                  CASE
                      WHEN tr.tier_number = 0::numeric THEN tr.t2a_total_cases
                      ELSE 0::bigint
                      END::numeric) AS untiered,
          sum(
                  CASE
                      WHEN tr.tier_number = 1::numeric THEN tr.total_filtered_cases
                      ELSE 0::numeric
                      END +
                  CASE
                      WHEN tr.tier_number = 1::numeric THEN tr.t2a_total_cases
                      ELSE 0::bigint
                      END::numeric) AS a3,
          sum(
                  CASE
                      WHEN tr.tier_number = 2::numeric THEN tr.total_filtered_cases
                      ELSE 0::numeric
                      END +
                  CASE
                      WHEN tr.tier_number = 2::numeric THEN tr.t2a_total_cases
                      ELSE 0::bigint
                      END::numeric) AS a2,
          sum(
                  CASE
                      WHEN tr.tier_number = 3::numeric THEN tr.total_filtered_cases
                      ELSE 0::numeric
                      END +
                  CASE
                      WHEN tr.tier_number = 3::numeric THEN tr.t2a_total_cases
                      ELSE 0::bigint
                      END::numeric) AS a1,
          sum(
                  CASE
                      WHEN tr.tier_number = 4::numeric THEN tr.total_filtered_cases
                      ELSE 0::numeric
                      END +
                  CASE
                      WHEN tr.tier_number = 4::numeric THEN tr.t2a_total_cases
                      ELSE 0::bigint
                      END::numeric) AS a0,
          sum(
                  CASE
                      WHEN tr.tier_number = 5::numeric THEN tr.total_filtered_cases
                      ELSE 0::numeric
                      END +
                  CASE
                      WHEN tr.tier_number = 5::numeric THEN tr.t2a_total_cases
                      ELSE 0::bigint
                      END::numeric) AS b3,
          sum(
                  CASE
                      WHEN tr.tier_number = 6::numeric THEN tr.total_filtered_cases
                      ELSE 0::numeric
                      END +
                  CASE
                      WHEN tr.tier_number = 6::numeric THEN tr.t2a_total_cases
                      ELSE 0::bigint
                      END::numeric) AS b2,
          sum(
                  CASE
                      WHEN tr.tier_number = 7::numeric THEN tr.total_filtered_cases
                      ELSE 0::numeric
                      END +
                  CASE
                      WHEN tr.tier_number = 7::numeric THEN tr.t2a_total_cases
                      ELSE 0::bigint
                      END::numeric) AS b1,
          sum(
                  CASE
                      WHEN tr.tier_number = 8::numeric THEN tr.total_filtered_cases
                      ELSE 0::numeric
                      END +
                  CASE
                      WHEN tr.tier_number = 8::numeric THEN tr.t2a_total_cases
                      ELSE 0::bigint
                      END::numeric) AS b0,
          sum(
                  CASE
                      WHEN tr.tier_number = 9::numeric THEN tr.total_filtered_cases
                      ELSE 0::numeric
                      END +
                  CASE
                      WHEN tr.tier_number = 9::numeric THEN tr.t2a_total_cases
                      ELSE 0::bigint
                      END::numeric) AS c3,
          sum(
                  CASE
                      WHEN tr.tier_number = 10::numeric THEN tr.total_filtered_cases
                      ELSE 0::numeric
                      END +
                  CASE
                      WHEN tr.tier_number = 10::numeric THEN tr.t2a_total_cases
                      ELSE 0::bigint
                      END::numeric) AS c2,
          sum(
                  CASE
                      WHEN tr.tier_number = 11::numeric THEN tr.total_filtered_cases
                      ELSE 0::numeric
                      END +
                  CASE
                      WHEN tr.tier_number = 11::numeric THEN tr.t2a_total_cases
                      ELSE 0::bigint
                      END::numeric) AS c1,
          sum(
                  CASE
                      WHEN tr.tier_number = 12::numeric THEN tr.total_filtered_cases
                      ELSE 0::numeric
                      END +
                  CASE
                      WHEN tr.tier_number = 12::numeric THEN tr.t2a_total_cases
                      ELSE 0::bigint
                      END::numeric) AS c0,
          sum(
                  CASE
                      WHEN tr.tier_number = 13::numeric THEN tr.total_filtered_cases
                      ELSE 0::numeric
                      END +
                  CASE
                      WHEN tr.tier_number = 13::numeric THEN tr.t2a_total_cases
                      ELSE 0::bigint
                      END::numeric) AS d3,
          sum(
                  CASE
                      WHEN tr.tier_number = 14::numeric THEN tr.total_filtered_cases
                      ELSE 0::numeric
                      END +
                  CASE
                      WHEN tr.tier_number = 14::numeric THEN tr.t2a_total_cases
                      ELSE 0::bigint
                      END::numeric) AS d2,
          sum(
                  CASE
                      WHEN tr.tier_number = 15::numeric THEN tr.total_filtered_cases
                      ELSE 0::numeric
                      END +
                  CASE
                      WHEN tr.tier_number = 15::numeric THEN tr.t2a_total_cases
                      ELSE 0::bigint
                      END::numeric) AS d1,
          sum(
                  CASE
                      WHEN tr.tier_number = 16::numeric THEN tr.total_filtered_cases
                      ELSE 0::numeric
                      END +
                  CASE
                      WHEN tr.tier_number = 16::numeric THEN tr.t2a_total_cases
                      ELSE 0::bigint
                      END::numeric) AS d0,
          sum(
                  CASE
                      WHEN tr.tier_number = 17::numeric THEN tr.total_filtered_cases
                      ELSE 0::numeric
                      END +
                  CASE
                      WHEN tr.tier_number = 17::numeric THEN tr.t2a_total_cases
                      ELSE 0::bigint
                      END::numeric) AS a3_s,
          sum(
                  CASE
                      WHEN tr.tier_number = 18::numeric THEN tr.total_filtered_cases
                      ELSE 0::numeric
                      END +
                  CASE
                      WHEN tr.tier_number = 18::numeric THEN tr.t2a_total_cases
                      ELSE 0::bigint
                      END::numeric) AS a2_s,
          sum(
                  CASE
                      WHEN tr.tier_number = 19::numeric THEN tr.total_filtered_cases
                      ELSE 0::numeric
                      END +
                  CASE
                      WHEN tr.tier_number = 19::numeric THEN tr.t2a_total_cases
                      ELSE 0::bigint
                      END::numeric) AS a1_s,
          sum(
                  CASE
                      WHEN tr.tier_number = 20::numeric THEN tr.total_filtered_cases
                      ELSE 0::numeric
                      END +
                  CASE
                      WHEN tr.tier_number = 20::numeric THEN tr.t2a_total_cases
                      ELSE 0::bigint
                      END::numeric) AS a0_s,
          sum(
                  CASE
                      WHEN tr.tier_number = 21::numeric THEN tr.total_filtered_cases
                      ELSE 0::numeric
                      END +
                  CASE
                      WHEN tr.tier_number = 21::numeric THEN tr.t2a_total_cases
                      ELSE 0::bigint
                      END::numeric) AS b3_s,
          sum(
                  CASE
                      WHEN tr.tier_number = 22::numeric THEN tr.total_filtered_cases
                      ELSE 0::numeric
                      END +
                  CASE
                      WHEN tr.tier_number = 22::numeric THEN tr.t2a_total_cases
                      ELSE 0::bigint
                      END::numeric) AS b2_s,
          sum(
                  CASE
                      WHEN tr.tier_number = 23::numeric THEN tr.total_filtered_cases
                      ELSE 0::numeric
                      END +
                  CASE
                      WHEN tr.tier_number = 23::numeric THEN tr.t2a_total_cases
                      ELSE 0::bigint
                      END::numeric) AS b1_s,
          sum(
                  CASE
                      WHEN tr.tier_number = 24::numeric THEN tr.total_filtered_cases
                      ELSE 0::numeric
                      END +
                  CASE
                      WHEN tr.tier_number = 24::numeric THEN tr.t2a_total_cases
                      ELSE 0::bigint
                      END::numeric) AS b0_s,
          sum(
                  CASE
                      WHEN tr.tier_number = 25::numeric THEN tr.total_filtered_cases
                      ELSE 0::numeric
                      END +
                  CASE
                      WHEN tr.tier_number = 25::numeric THEN tr.t2a_total_cases
                      ELSE 0::bigint
                      END::numeric) AS c3_s,
          sum(
                  CASE
                      WHEN tr.tier_number = 26::numeric THEN tr.total_filtered_cases
                      ELSE 0::numeric
                      END +
                  CASE
                      WHEN tr.tier_number = 26::numeric THEN tr.t2a_total_cases
                      ELSE 0::bigint
                      END::numeric) AS c2_s,
          sum(
                  CASE
                      WHEN tr.tier_number = 27::numeric THEN tr.total_filtered_cases
                      ELSE 0::numeric
                      END +
                  CASE
                      WHEN tr.tier_number = 27::numeric THEN tr.t2a_total_cases
                      ELSE 0::bigint
                      END::numeric) AS c1_s,
          sum(
                  CASE
                      WHEN tr.tier_number = 28::numeric THEN tr.total_filtered_cases
                      ELSE 0::numeric
                      END +
                  CASE
                      WHEN tr.tier_number = 28::numeric THEN tr.t2a_total_cases
                      ELSE 0::bigint
                      END::numeric) AS c0_s,
          sum(
                  CASE
                      WHEN tr.tier_number = 29::numeric THEN tr.total_filtered_cases
                      ELSE 0::numeric
                      END +
                  CASE
                      WHEN tr.tier_number = 29::numeric THEN tr.t2a_total_cases
                      ELSE 0::bigint
                      END::numeric) AS d3_s,
          sum(
                  CASE
                      WHEN tr.tier_number = 30::numeric THEN tr.total_filtered_cases
                      ELSE 0::numeric
                      END +
                  CASE
                      WHEN tr.tier_number = 30::numeric THEN tr.t2a_total_cases
                      ELSE 0::bigint
                      END::numeric) AS d2_s,
          sum(
                  CASE
                      WHEN tr.tier_number = 31::numeric THEN tr.total_filtered_cases
                      ELSE 0::numeric
                      END +
                  CASE
                      WHEN tr.tier_number = 31::numeric THEN tr.t2a_total_cases
                      ELSE 0::bigint
                      END::numeric) AS d1_s,
          sum(
                  CASE
                      WHEN tr.tier_number = 32::numeric THEN tr.total_filtered_cases
                      ELSE 0::numeric
                      END +
                  CASE
                      WHEN tr.tier_number = 32::numeric THEN tr.t2a_total_cases
                      ELSE 0::bigint
                      END::numeric) AS d0_s,
          sum(tr.total_filtered_cases + tr.t2a_total_cases::numeric) AS total_cases,
          count(*) AS count
   FROM app.tiers tr
            JOIN app.workload w ON tr.workload_id = w.id
            JOIN app.workload_points_calculations wpc ON wpc.workload_id = w.id
            JOIN app.workload_report wr ON wr.id = wpc.workload_report_id
            JOIN app.workload_owner wo ON wo.id = w.workload_owner_id
            JOIN app.offender_manager om ON om.id = wo.offender_manager_id
            JOIN app.offender_manager_type omt ON omt.id = om.type_id
   WHERE wr.effective_from IS NOT NULL AND wr.effective_to IS NULL
   GROUP BY wo.team_id, wo.id, om.forename, om.surname, omt.grade_code, tr.location;
