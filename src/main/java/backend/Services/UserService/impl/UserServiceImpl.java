package backend.Services.UserService.impl;

import java.util.concurrent.CopyOnWriteArrayList;

import backend.Services.UserService.UserService;
import backend.model.User.Administrator;
import backend.model.User.Client;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.Session;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import io.vertx.sqlclient.SqlClient;
import io.vertx.sqlclient.Tuple;

public class UserServiceImpl implements UserService {
	final SqlClient pgClient;

	public UserServiceImpl(SqlClient pgClient) {
		this.pgClient = pgClient;
	}

	@Override
	public void getAdministratorData(RoutingContext ctx) {
		HttpServerResponse response = ctx.response();
		Session session = ctx.session();
		Number adminId = session.get("id");
		System.out.println(adminId);
		pgClient.preparedQuery("SELECT (login, password, first_name, last_name, role) FROM db_user WHERE id = $1")
				.execute(Tuple.of(adminId), ar -> {
					if (ar.succeeded()) {
						System.out.println(ar.result().iterator().next().toJson().encodePrettily());
						response.setStatusCode(200).end();
					} else {
						response.setStatusCode(400).putHeader("content-type", "application/json; charset=UTF-8")
								.end(ar.cause().toString());
					}
				});

	}

	@Override
	public void getClientData(RoutingContext ctx) {
		// TODO Auto-generated method stub

	}

	@Override
	public void createClient(RoutingContext ctx) {
		HttpServerResponse response = ctx.response();
		Client client = ctx.getBodyAsJson().mapTo(Client.class);
		Session session = ctx.session();
		Number adminId = session.get("id");
		System.out.println(adminId);
		pgClient.preparedQuery("INSERT INTO db_user(login, password, first_name, last_name, role, invited_by)"
				+ "VALUES ($1, $2, $3, $4, $5, $6);")
				.execute(Tuple.of(client.getLogin(), client.getPassword(), client.getFirst_name(),
						client.getLast_name(), client.getRole(), adminId), ar -> {
							if (ar.succeeded()) {
								pgClient.preparedQuery(
										"UPDATE db_user SET invited_customers = invited_customers || lastval() WHERE id = $1;")
										.execute(Tuple.of(adminId), res -> {
											if (res.succeeded()) {
												response.setStatusCode(200)
														.putHeader("content-type", "application/json; charset=UTF-8")
														.end();

											} else {
												response.setStatusCode(400)
														.putHeader("content-type", "application/json; charset=UTF-8")
														.end(res.cause().toString());
											}
										});
							} else {
								response.setStatusCode(400).putHeader("content-type", "application/json; charset=UTF-8")
										.end(ar.cause().toString());
							}
						});

	}

	@Override
	public void getClients(RoutingContext ctx) {
		HttpServerResponse response = ctx.response();
		Session session = ctx.session();
		Number adminId = session.get("id");
		CopyOnWriteArrayList<Client> clients = new CopyOnWriteArrayList<Client>();

		System.out.println(adminId);
		pgClient.preparedQuery("SELECT id, login, first_name, last_name FROM db_user WHERE invited_by = $1")
				.execute(Tuple.of(adminId), ar -> {
					if (ar.succeeded()) {
						RowSet<Row> result = ar.result();
						for (Row row : result) {
							clients.add(JsonObject.mapFrom(row.toJson()).mapTo(Client.class));
						}
	                    JsonArray clientsJson = new JsonArray();
	                    clients.forEach(client -> clientsJson.add(JsonObject.mapFrom(client)));

						response.setStatusCode(200).end(clientsJson.encodePrettily());
					} else {
						response.setStatusCode(400).putHeader("content-type", "application/json; charset=UTF-8")
								.end(ar.cause().toString());
					}
				});

	}

}
