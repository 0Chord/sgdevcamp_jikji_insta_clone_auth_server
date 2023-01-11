package instagram_clone.sgdevcamp_jikji_insta_clone_auth_server.user.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoDao {
	private Integer id;
	private String email;
	private String nickname;
	private String phone;
}
