package uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity

import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.mapping.OffenderManagerCase
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.mapping.OffenderManagerCaseloadTotals
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.mapping.OffenderManagerOverview
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.ColumnResult
import javax.persistence.ConstructorResult
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.NamedNativeQuery
import javax.persistence.SqlResultSetMapping
import javax.persistence.Table

@SqlResultSetMapping(
  name = "OffenderManagerOverviewResult",
  classes = [
    ConstructorResult(
      targetClass = OffenderManagerOverview::class,
      columns = [
        ColumnResult(name = "forename"),
        ColumnResult(name = "surname"),
        ColumnResult(name = "grade_code"),
        ColumnResult(name = "total_community_cases", type = Int::class),
        ColumnResult(name = "total_filtered_custody_cases", type = Int::class),
        ColumnResult(name = "available_points"),
        ColumnResult(name = "total_points"),
        ColumnResult(name = "key"),
        ColumnResult(name = "description"),
        ColumnResult(name = "last_updated_on", type = LocalDateTime::class),
        ColumnResult(name = "workload_owner_id", type = Long::class),
        ColumnResult(name = "paroms_due_next_30_days")
      ]
    )
  ]
)
@SqlResultSetMapping(
  name = "OffenderManagerCaseloadTotals",
  classes = [
    ConstructorResult(
      targetClass = OffenderManagerCaseloadTotals::class,
      columns = [
        ColumnResult(name = "location"),
        ColumnResult(name = "untiered"),
        ColumnResult(name = "a3"),
        ColumnResult(name = "a2"),
        ColumnResult(name = "a1"),
        ColumnResult(name = "a0"),
        ColumnResult(name = "b3"),
        ColumnResult(name = "b2"),
        ColumnResult(name = "b1"),
        ColumnResult(name = "b0"),
        ColumnResult(name = "c3"),
        ColumnResult(name = "c2"),
        ColumnResult(name = "c1"),
        ColumnResult(name = "c0"),
        ColumnResult(name = "d3"),
        ColumnResult(name = "d2"),
        ColumnResult(name = "d1"),
        ColumnResult(name = "d0"),
      ]
    )
  ]
)
@SqlResultSetMapping(
  name = "OffenderManagerCases",
  classes = [
    ConstructorResult(
      targetClass = OffenderManagerCase::class,
      columns = [
        ColumnResult(name = "crn"),
        ColumnResult(name = "tier"),
        ColumnResult(name = "casecategory")
      ]
    )
  ]
)
@NamedNativeQuery(
  name = "OffenderManagerEntity.findByOverview",
  resultSetMapping = "OffenderManagerOverviewResult",
  query = """SELECT
    om.forename,om.surname, om_type.grade_code AS grade_code, (w.total_filtered_community_cases + w.total_filtered_license_cases) as total_community_cases, w.total_filtered_custody_cases , wpc.available_points AS available_points, wpc.total_points AS total_points, om."key", t.description, wpc.last_updated_on, wo.id as workload_owner_id, w.paroms_due_next_30_days
    FROM app.workload_owner AS wo
    JOIN app.team AS t
        ON wo.team_id = t.id
    JOIN app.workload AS w
        ON wo.id = w.workload_owner_id
    JOIN app.workload_points_calculations AS wpc
        ON wpc.workload_id = w.id
    JOIN app.workload_report AS wr
        ON wr.id = wpc.workload_report_id
    JOIN app.offender_manager AS om
        ON om.id = wo.offender_manager_id
    JOIN app.offender_manager_type AS om_type
        ON om_type.id = om.type_id
    WHERE wr.effective_from IS NOT NULL AND wr.effective_to IS NULL AND t.code = ?1 AND om."key" = ?2"""
)
@NamedNativeQuery(
  name = "OffenderManagerEntity.findByCaseloadTotals",
  resultSetMapping = "OffenderManagerCaseloadTotals",
  query = """
  SELECT location, untiered, a3, a2, a1, a0, b3, b2, b1, b0, c3, c2, c1, c0, d3, d2, d1, d0
  FROM app.team_caseload_view
  WHERE link_id = ?1
"""
)
@NamedNativeQuery(
  name = "OffenderManagerEntity.findCasesByTeamCodeAndStaffCode",
  resultSetMapping = "OffenderManagerCases",
  query = """
  SELECT
   c.case_ref_no AS crn, cc.category_name AS tier, c."location" AS casecategory
    FROM app.case_details AS c
    JOIN app.workload AS w
        ON c.workload_id = w.id
    JOIN app.workload_owner AS wo
        ON w.workload_owner_id = wo.id
    JOIN app.offender_manager AS om
        ON wo.offender_manager_id = om.id
    JOIN app.team AS t
        ON wo.team_id = t.id
    JOIN app.workload_report AS wr
        ON w.workload_report_id = wr.id
    JOIN app.case_category AS cc
        ON c.tier_code = cc.category_id
    WHERE wr.effective_from IS NOT NULL AND wr.effective_to IS null and c.row_type = 'N' and t.code = ?2 and om."key" = ?1
"""
)
@NamedNativeQuery(
  name = "OffenderManagerEntity.findCaseByTeamCodeAndStaffCodeAndCrn",
  resultSetMapping = "OffenderManagerCases",
  query = """
  SELECT
   c.case_ref_no AS crn, cc.category_name AS tier, c."location" AS casecategory
    FROM app.case_details AS c
    JOIN app.workload AS w
        ON c.workload_id = w.id
    JOIN app.workload_owner AS wo
        ON w.workload_owner_id = wo.id
    JOIN app.offender_manager AS om
        ON wo.offender_manager_id = om.id
    JOIN app.team AS t
        ON wo.team_id = t.id
    JOIN app.workload_report AS wr
        ON w.workload_report_id = wr.id
    JOIN app.case_category AS cc
        ON c.tier_code = cc.category_id
    WHERE wr.effective_from IS NOT NULL AND wr.effective_to IS null and c.row_type = 'N' and t.code = ?1 and om."key" = ?2 and c.case_ref_no = ?3
"""
)
@Entity
@Table(name = "offender_manager", schema = "app")
data class OffenderManagerEntity(
  @Id
  @Column
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  val id: Long? = null,

  @Column(name = "key")
  val code: String,

  @Column
  val forename: String,

  @Column
  val surname: String
)
