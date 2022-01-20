package backend.Services.OrderService;

import io.vertx.ext.web.RoutingContext;

public interface OrderService {
	void createOrder(RoutingContext ctx);
	void removeOrder(RoutingContext ctx_id);
	void getOrderById(RoutingContext ctx_id);
	
	void getContract(RoutingContext ctx);
	void saveContract(RoutingContext ctx);
}
