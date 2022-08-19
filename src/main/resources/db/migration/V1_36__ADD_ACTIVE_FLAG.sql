
ALTER TABLE PERSON_MANAGER ADD COLUMN IS_ACTIVE BOOLEAN NOT NULL DEFAULT TRUE;
ALTER TABLE EVENT_MANAGER ADD COLUMN IS_ACTIVE BOOLEAN NOT NULL DEFAULT TRUE;
ALTER TABLE REQUIREMENT_MANAGER ADD COLUMN IS_ACTIVE BOOLEAN NOT NULL DEFAULT TRUE;


update PERSON_MANAGER set IS_ACTIVE=FALSE WHERE ID NOT IN (SELECT DISTINCT ON (CRN) ID
                                                           FROM PERSON_MANAGER pm
                                                           ORDER BY CRN, CREATED_DATE DESC );

update EVENT_MANAGER set IS_ACTIVE=false where ID not in (SELECT DISTINCT ON (EVENT_ID) ID
                                                           FROM EVENT_MANAGER EM
                                                           ORDER BY EVENT_ID, CREATED_DATE DESC );

update REQUIREMENT_MANAGER set IS_ACTIVE=false where ID not in (SELECT DISTINCT ON (REQUIREMENT_ID) ID
                                                           FROM REQUIREMENT_MANAGER EM
                                                           ORDER BY REQUIREMENT_ID, CREATED_DATE DESC );