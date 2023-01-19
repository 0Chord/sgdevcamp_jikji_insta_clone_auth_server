package instagram_clone.sgdevcamp_jikji_insta_clone_auth_server.user.dto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "유저 정보 변경 요청 회원 정보", description = "회원 정보를 가진 Dto")
public class UpdateUserInfoDto {
	@ApiModelProperty(value = "유저 정보 변경 요청 이메일")
	private String email;
	@ApiModelProperty(value = "유저 정보 변경 요청 닉네임")
	private String nickname;
	@ApiModelProperty(value = "유저 정보 변경 요청 전화번호")
	private String phone;
	@ApiModelProperty(value = "유저 정보 변경 요청 이름")
	private String name;
}
