package LockbumApp;

import java.util.Scanner;

import LockbumApp.model.User;
import LockbumApp.network.LogInService;

public class Application {
	
	public static User user;
	
	public static LogInService loginService = new LogInService();

	public static void main(String[] args) {
		
		org.apache.xml.security.Init.init();
		
		Scanner input = new Scanner(System.in);

		
		// Glavna petlja aplikacije
		while (true) {
			
			// Log in loop
			// breaks if login is successful
			while (true) {
				System.out.println();
				System.out.println(" -- Lockbum -- ");
				System.out.println();
				
				String email;
				while (true) {
					System.out.println("Enter your email address: ");
					email = input.nextLine();
					
					if (email != null && !email.isEmpty())
						break;
				}
				
				String password;
				while (true) {
					System.out.println("Enter your password: ");
					password = input.nextLine();
					
					if (password != null && !password.isEmpty())
						break;
				}

				user = loginService.login(email, password);
				if(user != null) {
					break;
				}else {
					System.out.println("Incorect credentials, try again!");
				}
			}
			

			
			
			// Preuzimanje JKS fajla za ulogovanog korisnika
			if (loginService.getJKS(user.getToken())) {
				//System.out.println("JKS downloaded successfully!");
				
				// Pokretanje glavnog menija aplikacije
				Main.application(input);
			}
			
			
		}
		
		
		// --------- OVAJ DEO TI JE UPLOAD BUKVALNO
		// korisniku nekako treba da omogucis da odabere ovu putanju
		// umesto "E://testPodaci" korisnik treba da moze da izabere neku drugu putanju
		
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
