

alter table event_manager drop column event_id;
ALTER TABLE event_manager ALTER COLUMN event_number SET NOT NULL;


alter table requirement_manager drop column event_id;
ALTER TABLE requirement_manager ALTER COLUMN event_number SET NOT NULL;