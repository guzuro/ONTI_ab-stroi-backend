package backend.Routes;

import backend.Services.OrderService.OrderService;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;

public class OrderRoutes {
	final OrderService orderService;

	public OrderRoutes(OrderService orderService) {
		this.orderService = orderService;
	}

	public Router setOrderRoutes(Vertx vertx) {
		Router orderRouter = Router.router(vertx);

		orderRouter.post("/create").handler(rc -> orderService.createOrder(rc));
		orderRouter.post("/remove").handler(rc -> orderService.removeOrder(rc));
		orderRouter.post("/getbyid").handler(rc -> orderService.getOrderById(rc));
		orderRouter.post("/order/saveOrderDoc").handler(rc -> orderService.saveOrderDoc(rc));
		orderRouter.post("/order/approveOrder").handler(rc -> orderService.approveOrder(rc));
		
		orderRouter.post("/contract/get").handler(rc -> orderService.getContract(rc));
		orderRouter.post("/contract/save").handler(rc -> orderService.saveContract(rc));
		orderRouter.post("/contract/approve").handler(rc -> orderService.approveContract(rc));
		
		orderRouter.post("/smeta/save").handler(rc -> orderService.saveSmeta(rc));
		orderRouter.post("/smeta/approve").handler(rc -> orderService.approveSmeta(rc));

		return orderRouter;
	}	
}