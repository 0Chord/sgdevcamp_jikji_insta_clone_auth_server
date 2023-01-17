package instagram_clone.sgdevcamp_jikji_insta_clone_auth_server.user.dto;

import lombok.Data;

@Data
public class UpdateUserInfoDto {
	private String email;
	private String password;
	private String nickname;
	private String phone;
	private String name;
}
