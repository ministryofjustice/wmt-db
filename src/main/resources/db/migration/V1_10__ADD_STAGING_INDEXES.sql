CREATE INDEX ix_flag_warr_4_n_ix_flag_warr_4_n_om_key
ON staging.flag_warr_4_n
USING BTREE (om_key ASC) INCLUDE(team_code)
WITH (FILLFACTOR = 100);

CREATE INDEX ix_flag_upw_ix_flag_upw_om_key
ON staging.flag_upw
USING BTREE (om_key ASC) INCLUDE(team_code)
WITH (FILLFACTOR = 100);

CREATE INDEX ix_flag_priority_ix_flag_priority_om_key
ON staging.flag_priority
USING BTREE (om_key ASC) INCLUDE(team_code)
WITH (FILLFACTOR = 100);