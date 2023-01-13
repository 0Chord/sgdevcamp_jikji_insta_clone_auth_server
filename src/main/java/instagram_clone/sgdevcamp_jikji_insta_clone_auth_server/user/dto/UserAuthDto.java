package instagram_clone.sgdevcamp_jikji_insta_clone_auth_server.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserAuthDto {
	private String email;
	private String nickname;
	private String phone;
}
