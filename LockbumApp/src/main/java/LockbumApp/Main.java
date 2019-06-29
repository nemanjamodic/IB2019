package LockbumApp;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import LockbumApp.model.User;
import LockbumApp.network.DownloadService;
import LockbumApp.network.UploadService;
import LockbumApp.network.UserService;
import LockbumApp.util.ImageAnalizer;
import LockbumApp.util.ZipArchiver;
import LockbumApp.xml.XMLCreateDocument;

public class Main {
	
	private static DownloadService downloadService;
	
	private static UserService userService;

	public static void application(Scanner input) {
		downloadService = new DownloadService();
		
		String command = "";
		
		while (true) {
			System.out.println();
			System.out.println(" -- Lockbum -- ");
			System.out.println();
			
			if (!Application.user.isActive()) {
				System.out.println("Your account is inactive!");
				System.out.println("Wait for admin confirmation!");

				Application.user = null;
				
				return;
			}
			
			System.out.println("1. Upload images");
			System.out.println("2. Download images");
			
			
			if (Application.user.isAdmin()) {
				userService = new UserService();
				
				System.out.println("3. Activate user");
				System.out.println("X. Log out");
			} else {
				System.out.println("X. Log out");
			}
			
			command = input.nextLine();
			
			if (command.equals("1")) {
				System.out.println("Enter path to folder you want to upload: ");
				String path = input.nextLine();		
				File file = new File(path);
				
				if(!file.exists()) {
					System.out.println("Archive is null!");
					continue;
				}
				
				List<File> images = ImageAnalizer.findImagesInFolder(file);
				
				if (images.size() == 0) {
					System.out.println("That folder does not contain any images!");
					continue;
				}
				
				File signedXML = XMLCreateDocument.generateDocument(images);
				
				Date date = new Date();
				
				//String archiveName = date.getDay() + "." + date.getMonth() + "." + date.getYear() + "-" + date.getMinutes() + "." + date.getSeconds() + ".zip";
				String archiveName = date.toString().replace(" CEST ", " ").replace(' ', '-').replace(':', '-') + ".zip";
				
				File archive = ZipArchiver.imagesToZip(images, signedXML, archiveName);
				
				UploadService uploadService = new UploadService();
				uploadService.uploadArchive(archive);
			} else if (command.equals("2")) {
				ArrayList<String> fileNames = downloadService.getFileNames();
				
				if (fileNames.isEmpty()) {
					System.out.println("You have no files!");
					continue;
				} else {
					int counter = 1;
					for (String name : fileNames) {
						System.out.println(counter + ". " + name);
						counter++;
					}
				}

				while (true) {
					System.out.println("Enter a file number to download it: ");
					command = input.nextLine();
					
					try {
						int index = Integer.parseInt(command) - 1;
						
						String fileName = fileNames.get(index);
						
						if (downloadService.downloadFile(fileName)) {
							System.out.println("Successfully downloaded!");
						} else {
							System.out.println("Unable to download!");
						}
						
						break;
					} catch (Exception ex) {
						System.out.println("Please enter a correct number!");
					}
					
				}
				
			} else if (command.equals("3") && Application.user.isAdmin()) {
				ArrayList<User> inactiveUsers = userService.getInactiveUsers();

				if (inactiveUsers.isEmpty()) {
					System.out.println("There are no inactive users!");
					continue;
				} else {
					int counter = 1;
					for (User user : inactiveUsers) {
						System.out.println(counter + ". " + user.getEmail());
						counter++;
					}
				}

				while (true) {
					System.out.println("Enter a number to activate that account: ");
					command = input.nextLine();
					
					try {
						int index = Integer.parseInt(command) - 1;
						
						User inactiveUser = inactiveUsers.get(index);
						
						if (userService.activate(inactiveUser)) {
							System.out.println("Successfully activated!");
						} else {
							System.out.println("Unable to activate!");
						}
						
						break;
					} catch (Exception ex) {
						System.out.println("Please enter a correct number!");
					}
					
				}
			} else if (command.equals("X")) {
				Application.user = null;
				
				return;
			} else {
				System.out.println("Unknown command!");
			}
		}
	}
	
}
