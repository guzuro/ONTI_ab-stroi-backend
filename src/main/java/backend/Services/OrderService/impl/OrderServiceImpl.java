package backend.Services.OrderService.impl;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import backend.Services.OrderService.OrderService;
import backend.model.Order.Contract;
import backend.model.Order.Order;
import backend.model.Order.Smeta;
import backend.model.User.Client;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.FileUpload;
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
						Number order_id = ar.result().iterator().next().toJson().getNumber("id");
						JsonObject json = new JsonObject();
						json.put("order_id", order_id);
						LocalDateTime updateTime = LocalDateTime.now();
						pgClient.preparedQuery("UPDATE db_user SET updated_at=$1, order_id=$2 WHERE id = $3;")
								.execute(Tuple.of(updateTime, order_id, order.getClient_id()), res -> {
									if (ar.succeeded()) {
										response.setStatusCode(200)
												.putHeader("content-type", "application/json; charset=UTF-8")
												.end(json.encodePrettily());
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

		pgClient.preparedQuery("SELECT id, client_id, smeta FROM db_order WHERE id = $1").execute(Tuple.of(order_id),
				ar -> {
					if (ar.succeeded()) {
						Number clientId = ar.result().iterator().next().toJson().getNumber("client_id");
						Order order = new Order();
						order.setId(order_id);

						pgClient.preparedQuery(
								"SELECT id, login, first_name, last_name, role, order_id FROM db_user WHERE id = $1")
								.execute(Tuple.of(clientId), userResult -> {
									if (userResult.succeeded()) {
										JsonObject jsonUser = userResult.result().iterator().next().toJson();
										Client user = JsonObject.mapFrom(jsonUser).mapTo(Client.class);
										order.setClient_id(user);
										pgClient.preparedQuery(
												"SELECT id, contract_main, contract_signed FROM db_contract WHERE order_id = $1")
												.execute(Tuple.of(order_id), contractAr -> {
													if (contractAr.succeeded()) {
														if (contractAr.result().size() != 0) {
															order.setContract(contractAr.result().iterator().next()
																	.toJson().mapTo(Contract.class));
														}
														pgClient.preparedQuery(
																"SELECT id, item_name, unit, quantity, price, item_total FROM db_smeta WHERE order_id = $1")
																.execute(Tuple.of(order_id), smetaAr -> {
																	if (smetaAr.succeeded()) {
																		List<Smeta> smetaArr = new ArrayList<Smeta>();
																		RowSet<Row> result = smetaAr.result();
																		if (result.size() != 0) {
																			for (Row row : result) {
																				smetaArr.add(
																						JsonObject.mapFrom(row.toJson())
																								.mapTo(Smeta.class));
																			}

																		}
																		order.setSmeta(smetaArr);

																		response.setStatusCode(200).putHeader(
																				"content-type",
																				"application/json; charset=UTF-8")
																				.end(JsonObject.mapFrom(order)
																						.encodePrettily());

																	} else {
																		response.setStatusCode(400).putHeader(
																				"content-type",
																				"application/json; charset=UTF-8")
																				.end(ar.cause().toString());
																	}
																});

													} else {
														response.setStatusCode(400)
																.putHeader("content-type",
																		"application/json; charset=UTF-8")
																.end(ar.cause().toString());

													}
												});
									} else {
										response.setStatusCode(500)
												.putHeader("content-type", "application/json; charset=UTF-8")
												.end(userResult.cause().toString());
									}
								});

					} else {
						response.setStatusCode(400).putHeader("content-type", "application/json; charset=UTF-8")
								.end(ar.cause().toString());
					}

				});
	}

	@Override
	public void getContract(RoutingContext ctx) {
		HttpServerResponse response = ctx.response();
		HttpServerRequest req = ctx.request();

		Number order_id = ctx.getBodyAsJson().getNumber("order_id");

		System.out.println(req.path());
		pgClient.preparedQuery(
				"SELECT contract_main, contract_signed, contract_approved, contract_main_mime, contract_signed_mime FROM db_contract WHERE order_id = $1")
				.execute(Tuple.of(order_id), ar -> {
					if (ar.succeeded()) {
						Contract contract = ar.result().iterator().next().toJson().mapTo(Contract.class);
						response.setStatusCode(200).putHeader("content-type", "application/json; charset=UTF-8")
						.end(JsonObject.mapFrom(contract).encodePrettily());
					} else {
						response.setStatusCode(500).putHeader("content-type", "application/json; charset=UTF-8")
								.end(ar.cause().toString());
					}
				});

	}

	@Override
	public void saveContract(RoutingContext ctx) {
		ctx.response().setChunked(true);
		HttpServerResponse response = ctx.response();

		Number order_id = Integer.valueOf(ctx.request().getParam("order_id"));

		Set<FileUpload> uploads = ctx.fileUploads();
		Contract contract = new Contract();

		uploads.forEach(upload -> {
			System.out.println(upload.name());
			try {
				if (upload.name().contains("contract_main")) {
					File uploadedFile = new File(upload.uploadedFileName());
					uploadedFile.renameTo(new File("webroot/" + upload.fileName()));
					uploadedFile.createNewFile();
					contract.setContract_main(upload.fileName());
					contract.setContract_main_mime(upload.contentType());
				}
				if (upload.name().contains("contract_signed")) {
					File uploadedFile = new File(upload.uploadedFileName());
					uploadedFile.renameTo(new File("webroot/" + upload.fileName()));
					uploadedFile.createNewFile();
					contract.setContract_signed(upload.fileName());
					contract.setContract_signed_mime(upload.contentType());
				}
			} catch (IOException e) {
//						// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		pgClient.preparedQuery(
				"SELECT contract_main, contract_signed, contract_approved FROM db_contract WHERE order_id = $1;")
				.execute(Tuple.of(order_id), ar -> {
					if (ar.succeeded()) {
						if (ar.result().size() == 0) {
							pgClient.preparedQuery(
									"INSERT INTO db_contract(order_id, contract_main, contract_signed, contract_approved, contract_main_mime, contract_signed_mime) "
											+ "VALUES ($1, $2, $3, $4, $5, $6) RETURNING contract_signed;")
									.execute(Tuple.of(order_id, contract.getContract_main(),
											contract.getContract_signed(), contract.getContract_approved(),
											contract.getContract_main_mime(), contract.getContract_signed_mime()),
											res -> {
												if (res.succeeded()) {
													Contract contractRes = res.result().iterator().next().toJson()
															.mapTo(Contract.class);

													response.end(JsonObject.mapFrom(contractRes).toBuffer());
												} else {
													System.out.println(res.cause() + "1");
												}
											});
						} else {
							pgClient.preparedQuery(
									"UPDATE public.db_contract SET contract_approved=$1, contract_main=$2, contract_signed=$3, contract_main_mime=$5, contract_signed_mime=$6 WHERE order_id = $4 "
											+ "RETURNING contract_signed, contract_main, contract_approved, contract_main_mime, contract_signed_mime;")
									.execute(Tuple.of(contract.getContract_approved(), contract.getContract_main(),
											contract.getContract_signed(), order_id, contract.getContract_main_mime(),
											contract.getContract_signed_mime()), res -> {
												if (res.succeeded()) {
													Contract contractRes = res.result().iterator().next().toJson()
															.mapTo(Contract.class);
													response.end(JsonObject.mapFrom(contractRes).toBuffer());
												} else {
													System.out.println(res.cause() + "1");
												}
											});
						}
					} else {
						System.out.println(ar.cause());

					}
				});
	}

}
