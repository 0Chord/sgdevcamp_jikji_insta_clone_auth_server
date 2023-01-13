package instagram_clone.sgdevcamp_jikji_insta_clone_auth_server.mail.service;

import org.springframework.stereotype.Service;

import instagram_clone.sgdevcamp_jikji_insta_clone_auth_server.mail.MailAuth;
import instagram_clone.sgdevcamp_jikji_insta_clone_auth_server.mail.repository.MailAuthRepository;
import lombok.RequiredArgsConstructor;

@Service
public class MailAuthService {

	MailAuthRepository mailAuthRepository;

	public MailAuthService(MailAuthRepository mailAuthRepository) {
		this.mailAuthRepository = mailAuthRepository;
	}

	public void register(MailAuth mailAuth){
		mailAuthRepository.save(mailAuth);
	}

	public void deleteByEmail(String email){
		mailAuthRepository.deleteByEmail(email);
	}

	public MailAuth findByEmail(String email){
		return mailAuthRepository.findByEmail(email).orElse(null);
	}
}
