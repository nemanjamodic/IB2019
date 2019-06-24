package Lockbum.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import Lockbum.model.User;

public interface UserRepository extends JpaRepository<User, Integer>{

	public User findByEmail(String email);
	
	public User findUserById(Integer id);
	
	
}
