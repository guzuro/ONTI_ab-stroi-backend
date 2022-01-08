package backend.model.User;

import java.util.concurrent.CopyOnWriteArrayList;

import backend.model.Order.Order;

public class Client extends BaseUser {

	private CopyOnWriteArrayList<Order> order_id;
	private Number invited_by;

	public Client() {
	}

	
	
	
	public Client(Number id, String login, String first_name, String last_name) {
		super(id, login, first_name, last_name);

//		this.invited_by = invited_by;
//		this.order_id = order_id;
	}

	public CopyOnWriteArrayList<Order> getOrder_id() {
		return order_id;
	}

	public void setOrder_id(CopyOnWriteArrayList<Order> order_id) {
		this.order_id = order_id;
	}

	public Number getInvited_by() {
		return invited_by;
	}

	public void setInvited_by(Number invited_by) {
		this.invited_by = invited_by;
	}

	@Override
	public CopyOnWriteArrayList<Number> getInvited_customers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setInvited_customers(CopyOnWriteArrayList<Number> invited_customers) {
		// TODO Auto-generated method stub
		
	}
}
