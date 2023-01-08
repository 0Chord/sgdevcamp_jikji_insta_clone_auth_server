package instagram_clone.sgdevcamp_jikji_insta_clone_auth_server.user.controller;

import java.io.UnsupportedEncodingException;
import java.util.Objects;

import javax.mail.MessagingException;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import instagram_clone.sgdevcamp_jikji_insta_clone_auth_server.mail.service.MailService;
import instagram_clone.sgdevcamp_jikji_insta_clone_auth_server.user.User;
import instagram_clone.sgdevcamp_jikji_insta_clone_auth_server.user.service.UserService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@NoArgsConstructor
@AllArgsConstructor
@RequestMapping("/searchPassword")
public class SearchPasswordController {

	UserService userService;
	MailService mailService;

	@PostMapping("/authUser")
	public ResponseEntity<?> authUser(@RequestBody MultiValueMap<String, String> body) throws
		MessagingException,
		UnsupportedEncodingException {
		String userEmail = body.get("userEmail").get(0);
		String nickname = body.get("nickname").get(0);
		String phone = body.get("phone").get(0);

		User user = userService.findByEmail(userEmail);
		if(user != null && Objects.equals(user.getNickname(), nickname) && Objects.equals(user.getPhone(), phone)){
			String password = mailService.sendMail(userEmail);
			userService.updatePassword(userEmail,password);
			return null;
		}else{
			//오류 날 때 에러 처리
			return null;
		}
	}

}
