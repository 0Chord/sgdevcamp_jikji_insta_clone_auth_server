package instagram_clone.sgdevcamp_jikji_insta_clone_auth_server.user.dao;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "요청받은 유저 정보",description = "회원 고유 등록번호, 이메일, 닉네임, 전화번호를 담은 Dao")
public class UserInfoDao {
	@ApiModelProperty(value = "회원 고유 등록 번호")
	private Integer id;
	@ApiModelProperty(value = "회원 이메일")
	private String email;
	@ApiModelProperty(value = "회원 닉네임")
	private String nickname;
	@ApiModelProperty(value = "회원 전화번호")
	private String phone;
}
