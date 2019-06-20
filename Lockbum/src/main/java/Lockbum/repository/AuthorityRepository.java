package Lockbum.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import Lockbum.model.Authority;

public interface AuthorityRepository extends JpaRepository<Authority, Integer>{

	Authority findByName(String name);
	
}

