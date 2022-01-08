package backend;

import backend.Routes.AuthRoutes;
import backend.Routes.UserRoutes;
import backend.Services.AuthService.AuthService;
import backend.Services.AuthService.impl.AuthServiceImpl;
import backend.Services.UserService.UserService;
import backend.Services.UserService.impl.UserServiceImpl;
import backend.db.PostgresConnection;
import backend.model.User.UsersFabric;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.Session;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.SessionHandler;
import io.vertx.ext.web.sstore.LocalSessionStore;
import io.vertx.sqlclient.SqlClient;

public class MainVerticle extends AbstractVerticle {
	@Override
	public void start(Promise<Void> startPromise) throws Exception {

		SqlClient pgClient = PostgresConnection.createConnection(vertx);

		AuthService authService = new AuthServiceImpl(pgClient);
		AuthRoutes authRoutes = new AuthRoutes(authService);
		
		UserService userService = new UserServiceImpl(pgClient);
		UserRoutes userRoutes = new UserRoutes(userService);

		Router router = Router.router(vertx);
		HttpServer server = vertx.createHttpServer();

		router.route().handler(BodyHandler.create()).handler(SessionHandler.create(LocalSessionStore.create(vertx)));

		router.mountSubRouter("/auth", authRoutes.setAuthRoutes(vertx));
		router.mountSubRouter("/user", userRoutes.setUserRoutes(vertx));

		router.route().handler(ctx -> {
			HttpServerResponse response = ctx.response();
			Session session = ctx.session();
			if (session.get("login") != null) {
				response.setStatusCode(200).end("ZAHODI BRAT");
			} else {
				response.setStatusCode(401).end("POSHEL VON");
			}
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
