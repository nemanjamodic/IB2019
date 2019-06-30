package LockbumApp.network;

import java.io.File;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import LockbumApp.Application;

public class UploadService {

	private final String serverUrl;
	CloseableHttpClient client = HttpClients.createDefault();

	public UploadService() {
		serverUrl = "http://localhost:8080/upload";
	}

	// Param: zip archive with images
	public void uploadArchive(File archive) {
		HttpPost post = new HttpPost(serverUrl);

		post.setHeader("Authorization", "Bearer " + Application.user.getToken());

		FileBody body = new FileBody(archive, ContentType.DEFAULT_BINARY);

		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
		builder.addPart("file", body);

		HttpEntity entity = builder.build();

		post.setEntity(entity);

		try {
			client.execute(post);

			emptyFolder(new File("./data/upload"));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void emptyFolder(File folder) {
		File[] files = folder.listFiles();
		if (files != null) {
			for (File file : files) {
				if (file.isDirectory()) {
					emptyFolder(file);
				} else {
					file.delete();
				}
			}
		}
	}
}
