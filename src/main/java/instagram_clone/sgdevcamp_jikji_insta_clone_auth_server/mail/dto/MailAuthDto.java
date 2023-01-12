package instagram_clone.sgdevcamp_jikji_insta_clone_auth_server.mail.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public class MailAuthDto {
	private String email;
	private String code;
}

