package Lockbum.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Lockbum.model.Authority;
import Lockbum.repository.AuthorityRepository;

@Service
public class AuthorityService {

	@Autowired
	private AuthorityRepository authorityRepository;
	
	public Authority findRegular() {
		return authorityRepository.findByName("regular");
	};
}
