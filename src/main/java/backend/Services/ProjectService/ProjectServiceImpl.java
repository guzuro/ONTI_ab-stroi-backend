package backend.Services.ProjectService;

import java.util.ArrayList;
import java.util.List;

import backend.model.Order.Contract;
import backend.model.Order.Smeta;
import backend.model.Project.Project;
import backend.model.Project.ProjectStep;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import io.vertx.sqlclient.SqlClient;
import io.vertx.sqlclient.Tuple;

public class ProjectServiceImpl implements ProjectService {

	final SqlClient pgClient;

	public ProjectServiceImpl(SqlClient pgClient) {
		this.pgClient = pgClient;
	}

	@Override
	public void getProject(RoutingContext ctx) {

		HttpServerResponse response = ctx.response();
		Number order_id = ctx.getBodyAsJson().getNumber("order_id");

		pgClient.preparedQuery(
				"SELECT id, client_id, prepayment_sum, prepayment_success, order_id, project_end_date FROM db_project WHERE order_id = $1")
				.execute(Tuple.of(order_id), ar -> {
					if (ar.succeeded()) {
						if (ar.result().rowCount() == 0) {
							response.setStatusCode(200).putHeader("content-type", "application/json; charset=UTF-8")
									.end();

						} else {
							Project project = ar.result().iterator().next().toJson().mapTo(Project.class);

							pgClient.preparedQuery(
									"SELECT id, photo, end_date, title, description, step_payed, is_done, project_id, step_cost FROM db_project_step WHERE project_id = $1")
									.execute(Tuple.of(ar.result().iterator().next().toJson().getNumber("id")), res -> {
										if (res.succeeded()) {
											List<ProjectStep> projectSteps = new ArrayList<ProjectStep>();
											RowSet<Row> result = res.result();
											if (result.size() != 0) {
												for (Row row : result) {
													projectSteps.add(
															JsonObject.mapFrom(row.toJson()).mapTo(ProjectStep.class));
												}
											}
											project.setSteps(projectSteps);
											response.setStatusCode(200)
													.putHeader("content-type", "application/json; charset=UTF-8")
													.end(JsonObject.mapFrom(project).encodePrettily());
										} else {
											response.setStatusCode(500)
													.putHeader("content-type", "application/json; charset=UTF-8")
													.end(ar.cause().toString());
										}
									});
						}
					} else {
						response.setStatusCode(500).putHeader("content-type", "application/json; charset=UTF-8")
								.end(ar.cause().toString());
					}
				});
	}

	@Override
	public void addProject(RoutingContext ctx) {
		HttpServerResponse response = ctx.response();

		Project project = ctx.getBodyAsJson().mapTo(Project.class);
		pgClient.preparedQuery("SELECT item_name, unit, quantity, price, item_total FROM db_smeta WHERE order_id = $1")
				.execute(Tuple.of(project.getOrder_id()), res -> {
					if (res.succeeded()) {

						List<Smeta> smetaArr = new ArrayList<Smeta>();
						RowSet<Row> result = res.result();

						if (result.size() != 0) {
							for (Row row : result) {
								smetaArr.add(JsonObject.mapFrom(row.toJson()).mapTo(Smeta.class));
							}
						}

						int sum = 0;
						for (Smeta s : smetaArr)
							sum += s.getItem_total().intValue();

						project.setPrepayment_sum(sum);

						pgClient.preparedQuery(
								"INSERT INTO db_project(order_id, client_id, prepayment_sum, project_end_date) VALUES($1, $2, $3, $4)")
								.execute(Tuple.of(project.getOrder_id(), project.getClient_id(),
										project.getPrepayment_sum(), project.getProject_end_date()), ar -> {
											if (ar.succeeded()) {
												response.setStatusCode(200)
														.putHeader("content-type", "application/json; charset=UTF-8")
														.end();
											} else {
												response.setStatusCode(500)
														.putHeader("content-type", "application/json; charset=UTF-8")
														.end(ar.cause().toString());
											}
										});
					}
				});

	}

	@Override
	public void updateProject(RoutingContext ctx) {
//		HttpServerResponse response = ctx.response();
//
//		Project project = ctx.getBodyAsJson().mapTo(Project.class);
//		pgClient.preparedQuery("UPDATE db_project SET contract_id=$1 WHERE id = $2;")
//				.execute(Tuple.of(res.result().iterator().next().toJson().getNumber("id"), order_id), updRes -> {
//					if (updRes.succeeded()) {
//						Contract contractRes = res.result().iterator().next().toJson().mapTo(Contract.class);
//						response.end(JsonObject.mapFrom(contractRes).toBuffer());
//					} else {
//						response.setStatusCode(500).putHeader("content-type", "application/json; charset=UTF-8")
//								.end(res.cause().toString());
//					}
//				});
	}

	@Override
	public void doPrePayment(RoutingContext ctx) {
		HttpServerResponse response = ctx.response();

		Project project = ctx.getBodyAsJson().mapTo(Project.class);
		pgClient.preparedQuery("UPDATE db_project SET prepayment_success=$1 WHERE id = $2;")
				.execute(Tuple.of(project.getPrepayment_success(), project.getId()), updRes -> {
					if (updRes.succeeded()) {
						response.setStatusCode(200).end();
					} else {
						response.setStatusCode(500).putHeader("content-type", "application/json; charset=UTF-8")
								.end(updRes.cause().toString());
					}
				});
	}

	@Override
	public void addStep(RoutingContext ctx) {
		HttpServerResponse response = ctx.response();

		ProjectStep projectStep = ctx.getBodyAsJson().mapTo(ProjectStep.class);
		pgClient.preparedQuery(
				"INSERT INTO db_project_step(project_id, end_date, title, description, step_cost) VALUES($1, $2, $3, $4, $5)")
				.execute(Tuple.of(projectStep.getProject_id(), projectStep.getEnd_date(), projectStep.getTitle(),
						projectStep.getDescription(), projectStep.getStep_cost()), ar -> {
							if (ar.succeeded()) {
								response.setStatusCode(200).putHeader("content-type", "application/json; charset=UTF-8")
										.end();
							} else {
								response.setStatusCode(500).putHeader("content-type", "application/json; charset=UTF-8")
										.end(ar.cause().toString());
							}
						});

	}

	@Override
	public void doStepPayment(RoutingContext ctx) {
		HttpServerResponse response = ctx.response();

		Number stepId = ctx.getBodyAsJson().getNumber("id");
		Boolean isPayed = true;

		pgClient.preparedQuery("UPDATE db_project_step SET step_payed=$2 WHERE id = $1;")
				.execute(Tuple.of(stepId, isPayed), ar -> {
					if (ar.succeeded()) {
						response.setStatusCode(200).putHeader("content-type", "application/json; charset=UTF-8").end();
					} else {
						response.setStatusCode(500).putHeader("content-type", "application/json; charset=UTF-8")
								.end(ar.cause().toString());
					}
				});

	}
}