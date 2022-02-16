package backend.Services.OrderService;

import io.vertx.ext.web.RoutingContext;

public interface OrderService {
	void createOrder(RoutingContext ctx);
	void removeOrder(RoutingContext ctx_id);
	void getOrderById(RoutingContext ctx_id);
	
	void saveContract(RoutingContext ctx);
	void approveContract(RoutingContext ctx);
	
	void saveSmeta(RoutingContext ctx);
	void approveSmeta(RoutingContext ctx);
	
	void saveOrderDoc(RoutingContext ctx);
	void approveOrder(RoutingContext ctx);
	
}
