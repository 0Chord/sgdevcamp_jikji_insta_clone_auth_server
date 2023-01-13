package instagram_clone.sgdevcamp_jikji_insta_clone_auth_server.user.controller;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.Objects;
import java.util.regex.Pattern;

import javax.mail.MessagingException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import instagram_clone.sgdevcamp_jikji_insta_clone_auth_server.mail.MailAuth;
import instagram_clone.sgdevcamp_jikji_insta_clone_auth_server.mail.dto.MailAuthDto;
import instagram_clone.sgdevcamp_jikji_insta_clone_auth_server.mail.service.MailAuthService;
import instagram_clone.sgdevcamp_jikji_insta_clone_auth_server.mail.service.MailService;
import instagram_clone.sgdevcamp_jikji_insta_clone_auth_server.user.User;
import instagram_clone.sgdevcamp_jikji_insta_clone_auth_server.user.dto.UserDto;
import instagram_clone.sgdevcamp_jikji_insta_clone_auth_server.user.service.UserService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/signup")
public class SignupController {
	UserService userService;
	MailService mailService;
	MailAuthService mailAuthService;
	private static final String emailRegex =
		"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	private static final String phoneRegex = "^01(?:0|1|[6-9])(?:\\d{3}|\\d{4})\\d{4}$";
	private static final String passwordRegex = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,20}$";

	private static final String nicknameRegex = "^(?=.*[a-zA-Z0-9가-힣])[a-zA-Z0-9가-힣]{2,16}$";

	public SignupController(UserService userService, MailService mailService, MailAuthService mailAuthService) {
		this.userService = userService;
		this.mailService = mailService;
		this.mailAuthService = mailAuthService;
	}

	@PostMapping("/register")
	public ResponseEntity<?> enroll(@RequestBody UserDto userForm) throws
		MessagingException,
		UnsupportedEncodingException {
		User userByName = userService.findByEmail(userForm.getEmail());
		User userByNickname = userService.findByNickname(userForm.getNickname());
		if (userByName != null) {
			return new ResponseEntity<>("ExistsUserEmail", HttpStatus.OK);
		} else if (userByNickname != null) {
			return new ResponseEntity<>("ExistsUserNickname", HttpStatus.OK);
		} else if (!Pattern.matches(emailRegex, userForm.getEmail())) {
			return new ResponseEntity<>("WrongEmailFormat", HttpStatus.OK);
		} else if (!Pattern.matches(phoneRegex, userForm.getPhone())) {
			return new ResponseEntity<>("WrongPhoneFormat", HttpStatus.OK);
		} else if (!Pattern.matches(passwordRegex, userForm.getPassword())) {
			return new ResponseEntity<>("WrongPasswordFormat", HttpStatus.OK);
		} else if (!Pattern.matches(nicknameRegex, userForm.getNickname())) {
			return new ResponseEntity<>("WrongNicknameFormat", HttpStatus.OK);
		}
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String securePassword = encoder.encode(userForm.getPassword());
		System.out.println("userForm = " + userForm.getPassword()+"    "+securePassword);
		User user = User.builder().email(userForm.getEmail())
			.password(securePassword)
			.name(userForm.getName())
			.phone(userForm.getPhone())
			.roles(Collections.singletonList("ROLE_USER"))
			.loginAt(LocalDateTime.now())
			.profile(null)
			.status(false)
			.updateAt(LocalDateTime.now())
			.createAt(LocalDateTime.now())
			.nickname(userForm.getNickname()).build();
		userService.register(user);
		String code = mailService.sendMail(user.getEmail(),"mail");
		MailAuth mailAuth = MailAuth.builder().email(user.getEmail()).code(code).build();
		mailAuthService.register(mailAuth);

		return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
	}

	@PostMapping("/mailAuth")
	public ResponseEntity<?> authMail(@RequestBody MailAuthDto mailAuthForm) {
		System.out.println("mailAuthForm = " + mailAuthForm);
		MailAuth mailAuth = mailAuthService.findByEmail(mailAuthForm.getEmail());
		if (mailAuth == null) {
			return new ResponseEntity<>("NotExistsEmail", HttpStatus.OK);
		}
		if (!Objects.equals(mailAuth.getCode(), mailAuthForm.getCode())) {
			log.info("코드가 다르다.");
			return new ResponseEntity<>("WrongCode", HttpStatus.OK);
		}
		userService.updateEmailAuth(mailAuth.getEmail());
		mailAuthService.deleteByEmail(mailAuth.getEmail());
		return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
	}
}
