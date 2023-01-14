package instagram_clone.sgdevcamp_jikji_insta_clone_auth_server.user.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "알고싶은 회원의 이메일",description = "알고싶은 회원의 이메일을 제공하는 Dto")
public class GetUserInfoDto {
	@ApiModelProperty(value = "정보를 알고싶은 사용자의 이메일")
	private String email;
}

