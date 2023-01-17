package instagram_clone.sgdevcamp_jikji_insta_clone_auth_server.jwt.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import instagram_clone.sgdevcamp_jikji_insta_clone_auth_server.jwt.service.JwtService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@Api(tags = "RefreshToken 관련 컨트롤러")
@RequestMapping("/refreshToken")
public class RefreshTokenController {

	JwtService jwtService;

	public RefreshTokenController(JwtService jwtService) {
		this.jwtService = jwtService;
	}

	@Operation(summary = "RefreshToken 검증", description = "RefreshToken 검증을 통해 AccessToken 발급 API")
	@ApiResponse(code = 200, message = "OK")
	@PostMapping("/validation")
	public ResponseEntity<?> validateRefreshToken(@RequestBody @ApiParam(value = "refreshToken") MultiValueMap<String, String> body){
		log.info("refreshToken Controller  Refresh Token 검증 실행");
		Map<String, String> map = jwtService.validatedRefreshToken(body.get("refreshToken").get(0));

		if(map.get("status").equals("402")){
			log.info("Refresh Token 기한 만료");
			return new ResponseEntity<>("ExpiredRefreshToken", HttpStatus.UNAUTHORIZED);
		}

		log.info("Refresh Token 기한 유효");
		String accessToken = map.get("accessToken");
		return new ResponseEntity<>(accessToken, HttpStatus.OK);
	}

	@Operation(summary = "RefreshToken 삭제", description = "RefreshToken 삭제 API")
	@ApiResponse(code = 200, message = "OK")
	@PostMapping("/remove")
	public ResponseEntity<?> deleteRefreshToken(@RequestBody @ApiParam(value = "userEmail") MultiValueMap<String, String> body){
		String userEmail = body.get("userEmail").get(0);
		if(jwtService.checkRefreshTokenByUserEmail(userEmail)){
			jwtService.removeRefreshTokenByUserEmail(userEmail);
			return new ResponseEntity<>("SUCCESS",HttpStatus.OK);
		}else{
			return new ResponseEntity<>("NotFoundUserEmail",HttpStatus.BAD_REQUEST);
		}
	}

}
