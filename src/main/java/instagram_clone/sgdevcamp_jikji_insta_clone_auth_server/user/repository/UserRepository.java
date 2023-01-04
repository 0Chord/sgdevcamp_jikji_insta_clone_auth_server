package instagram_clone.sgdevcamp_jikji_insta_clone_auth_server.user.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import instagram_clone.sgdevcamp_jikji_insta_clone_auth_server.user.User;

@Repository
public interface UserRepository extends JpaRepository<User, String>{
	Optional<User> findByEmail(String email);

	Optional<User> findByNickname(String nickname);
}