package backend.Services.OrderService.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import backend.Services.OrderService.OrderService;
import backend.model.Order.Contract;
import backend.model.Order.Order;
import backend.model.Order.Smeta;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import io.vertx.sqlclient.SqlClient;
import io.vertx.sqlclient.Tuple;

public class OrderServiceImpl implements OrderService {
	final SqlClient pgClient;

	public OrderServiceImpl(SqlClient pgClient) {
		this.pgClient = pgClient;
	}

	@Override
	public void createOrder(RoutingContext ctx) {
		HttpServerResponse response = ctx.response();

		Order order = ctx.getBodyAsJson().mapTo(Order.class);

		pgClient.preparedQuery("INSERT INTO db_order(client_id) VALUES ($1) RETURNING id;")
				.execute(Tuple.of(order.getClient_id()), ar -> {
					if (ar.succeeded()) {
						Number orderId = ar.result().iterator().next().toJson().getNumber("id");
						LocalDateTime updateTime = LocalDateTime.now();
						pgClient.preparedQuery("UPDATE db_user SET updated_at=$1, order_id=$2 WHERE id = $3;")
								.execute(Tuple.of(updateTime, orderId, order.getClient_id()), res -> {
									if (ar.succeeded()) {
										response.setStatusCode(200)
												.putHeader("content-type", "application/json; charset=UTF-8").end();
									} else {
										response.setStatusCode(400)
												.putHeader("content-type", "application/json; charset=UTF-8")
												.end(ar.cause().toString());
									}
								});

					} else {
						response.setStatusCode(400).putHeader("content-type", "application/json; charset=UTF-8")
								.end(ar.cause().toString());
					}

				});

	}

	@Override
	public void saveOrder(RoutingContext ctx) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeOrder(RoutingContext ctx) {
		HttpServerResponse response = ctx.response();

		Number order_id = ctx.getBodyAsJson().getNumber("id");

		pgClient.preparedQuery("DELETE FROM db_order WHERE id = $1").execute(Tuple.of(order_id), ar -> {
			if (ar.succeeded()) {
				response.setStatusCode(200).putHeader("content-type", "application/json; charset=UTF-8").end();

			} else {
				response.setStatusCode(400).putHeader("content-type", "application/json; charset=UTF-8")
						.end(ar.cause().toString());
			}

		});
	}

	@Override
	public void getOrderById(RoutingContext ctx) {
		HttpServerResponse response = ctx.response();

		Number order_id = ctx.getBodyAsJson().getNumber("order_id");

		pgClient.preparedQuery("SELECT id, client_id, smeta FROM db_order WHERE id = $1")
				.execute(Tuple.of(order_id), ar -> {
					if (ar.succeeded()) {
						Order order = ar.result().iterator().next().toJson().mapTo(Order.class);
						pgClient.preparedQuery(
								"SELECT id, contract_main, contract_signed FROM db_contract WHERE order_id = $1")
								.execute(Tuple.of(order_id), contractAr -> {
									if (contractAr.succeeded()) {
										if (contractAr.result().size() != 0) {
											order.setContract(contractAr.result().iterator().next().toJson().mapTo(Contract.class));
										}
										pgClient.preparedQuery(
												"SELECT id, item_name, unit, quantity, price, item_total FROM db_smeta WHERE order_id = $1")
												.execute(Tuple.of(order_id), smetaAr -> {
													if (smetaAr.succeeded()) {
														List<Smeta> smetaArr = new ArrayList<Smeta>();
														RowSet<Row> result = smetaAr.result();
														if (result.size() != 0) {
															for (Row row : result) {
																smetaArr.add(JsonObject.mapFrom(row.toJson())
																		.mapTo(Smeta.class));
															}

														}
														order.setSmeta(smetaArr);


														response.setStatusCode(200)
																.putHeader("content-type",
																		"application/json; charset=UTF-8")
																.end(JsonObject.mapFrom(order).encodePrettily());

													} else {
														response.setStatusCode(400)
																.putHeader("content-type",
																		"application/json; charset=UTF-8")
																.end(ar.cause().toString());
													}
												});

									} else {
										response.setStatusCode(400)
												.putHeader("content-type", "application/json; charset=UTF-8")
												.end(ar.cause().toString());

									}
								});
//						response.setStatusCode(200).putHeader("content-type", "application/json; charset=UTF-8").end();

					} else {
						response.setStatusCode(400).putHeader("content-type", "application/json; charset=UTF-8")
								.end(ar.cause().toString());
					}

				});
	}

}
