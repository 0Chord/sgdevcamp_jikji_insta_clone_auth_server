package instagram_clone.sgdevcamp_jikji_insta_clone_auth_server.mail.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import instagram_clone.sgdevcamp_jikji_insta_clone_auth_server.mail.MailAuth;

@Repository
public interface MailAuthRepository extends JpaRepository<MailAuth, String> {

	@Transactional
	void deleteByEmail(String email);
	Optional<MailAuth> findByEmail(String email);


}