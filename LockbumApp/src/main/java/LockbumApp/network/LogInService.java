package LockbumApp.network;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.bouncycastle.crypto.tls.ClientAuthenticationType;

import com.fasterxml.jackson.databind.ObjectMapper;

import LockbumApp.Application;
import LockbumApp.model.TokenResponse;
import LockbumApp.model.User;

public class LogInService {

	private final String serverUrl;
	private final String loginUrl;
	private final String roleUrl;
	private final String statusUrl;
	CloseableHttpClient client = HttpClients.createDefault();

	public LogInService() {
		serverUrl = "http://localhost:8080/getJKS";
		loginUrl = "http://localhost:8080/login";
		statusUrl = "http://localhost:8080/userStatus";
		roleUrl = "http://localhost:8080/role";		
	}

	/**
	 * Obtains JKS for logged user and stores it in /data folder.
	 * 
	 * @param JSON Web Token for logged user.
	 */
	public boolean getJKS(String token) {
		HttpGet request = new HttpGet(serverUrl);

		request.setHeader("Authorization", "Bearer " + Application.user.getToken());

		try {
			HttpResponse response = client.execute(request);
			//System.out.println(response);

			BufferedInputStream bis = new BufferedInputStream(response.getEntity().getContent());

			String filePath = "./data/cert.jks";

			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File(filePath)));

			int inByte;
			while ((inByte = bis.read()) != -1)
				bos.write(inByte);

			bis.close();
			bos.close();
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

	
	public User login(String email, String password) {
		
		HttpPost post = new HttpPost(loginUrl);
				
		String data = "{\"email\": \"" + email + "\", \"password\": \"" + password + "\"}";
		
		post.addHeader("Content-type", "application/json");
		
		try {
			StringEntity json = new StringEntity(data);
			
			post.setEntity(json);
			
			HttpResponse response = client.execute(post);
			
			//String token = EntityUtils.toString(response.getEntity());
			System.out.println(response);
			ObjectMapper mapper = new ObjectMapper();
			
			TokenResponse tokenResponse = mapper.readValue(response.getEntity().getContent(), TokenResponse.class);
			
			
			if(tokenResponse.getToken() == null) {
				return null;
			}
			else {
				
				HttpGet get = new HttpGet(roleUrl);				
				get.setHeader("Authorization", "Bearer " + tokenResponse.getToken());
				HttpResponse resp = client.execute(get);
				String authority = EntityUtils.toString(resp.getEntity());
				boolean admin;
				if(authority.equals("admin")) {
					admin = true;
				}else {
					admin = false;
				}
				
				
				HttpGet statusGet = new HttpGet(statusUrl);
				statusGet.setHeader("Authorization", "Bearer " + tokenResponse.getToken());
				HttpResponse respo = client.execute(statusGet);
				//Boolean status = mapper.readValue(respo.getEntity().getContent(), Boolean.class);
				String isActive = EntityUtils.toString(respo.getEntity());
				boolean status = Boolean.parseBoolean(isActive);
				
				User user = new User(email, password, admin, tokenResponse.getToken(), status);
				return user;
			}

		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
}
