package project.demo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import project.demo.entity.User;

import java.util.List;


public interface UserRepo 
	extends JpaRepository<User, Integer> {
	
	//select user where username = ?
	User findByUsername(String username);
	
	//where name = ?
	Page<User> findByName(String s, Pageable pageable);
	
	@Query("SELECT u FROM User u WHERE u.name LIKE :x ")
	Page<User> searchByName(@Param("x") String s, Pageable pageable);
	
	@Query("SELECT u from User u WHERE u.name LIKE :x " 
			+ "OR u.username LIKE :x")
	List<User> searchByNameAndUsername(@Param("x") String s);
	
	@Modifying
	@Query("DELETE FROM User u WHERE u.username = :x")
	int deleteUser(@Param("x") String username);
	
	void deleteByUsername(String username);
	
	// tim ngay sinh nhat hom nay
	@Query("SELECT u from User u WHERE "
			+ "MONTH(u.birthdate) =:month AND DAY(u.birthdate) =:date ")
	List<User> searchByBirthday(
			@Param("date") int date, 
			@Param("month") int month );
}
