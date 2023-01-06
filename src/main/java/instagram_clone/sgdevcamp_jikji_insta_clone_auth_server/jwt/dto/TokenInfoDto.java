package instagram_clone.sgdevcamp_jikji_insta_clone_auth_server.jwt.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class TokenInfoDto {

	private String grantType;
	private String accessToken;
	private String refreshToken;
	private String userEmail;
}
