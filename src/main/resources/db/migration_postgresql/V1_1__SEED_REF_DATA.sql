
INSERT INTO app.reduction_category (category)
VALUES('Personal Circumstances'),
('Community Justice Learning'),
('Work Circumstances');

INSERT INTO app.reduction_reason (reason, reason_short_name, category_id, allowance_percentage, max_allowance_percentage, months_to_expiry)
VALUES('Disability', 'Disability', 1, null, null, null),
('Long Term Sickness Absence', 'Long Term Sickness Absence', 1, 100, null, null),
('Phased Return to Work', 'Phased Return to Work', 1 , null, null, null),
('Pregnancy', 'Pregnancy', 1, null, null, null),
('Maternity Leave', 'Maternity Leave', 1, 100, null, 6),
('Adoption Leave', 'Adoption Leave', 1, 100, null, 6),
('Special Leave', 'Special Leave', 1, 100, null, null),
('Trade Union Facility Time', 'Trade Union Facility Time', 1, null, 50, null),
('Other Paid Leave (e.g. Jury Service)', 'Other Paid Leave (e.g. Jury Service)', 1, 100, null, null),
( 'Other Unpaid Leave', 'Other Unpaid Leave', 1, 100, null, null),
('Other', 'Other', 1, null, null, null),
('Probation Qualification Framework/Professional Qualification in Probation - 1st 6 months', 'PQiP - 1st 6 months', 2, 80, null, 6),
('Probation Qualification Framework/Professional Qualification in Probation - 6 to 12 months', 'PQiP - 6 to 12 months', 2, 60, null, 6),
('Probation Qualification Framework/Professional Qualification in Probation - 12 to 18 months', 'PQiP - 12 to 18 months', 2, 40, null, 6),
('Newly Qualified Probation Officers', 'NQO', 2, 20, null, 9),
('PSO Learning & Development', 'PSO Learning & Development', 2, 20, null, 6),
('Vocational Qualification Level 3 (VQ3)', 'VQ3', 2, 5, null, 6),
('Level 4 and Level 5 Access', 'Level 4 and Level 5 Access', 2, 10, null, 6),
('Co-Worked Cases', 'Co-Worked Cases', 3, null, null, null),
('Groups (Group Supervision)', 'Groups (Group Supervision)', 3, null, null, null),
('Groups (Induction)', 'Groups (Induction)', 3, null, null, null),
('Court Duty', 'Court Duty', 3, null, null, null),
('Pre Sentence Reports – Court Duty / Sessional', 'Pre Sentence Reports – Court Duty / Sessional', 3, null, null, null),
('Split Role', 'Split Role', 3, null, null, null),
('OM in Custody - Transactional work', 'OM Custody', 3, null, null, null),
('Single Point of Contact (SPOC) leads', 'SPOC lead', 3, null, 5, null);

INSERT INTO app.roles (role)
 VALUES ('Manager'),
('System Admin'),
('Data Admin');

INSERT INTO app.adjustment_category (category)
 VALUES ('Case Management Support'),
('Group Supervision');

INSERT INTO app.adjustment_reason (contact_code, contact_description, category_id, points)
VALUES ('CMS01', 'CMS - Sentence Plan Intervention Delivery - Low', 1, 9),
('CMS02', 'CMS - Sentence Plan Intervention Delivery - Medium', 1, 13),
('CMS03', 'CMS - Sentence Plan Intervention Delivery - High', 1, 18),
('CMS04', 'CMS - Completing & Assisting with Referrals - Low', 1, 9),
('CMS05', 'CMS - Completing & Assisting with Referrals - Medium', 1, 18),
('CMS06', 'CMS - Completing & Assisting with Referrals - High', 1, 26),
('CMS07', 'CMS - Attending Partnership Meetings - Low', 1, 9),
('CMS08', 'CMS - Attending Partnership Meetings - Medium', 1, 18),
('CMS09', 'CMS - Attending Partnership Meetings - High', 1, 26),
('CMS10', 'CMS - Assistance with Case Conferencing - Low', 1, 9),
('CMS11', 'CMS - Assistance with Case Conferencing - Medium', 1, 18),
('CMS12', 'CMS - Assistance with Case Conferencing - High', 1, 26),
('CMS13', 'CMS - Information & Intelligence Gathering - Low', 1, 9),
('CMS14', 'CMS - Information & Intelligence Gathering - Medium', 1, 13),
('CMS15', 'CMS - Information & Intelligence Gathering - High', 1, 18),
('CMS16', 'CMS - Home & Prison Visits - Low', 1, 9),
('CMS17', 'CMS - Home & Prison Visits - Medium', 1, 13),
('CMS18', 'CMS - Home & Prison Visits - High', 1, 18),
('CMS19', 'CMS - HDC Assessments & Support - Low', 1, 9),
('CMS20', 'CMS - HDC Assessments & Support - Medium', 1, 13),
('CMS21', 'CMS - HDC Assessments & Support - High', 1, 18),
('CMS22', 'CMS - Victims Services Liaison - Low', 1, 5),
('CMS23', 'CMS - Victims Services Liaison - Medium', 1, 9),
('CMS24', 'CMS - Victims Services Liaison - High', 1, 13),
('CMS25', 'CMS - Court Liaison & Applications to Court - Low', 1, 9),
('CMS26', 'CMS - Court Liaison & Applications to Court - Medium', 1, 13),
('CMS27', 'CMS - Court Liaison & Applications to Court - High', 1, 18),
('CMS28', 'CMS - Case Related Communication - Low', 1, 5),
('CMS29', 'CMS - Case Related Communication - Medium', 1, 9),
('CMS30', 'CMS - Case Related Communication - High', 1, 18),
('CMS31', 'CMS - Assistance with Assessments - Low', 1, 9),
('CMS32', 'CMS - Assistance with Assessments - Medium', 1, 13),
('CMS33', 'CMS - Assistance with Assessments - High', 1, 18),
('CMS34', 'CMS - ROTL Assessments & Support - Low', 1, 9),
('CMS35', 'CMS - ROTL Assessments & Support - Medium', 1, 13),
('CMS36', 'CMS - ROTL Assessments & Support - High', 1, 18),
('NGS004', 'GS Being Social session NS', 2, 0),
('NGS002', 'GS Community session NS', 2, 0),
('NGS009', 'GS Dear Me session NS', 2, 0),
('NGS005', 'GS Disclosure session NS', 2, 0),
('NGS006', 'GS Employment session NS', 2, 0),
('NGS008', 'GS Finances session NS', 2, 0),
('NGS003', 'GS Identity session NS', 2, 0),
('NGS007', 'GS Keeping Accommodation session NS', 2, 0),
('NGS010', 'GS Moving On session NS', 2, 0),
('NGS001', 'GS Rights and Responsibilities session NS', 2, 0);


INSERT INTO app.workload_points (comm_tier_1,comm_tier_2,comm_tier_3,comm_tier_4,comm_tier_5,comm_tier_6,comm_tier_7,comm_tier_8,comm_tier_9,comm_tier_10,comm_tier_11,comm_tier_12,comm_tier_13,comm_tier_14,comm_tier_15,comm_tier_16,cust_tier_1,cust_tier_2,cust_tier_3,cust_tier_4,cust_tier_5,cust_tier_6,cust_tier_7,cust_tier_8,cust_tier_9,cust_tier_10,cust_tier_11,cust_tier_12,cust_tier_13,cust_tier_14,cust_tier_15,cust_tier_16,lic_tier_1,lic_tier_2,lic_tier_3,lic_tier_4,lic_tier_5,lic_tier_6,lic_tier_7,lic_tier_8,lic_tier_9,lic_tier_10,lic_tier_11,lic_tier_12,lic_tier_13,lic_tier_14,lic_tier_15,lic_tier_16,user_id,sdr,sdr_conversion,nominal_target_spo,nominal_target_po,default_contracted_hours_po,default_contracted_hours_pso,weighting_o,weighting_w,weighting_u,weighting_arms_lic,weighting_arms_comm,paroms_enabled,parom,is_t2a,default_contracted_hours_spo) VALUES
(206,158,146,110,146,115,102,72,79,63,50,35,51,41,29,29,75,60,59,0,59,48,47,0,30,24,23,0,17,14,13,0,219,175,163,0,161,132,119,0,77,65,52,0,51,43,31,0,0,138,65,2176,2176,37,37,0,0,100,35,69,true,120,false,0),
(75,60,59,0,59,48,47,0,30,24,23,0,17,14,13,0,75,60,59,0,59,48,47,0,30,24,23,0,17,14,13,0,75,60,59,0,59,48,47,0,30,24,23,0,17,14,13,0,0,0,0,0,0,0,0,0,0,0,0,0,false,0,true,0);
