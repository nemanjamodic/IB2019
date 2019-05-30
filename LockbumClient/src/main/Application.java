package main;

import java.io.File;
import java.security.Security;
import java.util.List;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import network.UploadService;
import util.ImageAnalizer;
import util.ZipArchiver;
import xml.XMLCreateDocument;

public class Application {
	
	static {
		Security.addProvider(new BouncyCastleProvider());
		org.apache.xml.security.Init.init();
	}

	public static void main(String[] args) {

		File file = new File("E://testPodaci");
		
		if(!file.exists()) {
			System.out.println("archive is null!");
			return;
		}
		
		List<File> images = ImageAnalizer.findImagesInFolder(file);	
		
		File signedXML = XMLCreateDocument.generateDocument(images);
		
		File archive = ZipArchiver.imagesToZip(images, signedXML, "slicice.zip");
		
		UploadService uploadService = new UploadService();
		uploadService.uploadArchive(archive);

	}

}
