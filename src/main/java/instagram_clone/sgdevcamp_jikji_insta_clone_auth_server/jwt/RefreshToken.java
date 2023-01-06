package instagram_clone.sgdevcamp_jikji_insta_clone_auth_server.jwt;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Data
@Table(name = "refresh_token")
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "refresh_token_id")
	private Long refreshTokenId;

	@Column(name = "refresh_token")
	private String refreshToken;

	@Column(name="user_email")
	private String userEmail;
}
