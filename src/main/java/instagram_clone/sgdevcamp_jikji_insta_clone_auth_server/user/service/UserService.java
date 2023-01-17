package instagram_clone.sgdevcamp_jikji_insta_clone_auth_server.user.service;

import java.time.LocalDateTime;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import instagram_clone.sgdevcamp_jikji_insta_clone_auth_server.user.User;
import instagram_clone.sgdevcamp_jikji_insta_clone_auth_server.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserService {

	UserRepository userRepository;

	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public void register(User user){
		userRepository.save(user);
	}

	public User findByEmail(String email){
		return userRepository.findByEmail(email).orElse(null);
	}

	public User findByNickname(String nickname){
		return userRepository.findByNickname(nickname).orElse(null);
	}

	@Transactional
	public void updateEmailAuth(String email){
		User user = userRepository.findByEmail(email).orElse(null);
		assert user != null;
		user.updateEmailAuth(true);
	}

	@Transactional
	public void updatePassword(String userEmail,String password){
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		User user = userRepository.findByEmail(userEmail).get();
		String securePassword = encoder.encode(password);
		user.updatePassword(securePassword);
	}

	@Transactional
	public void updateLoginAt(String userEmail){
		User user = userRepository.findByEmail(userEmail).get();
		user.updateLoginAt(LocalDateTime.now());
	}

	@Transactional
	public void updateUpdateAt(String userEmail){
		User user = userRepository.findByEmail(userEmail).get();
		log.info(userEmail+" 임시비밀번호 발급");
		user.updateUpdatedAt(LocalDateTime.now());
	}

	@Transactional
	public void updateNickname(String userEmail, String nickname){
		User user = userRepository.findByEmail(userEmail).get();
		user.updateNickname(nickname);
	}
}
