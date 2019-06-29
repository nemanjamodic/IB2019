package LockbumApp;

import java.util.Scanner;

import LockbumApp.model.User;
import LockbumApp.network.LogInService;
import LockbumApp.network.RegistrationService;

public class Application {
	
	public static User user;
	
	public static LogInService loginService = new LogInService();
	public static RegistrationService registrationService = new RegistrationService();

	public static void main(String[] args) {
		
		org.apache.xml.security.Init.init();
		
		Scanner input = new Scanner(System.in);

		String command;
		
		// Glavna petlja aplikacije
		while (true) {
			
			// Log in loop
			// breaks if login is successful
			while (true) {
				System.out.println();
				System.out.println(" -- Lockbum -- ");
				System.out.println();
				System.out.println("1. Log in");
				System.out.println("2. Register");
				
				command = input.nextLine();
				
				if (command.equals("1")) {
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
						if (loginService.getJKS(user.getToken())) {
							System.out.println("JKS downloaded successfully!");
							
							Main.application(input);
						} else {
							System.out.println("Unable to start app because JKS is missing!");
						}
					} else {
						System.out.println("Incorect credentials, try again!");
					}
				} else if (command.equals("2")) {
					String email;
					while (true) {
						System.out.println("Enter your email address: ");
						email = input.nextLine();
						
						if (email != null && !email.isEmpty())
							break;
					}
					
					String password;
					String passwordRepeated;
					while (true) {
						while (true) {
							System.out.println("Enter your password: ");
							password = input.nextLine();
							
							if (password != null && !password.isEmpty())
								break;
						}
						
						while (true) {
							System.out.println("Enter your password again: ");
							passwordRepeated = input.nextLine();
							
							if (passwordRepeated != null && !passwordRepeated.isEmpty())
								break;
						}
						
						if (!password.equals(passwordRepeated))
							System.out.println("Passwords don't match! Try again!");
						else
							break;
					}
					
					int status = registrationService.register(email, password);
					
					if (status == 200)
						System.out.println("Successfully registered!");
					else if (status == 400)
						System.out.println("Email already exists!");
					else
						System.out.println("There has been an error!");
				} else {
					System.out.println("Unknown command!");
				}
				
			}
			
		}

	}

}
