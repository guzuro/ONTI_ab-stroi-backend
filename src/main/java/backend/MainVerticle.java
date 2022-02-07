package backend;

import backend.Routes.AuthRoutes;
import backend.Routes.OrderRoutes;
import backend.Routes.ProjectRoutes;
import backend.Routes.UserRoutes;
import backend.Services.AuthService.AuthService;
import backend.Services.AuthService.impl.AuthServiceImpl;
import backend.Services.OrderService.OrderService;
import backend.Services.OrderService.impl.OrderServiceImpl;
import backend.Services.ProjectService.ProjectService;
import backend.Services.ProjectService.ProjectServiceImpl;
import backend.Services.UserService.UserService;
import backend.Services.UserService.impl.UserServiceImpl;
import backend.db.PostgresConnection;
import backend.model.User.UsersFabric;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.CookieSameSite;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.Session;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.handler.SessionHandler;
import io.vertx.ext.web.handler.StaticHandler;
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

		OrderService orderService = new OrderServiceImpl(pgClient);
		OrderRoutes orderRoutes = new OrderRoutes(orderService);

		ProjectService projectService = new ProjectServiceImpl(pgClient);
		ProjectRoutes projectRoutes = new ProjectRoutes(projectService);

		Router router = Router.router(vertx);
		HttpServer server = vertx.createHttpServer();

		router.route().handler(CorsHandler.create().allowedMethod(io.vertx.core.http.HttpMethod.GET)
				.allowedMethod(io.vertx.core.http.HttpMethod.POST).allowedMethod(io.vertx.core.http.HttpMethod.OPTIONS)
				.allowCredentials(true).allowedHeader("Access-Control-Allow-Headers")
				.allowedHeader("Access-Control-Allow-Method").allowedHeader("Access-Control-Allow-Origin")
				.allowedHeader("Access-Control-Allow-Credentials").allowedHeader("Content-Type"))
				.handler(BodyHandler.create().setMergeFormAttributes(true).setUploadsDirectory("webroot"))
				.handler(SessionHandler.create(LocalSessionStore.create(vertx)).setCookieSameSite(CookieSameSite.NONE)
						.setCookieHttpOnlyFlag(true).setCookieless(false).setCookieSecureFlag(true));

		router.route("/assets/*").handler(StaticHandler.create("webroot")).handler(handler -> {
			String path = handler.request().path();
			String[] pathSplited = path.split("/");
			String fileName = pathSplited[3];
			HttpServerResponse response = handler.response();
			response.putHeader("Transfer-Encoding", "chunked").sendFile("webroot/" + fileName);
		});

		router.mountSubRouter("/auth", authRoutes.setAuthRoutes(vertx));
		router.mountSubRouter("/user", userRoutes.setUserRoutes(vertx));
		router.mountSubRouter("/order", orderRoutes.setOrderRoutes(vertx));
		router.mountSubRouter("/project", projectRoutes.setProjectRoutes(vertx));

		router.route().handler(ctx -> {
			HttpServerResponse response = ctx.response();
			Session session = ctx.session();
			if (session.get("login") != null) {
				response.setStatusCode(200).end("ZAHODI BRAT");
			} else {
				response.setStatusCode(401).end("POSHEL VON");
			}
		});

		server.requestHandler(router).listen(3080, httpServerAsyncResult -> {
			if (httpServerAsyncResult.succeeded()) {
				System.out.println("HTTP server started on port 8080");
				startPromise.complete();
			} else {
				startPromise.fail(httpServerAsyncResult.cause());
			}
		});

	}
}
