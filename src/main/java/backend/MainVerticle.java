package backend;

import backend.db.PostgresConnection;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.SessionHandler;
import io.vertx.ext.web.sstore.LocalSessionStore;

public class MainVerticle extends AbstractVerticle {
	@Override
	public void start(Promise<Void> startPromise) throws Exception {

		Router router = Router.router(vertx);
		HttpServer server = vertx.createHttpServer();

		router.route().handler(BodyHandler.create());
		
		router.route().handler(SessionHandler.create(
				LocalSessionStore
				.create(vertx))
				.setCookieless(true)
				.setSessionTimeout(3600000 * 2)
			);

		router.route("/").handler(ctx -> {
			HttpServerResponse response = ctx.response();
			response
			.putHeader("content-type", "text/html; charset=utf-8")
			.end("<h1> Добрый вечер </h1>");
		});

		server.requestHandler(router).listen(8080, httpServerAsyncResult -> {
			if (httpServerAsyncResult.succeeded()) {
				System.out.println("HTTP server started on port 8080");
				startPromise.complete();
			} else {
				startPromise.fail(httpServerAsyncResult.cause());
			}
		});

	}
}
