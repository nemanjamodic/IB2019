package LockbumApp.model;

public class User {

	private String email;
	private String password;
	private String token;

	public User(String email, String password, String token) {
		super();
		this.email = email;
		this.password = password;
		this.token = token;
	}

	public String getEmail() {
		return email;
	}
	
	public String getPassword() {
		return password;
	}
	
	public String getToken() {
		return token;
	}

}
