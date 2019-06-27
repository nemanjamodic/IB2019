package Lockbum.dto;

import Lockbum.model.User;

public class UserDTO {

	private int id;
	private String email;
	private boolean admin;

	public UserDTO(User user) {
		super();
		this.id = user.getId();
		this.email = user.getEmail();

		if (user.getAuthority().getName().equals("admin"))
			this.admin = true;
		else
			this.admin = false;
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

	public boolean isAdmin() {
		return admin;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

}
