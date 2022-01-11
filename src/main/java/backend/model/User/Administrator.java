package backend.model.User;

import java.util.concurrent.CopyOnWriteArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import backend.model.UserRoleEnum;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Administrator extends BaseUser {

	private CopyOnWriteArrayList<Number> invited_customers;

	public Administrator() {
		super();
	}

	public Administrator(String login, String password, String first_name, String last_name, UserRoleEnum userRoleEnum,
			CopyOnWriteArrayList<Number> clients) {
		super(login, password, first_name, last_name, userRoleEnum);
		this.invited_customers = clients;
	}

	public Administrator(String login, String first_name, String last_name, UserRoleEnum userRoleEnum,
			CopyOnWriteArrayList<Number> clients) {
		super(login, first_name, last_name, userRoleEnum);
		this.invited_customers = clients;
	}

	public Administrator(String login, String first_name, String last_name, UserRoleEnum userRoleEnum) {
		super(login, first_name, last_name, userRoleEnum);
	}

	public CopyOnWriteArrayList<Number> getInvited_customers() {
		return invited_customers;
	}

	public void setInvited_customers(CopyOnWriteArrayList<Number> invited_customers) {
		this.invited_customers = invited_customers;
	}

}
