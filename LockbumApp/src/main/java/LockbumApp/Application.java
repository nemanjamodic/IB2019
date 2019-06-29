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
				
				// TODO: IVA ODRADI OVO
				// Pozovi neku metodu u LogInService koja ce pokusati da prijavi korisnika
				// i koja ce da vrati token
				// String token = loginService.login(email, password);
				String token = "eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJjaG9jbWFpbGJhY2tlbmQiLCJzdWIiOiJ2YWlAZ21haWwuY29tIiwiYXVkIjoiY2hvY3VzZXIiLCJpYXQiOjE1NjE2MzAzNDgsImV4cCI6MTU2MTYzMzM0OH0.MhOf0CfiyplIUE2ppsVvZN6K1MFuPxwLJMiub8iVgeBB89vUwEzIAcDspmVDTAt7si7gYScDwu7juaMoeKJfeQ";
				boolean admin = true;
				
				// if (token != null)
				user = new User(email, password, token, admin);
				
				// Active mozes da dobijes tako sto ces da posaljes zahtev na GET /userStatus koji ce ti odgovoriti sa boolean vrednosti
				boolean active = false;
				
				user.setActive(active);
				break;
				// kraj ifa
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
