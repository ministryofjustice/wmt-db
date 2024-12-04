CREATE INDEX idx_event_manager_created_date_team_code_is_active
    ON public.event_manager (created_date, team_code, is_active);