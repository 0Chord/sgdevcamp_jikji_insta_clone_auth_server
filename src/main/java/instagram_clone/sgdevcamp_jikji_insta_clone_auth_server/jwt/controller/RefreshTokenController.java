package instagram_clone.sgdevcamp_jikji_insta_clone_auth_server.jwt.controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import instagram_clone.sgdevcamp_jikji_insta_clone_auth_server.jwt.service.JwtService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/refreshToken")
public class RefreshTokenController {

	JwtService jwtService;

	public RefreshTokenController(JwtService jwtService) {
		this.jwtService = jwtService;
	}

	@PostMapping("/validateRefreshToken")
	public ResponseEntity<?> validateRefreshToken(@RequestBody MultiValueMap<String, String> body){
		log.info("refreshToken Controller  Refresh Token 검증 실행");
		Map<String, String> map = jwtService.validatedRefreshToken(body.get("refreshToken").get(0));

		if(map.get("status").equals("402")){
			log.info("Refresh Token 기한 만료");
			return new ResponseEntity<>(map, HttpStatus.UNAUTHORIZED);
		}

		log.info("Refresh Token 기한 유효");
		return new ResponseEntity<>(map, HttpStatus.OK);
	}

	@PostMapping("/deleteRefreshToken")
	public ResponseEntity<?> deleteRefreshToken(@RequestBody MultiValueMap<String, String> body){
		String userEmail = body.get("userEmail").get(0);
		if(jwtService.checkRefreshTokenByUserEmail(userEmail)){
			jwtService.removeRefreshTokenByUserEmail(userEmail);
			return new ResponseEntity<>("SUCCESS",HttpStatus.OK);
		}else{
			return new ResponseEntity<>("NotFoundUserEmail",HttpStatus.BAD_REQUEST);
		}
	}

}
