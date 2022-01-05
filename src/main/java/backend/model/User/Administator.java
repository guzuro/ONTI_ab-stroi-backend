package backend.model.User;

import java.time.LocalDateTime;
import java.util.concurrent.CopyOnWriteArrayList;

import backend.model.UserRoleEnum;

public class Administator extends BaseUser {
	
	private CopyOnWriteArrayList<Client> clients;
	public Administator(
			String login, 
			String name, 
			UserRoleEnum userRoleEnum			) {
		super(login, name, userRoleEnum);
	}
	public Administator(
			String login, 
			String name, 
			UserRoleEnum userRoleEnum, 
			LocalDateTime createdAt
			) {
		super(login, name, userRoleEnum, createdAt);
	}

	public Administator(
			String login, 
			String name, 
			UserRoleEnum userRoleEnum, 
			LocalDateTime createdAt,
			CopyOnWriteArrayList<Client> clients
			) {
		super(login, name, userRoleEnum, createdAt);
		this.clients = clients;
	}
	public Administator(
			String login, 
			String name, 
			UserRoleEnum userRoleEnum, 
			CopyOnWriteArrayList<Client> clients
			) {
		super(login, name, userRoleEnum);
		this.clients = clients;
	}
	public CopyOnWriteArrayList<Client> getClients() {
		return clients;
	}
	public void setClients(CopyOnWriteArrayList<Client> clients) {
		this.clients = clients;
	}

}
