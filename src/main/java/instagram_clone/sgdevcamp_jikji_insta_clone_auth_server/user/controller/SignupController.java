package instagram_clone.sgdevcamp_jikji_insta_clone_auth_server.user.controller;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

import javax.mail.MessagingException;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
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

	public SignupController(UserService userService, MailService mailService, MailAuthService mailAuthService) {
		this.userService = userService;
		this.mailService = mailService;
		this.mailAuthService = mailAuthService;
	}

	@PostMapping("/register")
	public String enroll(@RequestBody Map<String, String> userForm, BindingResult bindingResult) throws
		MessagingException,
		UnsupportedEncodingException {
		System.out.println("userForm = " + userForm);
		User userByName = userService.findByEmail(userForm.get("email"));
		User userByNickname = userService.findByNickname(userForm.get("nickname"));
		if (userByName != null) {
			bindingResult.reject("등록된 사용자가 있습니다.");
			return null;
		} else if (userByNickname != null) {
			bindingResult.reject("등록된 닉네임을 사용하는 사용자가 있습니다.");
			return null;
		} else if (!Pattern.matches(emailRegex, userForm.get("email"))) {
			bindingResult.reject("이메일 형식이 잘못되었습니다.");
			return null;
		} else if (!Pattern.matches(phoneRegex, userForm.get("phone"))) {
			bindingResult.reject("전화번호 형식이 잘못되었습니다.");
			return null;
		} else if (!Pattern.matches(passwordRegex, userForm.get("password"))) {
			bindingResult.reject("비밀번호 형식이 잘못되었습니다.");
			return null;
		}
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String securePassword = encoder.encode(userForm.get("password"));
		User user = User.builder().email(userForm.get("email"))
			.password(securePassword)
			.name(userForm.get("name"))
			.phone(userForm.get("phone"))
			.roles(Collections.singletonList("ROLE_USER"))
			.loginAt(LocalDate.now())
			.status(false)
			.updateAt(new Date())
			.createAt(new Date())
			.nickname(userForm.get("nickname")).build();
		userService.register(user);
		String code = mailService.sendMail(user.getEmail());
		MailAuth mailAuth = MailAuth.builder().email(user.getEmail()).code(code).build();
		mailAuthService.register(mailAuth);

		return user.getEmail();
	}

	@PostMapping("/mailAuth")
	public String authMail(@Validated MailAuthDto mailAuthForm) {
		MailAuth mailAuth = mailAuthService.findByEmail(mailAuthForm.getEmail());
		if (mailAuth == null) {
			log.info("이메일이 없다.");
		}
		assert mailAuth != null;
		if (!Objects.equals(mailAuth.getCode(), mailAuthForm.getCode())) {
			mailAuthService.deleteByEmail(mailAuth.getEmail());
			log.info("코드가 다르다.");
		}
		userService.updateEmailAuth(mailAuth.getEmail());
		mailAuthService.deleteByEmail(mailAuth.getEmail());
		log.info("코드가 같다.");

		return null;
	}

}
