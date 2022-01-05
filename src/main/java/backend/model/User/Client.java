package backend.model.User;

import java.time.LocalDateTime;
import java.util.concurrent.CopyOnWriteArrayList;

import backend.model.UserRoleEnum;
import backend.model.Order.Order;

public class Client extends BaseUser {

	private CopyOnWriteArrayList<Order> order;
	private Number administrator_id;
	
	
	public Client(String login, String name, UserRoleEnum userRoleEnum, LocalDateTime createdAt, Number administrator_id) {
		super(login, name, userRoleEnum, createdAt);
		this.administrator_id= administrator_id;
	}
	public Client(String login, String name, UserRoleEnum userRoleEnum, Number administrator_id) {
		super(login, name, userRoleEnum);
		this.administrator_id = administrator_id;		
	}
	public CopyOnWriteArrayList<Order> getOrder() {
		return order;
	}
	public void setOrder(CopyOnWriteArrayList<Order> order) {
		this.order = order;
	}
	public Number getAdministrator_id() {
		return administrator_id;
	}
	public void setAdministrator_id(Number administrator_id) {
		this.administrator_id = administrator_id;
	}

}
