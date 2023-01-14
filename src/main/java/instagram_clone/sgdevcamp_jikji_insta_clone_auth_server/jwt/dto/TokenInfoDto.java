package instagram_clone.sgdevcamp_jikji_insta_clone_auth_server.jwt.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
@ApiModel(value = "Jwt 정보",description = "토큰 정보를 가진 Dto")
public class TokenInfoDto {

	@ApiModelProperty(value = "부여 유형")
	private String grantType;
	@ApiModelProperty(value = "accessToken")
	private String accessToken;
	@ApiModelProperty(value = "refreshToken")
	private String refreshToken;
	@ApiModelProperty(value = "유저 정보")
	private String userEmail;
}
