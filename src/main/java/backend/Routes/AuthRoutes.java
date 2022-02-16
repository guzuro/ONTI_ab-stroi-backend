package backend.Routes;

import backend.Services.AuthService.AuthService;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;

public class AuthRoutes {

	final AuthService authService;

	public AuthRoutes(AuthService authService) {
		this.authService = authService;
	}

	public Router setAuthRoutes(Vertx vertx) {
		Router authRouter = Router.router(vertx);

		authRouter.post("/login").handler(rc -> authService.login(rc));
		authRouter.post("/register").handler(rc -> authService.register(rc));
		authRouter.post("/logout").handler(rc -> authService.logout(rc));
		authRouter.post("/checklogin").handler(rc -> authService.checkLogin(rc));

		return authRouter;
	}
}
