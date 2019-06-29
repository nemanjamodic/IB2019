package LockbumApp.network;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import LockbumApp.Application;

public class LogInService {

	private final String serverUrl;
	CloseableHttpClient client = HttpClients.createDefault();

	public LogInService() {
		serverUrl = "http://localhost:8080/getJKS";
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

}
