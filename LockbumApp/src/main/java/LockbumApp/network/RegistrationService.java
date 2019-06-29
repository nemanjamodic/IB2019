package LockbumApp.network;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class RegistrationService {

	private final String registrationUrl;
	CloseableHttpClient client = HttpClients.createDefault();

	public RegistrationService() {
		registrationUrl = "http://localhost:8080/registration";		
	}
	
	public int register(String email, String password) {
		
		HttpPost request = new HttpPost(registrationUrl);
				
		String data = "{\"email\": \"" + email + "\", \"password\": \"" + password + "\"}";
		
		request.addHeader("Content-type", "application/json");
		
		try {
			StringEntity json = new StringEntity(data);
			
			request.setEntity(json);
			
			HttpResponse response = client.execute(request);
		
			int status = response.getStatusLine().getStatusCode();
			
			return status;

		}catch(Exception e) {
			e.printStackTrace();
			return 0;
		}
		
	}
}
