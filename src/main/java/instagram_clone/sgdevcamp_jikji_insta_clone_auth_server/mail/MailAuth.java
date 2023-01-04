package instagram_clone.sgdevcamp_jikji_insta_clone_auth_server.mail;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "mail_auth")
public class MailAuth {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "user_email")
	private String email;

	private String code;

	@Builder
	public MailAuth(String email, String code) {
		this.email = email;
		this.code = code;
	}
}
