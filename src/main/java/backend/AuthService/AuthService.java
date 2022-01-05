package backend.AuthService;

import backend.model.LoginData;

public interface AuthService {
	void login(LoginData authData);
	void logout();
}