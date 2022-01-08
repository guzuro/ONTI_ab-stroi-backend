package backend.Services.UserService;

import io.vertx.ext.web.RoutingContext;

public interface UserService {
	public void getAdministratorData(RoutingContext ctx);
	public void getClientData(RoutingContext ctx);
	
	public void createClient(RoutingContext ctx);
	public void getClients(RoutingContext ctx);
}