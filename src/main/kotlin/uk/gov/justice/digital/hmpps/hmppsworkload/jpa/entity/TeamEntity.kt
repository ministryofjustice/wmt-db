package uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity

import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.mapping.TeamOverview
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EntityResult
import javax.persistence.FieldResult
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.NamedNativeQuery
import javax.persistence.SqlResultSetMapping
import javax.persistence.Table

@SqlResultSetMapping(
  name = "TeamOverviewResult",
  entities = [
    EntityResult(
      entityClass = TeamOverview::class,
      fields = [
        FieldResult(name = "forename", column = "forename"),
        FieldResult(name = "surname", column = "surname"),
        FieldResult(name = "grade", column = "grade_code"),
        FieldResult(name = "totalCommunityCases", column = "total_community_cases"),
        FieldResult(name = "totalCustodyCases", column = "total_filtered_custody_cases"),
        FieldResult(name = "availablePoints", column = "available_points"),
        FieldResult(name = "totalPoints", column = "total_points")
      ]
    )
  ]
)
@NamedNativeQuery(
  name = "TeamEntity.findByOverview",
  query = """SELECT
    om.forename,om.surname, om_type.grade_code AS grade_code, (w.total_filtered_community_cases + w.total_filtered_license_cases) as total_community_cases, w.total_filtered_custody_cases , wpc.available_points AS available_points, wpc.total_points AS total_points
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
    WHERE wr.effective_from IS NOT NULL AND wr.effective_to IS NULL AND t.code = ?1"""
)
@Entity
@Table(name = "team", schema = "app")
data class TeamEntity(
  @Id
  @Column
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  val id: Long? = null,

  @Column
  val code: String,

  @Column
  val description: String,

  @ManyToOne
  @JoinColumn(name = "ldu_id")
  val ldu: LduEntity,
)
