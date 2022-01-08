package backend.Services.OrderService;

import java.util.concurrent.CopyOnWriteArrayList;

import backend.model.Order.Order;

public interface OrderService {
	void saveOrder(Order order);
	void removeOrder(Number order_id);
	Order getOrderById(Number order_id);
	CopyOnWriteArrayList<Order> getOrdersByClientId(Number client_id);
}
