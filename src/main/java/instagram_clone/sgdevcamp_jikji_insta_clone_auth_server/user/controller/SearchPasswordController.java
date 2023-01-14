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
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@Api(tags = "비밀번호 찾기 관련 컨트롤러")
@RequestMapping("/searchPassword")
public class SearchPasswordController {

	UserService userService;
	MailService mailService;

	public SearchPasswordController(UserService userService, MailService mailService) {
		this.userService = userService;
		this.mailService = mailService;
	}

	@Operation(summary = "비밀번호 찾기", description = "회원정보를 통해 인증 완료 시 임시비밀번호 발급")
	@ApiResponse(code = 200, message = "OK")
	@PostMapping("/authUser")
	public ResponseEntity<?> authUser(@RequestBody @ApiParam(value = "회원 정보") UserAuthDto body) throws
		MessagingException,
		UnsupportedEncodingException {
		String userEmail = body.getEmail();
		String nickname = body.getNickname();
		String phone = body.getPhone();
		User user = userService.findByEmail(userEmail);
		if(user == null){
			return new ResponseEntity<>("NotExistsUser", HttpStatus.OK);
		}else if(!Objects.equals(user.getNickname(), nickname)){
			return new ResponseEntity<>("WrongNickname",HttpStatus.OK);
		}else if(!Objects.equals(user.getPhone(), phone)){
			return new ResponseEntity<>("WrongPhone",HttpStatus.OK);
		}

		String newPassword = mailService.sendMail(userEmail,"password");
		userService.updateUpdateAt(userEmail);
		userService.updatePassword(userEmail,newPassword);
		return new ResponseEntity<>("SUCCESS",HttpStatus.OK);
	}

}
