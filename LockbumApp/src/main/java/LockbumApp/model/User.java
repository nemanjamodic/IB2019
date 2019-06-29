package LockbumApp.model;

public class User {

	private int id;
	private String email;
	private String password;
	private String token;
	private boolean admin;
	private boolean active;

	public User(String email, String password, String token, boolean admin) {
		super();
		this.email = email;
		this.password = password;
		this.token = token;
		this.admin = admin;
	}
	
	public User(String email, String password, boolean admin, String token, boolean active) {
		super();
		this.email = email;
		this.password = password;
		this.admin = admin;
		this.token = token;
		this.active = active;
	}
	public User() {

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public boolean isAdmin() {
		return admin;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

}
