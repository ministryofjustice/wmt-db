package uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity

import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.mapping.OffenderManagerOverview
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
        ColumnResult(name = "total_community_cases"),
        ColumnResult(name = "total_filtered_custody_cases"),
        ColumnResult(name = "available_points"),
        ColumnResult(name = "total_points"),
        ColumnResult(name = "key"),
        ColumnResult(name = "description"),
        ColumnResult(name = "reduction_hours"),
        ColumnResult(name = "contracted_hours")
      ]
    )
  ]
)
@NamedNativeQuery(
  name = "OffenderManagerEntity.findByOverview",
  resultSetMapping = "OffenderManagerOverviewResult",
  query = """SELECT
    om.forename,om.surname, om_type.grade_code AS grade_code, (w.total_filtered_community_cases + w.total_filtered_license_cases) as total_community_cases, w.total_filtered_custody_cases , wpc.available_points AS available_points, wpc.total_points AS total_points, om."key", t.description, wpc.reduction_hours, wpc.contracted_hours
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
