SET IDENTITY_INSERT app.reduction_category ON;

INSERT INTO app.reduction_category (id, category) VALUES(1, 'Personal Circumstances'),
(2, 'Community Justice Learning'),
(3, 'Work Circumstances');

SET IDENTITY_INSERT app.reduction_category OFF;

SET IDENTITY_INSERT app.reduction_reason ON;

INSERT INTO app.reduction_reason (id, reason, reason_short_name, category_id, allowance_percentage, max_allowance_percentage, months_to_expiry) VALUES(1, 'Disability', 'Disability', 1, null, null, null),
(2, 'Long Term Sickness Absence', 'Long Term Sickness Absence', 1, 100, null, null),
(3, 'Phased Return to Work', 'Phased Return to Work', 1 , null, null, null),
(4, 'Pregnancy', 'Pregnancy', 1, null, null, null),
(5, 'Maternity Leave', 'Maternity Leave', 1, 100, null, 6),
(6, 'Adoption Leave', 'Adoption Leave', 1, 100, null, 6),
(7, 'Special Leave', 'Special Leave', 1, 100, null, null),
(8, 'Trade Union Facility Time', 'Trade Union Facility Time', 1, null, 50, null),
(9, 'Other Paid Leave (e.g. Jury Service)', 'Other Paid Leave (e.g. Jury Service)', 1, 100, null, null),
(10, 'Other Unpaid Leave', 'Other Unpaid Leave', 1, 100, null, null),
(11, 'Other', 'Other', 1, null, null, null),
(12, 'Probation Qualification Framework/Professional Qualification in Probation - 1st 6 months', 'PQiP - 1st 6 months', 2, 80, null, 6),
(13, 'Probation Qualification Framework/Professional Qualification in Probation - 6 to 12 months', 'PQiP - 6 to 12 months', 2, 60, null, 6),
(14, 'Probation Qualification Framework/Professional Qualification in Probation - 12 to 18 months', 'PQiP - 12 to 18 months', 2, 40, null, 6),
(15, 'Newly Qualified Probation Officers', 'NQO', 2, 20, null, 9),
(16, 'PSO Learning & Development', 'PSO Learning & Development', 2, 20, null, 6),
(17, 'Vocational Qualification Level 3 (VQ3)', 'VQ3', 2, 5, null, 6),
(18, 'Level 4 and Level 5 Access', 'Level 4 and Level 5 Access', 2, 10, null, 6),
(19, 'Co-Worked Cases', 'Co-Worked Cases', 3, null, null, null),
(20, 'Groups (Group Supervision)', 'Groups (Group Supervision)', 3, null, null, null),
(21, 'Groups (Induction)', 'Groups (Induction)', 3, null, null, null),
(22, 'Court Duty', 'Court Duty', 3, null, null, null),
(23, 'Pre Sentence Reports – Court Duty / Sessional', 'Pre Sentence Reports – Court Duty / Sessional', 3, null, null, null),
(24, 'Split Role', 'Split Role', 3, null, null, null),
(25, 'OM in Custody - Transactional work', 'OM Custody', 3, null, null, null),
(26, 'Single Point of Contact (SPOC) leads', 'SPOC lead', 3, null, 5, null)

SET IDENTITY_INSERT app.reduction_reason OFF;

SET IDENTITY_INSERT app.roles ON;

INSERT INTO app.roles (id, role) VALUES (1, 'Manager'),
(2, 'System Admin'),
(3, 'Data Admin')

SET IDENTITY_INSERT app.roles OFF;

SET IDENTITY_INSERT app.adjustment_category ON;

INSERT INTO app.adjustment_category (id, category) VALUES (1, 'Case Management Support'),
(2, 'Group Supervision')

SET IDENTITY_INSERT app.adjustment_category OFF;

SET IDENTITY_INSERT app.adjustment_reason ON;

INSERT INTO app.adjustment_reason (id, contact_code, contact_description, category_id, points) VALUES (1, 'CMS01', 'CMS - Sentence Plan Intervention Delivery - Low', 1, 9),
(2, 'CMS02', 'CMS - Sentence Plan Intervention Delivery - Medium', 1, 13),
(3, 'CMS03', 'CMS - Sentence Plan Intervention Delivery - High', 1, 18),
(4, 'CMS04', 'CMS - Completing & Assisting with Referrals - Low', 1, 9),
(5, 'CMS05', 'CMS - Completing & Assisting with Referrals - Medium', 1, 18),
(6, 'CMS06', 'CMS - Completing & Assisting with Referrals - High', 1, 26),
(7, 'CMS07', 'CMS - Attending Partnership Meetings - Low', 1, 9),
(8, 'CMS08', 'CMS - Attending Partnership Meetings - Medium', 1, 18),
(9, 'CMS09', 'CMS - Attending Partnership Meetings - High', 1, 26),
(10, 'CMS10', 'CMS - Assistance with Case Conferencing - Low', 1, 9),
(11, 'CMS11', 'CMS - Assistance with Case Conferencing - Medium', 1, 18),
(12, 'CMS12', 'CMS - Assistance with Case Conferencing - High', 1, 26),
(13, 'CMS13', 'CMS - Information & Intelligence Gathering - Low', 1, 9),
(14, 'CMS14', 'CMS - Information & Intelligence Gathering - Medium', 1, 13),
(15, 'CMS15', 'CMS - Information & Intelligence Gathering - High', 1, 18),
(16, 'CMS16', 'CMS - Home & Prison Visits - Low', 1, 9),
(17, 'CMS17', 'CMS - Home & Prison Visits - Medium', 1, 13),
(18, 'CMS18', 'CMS - Home & Prison Visits - High', 1, 18),
(19, 'CMS19', 'CMS - HDC Assessments & Support - Low', 1, 9),
(20, 'CMS20', 'CMS - HDC Assessments & Support - Medium', 1, 13),
(21, 'CMS21', 'CMS - HDC Assessments & Support - High', 1, 18),
(22, 'CMS22', 'CMS - Victims Services Liaison - Low', 1, 5),
(23, 'CMS23', 'CMS - Victims Services Liaison - Medium', 1, 9),
(24, 'CMS24', 'CMS - Victims Services Liaison - High', 1, 13),
(25, 'CMS25', 'CMS - Court Liaison & Applications to Court - Low', 1, 9),
(26, 'CMS26', 'CMS - Court Liaison & Applications to Court - Medium', 1, 13),
(27, 'CMS27', 'CMS - Court Liaison & Applications to Court - High', 1, 18),
(28, 'CMS28', 'CMS - Case Related Communication - Low', 1, 5),
(29, 'CMS29', 'CMS - Case Related Communication - Medium', 1, 9),
(30, 'CMS30', 'CMS - Case Related Communication - High', 1, 18),
(31, 'CMS31', 'CMS - Assistance with Assessments - Low', 1, 9),
(32, 'CMS32', 'CMS - Assistance with Assessments - Medium', 1, 13),
(33, 'CMS33', 'CMS - Assistance with Assessments - High', 1, 18),
(34, 'CMS34', 'CMS - ROTL Assessments & Support - Low', 1, 9),
(35, 'CMS35', 'CMS - ROTL Assessments & Support - Medium', 1, 13),
(36, 'CMS36', 'CMS - ROTL Assessments & Support - High', 1, 18),
(37, 'NGS004', 'GS Being Social session NS', 2, 0),
(38, 'NGS002', 'GS Community session NS', 2, 0),
(39, 'NGS009', 'GS Dear Me session NS', 2, 0),
(40, 'NGS005', 'GS Disclosure session NS', 2, 0),
(41, 'NGS006', 'GS Employment session NS', 2, 0),
(42, 'NGS008', 'GS Finances session NS', 2, 0),
(43, 'NGS003', 'GS Identity session NS', 2, 0),
(44, 'NGS007', 'GS Keeping Accommodation session NS', 2, 0),
(45, 'NGS010', 'GS Moving On session NS', 2, 0),
(46, 'NGS001', 'GS Rights and Responsibilities session NS', 2, 0)

SET IDENTITY_INSERT app.adjustment_reason OFF;

SET IDENTITY_INSERT app.workload_points ON;

INSERT INTO app.workload_points (id,comm_tier_1,comm_tier_2,comm_tier_3,comm_tier_4,comm_tier_5,comm_tier_6,comm_tier_7,comm_tier_8,comm_tier_9,comm_tier_10,comm_tier_11,comm_tier_12,comm_tier_13,comm_tier_14,comm_tier_15,comm_tier_16,cust_tier_1,cust_tier_2,cust_tier_3,cust_tier_4,cust_tier_5,cust_tier_6,cust_tier_7,cust_tier_8,cust_tier_9,cust_tier_10,cust_tier_11,cust_tier_12,cust_tier_13,cust_tier_14,cust_tier_15,cust_tier_16,lic_tier_1,lic_tier_2,lic_tier_3,lic_tier_4,lic_tier_5,lic_tier_6,lic_tier_7,lic_tier_8,lic_tier_9,lic_tier_10,lic_tier_11,lic_tier_12,lic_tier_13,lic_tier_14,lic_tier_15,lic_tier_16,user_id,sdr,sdr_conversion,nominal_target_spo,nominal_target_po,default_contracted_hours_po,default_contracted_hours_pso,weighting_o,weighting_w,weighting_u,weighting_arms_lic,weighting_arms_comm,paroms_enabled,parom,is_t2a,default_contracted_hours_spo) VALUES
(1,206,158,146,110,146,115,102,72,79,63,50,35,51,41,29,29,75,60,59,0,59,48,47,0,30,24,23,0,17,14,13,0,219,175,163,0,161,132,119,0,77,65,52,0,51,43,31,0,0,138,65,2176,2176,37,37,0,0,100,35,69,1,120,0,0),
(2,75,60,59,0,59,48,47,0,30,24,23,0,17,14,13,0,75,60,59,0,59,48,47,0,30,24,23,0,17,14,13,0,75,60,59,0,59,48,47,0,30,24,23,0,17,14,13,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0);

SET IDENTITY_INSERT app.workload_points OFF;