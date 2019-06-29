package LockbumApp.network;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.fasterxml.jackson.databind.ObjectMapper;

import LockbumApp.Application;

public class DownloadService {

	private final String serverUrl; 	
	CloseableHttpClient client = HttpClients.createDefault();
	
	public DownloadService()
	{		
		serverUrl = "http://localhost:8080/";
	}
	
	public boolean downloadFile(String fileName) {
		
		HttpGet request = new HttpGet(serverUrl + "download/" + fileName);
		
		request.addHeader("Authorization", "Bearer " + Application.user.getToken());
		
		try {
			HttpResponse response = client.execute(request);
			
			BufferedInputStream bis = new BufferedInputStream(response.getEntity().getContent());
			
			int status = response.getStatusLine().getStatusCode();
			
			if (status == 401) {
				System.out.println("You are unauthorized!");
				return false;
			} else if (status != 200) {
				System.out.println("Unable to download that file!");
				return false;
			}
				
			String filePath = "./data/images/" + fileName;

			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File(filePath)));

			int inByte;
			while ((inByte = bis.read()) != -1)
				bos.write(inByte);

			bis.close();
			bos.close();
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return false;
	}
	
	public ArrayList<String> getFileNames() {
		HttpGet request = new HttpGet(serverUrl + "myFiles");

		request.addHeader("Authorization", "Bearer " + Application.user.getToken());
		
		try {
			HttpResponse response = client.execute(request);
			
			int status = response.getStatusLine().getStatusCode(); 
			
			if (status == 401) {
				System.out.println("You are unauthorized!");
				return new ArrayList<String>();
			} else if (status != 200) {
				System.out.println("Unable to fetch list of files!");
				return new ArrayList<String>();
			}

			ObjectMapper mapper = new ObjectMapper();
			
			ArrayList<String> fileNames = mapper.readValue(response.getEntity().getContent(), mapper.getTypeFactory().constructCollectionType(ArrayList.class, String.class));
			
			return fileNames;
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return new ArrayList<String>();
	}
	
}
