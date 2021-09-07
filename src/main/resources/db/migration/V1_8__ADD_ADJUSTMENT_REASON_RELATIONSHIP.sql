ALTER TABLE app.adjustments
ADD CONSTRAINT adjustments_reason_id_foreign_07092021 FOREIGN KEY (adjustment_reason_id)
REFERENCES app.adjustment_reason (id)
ON UPDATE NO ACTION
ON DELETE NO ACTION;