package instagram_clone.sgdevcamp_jikji_insta_clone_auth_server.user.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import instagram_clone.sgdevcamp_jikji_insta_clone_auth_server.jwt.dto.TokenInfoDto;
import instagram_clone.sgdevcamp_jikji_insta_clone_auth_server.jwt.service.CookieService;
import instagram_clone.sgdevcamp_jikji_insta_clone_auth_server.jwt.service.JwtService;
import instagram_clone.sgdevcamp_jikji_insta_clone_auth_server.user.User;
import instagram_clone.sgdevcamp_jikji_insta_clone_auth_server.user.dao.UserInfoDao;
import instagram_clone.sgdevcamp_jikji_insta_clone_auth_server.user.dto.GetUserInfoDto;
import instagram_clone.sgdevcamp_jikji_insta_clone_auth_server.user.dto.LoginDto;
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
	CookieService cookieService;

	public UserController(UserService userService, JwtService jwtService, CookieService cookieService) {
		this.userService = userService;
		this.jwtService = jwtService;
		this.cookieService = cookieService;
	}

	@Operation(summary = "로그인", description = "로그인 요청 시 JWT 클라이언트에 쿠키로 발급")
	@ApiResponse(code = 200, message = "OK")
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody @ApiParam(value = "로그인 유저 정보") LoginDto body, @ApiParam(value = "쿠키전달") HttpServletResponse httpServletResponse) {
		String userEmail = body.getEmail();
		String password = body.getPassword();
		User user = userService.findByEmail(userEmail);
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

		if (user == null) {
			return new ResponseEntity<>("NotFoundUser", HttpStatus.OK);
		} else if (!encoder.matches(password, user.getPassword())) {
			return new ResponseEntity<>("WrongPassword", HttpStatus.OK);
		} else if (!user.getStatus()) {
			return new ResponseEntity<>("NotEmailAuthUser", HttpStatus.OK);
		}
		TokenInfoDto token = jwtService.createToken(userEmail, user.getRoles());
		jwtService.login(token);
		userService.updateLoginAt(userEmail);
		//클라이언트 쿠키 로직 추가
		Cookie refreshCookie = cookieService.setRefreshCookie(token.getRefreshToken());
		httpServletResponse.addCookie(refreshCookie);
		return new ResponseEntity<>(token.getAccessToken(), HttpStatus.OK);
	}

	@Operation(summary = "유저정보 요청", description = "유저정보 요청 시 유저정보 전달")
	@ApiResponse(code = 200, message = "OK")
	@PostMapping("/get-user-info")
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
}
