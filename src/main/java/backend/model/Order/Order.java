package backend.model.Order;

import java.time.LocalDateTime;

import backend.model.User.Client;

public class Order {

	private Number orderId;
	private Client client;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
		
	public Order() {}
	public Order(Number orderId,Client client, LocalDateTime createdAt, LocalDateTime updatedAt) {
		this.orderId = orderId;
		this.client = client;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}
	
	
	public Number getOrderId() {
		return orderId;
	}
	public void setOrderId(Number orderId) {
		this.orderId = orderId;
	}
	public Client getClient() {
		return client;
	}
	public void setClient(Client client) {
		this.client = client;
	}
	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
		
}
