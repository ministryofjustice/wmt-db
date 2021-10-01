ALTER TABLE "app"."adjustments" DROP CONSTRAINT "adjustments_reason_id_foreign_07092021";
ALTER TABLE "app"."case_details" DROP CONSTRAINT "case_details_workload_id_foreign_1613248802";
ALTER TABLE "app"."court_reports" DROP CONSTRAINT "court_reports_workload_owner_id_foreign_557245040";
ALTER TABLE "app"."court_reports_calculations" DROP CONSTRAINT "court_reports_calculations_court_reports_id_foreign_621245268";
ALTER TABLE "app"."court_reports_calculations" DROP CONSTRAINT "court_reports_calculations_workload_points_id_foreign_637245325";
ALTER TABLE "app"."court_reports_calculations" DROP CONSTRAINT "court_reports_calculations_workload_report_id_foreign_653245382";
ALTER TABLE "app"."ldu" DROP CONSTRAINT "ldu_region_id_foreign_109243444";
ALTER TABLE "app"."offender_manager" DROP CONSTRAINT "offender_manager_type_id_foreign_173243672";
ALTER TABLE "app"."omic_tiers" DROP CONSTRAINT "fk__omic_tier__omic___4f5e6b2a_1821249543";
ALTER TABLE "app"."omic_workload" DROP CONSTRAINT "fk__omic_work__workl__42f89445_861246123";
ALTER TABLE "app"."omic_workload_points_calculations" DROP CONSTRAINT "fk__omic_work__omic___5dac8a81_941246408";
ALTER TABLE "app"."omic_workload_points_calculations" DROP CONSTRAINT "fk__omic_work__t2a_w__5f94d2f3_957246465";
ALTER TABLE "app"."omic_workload_points_calculations" DROP CONSTRAINT "fk__omic_work__workl__5bc4420f_973246522";
ALTER TABLE "app"."omic_workload_points_calculations" DROP CONSTRAINT "fk__omic_work__workl__5cb86648_989246579";
ALTER TABLE "app"."reduction_reason" DROP CONSTRAINT "reduction_reason_category_id_foreign_237243900";
ALTER TABLE "app"."reductions" DROP CONSTRAINT "reductions_reduction_reason_id_foreign_1069246864";
ALTER TABLE "app"."reductions" DROP CONSTRAINT "reductions_workload_owner_id_foreign_1085246921";
ALTER TABLE "app"."team" DROP CONSTRAINT "team_ldu_id_foreign_365244356";
ALTER TABLE "app"."tiers" DROP CONSTRAINT "tiers_workload_id_foreign_1981250113";
ALTER TABLE "app"."workload" DROP CONSTRAINT "workload_workload_owner_id_foreign_1389248004";
ALTER TABLE "app"."workload_owner" DROP CONSTRAINT "workload_owner_offender_manager_id_foreign_493244812";
ALTER TABLE "app"."workload_owner" DROP CONSTRAINT "workload_owner_team_id_foreign_509244869";
ALTER TABLE "app"."workload_points_calculations" DROP CONSTRAINT "workload_points_calculations_t2a_workload_points_id_foreign_151";
ALTER TABLE "app"."workload_points_calculations" DROP CONSTRAINT "workload_points_calculations_workload_id_foreign_1533248517";
ALTER TABLE "app"."workload_points_calculations" DROP CONSTRAINT "workload_points_calculations_workload_points_id_foreign_1549248";
ALTER TABLE "app"."workload_points_calculations" DROP CONSTRAINT "workload_points_calculations_workload_report_id_foreign_1565248";
ALTER TABLE "app"."omic_case_details" DROP CONSTRAINT "fk__omic_case__omic___58e7d564_1661248973";
ALTER TABLE "app"."reductions_history" DROP CONSTRAINT "fk__reduction__reduc__2acc04f9_1165247206";
ALTER TABLE "app"."reductions_history" DROP CONSTRAINT "fk__reduction__reduc__2cb44d6b_1181247263";
ALTER TABLE "app"."tasks" DROP CONSTRAINT "tasks_workload_report_id_foreign_301244128";
ALTER TABLE "app"."user_role" DROP CONSTRAINT "user_role_role_id_foreign_429244584";
ALTER TABLE "app"."user_role" DROP CONSTRAINT "user_role_user_id_foreign_445244641";