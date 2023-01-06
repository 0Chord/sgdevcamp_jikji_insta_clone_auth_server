package instagram_clone.sgdevcamp_jikji_insta_clone_auth_server.jwt.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import instagram_clone.sgdevcamp_jikji_insta_clone_auth_server.jwt.RefreshToken;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {
	Optional<RefreshToken> findByRefreshToken(String refreshToken);

	boolean existsByUserEmail(String email);

	void deleteByUserEmail(String email);
}
