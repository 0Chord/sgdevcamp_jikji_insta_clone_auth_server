package instagram_clone.sgdevcamp_jikji_insta_clone_auth_server.mail.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
@ApiModel(value = "메일인증 정보",description = "메일 인증 정보를 지닌 Dto")
public class MailAuthDto {
	@ApiModelProperty(value = "메일 인증 유저 이메일")
	private String email;
	@ApiModelProperty(value = "메일 인증 코드")
	private String code;
}

