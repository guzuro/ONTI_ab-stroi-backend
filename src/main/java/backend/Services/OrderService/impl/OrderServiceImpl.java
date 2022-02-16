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

		Number client_id = ctx.getBodyAsJson().getNumber("client_id");

		pgClient.preparedQuery("INSERT INTO db_order(client_id) VALUES ($1) RETURNING id;").execute(Tuple.of(client_id),
				ar -> {
					if (ar.succeeded()) {
						Number order_id = ar.result().iterator().next().toJson().getNumber("id");
						JsonObject json = new JsonObject();
						json.put("order_id", order_id);
						LocalDateTime updateTime = LocalDateTime.now();
						pgClient.preparedQuery("UPDATE db_user SET updated_at=$1, order_id=$2 WHERE id = $3;")
								.execute(Tuple.of(updateTime, order_id, client_id), res -> {
									if (ar.succeeded()) {
										pgClient.preparedQuery("INSERT INTO db_contract(order_id) VALUES ($1);")
												.execute(Tuple.of(order_id), contRes -> {
													if (contRes.succeeded()) {
														response.setStatusCode(200)
																.putHeader("content-type",
																		"application/json; charset=UTF-8")
																.end(json.encodePrettily());
													} else {
														response.setStatusCode(500)
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

		pgClient.preparedQuery(
				"SELECT id, client_id, smeta, smeta_approved,order_approved, order_doc_main, order_doc_signed FROM db_order WHERE id = $1")
				.execute(Tuple.of(order_id), ar -> {
					if (ar.succeeded()) {
						Number clientId = ar.result().iterator().next().toJson().getNumber("client_id");
						Order order = new Order();
						order.setId(order_id);
						order.setSmeta_approved(ar.result().iterator().next().toJson().getBoolean("smeta_approved"));
						order.setOrder_approved(ar.result().iterator().next().toJson().getBoolean("order_approved"));

						order.setOrder_doc_main(ar.result().iterator().next().toJson().getString("order_doc_main"));
						order.setOrder_doc_signed(ar.result().iterator().next().toJson().getString("order_doc_signed"));
						pgClient.preparedQuery(
								"SELECT id, login, first_name, last_name, role, order_id FROM db_user WHERE id = $1")
								.execute(Tuple.of(clientId), userResult -> {
									if (userResult.succeeded()) {
										JsonObject jsonUser = userResult.result().iterator().next().toJson();
										Client user = JsonObject.mapFrom(jsonUser).mapTo(Client.class);
										order.setClient_id(user);
										pgClient.preparedQuery(
												"SELECT id, contract_main, contract_signed, contract_approved FROM db_contract WHERE order_id = $1")
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
	public void saveContract(RoutingContext ctx) {
		ctx.response().setChunked(true);
		HttpServerResponse response = ctx.response();

		Number order_id = Integer.valueOf(ctx.request().getParam("order_id"));

		Set<FileUpload> uploads = ctx.fileUploads();
		Contract contract = new Contract();

		if (ctx.request().getParam("contract_main") != null) {
			contract.setContract_main(ctx.request().getParam("contract_main"));
		}

		if (ctx.request().getParam("contract_signed") != null) {
			contract.setContract_signed(ctx.request().getParam("contract_signed"));
		}

		uploads.forEach(upload -> {
			try {
				if (upload.name().contains("contract_main")) {
					File uploadedFile = new File(upload.uploadedFileName());
					uploadedFile.renameTo(new File("webroot/" + upload.fileName()));
					uploadedFile.createNewFile();
					contract.setContract_main(upload.fileName());
				}
				if (upload.name().contains("contract_signed")) {
					File uploadedFile = new File(upload.uploadedFileName());
					uploadedFile.renameTo(new File("webroot/" + upload.fileName()));
					uploadedFile.createNewFile();
					contract.setContract_signed(upload.fileName());
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
									"INSERT INTO db_contract(order_id, contract_main, contract_signed, contract_approved) "
											+ "VALUES ($1, $2, $3, $4) RETURNING id;")
									.execute(
											Tuple.of(order_id, contract.getContract_main(),
													contract.getContract_signed(), contract.getContract_approved()),
											res -> {
												if (res.succeeded()) {
													pgClient.preparedQuery(
															"UPDATE db_order SET contract_id=$1 WHERE id = $2;")
															.execute(Tuple.of(res.result().iterator().next().toJson()
																	.getNumber("id"), order_id), updRes -> {
																		if (updRes.succeeded()) {
																			Contract contractRes = res.result()
																					.iterator().next().toJson()
																					.mapTo(Contract.class);
																			response.end(JsonObject.mapFrom(contractRes)
																					.toBuffer());
																		} else {
																			response.setStatusCode(500).putHeader(
																					"content-type",
																					"application/json; charset=UTF-8")
																					.end(res.cause().toString());
																		}
																	});
												} else {
													response.setStatusCode(500)
															.putHeader("content-type",
																	"application/json; charset=UTF-8")
															.end(res.cause().toString());
												}
											});
						} else {
							pgClient.preparedQuery(
									"UPDATE public.db_contract SET contract_approved=$1, contract_main=$2, contract_signed=$3 WHERE order_id = $4 "
											+ "RETURNING contract_signed, contract_main, contract_approved;")
									.execute(Tuple.of(contract.getContract_approved(), contract.getContract_main(),
											contract.getContract_signed(), order_id), res -> {
												if (res.succeeded()) {
													Contract contractRes = res.result().iterator().next().toJson()
															.mapTo(Contract.class);
													response.end(JsonObject.mapFrom(contractRes).toBuffer());
												} else {
													response.setStatusCode(500)
															.putHeader("content-type",
																	"application/json; charset=UTF-8")
															.end(res.cause().toString());
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
	public void approveContract(RoutingContext ctx) {
		HttpServerResponse response = ctx.response();

		Number order_id = ctx.getBodyAsJson().getNumber("order_id");
		Boolean contract_approved = ctx.getBodyAsJson().getBoolean("contract_approved");

		pgClient.preparedQuery("UPDATE public.db_contract SET contract_approved=$1 WHERE order_id = $2;")
				.execute(Tuple.of(contract_approved, order_id), res -> {
					if (res.succeeded()) {
						response.setStatusCode(200).end();
					} else {
						response.setStatusCode(500).putHeader("content-type", "application/json; charset=UTF-8")
								.end(res.cause().toString());
					}
				});

	}

	@Override
	public void saveSmeta(RoutingContext ctx) {
		HttpServerResponse response = ctx.response();

		Number order_id = ctx.getBodyAsJson().getNumber("order_id");
		ArrayList<Smeta> smeta = new ArrayList<Smeta>();
		ctx.getBodyAsJson().getJsonArray("smeta").forEach(object -> {
			smeta.add(JsonObject.mapFrom(object).mapTo(Smeta.class));
		});
		try {
			smeta.forEach(smetaItem -> {
				if (smetaItem.getTo_delete() != null && smetaItem.getTo_delete() == true) {
					pgClient.preparedQuery("DELETE FROM db_smeta WHERE id = $1").execute(Tuple.of(smetaItem.getId()));

				}
				if (smetaItem.getId() == null) {
					pgClient.preparedQuery(
							"INSERT INTO db_smeta(item_name, unit, quantity, price, item_total, order_id) VALUES ($1, $2, $3, $4, $5, $6) RETURNING id;")
							.execute(Tuple.of(smetaItem.getItem_name(), smetaItem.getUnit(), smetaItem.getQuantity(),
									smetaItem.getPrice(), smetaItem.getItem_total(), order_id));
				} else {
					pgClient.preparedQuery(
							"UPDATE db_smeta SET item_name=$1, unit=$2, quantity=$3, price=$4, item_total=$5 WHERE id = $6 RETURNING id;")
							.execute(Tuple.of(smetaItem.getItem_name(), smetaItem.getUnit(), smetaItem.getQuantity(),
									smetaItem.getPrice(), smetaItem.getItem_total(), smetaItem.getId()));
				}

			});
			response.setStatusCode(200).end();
		} catch (Exception e) {
			response.setStatusCode(500).putHeader("content-type", "application/json; charset=UTF-8")
					.end(e.getMessage());
		}
	}

	@Override
	public void approveSmeta(RoutingContext ctx) {
		HttpServerResponse response = ctx.response();

		Number order_id = ctx.getBodyAsJson().getNumber("order_id");
		Boolean smeta_approved = ctx.getBodyAsJson().getBoolean("smeta_approved");

		pgClient.preparedQuery("UPDATE public.db_order SET smeta_approved=$1 WHERE id = $2;")
				.execute(Tuple.of(smeta_approved, order_id), res -> {
					if (res.succeeded()) {
						response.setStatusCode(200).end();
					} else {
						response.setStatusCode(500).putHeader("content-type", "application/json; charset=UTF-8")
								.end(res.cause().toString());
					}
				});

	}

	@Override
	public void saveOrderDoc(RoutingContext ctx) {
		ctx.response().setChunked(true);
		HttpServerResponse response = ctx.response();

		Set<FileUpload> uploads = ctx.fileUploads();

		Order order = new Order();
		order.setId(Integer.valueOf(ctx.request().getParam("order_id")));

		if (ctx.request().getParam("order_doc_main") != null) {
			order.setOrder_doc_main(ctx.request().getParam("order_doc_main"));
		}

		if (ctx.request().getParam("order_doc_signed") != null) {
			order.setOrder_doc_signed(ctx.request().getParam("order_doc_signed"));
		}

		uploads.forEach(upload -> {
			try {
				if (upload.name().contains("order_doc_main")) {
					File uploadedFile = new File(upload.uploadedFileName());
					uploadedFile.renameTo(new File("webroot/" + upload.fileName()));
					uploadedFile.createNewFile();
					order.setOrder_doc_main(upload.fileName());
				}
				if (upload.name().contains("order_doc_signed")) {
					File uploadedFile = new File(upload.uploadedFileName());
					uploadedFile.renameTo(new File("webroot/" + upload.fileName()));
					uploadedFile.createNewFile();
					order.setOrder_doc_signed(upload.fileName());
				}
			} catch (IOException e) {
//						// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});

		pgClient.preparedQuery("UPDATE public.db_order SET order_doc_main=$1, order_doc_signed=$2 WHERE id = $3;")
				.execute(Tuple.of(order.getOrder_doc_main(), order.getOrder_doc_signed(), order.getId()), res -> {
					if (res.succeeded()) {
						response.setStatusCode(200).end();
					} else {
						response.setStatusCode(500).putHeader("content-type", "application/json; charset=UTF-8")
								.end(res.cause().toString());
					}
				});
	}

	@Override
	public void approveOrder(RoutingContext ctx) {
		HttpServerResponse response = ctx.response();

		Number order_id = ctx.getBodyAsJson().getNumber("order_id");
		Boolean order_approved = ctx.getBodyAsJson().getBoolean("order_approved");

		pgClient.preparedQuery("UPDATE public.db_order SET order_approved=$1 WHERE id = $2;")
				.execute(Tuple.of(order_approved, order_id), res -> {
					if (res.succeeded()) {
						response.setStatusCode(200).end();
					} else {
						response.setStatusCode(500).putHeader("content-type", "application/json; charset=UTF-8")
								.end(res.cause().toString());
					}
				});

	}

}
