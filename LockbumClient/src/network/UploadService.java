package network;

import java.io.File;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;


public class UploadService {

	private final String serverUrl; 	
	CloseableHttpClient client = HttpClients.createDefault();
	
	public UploadService()
	{		
		serverUrl = "http://localhost:8080/upload";		
	}
	
	// Param: zip archive with images
	public void uploadArchive(File archive)
	{		
		HttpPost post = new HttpPost(serverUrl);
		FileBody body =  new FileBody(archive, ContentType.DEFAULT_BINARY);
		
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
		builder.addPart("file", body);
		
		HttpEntity entity = builder.build();
		
		post.setEntity(entity);
			
		try {
			HttpResponse response = client.execute(post);
			System.out.println(response);		
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
}
