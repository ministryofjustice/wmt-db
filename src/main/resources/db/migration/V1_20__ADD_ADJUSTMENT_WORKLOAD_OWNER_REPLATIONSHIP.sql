ALTER TABLE app.adjustments
ADD CONSTRAINT adjustments_workload_owner_id_foreign_06012022 FOREIGN KEY (workload_owner_id)
REFERENCES app.workload_owner (id)
ON UPDATE NO ACTION
ON DELETE NO ACTION;