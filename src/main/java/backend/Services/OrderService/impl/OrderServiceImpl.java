package backend.Services.OrderService.impl;

import java.time.LocalDateTime;

import backend.Services.OrderService.OrderService;
import backend.model.Order.Order;
import backend.model.User.Client;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;
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
		// TODO Auto-generated method stub

	}

	@Override
	public void getOrderById(RoutingContext ctx) {
		// TODO Auto-generated method stub
	}

	@Override
	public void getOrdersByClientId(RoutingContext ctx) {
		// TODO Auto-generated method stub
	}

}
