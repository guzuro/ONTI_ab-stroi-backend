package backend.Routes;

import backend.Services.UserService.UserService;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;

public class UserRoutes {
	final UserService userService;

	public UserRoutes(UserService userService) {
		this.userService = userService;
	}

	public Router setUserRoutes(Vertx vertx) {
		Router authRouter = Router.router(vertx);

		authRouter.post("/createclient").handler(rc -> userService.createClient(rc));
		authRouter.post("/getadmindata").handler(rc -> userService.getAdministratorData(rc));
		authRouter.post("/getclientdata").handler(rc -> userService.getClientData(rc));
		authRouter.post("/getclients").handler(rc -> userService.getClients(rc));

		return authRouter;
	}

}