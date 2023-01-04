package instagram_clone.sgdevcamp_jikji_insta_clone_auth_server.mail;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MailAuthRepository extends JpaRepository<MailAuth, String> {
	void deleteByEmail(String email);

	Optional<MailAuth> findByEmail(String email);

}
