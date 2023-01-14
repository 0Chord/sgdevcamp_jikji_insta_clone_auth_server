package instagram_clone.sgdevcamp_jikji_insta_clone_auth_server.user.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ApiModel(value = "회원가입",description = "회원가입을 위한 유저정보를 가진 Dto")
public class UserDto {
	@ApiModelProperty(value = "회원가입 요청 유저의 이메일")
	private String email;
	@ApiModelProperty(value = "회원가입 요청 유저의 비밀번호")
	private String password;
	@ApiModelProperty(value = "회원가입 요청 유저의 닉네임")
	private String nickname;
	@ApiModelProperty(value = "회원가입 요청 유저의 전화번호")
	private String phone;
	@ApiModelProperty(value = "회원가입 요청 유저의 이름")
	private String name;
}

