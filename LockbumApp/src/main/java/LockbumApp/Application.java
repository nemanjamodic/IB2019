package LockbumApp;

import java.io.File;
import java.util.List;

import LockbumApp.model.User;
import LockbumApp.network.LogInService;
import LockbumApp.network.UploadService;
import LockbumApp.util.ImageAnalizer;
import LockbumApp.util.ZipArchiver;
import LockbumApp.xml.XMLCreateDocument;

public class Application {
	
	public static User user;

	public static void main(String[] args) {
		
		org.apache.xml.security.Init.init();

		user = new User("nemza@gmail.com", "pass", "eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJjaG9jbWFpbGJhY2tlbmQiLCJzdWIiOiJuZW16YUBnbWFpbC5jb20iLCJhdWQiOiJjaG9jdXNlciIsImlhdCI6MTU2MTM4MDU0OSwiZXhwIjoxNTYxMzgzNTQ5fQ.cLeHhMPc_Qs_Ys4giT6sBQxfO0PiCe3Vpkjl3rF5JixwAU_KYglKffn_MKFlrtDZL7GEVz1vghcoECek-Rtb_Q");
		
		LogInService service = new LogInService();
		
		if (service.getJKS(user.getToken())) {
			System.out.println("Success");
		}
		
		/*File file = new File("E://testPodaci");
		
		if(!file.exists()) {
			System.out.println("archive is null!");
			return;
		}
		
		List<File> images = ImageAnalizer.findImagesInFolder(file);	
		
		File signedXML = XMLCreateDocument.generateDocument(images);
		
		File archive = ZipArchiver.imagesToZip(images, signedXML, "slicice.zip");
		
		UploadService uploadService = new UploadService();
		uploadService.uploadArchive(archive);*/

	}

}
