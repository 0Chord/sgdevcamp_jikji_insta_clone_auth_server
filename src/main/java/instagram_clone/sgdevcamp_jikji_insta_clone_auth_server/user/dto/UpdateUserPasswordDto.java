package instagram_clone.sgdevcamp_jikji_insta_clone_auth_server.user.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "비밀번호 변경 요청 Dto",description = "비밀번호 변경 요청 할 때 변경할 비밀번호를 전달합니다")
public class UpdateUserPasswordDto {

	@ApiModelProperty(value = "비밀번호 변경 요청 이메일")
	private String email;
	@ApiModelProperty(value = "변경할 비밀번호")
	private String password;
	@ApiModelProperty(value = "변경할 비밀번호 확인용")
	private String verifyPassword;
}
