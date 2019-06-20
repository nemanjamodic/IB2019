package Lockbum.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import Lockbum.model.Authority;
import Lockbum.model.Registration;
import Lockbum.model.User;
import Lockbum.repository.AuthorityRepository;
import Lockbum.repository.UserRepository;
import Lockbum.service.AuthorityService;

@RestController
public class UserController {
	
	@Autowired
	private AuthorityService authorityService;
	
	@Autowired
	private UserRepository userRepository;
	
	
	@PostMapping("/registration")
	public ResponseEntity<?> registration(
			@RequestBody Registration registration)
	{
		
		String email = registration.getEmail();
		String password = registration.getPassword();
		String certificate = "file name";
		
		Authority authority = authorityService.findRegular();
		
		User user = new User(email, password, certificate, false, authority);
		
		userRepository.save(user);
		
		return new ResponseEntity<String>("Uspesno kreiran!", HttpStatus.OK);
		
	}
	

}
