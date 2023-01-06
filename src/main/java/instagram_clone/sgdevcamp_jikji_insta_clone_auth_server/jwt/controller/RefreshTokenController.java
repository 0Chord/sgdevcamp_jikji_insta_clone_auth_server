package instagram_clone.sgdevcamp_jikji_insta_clone_auth_server.jwt.controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import instagram_clone.sgdevcamp_jikji_insta_clone_auth_server.jwt.service.JwtService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class RefreshTokenController {

	JwtService jwtService;

	public RefreshTokenController(JwtService jwtService) {
		this.jwtService = jwtService;
	}

	@PostMapping("/refresh")
	public ResponseEntity<?> validateRefreshToken(@RequestBody Map<String, String> body){
		log.info("refresh Controller 실행");
		Map<String, String> map = jwtService.validatedRefreshToken(body.get("refreshToken"));

		if(map.get("status").equals("402")){
			log.info("Refresh Token 기한 만료");
			return new ResponseEntity<>(map, HttpStatus.UNAUTHORIZED);
		}

		log.info("Refresh Token 기한 유효");
		return new ResponseEntity<>(map, HttpStatus.OK);
	}
}
