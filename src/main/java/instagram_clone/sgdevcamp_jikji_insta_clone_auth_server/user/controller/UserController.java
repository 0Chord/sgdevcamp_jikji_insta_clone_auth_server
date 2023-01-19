package instagram_clone.sgdevcamp_jikji_insta_clone_auth_server.user.controller;

import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import instagram_clone.sgdevcamp_jikji_insta_clone_auth_server.jwt.dto.TokenInfoDto;
import instagram_clone.sgdevcamp_jikji_insta_clone_auth_server.jwt.service.JwtService;
import instagram_clone.sgdevcamp_jikji_insta_clone_auth_server.user.User;
import instagram_clone.sgdevcamp_jikji_insta_clone_auth_server.user.dao.UserInfoDao;
import instagram_clone.sgdevcamp_jikji_insta_clone_auth_server.user.dto.GetUserInfoDto;
import instagram_clone.sgdevcamp_jikji_insta_clone_auth_server.user.dto.LoginDto;
import instagram_clone.sgdevcamp_jikji_insta_clone_auth_server.user.dto.UpdateUserInfoDto;
import instagram_clone.sgdevcamp_jikji_insta_clone_auth_server.user.dto.UpdateUserPasswordDto;
import instagram_clone.sgdevcamp_jikji_insta_clone_auth_server.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Api(tags = "유저 정보 관련 컨트롤러")
@RestController
@RequestMapping("/user")
public class UserController {

	UserService userService;
	JwtService jwtService;

	BCryptPasswordEncoder bCryptPasswordEncoder;

	private static final String phoneRegex = "^01(?:0|1|[6-9])(?:\\d{3}|\\d{4})\\d{4}$";
	private static final String passwordRegex = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,20}$";

	private static final String nicknameRegex = "^(?=.*[a-zA-Z0-9가-힣])[a-zA-Z0-9가-힣]{2,16}$";

	public UserController(UserService userService, JwtService jwtService, BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.userService = userService;
		this.jwtService = jwtService;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}

	@Operation(summary = "로그인", description = "로그인 요청 시 JWT 클라이언트에 쿠키로 발급")
	@ApiResponse(code = 200, message = "OK")
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody @ApiParam(value = "로그인 유저 정보") LoginDto body,
		@ApiParam(value = "쿠키전달") HttpServletResponse httpServletResponse) {
		String userEmail = body.getEmail();
		String password = body.getPassword();
		User user = userService.findByEmail(userEmail);

		if (user == null) {
			return new ResponseEntity<>("NotFoundUser", HttpStatus.OK);
		} else if (!bCryptPasswordEncoder.matches(password, user.getPassword())) {
			return new ResponseEntity<>("WrongPassword", HttpStatus.OK);
		} else if (!user.getStatus()) {
			return new ResponseEntity<>("NotEmailAuthUser", HttpStatus.OK);
		}
		TokenInfoDto token = jwtService.createToken(userEmail, user.getRoles());
		jwtService.login(token);
		userService.updateLoginAt(userEmail);
		//클라이언트 쿠키 로직 추가

		return new ResponseEntity<>(token, HttpStatus.OK);
	}

	@Operation(summary = "유저정보 요청", description = "유저정보 요청 시 유저정보 전달")
	@ApiResponse(code = 200, message = "OK")
	@PostMapping("/user-info")
	public ResponseEntity<?> getUserInfo(@RequestBody @ApiParam(value = "유저정보 Dto") GetUserInfoDto body) {
		String userEmail = body.getEmail();
		User user = userService.findByEmail(userEmail);
		if (user == null) {
			return new ResponseEntity<>("NotExistsUser", HttpStatus.OK);
		}
		UserInfoDao userInfoDao = UserInfoDao.builder()
			.id(user.getId())
			.email(user.getEmail())
			.phone(user.getPhone())
			.nickname(user.getNickname())
			.build();

		return new ResponseEntity<>(userInfoDao, HttpStatus.OK);
	}

	@Operation(summary = "유저정보 변경", description = "유저정보 변경 요청 시 유저정보 변경")
	@ApiResponse(code = 200, message = "OK")
	@PostMapping("/update-info")
	public ResponseEntity<?> updateUserInfo(@RequestBody @ApiParam(value = "변경 요청 정보 Dto") UpdateUserInfoDto body) {
		String email = body.getEmail();
		String nickname = body.getNickname();
		User userByNickname = userService.findByNickname(nickname);
		if (userByNickname != null) {
			return new ResponseEntity<>("ExistsUserNickname", HttpStatus.OK);
		} else if (!Pattern.matches(nicknameRegex, nickname)) {
			return new ResponseEntity<>("WrongNicknameFormat", HttpStatus.OK);
		}

		userService.updateUpdateAt(email);
		userService.updateNickname(email, nickname);

		return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
	}

	@Operation(summary = "유저 비밀번호 변경", description = "유저 비밀번호 변경 API")
	@ApiResponse(code = 200, message = "OK")
	@PostMapping("/update-password")
	public ResponseEntity<?> updateUserPassword(
		@RequestBody @ApiParam(value = "비밀번호 변경 요청 Dto") UpdateUserPasswordDto body) {
		String email = body.getEmail();
		String password = body.getPassword();
		String verifyPassword = body.getVerifyPassword();

		User user = userService.findByEmail(email);
		if (user == null) {
			return new ResponseEntity<>("NotExistsUserEmail", HttpStatus.OK);
		} else if (!Objects.equals(password, verifyPassword)) {
			return new ResponseEntity<>("DifferentVerifyPassword", HttpStatus.OK);
		} else if (!Pattern.matches(passwordRegex, password)) {
			return new ResponseEntity<>("WrongPasswordFormat", HttpStatus.OK);
		}
		userService.updatePassword(email, password);
		userService.updateUpdateAt(email);

		return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
	}
}
