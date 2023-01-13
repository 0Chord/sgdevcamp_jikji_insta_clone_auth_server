package instagram_clone.sgdevcamp_jikji_insta_clone_auth_server.user.controller;

import java.io.UnsupportedEncodingException;
import java.util.Objects;

import javax.mail.MessagingException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import instagram_clone.sgdevcamp_jikji_insta_clone_auth_server.mail.service.MailService;
import instagram_clone.sgdevcamp_jikji_insta_clone_auth_server.user.User;
import instagram_clone.sgdevcamp_jikji_insta_clone_auth_server.user.dto.UserAuthDto;
import instagram_clone.sgdevcamp_jikji_insta_clone_auth_server.user.service.UserService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/searchPassword")
public class SearchPasswordController {

	UserService userService;
	MailService mailService;

	public SearchPasswordController(UserService userService, MailService mailService) {
		this.userService = userService;
		this.mailService = mailService;
	}

	@PostMapping("/authUser")
	public ResponseEntity<?> authUser(@RequestBody UserAuthDto body) throws
		MessagingException,
		UnsupportedEncodingException {
		String userEmail = body.getEmail();
		String nickname = body.getNickname();
		String phone = body.getPhone();
		System.out.println("body = " + body);
		User user = userService.findByEmail(userEmail);
		if(user == null){
			return new ResponseEntity<>("NotExistsUser", HttpStatus.OK);
		}else if(!Objects.equals(user.getNickname(), nickname)){
			return new ResponseEntity<>("WrongNickname",HttpStatus.OK);
		}else if(!Objects.equals(user.getPhone(), phone)){
			return new ResponseEntity<>("WrongPhone",HttpStatus.OK);
		}

		String newPassword = mailService.sendMail(userEmail,"password");
		userService.updatePassword(userEmail,newPassword);
		return new ResponseEntity<>("SUCCESS",HttpStatus.OK);
	}

}
