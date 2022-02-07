package backend.Services.ProjectService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import backend.model.UserRoleEnum;
import backend.model.Order.Smeta;
import backend.model.Project.Project;
import backend.model.Project.ProjectStep;
import backend.model.User.Administrator;
import backend.model.User.Client;
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
				"SELECT id, client_id, prepayment_sum, prepayment_sucess FROM db_project WHERE order_id = $1")
				.execute(Tuple.of(order_id), ar -> {
					if (ar.succeeded()) {
						Project project = ar.result().iterator().next().toJson().mapTo(Project.class);

						pgClient.preparedQuery("SELECT id, photo, end_date FROM db_project_step WHERE project_id = $1")
								.execute(Tuple.of(order_id), res -> {
									if (res.succeeded()) {
										List<ProjectStep> projectSteps = new ArrayList<ProjectStep>();
										RowSet<Row> result = res.result();
										if (result.size() != 0) {
											for (Row row : result) {
												projectSteps
														.add(JsonObject.mapFrom(row.toJson()).mapTo(ProjectStep.class));
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

		pgClient.preparedQuery(
				"SELECT id, item_name, unit, quantity, price, item_total FROM db_smeta WHERE order_id = $1")
				.execute(Tuple.of(project.getOrder_id()), res -> {
					if (res.succeeded()) {

						List<Smeta> smetaArr = new ArrayList<Smeta>();
						RowSet<Row> result = res.result();

						if (result.size() != 0) {
							for (Row row : result) {
								smetaArr.add(JsonObject.mapFrom(row.toJson()).mapTo(Smeta.class));
							}
						}
					}
				});

		pgClient.preparedQuery("INSERT INTO db_project(order_id, client_id, prepayment_sum) VALUES ($1, $2, $3);")
				.execute(Tuple.of(project.getOrder_id(), project.getClient_id(), project.getPrepayment_sum()), ar -> {
					if (ar.succeeded()) {
						response.setStatusCode(200).putHeader("content-type", "application/json; charset=UTF-8").end();
					} else {
						response.setStatusCode(500).putHeader("content-type", "application/json; charset=UTF-8")
								.end(ar.cause().toString());
					}
				});
	}

	public void updateProject(RoutingContext ctx) {

	}
}