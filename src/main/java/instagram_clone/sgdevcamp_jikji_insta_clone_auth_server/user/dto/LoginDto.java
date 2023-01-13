package instagram_clone.sgdevcamp_jikji_insta_clone_auth_server.user.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class LoginDto {
	@ApiModelProperty(value = "이메일")
	private String email;

	@ApiModelProperty(value = "비밀번호")
	private String password;
}
