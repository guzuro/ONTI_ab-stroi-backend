package backend.Services.OrderService;

import io.vertx.ext.web.RoutingContext;

public interface OrderService {
	void createOrder(RoutingContext ctx);
	void saveOrder(RoutingContext ctx);
	void removeOrder(RoutingContext ctx_id);
	void getOrderById(RoutingContext ctx_id);
	void getOrdersByClientId(RoutingContext ctx);
}
