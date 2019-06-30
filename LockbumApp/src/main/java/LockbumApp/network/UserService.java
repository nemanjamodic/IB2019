package LockbumApp.network;

import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.fasterxml.jackson.databind.ObjectMapper;

import LockbumApp.Application;
import LockbumApp.model.User;

public class UserService {

	private final String serverUrl; 	
	CloseableHttpClient client = HttpClients.createDefault();
	
	public UserService()
	{		
		serverUrl = "http://localhost:8080/";
	}
	
	public ArrayList<User> getInactiveUsers() {
		HttpGet request = new HttpGet(serverUrl + "inactiveUsers");
		
		request.setHeader("Authorization", "Bearer " + Application.user.getToken());
		
		try {
			HttpResponse response = client.execute(request);
			
			int status = response.getStatusLine().getStatusCode(); 
			
			if (status == 401) {
				System.out.println("You are unauthorized!");
				return new ArrayList<User>();
			} else if (status != 200) {
				System.out.println("Unable to fetch list of inactive users!");
				return new ArrayList<User>();
			}

			ObjectMapper mapper = new ObjectMapper();
			
			ArrayList<User> inactiveUserEmails = mapper.readValue(response.getEntity().getContent(), mapper.getTypeFactory().constructCollectionType(ArrayList.class, User.class));
			
			return inactiveUserEmails;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return new ArrayList<User>();
	}
	
	public boolean activate(User inactiveUser) {
		HttpPost request = new HttpPost(serverUrl + "activate/" + inactiveUser.getId());
		
		request.setHeader("Authorization", "Bearer " + Application.user.getToken());
		
		try {
			HttpResponse response = client.execute(request);
			
			int status = response.getStatusLine().getStatusCode(); 
			
			if (status == 401) {
				System.out.println("You are unauthorized!");
				return false;
			} else if (status != 200) {
				System.out.println("There has been an error!");
				return false;
			}
			
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return false;
	}
	
}
