package LockbumApp.model;

public class TokenResponse {

	private String token;
	private long expiresIn;
	
	public TokenResponse(String token, long expiresIn) {
		
		this.token = token;
		this.expiresIn = expiresIn;
	}

	public TokenResponse() {}
	
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public long getExpiresIn() {
		return expiresIn;
	}

	public void setExpiresIn(long expiresIn) {
		this.expiresIn = expiresIn;
	}
	
	
}
