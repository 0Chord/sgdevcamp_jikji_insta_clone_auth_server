package instagram_clone.sgdevcamp_jikji_insta_clone_auth_server.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserDto {
	private String email;
	private String password;
	private String nickname;
	private String phone;
	private String name;
}

