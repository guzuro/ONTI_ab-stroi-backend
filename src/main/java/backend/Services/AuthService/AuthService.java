package backend.Services.AuthService;

import io.vertx.ext.web.RoutingContext;

public interface AuthService {
	void register(RoutingContext ctx);
	void login(RoutingContext ctx);
	void logout(RoutingContext ctx);
	void checkLogin(RoutingContext ctx);

}