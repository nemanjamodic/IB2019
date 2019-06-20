package Lockbum.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import Lockbum.model.User;

public interface UserRepository extends JpaRepository<User, Integer>{

	User findUserByEmail(String email);
	
}
