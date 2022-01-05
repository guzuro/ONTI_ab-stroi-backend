package backend.model.User;

import java.time.LocalDateTime;

import backend.model.UserRoleEnum;

public class BaseUser {
	
	private Number id;
	private String login;
	private String name;
	
	private UserRoleEnum userRoleEnum;
	private LocalDateTime createdAt;

	public BaseUser(String login, String name, UserRoleEnum userRoleEnum) {
		this.login = login;
		this.name = name;
		this.userRoleEnum = userRoleEnum;
	}
	
	public BaseUser(String login, String name, UserRoleEnum userRoleEnum, LocalDateTime createdAt) {
		this.login = login;
		this.name = name;
		this.userRoleEnum = userRoleEnum;
		this.createdAt = createdAt;
	}
	
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	public UserRoleEnum getUserRoleEnum() {
		return userRoleEnum;
	}
	public void setUserRoleEnum(UserRoleEnum userRoleEnum) {
		this.userRoleEnum = userRoleEnum;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
}
