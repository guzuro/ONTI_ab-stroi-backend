package backend.Services.AuthService.impl;

import com.fasterxml.jackson.databind.util.JSONPObject;

import backend.Services.AuthService.AuthService;
import backend.model.LoginData;
import backend.model.User.Administrator;
import backend.model.User.UsersFabric;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.Session;
import io.vertx.sqlclient.SqlClient;
import io.vertx.sqlclient.Tuple;

public class AuthServiceImpl implements AuthService {

	final SqlClient pgClient;
	final UsersFabric usersFabric = new UsersFabric();

	public AuthServiceImpl(SqlClient pgClient) {
		this.pgClient = pgClient;
	}

	@Override
	public void register(RoutingContext ctx) {
		HttpServerResponse response = ctx.response();
		Administrator administrator = ctx.getBodyAsJson().mapTo(Administrator.class);
		pgClient.preparedQuery(
				"INSERT INTO db_user(login, password, first_name, last_name, role) VALUES ($1, $2, $3, $4, $5)")
				.execute(Tuple.of(administrator.getLogin(), administrator.getPassword(), administrator.getFirst_name(),
						administrator.getLast_name(), administrator.getRole()), ar -> {
							if (ar.succeeded()) {
								response.setStatusCode(200).end();
							} else {
								response.setStatusCode(400).putHeader("content-type", "application/json; charset=UTF-8")
										.end(ar.cause().toString());
							}
						});
	}

	@Override
	public void login(RoutingContext ctx) {
		HttpServerResponse response = ctx.response();
		LoginData loginData = ctx.getBodyAsJson().mapTo(LoginData.class);
		pgClient.preparedQuery(
				"SELECT id, login, first_name, last_name, role, order_id, invited_by, invited_customers, created_at FROM db_user WHERE login=$1 AND password=$2")
				.execute(Tuple.of(loginData.getLogin(), loginData.getPassword()), ar -> {
					if (ar.succeeded()) {
						if (ar.result().rowCount() == 1) {
							JsonObject resultJson = ar.result().iterator().next().toJson();

							Session session = ctx.session();
							session.put("role", resultJson.getString("role").toString());
							session.put("login", resultJson.getString("login").toString());
							session.put("id", resultJson.getNumber("id"));

							response.setStatusCode(200).putHeader("content-type", "application/json; charset=UTF-8")
									.end();
						} else {
							response.setStatusCode(404).end();
						}
					} else {
						response.setStatusCode(400).putHeader("content-type", "application/json; charset=UTF-8")
								.end(ar.cause().toString());

					}
				});

	}

	@Override
	public void logout(RoutingContext ctx) {
		Session session = ctx.session();
		session.destroy();

		HttpServerResponse response = ctx.response();
		response.putHeader("content-type", "text/html; charset=utf-8").end("<h1> logout </h1>");
	}

	@Override
	public void checkLogin(RoutingContext ctx) {
		Session session = ctx.session();
		HttpServerResponse response = ctx.response();

		if (session.get("id") != null) {

			response.setStatusCode(200).putHeader("content-type", "application/json; charset=UTF-8")
					.end(new JsonObject().put("id", session.get("id")).encodePrettily());
		} else {
			response.setStatusCode(401).end();
		}
	}

}