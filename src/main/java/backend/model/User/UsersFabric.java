package backend.model.User;

import backend.model.UserRoleEnum;

public class UsersFabric {
	public BaseUser getUserByRole(UserRoleEnum userRole) {
		if (userRole == UserRoleEnum.ADMINISTRATOR) {
			return new Administrator();
		}
		if (userRole == UserRoleEnum.CUSTOMER) {
			return new Client();
		}
		return null;
	}

}
