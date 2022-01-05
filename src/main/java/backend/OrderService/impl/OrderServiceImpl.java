package backend.OrderService.impl;

import java.util.concurrent.CopyOnWriteArrayList;

import backend.OrderService.OrderService;
import backend.model.Order.Order;

public class OrderServiceImpl implements OrderService {

	@Override
	public void saveOrder(Order order) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeOrder(Number order_id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Order getOrderById(Number order_id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CopyOnWriteArrayList<Order> getOrdersByClientId(Number client_id) {
		// TODO Auto-generated method stub
		return null;
	}

}
