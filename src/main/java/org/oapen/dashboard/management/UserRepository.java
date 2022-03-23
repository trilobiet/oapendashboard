package org.oapen.dashboard.management;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, String> {

	@Query(value = ""
		+ "SELECT * FROM user "
		+ "WHERE username = ?1", nativeQuery = true)
	Optional<User> findByUsername(String itemId);

}
