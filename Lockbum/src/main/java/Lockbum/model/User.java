package Lockbum.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", columnDefinition = "BIGINT", unique = true, nullable = false)
	private int id;
	
	@Column(name = "email", columnDefinition = "VARCHAR(50)", unique = true, nullable = false)
	private String email;
	
	@Column(name = "password", columnDefinition = "VARCHAR(120)", nullable = false)
	private String password;
	
	@Column(name = "certificate", columnDefinition = "VARCHAR(50)", nullable = false)
	private String certificate;
	
	@Column(name = "active", columnDefinition = "BIT", nullable = false)
	private boolean active;
	
	@OneToOne
	@JoinColumn(name = "authority", referencedColumnName = "id", nullable = false)
	private Authority authority;
	
	public User() {}
	
	public User(String email, String password, String certificate, boolean active, Authority authority)
	{
		this.email = email;
		this.password = password;
		this.certificate = certificate;
		this.active = active;
		this.authority = authority;	
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

	public String getCertificate() {
		return certificate;
	}

	public void setCertificate(String certificate) {
		this.certificate = certificate;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public Authority getAuthority() {
		return authority;
	}

	public void setAuthority(Authority authority) {
		this.authority = authority;
	}
	
	
	
}
