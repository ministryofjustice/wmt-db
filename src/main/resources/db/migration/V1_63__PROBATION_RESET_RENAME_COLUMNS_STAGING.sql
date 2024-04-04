/*rename required due to NART extract changing column names*/

/* wmt_extract */
ALTER TABLE staging.wmt_extract RENAME COLUMN commtiera0_s TO commtiera0s;
ALTER TABLE staging.wmt_extract RENAME COLUMN commtiera1_s TO commtiera1s;
ALTER TABLE staging.wmt_extract RENAME COLUMN commtiera2_s TO commtiera2s;
ALTER TABLE staging.wmt_extract RENAME COLUMN commtiera3_s TO commtiera3s;
ALTER TABLE staging.wmt_extract RENAME COLUMN commtierb0_s TO commtierb0s;
ALTER TABLE staging.wmt_extract RENAME COLUMN commtierb1_s TO commtierb1s;
ALTER TABLE staging.wmt_extract RENAME COLUMN commtierb2_s TO commtierb2s;
ALTER TABLE staging.wmt_extract RENAME COLUMN commtierb3_s TO commtierb3s;
ALTER TABLE staging.wmt_extract RENAME COLUMN commtierc0_s TO commtierc0s;
ALTER TABLE staging.wmt_extract RENAME COLUMN commtierc1_s TO commtierc1s;
ALTER TABLE staging.wmt_extract RENAME COLUMN commtierc2_s TO commtierc2s;
ALTER TABLE staging.wmt_extract RENAME COLUMN commtierc3_s TO commtierc3s;
ALTER TABLE staging.wmt_extract RENAME COLUMN commtierd0_s TO commtierd0s;
ALTER TABLE staging.wmt_extract RENAME COLUMN commtierd1_s TO commtierd1s;
ALTER TABLE staging.wmt_extract RENAME COLUMN commtierd2_s TO commtierd2s;
ALTER TABLE staging.wmt_extract RENAME COLUMN commtierd3_s TO commtierd3s;

ALTER TABLE staging.wmt_extract RENAME COLUMN licencetiera0_s TO licencetiera0s;
ALTER TABLE staging.wmt_extract RENAME COLUMN licencetiera1_s TO licencetiera1s;
ALTER TABLE staging.wmt_extract RENAME COLUMN licencetiera2_s TO licencetiera2s;
ALTER TABLE staging.wmt_extract RENAME COLUMN licencetiera3_s TO licencetiera3s;
ALTER TABLE staging.wmt_extract RENAME COLUMN licencetierb0_s TO licencetierb0s;
ALTER TABLE staging.wmt_extract RENAME COLUMN licencetierb1_s TO licencetierb1s;
ALTER TABLE staging.wmt_extract RENAME COLUMN licencetierb2_s TO licencetierb2s;
ALTER TABLE staging.wmt_extract RENAME COLUMN licencetierb3_s TO licencetierb3s;
ALTER TABLE staging.wmt_extract RENAME COLUMN licencetierc0_s TO licencetierc0s;
ALTER TABLE staging.wmt_extract RENAME COLUMN licencetierc1_s TO licencetierc1s;
ALTER TABLE staging.wmt_extract RENAME COLUMN licencetierc2_s TO licencetierc2s;
ALTER TABLE staging.wmt_extract RENAME COLUMN licencetierc3_s TO licencetierc3s;
ALTER TABLE staging.wmt_extract RENAME COLUMN licencetierd0_s TO licencetierd0s;
ALTER TABLE staging.wmt_extract RENAME COLUMN licencetierd1_s TO licencetierd1s;
ALTER TABLE staging.wmt_extract RENAME COLUMN licencetierd2_s TO licencetierd2s;
ALTER TABLE staging.wmt_extract RENAME COLUMN licencetierd3_s TO licencetierd3s;

ALTER TABLE staging.wmt_extract RENAME COLUMN custtiera0_s TO custtiera0s;
ALTER TABLE staging.wmt_extract RENAME COLUMN custtiera1_s TO custtiera1s;
ALTER TABLE staging.wmt_extract RENAME COLUMN custtiera2_s TO custtiera2s;
ALTER TABLE staging.wmt_extract RENAME COLUMN custtiera3_s TO custtiera3s;
ALTER TABLE staging.wmt_extract RENAME COLUMN custtierb0_s TO custtierb0s;
ALTER TABLE staging.wmt_extract RENAME COLUMN custtierb1_s TO custtierb1s;
ALTER TABLE staging.wmt_extract RENAME COLUMN custtierb2_s TO custtierb2s;
ALTER TABLE staging.wmt_extract RENAME COLUMN custtierb3_s TO custtierb3s;
ALTER TABLE staging.wmt_extract RENAME COLUMN custtierc0_s TO custtierc0s;
ALTER TABLE staging.wmt_extract RENAME COLUMN custtierc1_s TO custtierc1s;
ALTER TABLE staging.wmt_extract RENAME COLUMN custtierc2_s TO custtierc2s;
ALTER TABLE staging.wmt_extract RENAME COLUMN custtierc3_s TO custtierc3s;
ALTER TABLE staging.wmt_extract RENAME COLUMN custtierd0_s TO custtierd0s;
ALTER TABLE staging.wmt_extract RENAME COLUMN custtierd1_s TO custtierd1s;
ALTER TABLE staging.wmt_extract RENAME COLUMN custtierd2_s TO custtierd2s;
ALTER TABLE staging.wmt_extract RENAME COLUMN custtierd3_s TO custtierd3s;

/* WMT_FILTERED */

ALTER TABLE staging.wmt_extract_filtered RENAME COLUMN commtiera0_s TO commtiera0s;
ALTER TABLE staging.wmt_extract_filtered RENAME COLUMN commtiera1_s TO commtiera1s;
ALTER TABLE staging.wmt_extract_filtered RENAME COLUMN commtiera2_s TO commtiera2s;
ALTER TABLE staging.wmt_extract_filtered RENAME COLUMN commtiera3_s TO commtiera3s;
ALTER TABLE staging.wmt_extract_filtered RENAME COLUMN commtierb0_s TO commtierb0s;
ALTER TABLE staging.wmt_extract_filtered RENAME COLUMN commtierb1_s TO commtierb1s;
ALTER TABLE staging.wmt_extract_filtered RENAME COLUMN commtierb2_s TO commtierb2s;
ALTER TABLE staging.wmt_extract_filtered RENAME COLUMN commtierb3_s TO commtierb3s;
ALTER TABLE staging.wmt_extract_filtered RENAME COLUMN commtierc0_s TO commtierc0s;
ALTER TABLE staging.wmt_extract_filtered RENAME COLUMN commtierc1_s TO commtierc1s;
ALTER TABLE staging.wmt_extract_filtered RENAME COLUMN commtierc2_s TO commtierc2s;
ALTER TABLE staging.wmt_extract_filtered RENAME COLUMN commtierc3_s TO commtierc3s;
ALTER TABLE staging.wmt_extract_filtered RENAME COLUMN commtierd0_s TO commtierd0s;
ALTER TABLE staging.wmt_extract_filtered RENAME COLUMN commtierd1_s TO commtierd1s;
ALTER TABLE staging.wmt_extract_filtered RENAME COLUMN commtierd2_s TO commtierd2s;
ALTER TABLE staging.wmt_extract_filtered RENAME COLUMN commtierd3_s TO commtierd3s;

ALTER TABLE staging.wmt_extract_filtered RENAME COLUMN licencetiera0_s TO licencetiera0s;
ALTER TABLE staging.wmt_extract_filtered RENAME COLUMN licencetiera1_s TO licencetiera1s;
ALTER TABLE staging.wmt_extract_filtered RENAME COLUMN licencetiera2_s TO licencetiera2s;
ALTER TABLE staging.wmt_extract_filtered RENAME COLUMN licencetiera3_s TO licencetiera3s;
ALTER TABLE staging.wmt_extract_filtered RENAME COLUMN licencetierb0_s TO licencetierb0s;
ALTER TABLE staging.wmt_extract_filtered RENAME COLUMN licencetierb1_s TO licencetierb1s;
ALTER TABLE staging.wmt_extract_filtered RENAME COLUMN licencetierb2_s TO licencetierb2s;
ALTER TABLE staging.wmt_extract_filtered RENAME COLUMN licencetierb3_s TO licencetierb3s;
ALTER TABLE staging.wmt_extract_filtered RENAME COLUMN licencetierc0_s TO licencetierc0s;
ALTER TABLE staging.wmt_extract_filtered RENAME COLUMN licencetierc1_s TO licencetierc1s;
ALTER TABLE staging.wmt_extract_filtered RENAME COLUMN licencetierc2_s TO licencetierc2s;
ALTER TABLE staging.wmt_extract_filtered RENAME COLUMN licencetierc3_s TO licencetierc3s;
ALTER TABLE staging.wmt_extract_filtered RENAME COLUMN licencetierd0_s TO licencetierd0s;
ALTER TABLE staging.wmt_extract_filtered RENAME COLUMN licencetierd1_s TO licencetierd1s;
ALTER TABLE staging.wmt_extract_filtered RENAME COLUMN licencetierd2_s TO licencetierd2s;
ALTER TABLE staging.wmt_extract_filtered RENAME COLUMN licencetierd3_s TO licencetierd3s;

ALTER TABLE staging.wmt_extract_filtered RENAME COLUMN custtiera0_s TO custtiera0s;
ALTER TABLE staging.wmt_extract_filtered RENAME COLUMN custtiera1_s TO custtiera1s;
ALTER TABLE staging.wmt_extract_filtered RENAME COLUMN custtiera2_s TO custtiera2s;
ALTER TABLE staging.wmt_extract_filtered RENAME COLUMN custtiera3_s TO custtiera3s;
ALTER TABLE staging.wmt_extract_filtered RENAME COLUMN custtierb0_s TO custtierb0s;
ALTER TABLE staging.wmt_extract_filtered RENAME COLUMN custtierb1_s TO custtierb1s;
ALTER TABLE staging.wmt_extract_filtered RENAME COLUMN custtierb2_s TO custtierb2s;
ALTER TABLE staging.wmt_extract_filtered RENAME COLUMN custtierb3_s TO custtierb3s;
ALTER TABLE staging.wmt_extract_filtered RENAME COLUMN custtierc0_s TO custtierc0s;
ALTER TABLE staging.wmt_extract_filtered RENAME COLUMN custtierc1_s TO custtierc1s;
ALTER TABLE staging.wmt_extract_filtered RENAME COLUMN custtierc2_s TO custtierc2s;
ALTER TABLE staging.wmt_extract_filtered RENAME COLUMN custtierc3_s TO custtierc3s;
ALTER TABLE staging.wmt_extract_filtered RENAME COLUMN custtierd0_s TO custtierd0s;
ALTER TABLE staging.wmt_extract_filtered RENAME COLUMN custtierd1_s TO custtierd1s;
ALTER TABLE staging.wmt_extract_filtered RENAME COLUMN custtierd2_s TO custtierd2s;
ALTER TABLE staging.wmt_extract_filtered RENAME COLUMN custtierd3_s TO custtierd3s;

/* t2a*/

ALTER TABLE staging.t2a RENAME COLUMN commtiera0_s TO commtiera0s;
ALTER TABLE staging.t2a RENAME COLUMN commtiera1_s TO commtiera1s;
ALTER TABLE staging.t2a RENAME COLUMN commtiera2_s TO commtiera2s;
ALTER TABLE staging.t2a RENAME COLUMN commtiera3_s TO commtiera3s;
ALTER TABLE staging.t2a RENAME COLUMN commtierb0_s TO commtierb0s;
ALTER TABLE staging.t2a RENAME COLUMN commtierb1_s TO commtierb1s;
ALTER TABLE staging.t2a RENAME COLUMN commtierb2_s TO commtierb2s;
ALTER TABLE staging.t2a RENAME COLUMN commtierb3_s TO commtierb3s;
ALTER TABLE staging.t2a RENAME COLUMN commtierc0_s TO commtierc0s;
ALTER TABLE staging.t2a RENAME COLUMN commtierc1_s TO commtierc1s;
ALTER TABLE staging.t2a RENAME COLUMN commtierc2_s TO commtierc2s;
ALTER TABLE staging.t2a RENAME COLUMN commtierc3_s TO commtierc3s;
ALTER TABLE staging.t2a RENAME COLUMN commtierd0_s TO commtierd0s;
ALTER TABLE staging.t2a RENAME COLUMN commtierd1_s TO commtierd1s;
ALTER TABLE staging.t2a RENAME COLUMN commtierd2_s TO commtierd2s;
ALTER TABLE staging.t2a RENAME COLUMN commtierd3_s TO commtierd3s;

ALTER TABLE staging.t2a RENAME COLUMN licencetiera0_s TO licencetiera0s;
ALTER TABLE staging.t2a RENAME COLUMN licencetiera1_s TO licencetiera1s;
ALTER TABLE staging.t2a RENAME COLUMN licencetiera2_s TO licencetiera2s;
ALTER TABLE staging.t2a RENAME COLUMN licencetiera3_s TO licencetiera3s;
ALTER TABLE staging.t2a RENAME COLUMN licencetierb0_s TO licencetierb0s;
ALTER TABLE staging.t2a RENAME COLUMN licencetierb1_s TO licencetierb1s;
ALTER TABLE staging.t2a RENAME COLUMN licencetierb2_s TO licencetierb2s;
ALTER TABLE staging.t2a RENAME COLUMN licencetierb3_s TO licencetierb3s;
ALTER TABLE staging.t2a RENAME COLUMN licencetierc0_s TO licencetierc0s;
ALTER TABLE staging.t2a RENAME COLUMN licencetierc1_s TO licencetierc1s;
ALTER TABLE staging.t2a RENAME COLUMN licencetierc2_s TO licencetierc2s;
ALTER TABLE staging.t2a RENAME COLUMN licencetierc3_s TO licencetierc3s;
ALTER TABLE staging.t2a RENAME COLUMN licencetierd0_s TO licencetierd0s;
ALTER TABLE staging.t2a RENAME COLUMN licencetierd1_s TO licencetierd1s;
ALTER TABLE staging.t2a RENAME COLUMN licencetierd2_s TO licencetierd2s;
ALTER TABLE staging.t2a RENAME COLUMN licencetierd3_s TO licencetierd3s;

ALTER TABLE staging.t2a RENAME COLUMN custtiera0_s TO custtiera0s;
ALTER TABLE staging.t2a RENAME COLUMN custtiera1_s TO custtiera1s;
ALTER TABLE staging.t2a RENAME COLUMN custtiera2_s TO custtiera2s;
ALTER TABLE staging.t2a RENAME COLUMN custtiera3_s TO custtiera3s;
ALTER TABLE staging.t2a RENAME COLUMN custtierb0_s TO custtierb0s;
ALTER TABLE staging.t2a RENAME COLUMN custtierb1_s TO custtierb1s;
ALTER TABLE staging.t2a RENAME COLUMN custtierb2_s TO custtierb2s;
ALTER TABLE staging.t2a RENAME COLUMN custtierb3_s TO custtierb3s;
ALTER TABLE staging.t2a RENAME COLUMN custtierc0_s TO custtierc0s;
ALTER TABLE staging.t2a RENAME COLUMN custtierc1_s TO custtierc1s;
ALTER TABLE staging.t2a RENAME COLUMN custtierc2_s TO custtierc2s;
ALTER TABLE staging.t2a RENAME COLUMN custtierc3_s TO custtierc3s;
ALTER TABLE staging.t2a RENAME COLUMN custtierd0_s TO custtierd0s;
ALTER TABLE staging.t2a RENAME COLUMN custtierd1_s TO custtierd1s;
ALTER TABLE staging.t2a RENAME COLUMN custtierd2_s TO custtierd2s;
ALTER TABLE staging.t2a RENAME COLUMN custtierd3_s TO custtierd3s;


/* omic_teams */

ALTER TABLE staging.omic_teams RENAME COLUMN commtiera0_s TO commtiera0s;
ALTER TABLE staging.omic_teams RENAME COLUMN commtiera1_s TO commtiera1s;
ALTER TABLE staging.omic_teams RENAME COLUMN commtiera2_s TO commtiera2s;
ALTER TABLE staging.omic_teams RENAME COLUMN commtiera3_s TO commtiera3s;
ALTER TABLE staging.omic_teams RENAME COLUMN commtierb0_s TO commtierb0s;
ALTER TABLE staging.omic_teams RENAME COLUMN commtierb1_s TO commtierb1s;
ALTER TABLE staging.omic_teams RENAME COLUMN commtierb2_s TO commtierb2s;
ALTER TABLE staging.omic_teams RENAME COLUMN commtierb3_s TO commtierb3s;
ALTER TABLE staging.omic_teams RENAME COLUMN commtierc0_s TO commtierc0s;
ALTER TABLE staging.omic_teams RENAME COLUMN commtierc1_s TO commtierc1s;
ALTER TABLE staging.omic_teams RENAME COLUMN commtierc2_s TO commtierc2s;
ALTER TABLE staging.omic_teams RENAME COLUMN commtierc3_s TO commtierc3s;
ALTER TABLE staging.omic_teams RENAME COLUMN commtierd0_s TO commtierd0s;
ALTER TABLE staging.omic_teams RENAME COLUMN commtierd1_s TO commtierd1s;
ALTER TABLE staging.omic_teams RENAME COLUMN commtierd2_s TO commtierd2s;
ALTER TABLE staging.omic_teams RENAME COLUMN commtierd3_s TO commtierd3s;

ALTER TABLE staging.omic_teams RENAME COLUMN licencetiera0_s TO licencetiera0s;
ALTER TABLE staging.omic_teams RENAME COLUMN licencetiera1_s TO licencetiera1s;
ALTER TABLE staging.omic_teams RENAME COLUMN licencetiera2_s TO licencetiera2s;
ALTER TABLE staging.omic_teams RENAME COLUMN licencetiera3_s TO licencetiera3s;
ALTER TABLE staging.omic_teams RENAME COLUMN licencetierb0_s TO licencetierb0s;
ALTER TABLE staging.omic_teams RENAME COLUMN licencetierb1_s TO licencetierb1s;
ALTER TABLE staging.omic_teams RENAME COLUMN licencetierb2_s TO licencetierb2s;
ALTER TABLE staging.omic_teams RENAME COLUMN licencetierb3_s TO licencetierb3s;
ALTER TABLE staging.omic_teams RENAME COLUMN licencetierc0_s TO licencetierc0s;
ALTER TABLE staging.omic_teams RENAME COLUMN licencetierc1_s TO licencetierc1s;
ALTER TABLE staging.omic_teams RENAME COLUMN licencetierc2_s TO licencetierc2s;
ALTER TABLE staging.omic_teams RENAME COLUMN licencetierc3_s TO licencetierc3s;
ALTER TABLE staging.omic_teams RENAME COLUMN licencetierd0_s TO licencetierd0s;
ALTER TABLE staging.omic_teams RENAME COLUMN licencetierd1_s TO licencetierd1s;
ALTER TABLE staging.omic_teams RENAME COLUMN licencetierd2_s TO licencetierd2s;
ALTER TABLE staging.omic_teams RENAME COLUMN licencetierd3_s TO licencetierd3s;


ALTER TABLE staging.omic_teams RENAME COLUMN custtiera0_s TO custtiera0s;
ALTER TABLE staging.omic_teams RENAME COLUMN custtiera1_s TO custtiera1s;
ALTER TABLE staging.omic_teams RENAME COLUMN custtiera2_s TO custtiera2s;
ALTER TABLE staging.omic_teams RENAME COLUMN custtiera3_s TO custtiera3s;
ALTER TABLE staging.omic_teams RENAME COLUMN custtierb0_s TO custtierb0s;
ALTER TABLE staging.omic_teams RENAME COLUMN custtierb1_s TO custtierb1s;
ALTER TABLE staging.omic_teams RENAME COLUMN custtierb2_s TO custtierb2s;
ALTER TABLE staging.omic_teams RENAME COLUMN custtierb3_s TO custtierb3s;
ALTER TABLE staging.omic_teams RENAME COLUMN custtierc0_s TO custtierc0s;
ALTER TABLE staging.omic_teams RENAME COLUMN custtierc1_s TO custtierc1s;
ALTER TABLE staging.omic_teams RENAME COLUMN custtierc2_s TO custtierc2s;
ALTER TABLE staging.omic_teams RENAME COLUMN custtierc3_s TO custtierc3s;
ALTER TABLE staging.omic_teams RENAME COLUMN custtierd0_s TO custtierd0s;
ALTER TABLE staging.omic_teams RENAME COLUMN custtierd1_s TO custtierd1s;
ALTER TABLE staging.omic_teams RENAME COLUMN custtierd2_s TO custtierd2s;
ALTER TABLE staging.omic_teams RENAME COLUMN custtierd3_s TO custtierd3s;
